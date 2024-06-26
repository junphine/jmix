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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

@AutoConfiguration
@ConditionalOnMissingBean(EclipseLinkChannelSupplier.class)
//@AutoConfigureAfter(IgniteAutoConfiguration.class)
@Import({JmixEclipseLinkChannelAutoConfiguration.IgniteChannelConfiguration.class,
        JmixEclipseLinkChannelAutoConfiguration.NoOpChannelConfiguration.class})
public class JmixEclipseLinkChannelAutoConfiguration {


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(Ignite.class)
    @ConditionalOnSingleCandidate(Ignite.class)
    @Order(10)
    public static class IgniteChannelConfiguration {
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnClass(Ignite.class)
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
