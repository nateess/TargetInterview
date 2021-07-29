package target.myretail.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Price implements Serializable {

    BigDecimal value;

    @JsonProperty("currency_code")
    String currencyCode;

    public Price() {
    }

    public Price(BigDecimal value, String currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;
    }

}
