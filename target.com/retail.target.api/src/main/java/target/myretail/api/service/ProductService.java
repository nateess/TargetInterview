package target.myretail.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import target.myretail.api.client.MyRetailClient;
import target.myretail.api.dao.ProductDao;
import target.myretail.api.model.Price;
import target.myretail.api.model.Product;
import target.myretail.api.model.ProductTable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class ProductService {

    @Value("${isCachable}")
    boolean isCachable;

    @Autowired
    MyRetailClient myRetailClient;

    @Autowired
    ProductDao productDao;

    @Autowired
    ObjectMapper objectMapper;

    @Cacheable(key = "'ProductCache'+#productId", value = "productId-cache")
    public Mono<Product> getProductDetails(Long productId) {
        return productDao.findById(productId).log()
                .doOnError(throwable -> {
                    log.error("ProductService:: method=getProductDetails, ProductId=" + productId + " status=error, exception=" + throwable.getLocalizedMessage());
                    throw new RuntimeException("Exception thrown when fetching data from cassandra" + throwable.getLocalizedMessage());
                }).flatMap(productTable -> {
                    return myRetailClient.getProductDescription(productId).flatMap(o -> {
                        return populateProduct(getTitle(o), productTable);
                    }).log();
                });
    }


    private String getTitle(Object o) {

        Map<String, Object> productMap = objectMapper.convertValue(o, Map.class);

        String title = null;
        if (((Map<String, Object>) productMap.get("product")).get("item") != null &&
                ((Map<String, Object>) (((Map<String, Object>) productMap.get("product")).get("item"))).get("product_description") != null) {
            Map<String, Object> prodDescriptionMap = (Map<String, Object>) ((Map<String, Object>) (((Map<String, Object>) productMap.get("product")).get("item"))).get("product_description");
            title = (String) prodDescriptionMap.get("title");

        } else {
            log.info("ProductService:: method=getTitle, message=title is empty");
        }
        return title;
    }

    private Mono<Product> populateProduct(String title, ProductTable product) {
        return Mono.just(new Product(product.getProductId(), title,
                new Price(new BigDecimal(product.getPrice().toString()), "USD"),
                null));
    }

    @CacheEvict(key = "'ProductCache'+#id", value = "productId-cache", beforeInvocation = true)
    public Mono<ResponseEntity<ProductTable>> updateProductDetails(Long productId, Product product) {

        return productDao.findById(productId).flatMap(productTable -> {
            BigDecimal price = product.getPrice().getValue();
            productTable.setPrice(price);
            productTable.setDate(new Date());
            return productDao.save(productTable);
        }).map(productTable -> {
            log.info("ProductService:: method=updateProductDetails,ProductId=" + productId + "  status=success");
            return new ResponseEntity<>(productTable, HttpStatus.OK);
        });

    }
}





