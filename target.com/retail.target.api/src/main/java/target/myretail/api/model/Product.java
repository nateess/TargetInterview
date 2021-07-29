package target.myretail.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {


    Long id;

    String name;
    @JsonProperty("current_price")
    @NotNull
    Price price;
    Error error;

    public Product() {

    }

    public Product(Long id, String name, Price price, Error error) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.error = error;
    }


}


