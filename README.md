# Getting Started

### Tools Required
* Java 11+
* Spring-boot
* Maven
* MySql5 server

### Setting Up
Run following command on MySql terminal:
* CREATE DATABASE shoperdb;
* CREATE USER testuser@localhost IDENTIFIED BY 'secret';
* GRANT ALL ON shoperdb.* TO testuser@localhost;
**OR** Modify application.properties and persistence.xml accordingly, located at main and test resources

Navigate to project root folder on bash terminal and run:
* mvn clean install
If tests are failing, it can be beacause:
* MySql server is not running or is not set up properly
* The app is not configured properly to run on your device: Check application.properties and persistence.xml

### Running Locally
* Start Sql Server (this app is configured to connect to sql server running at port: 3306)
* Run command: **mvn clean compile spring-boot:run** (This app configured to run at port 3000)

### Avaiable end points
Inventory
1. Create inventory item: POST http://localhost:3000/inventories
2. Read all inventory items: GET http://localhost:3000/inventories
3. Read single inventory item: GET http://localhost:3000/inventories/{id}
4. Update inventory item: PUT http://localhost:3000/inventories/{id}
5. Delete inventory item: DELETE http://localhost:3000/inventories/{id}

Order:
1. Create order: POST http://localhost:3000/orders
2. Read all orders: GET http://localhost:3000/orders
3. Read single order: GET http://localhost:3000/orders/{id}
4. Update order: PUT http://localhost:3000/orders/{id}
5. Delete order: DELETE http://localhost:3000/orders/{id}

### Working with API's
**Inventory**
Example JSON body structure for making requests:
{
    "name": null,
    "description": null,
    "price": 0.0,
    "quantity": 0
}
* all parameters are optional and have given default values
* "price" and "quantity" are only valid when non-negetive

**Orders**
Example JSON body structure for making requests:
{
    "customerEmail": null,
    "status": "created",
    "quantities": [
        {
            "key": 2, **// This corresponds to inventory id of required item**
            "value": 5 **// This corresponds to Quantity requrired for the item**
        },
        {
            "key": 1, 
            "value": 2
        }
    ]
}
* dates are auto-genrated/updated
* all parameters are optional

# About

### Why I choose spring-boot:
Spring boot makes it painless to whip up APIs pretty quickly especially if complexity is minimal. Though testing of these APIs can be pain in the seat.
NodesJS is another great option combined with Express. It is super lightweight and super easy to set up.

### Project structure
* Entity models can be found at com.treez.shoper.model
* API's can be found at com.treez.shoper.Controller
* Service logic can be found at com.treez.shoper.service
* Some utils can be found at com.treez.shoper.utils for serializing/deserializing key, value pairs for conversion to from JSON.

### Room for improvement
* Exception handling can decoupled from APIs
* Logic for updating inventory when updating orders could be improved in terms on efficiency.
* Tests coverage does not test all the edge cases.
