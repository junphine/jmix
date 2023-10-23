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
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.util.UUID;

public class EclipseLinkIgniteChanelSupplier implements EclipseLinkChannelSupplier {

    protected final SubscribableChannel messageChannel;

    /** IgniteClient instance. */

    private Ignite igniteClientInstance;

    protected static class IgniteMessageChannel implements SubscribableChannel {
        protected final IgniteMessaging messaging;

        /** Message topics. */
        private enum TOPIC { ORDERED, UNORDERED }

        private IgniteBiPredicate<UUID, ?> listener = null;

        public IgniteMessageChannel(IgniteMessaging topic) {
            this.messaging = topic;
        }

        @Override
        public boolean subscribe(MessageHandler handler) {
            listener = (UUID nodeId, Message<?> message) -> {
                handler.handleMessage(message);
                return false;
            };
            messaging.localListen(TOPIC.ORDERED,listener);
            return true;
        }

        @Override
        public boolean unsubscribe(MessageHandler handler) {
            messaging.stopLocalListen(TOPIC.ORDERED,listener);
            return false;
        }

        @Override
        public boolean send(Message<?> message) {
            messaging.sendOrdered(TOPIC.ORDERED,message,0);
            return true;
        }

        @Override
        public boolean send(Message<?> message, long timeout) {
            messaging.sendOrdered(TOPIC.ORDERED,message,timeout);
            return true;
        }
    }

    public EclipseLinkIgniteChanelSupplier(Ignite igniteClientInstance) {
        this.igniteClientInstance = igniteClientInstance;
        //IgniteMessaging localMsg = igniteClientInstance.message(igniteClientInstance.cluster().forLocal());
        IgniteMessaging remoteMsg = igniteClientInstance.message();
        messageChannel = new IgniteMessageChannel(remoteMsg);
    }

    @Override
    public SubscribableChannel get() {
        return messageChannel;
    }
}
