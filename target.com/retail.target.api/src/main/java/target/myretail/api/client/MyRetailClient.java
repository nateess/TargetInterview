package target.myretail.api.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MyRetailClient extends BaseWebClient {

    @Value("${api.product.baseurl}")
    String baseUrl;

    @Value("${api.product.url}")
    private String myRetailEndpoint;

    @Value("${api.product.connectionTimeOut}")
    private int connectionTimeOut;

    @Value("${api.product.readTimeOut}")
    private int readTimeOut;

    @Value("${api.product.keepAlive}")
    private Boolean keepAlive;


    public Mono<Object> getProductDescription(Long productId) {
        return invokeGetService(myRetailEndpoint, HttpMethod.GET, null, Object.class, productId, "MyRetailApi");

    }

    @Override
    int getConnectionTimeoutMillis() {
        return connectionTimeOut;
    }

    @Override
    int getReadTimeoutSeconds() {
        return readTimeOut;
    }

    @Override
    boolean getKeepAlive() {
        return keepAlive;
    }

    @Override
    String[] getCookieList() {
        return new String[0];
    }

    @Override
    String getBaseUrl() {
        return baseUrl;
    }
}
