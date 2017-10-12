package com.company.kafka;

import com.company.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    public void send(List<KafkaObservationData> data) {

        Properties props = getProducerConfig();

        try (KafkaProducer<String, KafkaObservationData> producer = new KafkaProducer<>(props))  {
            for (KafkaObservationData dataPoint : data) {
                ProducerRecord<String, KafkaObservationData> record = new ProducerRecord<>(dataPoint.locationIdentifier, dataPoint);
                producer.send(record).get();
                logger.info("Sent record for location: " + dataPoint.locationIdentifier + "(" + dataPoint.locationName + "), at time: " + dataPoint.dataDate);
            }

            producer.flush();

        } catch (ExecutionException | InterruptedException e) {
            logger.error("Exception thrown in producer", e);
        }
    }

    private static Properties getProducerConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.kafkaHost());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "WeatherMonitor");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObservationDataSerializer.class.getName());
        return props;
    }

}
