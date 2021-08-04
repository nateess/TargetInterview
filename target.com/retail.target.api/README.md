# retail-api

The goal for this exercise is to create an end-to-end Proof-of-Concept for a products API, which will aggregate product
data from multiple sources and return it as JSON to the caller.

Tech Stack :

- Spring Boot
- Spring data Cassandra
- Spring webflux
- DataStax Cassandra
- Unit testing
- Java 8
- Lombok

For running the application in your local machine. Please follow the below steps:

- Install DataStax Cassandra free version and make sure after startup. Cassandra is in localhost:9042 port. If not
  please make changes in the Cassandra installation folder ~resources/cassandra/conf/cassandra.yaml file to change
  the ip address to 127.0.01.
- Install java 8
- IntelliJ for IDE
- Gradle

After installing the above . Please run the cql statements inside the project folder /retail-api/schema/products.cql .
Please run the keyspace and column family details present in the cql file.

Navigate to the project folder and execute the below command in the terminal. -> gradle bootRun

Above command will bring up the application. Please use sample postman collection available in the
/retail.target.api/schema/Retail.postman_collection.json file and import in POSTMAN to test the api's.

Note:
If you are running this from the main method in the java file or running the test cases seperately. Please perform the
following:

- Install Lombok plugin in your IDE to support the @Slf4j and @Data annotations.
- Enable annotation processing in IntelliJ IDE in the preferences menu.

# Endpoints:

# GET

http://localhost:8083/products/13860428

# UPDATE

http://localhost:8083/products/13860428

Body:
{
"id": 13860428,
"name": "The Big Lebowski (Blu-ray)",
"current_price": {
"value": 15130,
"currency_code": "USD"
} }
