package target.myretail.api.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(value = "PRODUCT_DETAILS")
public class ProductTable implements Serializable {

    @PrimaryKey(value = "PRODUCT_ID")
    private Long productId;

    @Column(value = "PRICE")
    private BigDecimal price;

    @Column(value = "create_timestamp")
    private Date date;

    public ProductTable(Long productId, BigDecimal price, Date date) {
        this.productId = productId;
        this.price = price;
        this.date = date;
    }


}
