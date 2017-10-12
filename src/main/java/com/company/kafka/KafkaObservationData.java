package com.company.kafka;

import java.io.Serializable;
import java.time.LocalDateTime;

public class KafkaObservationData implements Serializable {

    private static final long serialVersionUID = 123L;

    public String dataDate;
    public String locationName;
    public String locationElevation;
    public String windGustMph;
    public String temperatureCelcius;
    public String visibilityMetres;
    public String windDirection;
    public String windSpeedMph;
    public String weatherType;
    public String pressureHpa;
    public String pressureTendencyPaps;
    public String dewPointCelcius;
    public String humidityPercentage;
    public String locationIdentifier;
}
