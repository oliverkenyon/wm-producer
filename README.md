# Weather monitor producer

## Configuration

The application needs the following configuration settings, which should be set in environment variables:

 - WM_METOFFICE_API_KEY: Needed to access API, available free from https://www.metoffice.gov.uk/datapoint/api
 - WM_KAFKA_HOST: Host and port e.g. "localhost:9092"
 - WM_FETCH_HISTORICAL_DATA: optional, if set to true, on startup the application will query the previous 24 hours of data from the API and send this to Kafka
 
## Build

The project uses gradle, so install this, add it to the path, CD into the project directory and type:

gradle build

## Run

java -jar build/lib/WmProducer-SNAPSHOT-1.0.jar

Application will run until stopped, querying the API every hour. 