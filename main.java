public class Shipment {
    private String shipmentId;
    private String origin;
    private String destination;
    private double weight;
    // Add more attributes as per your requirements
    
    // Constructors, getters, and setters
}

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

public class ShipmentService {
    private final Table shipmentsTable;

    public ShipmentService(DynamoDB dynamoDB, String tableName) {
        this.shipmentsTable = dynamoDB.getTable(tableName);
    }

    public void addShipment(Shipment shipment) {
        PutItemSpec putItemSpec = new PutItemSpec().withItem(new Item()
                .withString("shipmentId", shipment.getShipmentId())
                .withString("origin", shipment.getOrigin())
                .withString("destination", shipment.getDestination())
                .withDouble("weight", shipment.getWeight()));
        // Add more attributes to the Item as needed
        
        PutItemOutcome outcome = shipmentsTable.putItem(putItemSpec);
        // Handle the outcome if needed
    }
}
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ShipmentService shipmentService;

    public LambdaHandler() {
        // Initialize AWS services and create an instance of ShipmentService
        // For simplicity, we're using hard-coded table name here; you can use environment variables for dynamic values.
        shipmentService = new ShipmentService(AmazonDynamoDBClientBuilder.defaultClient(), "shipments_table");
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            // Parse the input JSON and create a Shipment object
            Shipment shipment = parseInputToShipment(input.getBody());

            // Add the shipment to DynamoDB
            shipmentService.addShipment(shipment);

            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Shipment added successfully");
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error adding shipment: " + e.getMessage());
        }
    }
    
    private Shipment parseInputToShipment(String inputJson) {
        // Implement JSON parsing and create a Shipment object
        // You can use libraries like Jackson or Gson for JSON parsing.
        // For simplicity, we're using a basic implementation here.
        // Make sure to add appropriate error handling for real-world scenarios.

        // Example implementation:
        Gson gson = new Gson();
        return gson.fromJson(inputJson, Shipment.class);
    }
}
