package io.jmix.autoconfigure.eclipselink;
import org.apache.ignite.Ignite;
import org.apache.ignite.events.EventType;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;

import org.apache.ignite.springframework.boot.autoconfigure.IgniteConfigurer;
import org.apache.ignite.events.EventType;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Ignite.class)
public class JmixEclipseLinkChannelIgniteConfigurer {

    /**
     * Providing configurer for the Ignite.
     * @return Ignite Configurer.
     */
    @Bean
    public IgniteConfigurer configurer() {
        return cfg -> {
            //Setting consistent id.
            //See `application.yml` for the additional properties.
            TcpCommunicationSpi tcp = new TcpCommunicationSpi();
            tcp.setLocalAddress("127.0.0.1");
            tcp.setLocalPort(47100);
            tcp.setLocalPortRange(5);
            cfg.setCommunicationSpi(tcp);
            cfg.setIncludeEventTypes(EventType.EVT_CACHE_OBJECT_PUT,EventType.EVT_CACHE_OBJECT_READ,EventType.EVT_CACHE_OBJECT_REMOVED);
        };
    }

}
