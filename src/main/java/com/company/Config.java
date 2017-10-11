package com.company;

public class Config {

    public static String metOfficeApiKey() {
        return System.getenv("WM_METOFFICE_API_KEY");
    }

    public static Boolean fetchHistoricalData() {
        return "true".equals(System.getenv("WM_FETCH_HISTORICAL_DATA"));
    }

    public static String kafkaHost() {
        return System.getenv("WM_KAFKA_HOST");
    }
}
