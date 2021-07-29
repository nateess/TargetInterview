package target.myretail.api.initialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import target.myretail.api.dao.ProductDao;
import target.myretail.api.model.ProductTable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@Profile("dev")
public class PriceDataInitializer implements CommandLineRunner {


    @Autowired
    ProductDao productDao;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
    }


    public List<ProductTable> data() {
        List<ProductTable> productList = Arrays.asList(new ProductTable(13860428l, new BigDecimal(300.00), new Date()),
                new ProductTable(13860429l, new BigDecimal(300.00), new Date()),
                new ProductTable(14860429l, new BigDecimal(300.00), new Date()),
                new ProductTable(16860429l, new BigDecimal(3300.00), new Date()));
        return productList;
    }

    private void initialDataSetup() {

        productDao.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(productDao::save)
                .thenMany(productDao.findAll())
                .subscribe((product -> {
                    log.info("PriceDataInitializer, method=initialDataSetup, status=Data Inserted");
                }));
    }
}
