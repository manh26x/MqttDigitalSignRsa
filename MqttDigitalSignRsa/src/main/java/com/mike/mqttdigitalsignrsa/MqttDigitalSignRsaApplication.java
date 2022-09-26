package com.mike.mqttdigitalsignrsa;

import com.google.gson.Gson;
import com.mike.mqttdigitalsignrsa.entity.DataItem;
import com.mike.mqttdigitalsignrsa.entity.DataItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@SpringBootApplication
public class MqttDigitalSignRsaApplication {



    public static void main(String[] args) {
        SpringApplication.run(MqttDigitalSignRsaApplication.class, args);
    }

    @Autowired
    private DataItemRepository dataItemRepository;

//    @Bean
//    public MessageChannel mqttInputChannel() {
//        return new DirectChannel();
//    }

//    @Bean
//    public MessageProducer inbound() {
//        MqttPahoMessageDrivenChannelAdapter adapter =
//                new MqttPahoMessageDrivenChannelAdapter("tcp://103.183.113.120:1883", "testClient",
//                        "Connection", "tatrach");
//        adapter.setCompletionTimeout(5000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
////        adapter.setQos(1);
//        adapter.setOutputChannel(mqttInputChannel());
////        adapter.set
//        return adapter;
//    }

    @Bean
    public IntegrationFlow mqttInbound() {
        return IntegrationFlows.from(
                        new MqttPahoMessageDrivenChannelAdapter("tcp://103.183.113.120:1883", "testClient",
                                 "tatrach"))
                .handle(m ->this.handleMessage(m, "tatrach"))
                .log()
                .get();
    }

    private void handleMessage(Message<?> message, String construction) {
        Gson gson = new Gson();
        HashMap payloadMap = gson.fromJson(message.getPayload().toString(), HashMap.class);
        payloadMap.keySet().forEach(e -> {
            float value = Float.parseFloat("" + payloadMap.get(e));
            this.dataItemRepository.save(new DataItem(Timestamp.from(Instant.now()), value, (String) e, construction));

        });
    }
//    @Bean
//    @ServiceActivator(inputChannel = "tatrach")
//    public MessageHandler handler() {
//        return new MessageHandler() {
//
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                System.out.println(message.getPayload());
//            }
//
//        };
//    }

//    @Bean
//    @ServiceActivator(inputChannel = "Connection")
//    public MessageHandler handlerConnection() {
//        return new MessageHandler() {
//
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                System.out.println(message.getPayload());
//            }
//
//        };
//    }

}
