package com.company.util;

import com.company.kafka.KafkaObservationData;
import com.company.metoffice.ObservationData;
import com.company.metoffice.ObservationLocation;
import com.company.metoffice.ObservationPeriod;
import com.company.metoffice.ObservationsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MetOfficeDataProcessor {

    public List<KafkaObservationData> process(ObservationsResponse observationsResponse) {

        List<KafkaObservationData> processedData = new ArrayList<>();

        // The MetOffice API gives us readings for all 140+ available locations in one call,
        // and potentially for multiple days, each with multiple hourly readings.
        // We split this out into a flat list of individual readings to be fed into Kafka
        for (ObservationLocation location : observationsResponse.SiteRep.DV.Location) {
            for (ObservationPeriod period : location.Period) {
                for (ObservationData data : period.Rep) {
                    KafkaObservationData kafkaData = createKafkaObservationData(data);

                    // The property named "$" contains the number of minutes since the start of the day to this particular
                    // reading. It's always a multiple of 60 as the max resolution of readings is hourly, which is what we request.
                    Integer hours = Integer.parseInt(data.$) / 60;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'Z'H");
                    LocalDateTime dateTime = LocalDateTime.parse(period.value + hours, formatter);

                    kafkaData.dataDate = dateTime.toString();
                    kafkaData.locationElevation = location.elevation;
                    kafkaData.locationName = location.name;
                    kafkaData.locationIdentifier = location.i;

                    processedData.add(kafkaData);
                }
            }
        }

        return processedData;
    }

    private static KafkaObservationData createKafkaObservationData(ObservationData observationData) {
        // Because we are serializing the returned class into Kafka, we need to be careful here
        // not to capture the incoming ObservationData in a closure and not to reference it from the
        // KafkaObservationData class, otherwise our consumers will have a dependency on these MetOffice
        // specific value objects.
        KafkaObservationData data = new KafkaObservationData();
        data.windGustMph = observationData.G;
        data.temperatureCelcius = observationData.T;
        data.visibilityMetres = observationData.V;
        data.windDirection = observationData.D;
        data.windSpeedMph = observationData.S;
        data.weatherType = observationData.W;
        data.pressureHpa = observationData.P;
        data.pressureTendencyPaps = observationData.Pt;
        data.dewPointCelcius = observationData.Dp;
        data.humidityPercentage = observationData.H;
        return data;
    }
}
