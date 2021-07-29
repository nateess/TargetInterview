package target.myretail.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;

@Configuration
@EnableCaching
public class RootConfig extends CachingConfigurerSupport {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    ReactorResourceFactory reactorResourceFactory() {
        return new ReactorResourceFactory();
    }


    @Bean
    public net.sf.ehcache.CacheManager ehCacheManager() {

        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("productId-cache");
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setMaxEntriesLocalHeap(1000);
        cacheConfiguration.setTimeToLiveSeconds(20000);

        net.sf.ehcache.config.Configuration configuration = new net.sf.ehcache.config.Configuration();
        configuration.addCache(cacheConfiguration);
        return net.sf.ehcache.CacheManager.newInstance(configuration);

    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

}
