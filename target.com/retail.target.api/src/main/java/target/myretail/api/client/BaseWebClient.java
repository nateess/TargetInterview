package target.myretail.api.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Data
@Slf4j
abstract class BaseWebClient {

    WebClient webClient;

    @Autowired
    ReactorResourceFactory reactorResourceFactory;

    abstract int getConnectionTimeoutMillis();

    abstract int getReadTimeoutSeconds();

    abstract boolean getKeepAlive();

    abstract String[] getCookieList();

    abstract String getBaseUrl();

    void initializeWebClient() {
        this.webClient = WebClient.builder().baseUrl(getBaseUrl()).clientConnector(new ReactorClientHttpConnector(reactorResourceFactory, httpClient ->
                httpClient.followRedirect(true).tcpConfiguration(tcpClient ->
                        tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectionTimeoutMillis()).doOnConnected(connection -> {
                            connection.markPersistent(getKeepAlive()).addHandlerLast(new ReadTimeoutHandler(getReadTimeoutSeconds()));
                        })))).build();

        // this.webClient = WebClient.create(getBaseUrl());
    }


    protected <B, T> Mono<T> invokeGetService(String url, HttpMethod httpMethod, HttpHeaders headers, Class<T> request, B productId, String extraLogInfo) {
        log.info("BaseWebClient, method=invokeGetService, clientName=" + extraLogInfo);
        long start = System.currentTimeMillis();
        initializeWebClient();
        return webClient.method(httpMethod).uri(uriBuilder -> uriBuilder.path(url)
                        .build(productId)).retrieve().bodyToMono(request)
                .doOnSuccess(t -> {
                    log.info("BaseWebClient, method=invokeGetService, status=success, request_duration=" + (System.currentTimeMillis() - start));
                }).doOnError(throwable -> {
                    log.error("BaseWebClient, method=invokeGetService, status=error, request_duration=" + (System.currentTimeMillis() - start) + " exception=" + throwable);

                });
    }


}
