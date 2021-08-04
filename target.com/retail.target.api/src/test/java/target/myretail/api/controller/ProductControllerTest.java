package target.myretail.api.controller;

import org.apache.thrift.transport.TTransportException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import target.myretail.api.constants.ProductConstants;
import target.myretail.api.dao.ProductDao;
import target.myretail.api.model.Price;
import target.myretail.api.model.Product;
import target.myretail.api.model.ProductTable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ProductControllerTest {

    @Autowired
    ProductDao productDao;

    @Autowired
    WebTestClient webTestClient;

    public List<ProductTable> data() {
        List<ProductTable> productList = Arrays.asList(new ProductTable(13860428, new BigDecimal(1300.00), new Date()),
                new ProductTable(54456119, new BigDecimal(2300.00), new Date()),
                new ProductTable(13264003, new BigDecimal(3300.00), new Date()),
                new ProductTable(12954218, new BigDecimal(3300.00), new Date()));
        return productList;
    }

    @Before
    public void setUp() throws IOException, TTransportException {

        // Flux.fromIterable(data()).flatMap(productDao::delete);
        productDao.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(productDao::save)
                .doOnNext((productTable -> {
                    //  System.out.println("Inserted"+ productTable);
                })).blockLast();

    }


    @Test
    public void getProductTitle() {

        // setUp();
        webTestClient.get().uri(ProductConstants.PRODUCT_END_POINT_V1.concat("/{id}"), "13860428")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Product.class)
                .consumeWith((response) -> {
                    List<Product> list = response.getResponseBody();
                    list.forEach((item) -> {
                        assert item.getId() == 13860428;
                        assert item.getName() != null;
                    });
                });

    }

    @Test
    public void getProductPrice() {
        webTestClient.get().uri(ProductConstants.PRODUCT_END_POINT_V1.concat("/{id}"), "13860428")
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price.value", 3020.00);
    }

    @Test
    public void getItem_invalidId() {

        webTestClient.get().uri(ProductConstants.PRODUCT_END_POINT_V1.concat("/{id}"), "138604282323")
                .exchange().expectStatus().is4xxClientError();

    }

    @Test
    public void getItem_idNotFoundInAPI() {

        webTestClient.get().uri(ProductConstants.PRODUCT_END_POINT_V1.concat("/{id}"), "14860429")
                .exchange().expectStatus().isNotFound();

    }

    @Test
    public void getAllItems() {

        Flux<Product> itemFlux = webTestClient.get().uri(ProductConstants.PRODUCT_END_POINT_V1.concat("/{id}"), "13860428")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(Product.class)
                .getResponseBody();

        StepVerifier.create(itemFlux.log())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void updateProduct() {

        Price price = new Price(new BigDecimal(1850.0), "USD");
        Product product = new Product(13860428l, "The Big Lebowski (Blu-ray)", price, null);

        webTestClient.put().uri(ProductConstants.PRODUCT_END_POINT_V1.concat("/{id}"), "13860428")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo("1850");
    }

    @Test
    public void updateItem_invalidId() {

        Price price = new Price(new BigDecimal(850.0), "USD");
        Product product = new Product(13860l, "The Big Lebowski (Blu-ray)", price, null);

        webTestClient.put().uri(ProductConstants.PRODUCT_END_POINT_V1.concat("/{id}"), 13860l)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isNotFound();
    }

}
