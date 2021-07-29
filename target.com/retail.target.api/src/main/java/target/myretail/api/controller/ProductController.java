package target.myretail.api.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import target.myretail.api.constants.ProductConstants;
import target.myretail.api.model.Error;
import target.myretail.api.model.Product;
import target.myretail.api.model.ProductTable;
import target.myretail.api.service.ProductService;

import javax.validation.Valid;

@Slf4j
@RestController
@Data
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(ProductConstants.PRODUCT_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Product>> getProduct(@PathVariable("id") Long id) {

        return productService.getProductDetails(id).flatMap(product -> {
                    ResponseEntity<Product> responseEntity = ResponseEntity.status(HttpStatus.OK).body(product);
                    return Mono.just(responseEntity);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(new Product(null, null, null, new Error(HttpStatus.UNPROCESSABLE_ENTITY.value(), "No data found in database"))));

    }

    @PutMapping(ProductConstants.PRODUCT_END_POINT_V1 + "/{id}")
    private Mono<ResponseEntity<ProductTable>> updateProduct(@PathVariable("id") Long id, @RequestBody @Valid Product product) {

        return productService.updateProductDetails(id, product).defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ProductTable(null, null, null)));
    }
}
