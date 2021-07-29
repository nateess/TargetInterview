
package target.myretail.api.dao;

import org.apache.thrift.transport.TTransportException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import target.myretail.api.model.ProductTable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
//@EnableReactiveCassandraRepositories
public class ProductDaoTest {

    @Autowired
    ProductDao productDao;

    public List<ProductTable> data() {
        List<ProductTable> productList = Arrays.asList(new ProductTable(1111l, new BigDecimal(1300.00), new Date()),
                new ProductTable(2222l, new BigDecimal(2300.00), new Date()),
                new ProductTable(3333l, new BigDecimal(3300.00), new Date()),
                new ProductTable(4444l, new BigDecimal(3300.00), new Date()));
        return productList;
    }

    @Before
    public void setUp() throws IOException, TTransportException {

        // Flux.fromIterable(data()).flatMap(productDao::delete);
        productDao.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(productDao::save)
                .doOnNext((productTable -> {
                    // System.out.println("Inserted"+ productTable);
                })).blockLast();

    }

    @Test
    public void getAllItems() {

        StepVerifier.create(productDao.findAll()).expectSubscription().expectNextCount(4).verifyComplete();
    }

    @Test
    public void getProductById() {
        StepVerifier.create(productDao.findById(1111l))
                .expectSubscription()
                .expectNextMatches((productTable -> productTable.getPrice().compareTo(new BigDecimal(1300.00)) == 0))
                .verifyComplete();
    }

}

