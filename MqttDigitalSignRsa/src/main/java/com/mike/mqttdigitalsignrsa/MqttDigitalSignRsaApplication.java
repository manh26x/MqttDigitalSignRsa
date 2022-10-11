package com.mike.mqttdigitalsignrsa;

import com.google.gson.Gson;
import com.mike.mqttdigitalsignrsa.entity.DataItem;
import com.mike.mqttdigitalsignrsa.entity.DataItemRepository;
import com.mike.mqttdigitalsignrsa.utils.LuaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@SpringBootApplication
@EnableWebFlux
@RestController
public class MqttDigitalSignRsaApplication {



    public static void main(String[] args) {
        LuaUtils.compile(new File("./rsa.lua"));
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
//                        "rsa_sign/digital", "tatrach");
//        adapter.setCompletionTimeout(5000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(2);
////        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }

//    @Bean
//    public IntegrationFlow mqttInboundDecrypt() {
//        MqttPahoMessageDrivenChannelAdapter adapter =new MqttPahoMessageDrivenChannelAdapter("tcp://103.183.113.120:1883", "testClient",
//                "rsa_sign/digital");
//        adapter.setQos(2);
//        adapter.setCompletionTimeout(5000);
//        return IntegrationFlows.from(adapter)
////                .handle( message -> this.eventReceiveData(message, "tatrach"))
//                .handle(m ->this.handleMessage(m, "tatrach"))
//                .get();
//    }

    @Bean
    public IntegrationFlow mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =new MqttPahoMessageDrivenChannelAdapter("tcp://103.183.113.120:1883", "testClient",
                "tatrach");
        adapter.setQos(2);
        adapter.setCompletionTimeout(5000);
        return IntegrationFlows.from(adapter)
                .handle( message -> this.eventReceiveData(message, "tatrach"))
//                .handle(m ->this.handleMessage(m, "tatrach"))
                .get();
    }

    private void eventReceiveData(Message<?> message, String construction) {
        rawEvent.onNext(message);
        Gson gson = new Gson();
        Map<String, Object> payloadMap = gson.fromJson(message.getPayload().toString(), HashMap.class);
        Optional<String> key = payloadMap.keySet().stream().filter(e -> e.contains("tcp")).findFirst();
        double realValue =  Double.parseDouble("" + payloadMap.get(key.get()));
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(payloadMap.get("timestamp").toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.dataItemRepository.save(new DataItem(this.dataItemRepository.getNewItemId(), Timestamp.from(date.toInstant()), (float) realValue, key.get(), construction));

    }
    private void handleMessage(Message<?> message, String construction) {

        Gson gson = new Gson();
//        processor.onNext(message.getPayload());
        Map<String, Object> payloadMap = gson.fromJson(message.getPayload().toString(), HashMap.class);
        Optional<String> key = payloadMap.keySet().stream().filter(e -> e.contains("tcp")).findFirst();
        double valueEncrypted = Double.parseDouble("" + payloadMap.get(key.get()));
        int value = LuaUtils.invoke("encrypt_byte", (int) valueEncrypted, 5, 20453369).toint();
        int salt = (int) Double.parseDouble(payloadMap.get("salt").toString());
        int hash = (int) Double.parseDouble(payloadMap.get("hash").toString());
        try {
            LuaUtils.invoke("encrypt_byte", value, salt, hash);
            double realValue = (value * 1.0) / 1000;
            if((double)payloadMap.get("signed") > 0) {
                realValue = -realValue;
            }
            payloadMap.put("decrypt", realValue);
            payloadMap.put("verify", true);
        }catch (Exception e) {
            e.printStackTrace();
            payloadMap.put("verify", false);
        }
        processor.onNext(payloadMap);
    }

    private final FluxProcessor<Object, Object> processor = DirectProcessor.create();
    private final FluxProcessor<Object, Object> rawEvent = DirectProcessor.create();
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> eventFlux() {
        return processor;
    }
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, path = "/raw")
    public Flux<Object> eventRawData() {
        return rawEvent;
    }

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
