package com.company.util;


import com.company.kafka.KafkaObservationData;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class ObservationDataSerializer implements Serializer<KafkaObservationData> {

    private final static Logger logger = LoggerFactory.getLogger(ObservationDataSerializer.class);

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, KafkaObservationData data) {
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArray);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            byte[] b = byteArray.toByteArray();
            return b;
        } catch (IOException e) {
            logger.error("Failed to serialize KafkaObservationData object" + e);
            return new byte[0];
        }
    }

    @Override
    public void close() {

    }
}
