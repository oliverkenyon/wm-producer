package com.company;

import com.company.kafka.KafkaObservationData;
import com.company.kafka.KafkaSender;
import com.company.metoffice.MetOfficeConnector;
import com.company.metoffice.ObservationsResponse;
import com.company.util.MetOfficeDataProcessor;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Producer {

    private final KafkaSender kafkaSender;
    private final MetOfficeConnector connector;
    private final MetOfficeDataProcessor processor;

    public Producer() {
        kafkaSender = new KafkaSender();
        connector = new MetOfficeConnector();
        processor = new MetOfficeDataProcessor();
    }
    
    public void start() {
        if (Config.fetchHistoricalData()) {
            ObservationsResponse observationsResponse = connector.getAllAvailableObservations();
            List<KafkaObservationData> list = processor.process(observationsResponse);
            kafkaSender.send(list);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ObservationsResponse observationsResponse = connector.getLatestObservations();
                List<KafkaObservationData> list = processor.process(observationsResponse);
                kafkaSender.send(list);

            }
        }, Date.from(Instant.now()), 3600000);
    }

}
