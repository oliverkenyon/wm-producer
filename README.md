# Weather monitor producer

Queries the MetOffice API for observation data from all available locations across the UK every hour. This is the max frequency that the data is made available. 

The data is processed into individual readings per location/time and each of these is sent to Kafka using the standard Java driver. The MetOffice only provides the last 24 hours of data, but by saving it into Kafka we can analyse longer periods of time. 

## Configuration

The application needs the following configuration settings, which should be set in environment variables:

 - WM_METOFFICE_API_KEY: Needed to access API, available free from https://www.metoffice.gov.uk/datapoint/api
 - WM_KAFKA_HOST: Host and port e.g. "localhost:9092"
 - WM_FETCH_HISTORICAL_DATA: optional, if set to true, on startup the application will query the previous 24 hours of data from the API and send this to Kafka. After this, it will continue as normal to poll and send data every hour.
 
## Build

The project uses gradle, so install this, add it to the path, CD into the project directory and type:

gradle build

## Run

java -jar build/libs/WmProducer-1.0-SNAPSHOT.jar

Application will run until stopped, querying the API every hour. 
