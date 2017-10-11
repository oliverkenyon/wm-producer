package com.company.metoffice;

import com.company.Config;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class MetOfficeConnector {

    private static final String  baseUrl = "http://datapoint.metoffice.gov.uk/public/data/val/wxobs/all/json";

    public ObservationsResponse getAllAvailableObservations() {
        return getObservations("/all?res=hourly");
    }

    public ObservationsResponse getLatestObservations() {
        String sampleDate = Instant.now()
                .truncatedTo(ChronoUnit.HOURS)
                .minus(1L, ChronoUnit.HOURS).toString();

        String endpointUrl = "/all?res=hourly&time=" + sampleDate;
        return getObservations(endpointUrl);
    }

    private ObservationsResponse getObservations(String endpointUrl) {
        String url = baseUrl + endpointUrl + "&key=" + Config.metOfficeApiKey();

        Client client = Client.create();
        WebResource resource = client.resource(url);
        ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
        String responseString = response.getEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(responseString, ObservationsResponse.class);
        }
        catch (IOException e) {
            throw new RuntimeException("IOException reading json from string!", e);
        }
    }
}
