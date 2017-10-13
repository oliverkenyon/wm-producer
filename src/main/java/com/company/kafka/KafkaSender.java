package com.company.kafka;

import com.company.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class KafkaSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    public void send(List<KafkaObservationData> data) {
        Properties props = getProducerConfig();
        KafkaProducer<String, KafkaObservationData> producer = new KafkaProducer<>(props);

        data.forEach(dataPoint -> {
            ProducerRecord<String, KafkaObservationData> record
                    = new ProducerRecord<>(dataPoint.locationIdentifier, dataPoint);

            logSend(dataPoint);
            producer.send(record, this::logResult);
        });

        producer.flush();
    }

    private static Properties getProducerConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.kafkaHost());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "WeatherMonitor");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObservationDataSerializer.class.getName());
        return props;
    }

    private void logSend(KafkaObservationData dataPoint) {
        logger.info("Sending record for location: "
                + dataPoint.locationIdentifier + "(" + dataPoint.locationName + "), at time: "
                + dataPoint.dataDate);
    }

    private void logResult(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            logger.error("Error sending data", exception);
        }
        else {
            logger.info("Successfully sent data to topic: "
                    + metadata.topic() + " and partition: "
                    + metadata.partition() + " with offset: "
                    + metadata.offset());
        }
    }

}
