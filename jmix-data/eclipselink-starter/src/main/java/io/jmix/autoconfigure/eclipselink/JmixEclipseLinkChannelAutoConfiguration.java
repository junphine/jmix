/*
 * Copyright 2020 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.autoconfigure.eclipselink;

import io.jmix.eclipselink.impl.support.EclipseLinkChannelSupplier;
import org.apache.ignite.Ignite;
import org.apache.ignite.events.EventType;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.springframework.boot.autoconfigure.IgniteConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

@AutoConfiguration
@ConditionalOnMissingBean(EclipseLinkChannelSupplier.class)
@AutoConfigureAfter(HazelcastAutoConfiguration.class)
@Import({JmixEclipseLinkChannelAutoConfiguration.HazelcastChannelConfiguration.class,
        JmixEclipseLinkChannelAutoConfiguration.NoOpChannelConfiguration.class})
public class JmixEclipseLinkChannelAutoConfiguration {

    /**
     * Providing configurer for the Ignite.
     * @return Ignite Configurer.
     */
    @Bean
    public IgniteConfigurer configurer() {
        return cfg -> {
            //Setting consistent id.
            //See `application.yml` for the additional properties.
            cfg.setClientMode(true);
            cfg.setCommunicationSpi(new TcpCommunicationSpi());
            cfg.setIncludeEventTypes(EventType.EVT_CACHE_OBJECT_PUT,EventType.EVT_CACHE_OBJECT_READ,EventType.EVT_CACHE_OBJECT_REMOVED);
        };
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(Ignite.class)
    @ConditionalOnSingleCandidate(Ignite.class)
    @Order(10)
    public static class HazelcastChannelConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public EclipseLinkChannelSupplier eclipseLinkChannelSupplier(Ignite hazelcastInstance) {
            return new EclipseLinkIgniteChanelSupplier(hazelcastInstance);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @Order
    public static class NoOpChannelConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public EclipseLinkChannelSupplier eclipseLinkChannelSupplier() {
            return new EclipseLinkNoOpChannelSupplier();
        }
    }
}
