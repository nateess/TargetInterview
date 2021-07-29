package target.myretail.api.model;

import lombok.Data;

@Data
public class Error {

    private int errorCode;
    private String errorDescription;

    public Error(int errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

}
