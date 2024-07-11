package APITestCase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ManageQuotation {
	String authToken;
	Faker faker=new Faker();
	String randomName = faker.name().fullName();
    String randomPhoneNumber = generateRandomIndianPhoneNumber();
    String email=faker.internet().emailAddress();
    String secondaryPhone=generateRandomIndianPhoneNumber();
    String randomText = faker.lorem().characters(15, true, true);
    int leadId;
    
	@Test
    public void Login() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\PreLogin\\PasswordLogin.json");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .post("/login")
                .andReturn();
        System.out.println("        ******** MANAGE QUOTATION ********");
        Assert.assertEquals(response.getStatusCode(), 200);

        String res = response.getBody().asString();
        this.authToken = JsonPath.from(res).get("data.token");
        response.then().assertThat().body("message", equalTo("Login successfully"));
    } 
	
	// Add quotation
	@Test(dependsOnMethods="Login")
    public void AddQuotation() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageQuotation\\addQuote.json");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/createQuotation")
                .andReturn();

        String res = response.getBody().asString();
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Added successfully"));
    } 
	
	//Get Quotations
	@Test(dependsOnMethods="Login")
    public void GetQuotation() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       
        String file="{\r\n"
        		+ "    \"pageNumber\": 1,\r\n"
        		+ "    \"pageSize\": 10,\r\n"
        		+ "    \"search\": \"\",\r\n"
        		+ "    \"sort\": \"\"\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/getQuotations")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of get quotations :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Record found successfully."));
    } 
	
	//Update Quotations
	@Test(dependsOnMethods="Login")
    public void UpdateQuotation() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file=new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageQuotation\\updateQuote.json");
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/updateQuotation")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of update quotation :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Updated successfully."));
    } 
	
	//Export
	@Test(dependsOnMethods="Login")
    public void ExportQuotation() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file ="{\r\n"
        		+ "    \"search\": \"\"\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/exportQuotations")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of export quotation :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Record found successfully"));
    } 
	
	//Delete Quotation
	@Test(dependsOnMethods="Login")
    public void DeleteQuotation() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file ="{\r\n"
        		+ "    \"id\": 67\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/deleteQuotation")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of delete quotation :"+res);
       
        if (res.contains("Please provide valid id.")) {
            // Handle case where the quotation is already deleted
            Assert.assertEquals(response.getStatusCode(), 403);
            response.then().assertThat().body("message", equalTo("Please provide valid id."));
        } else {
            // Normal case where the quotation is deleted successfully
            Assert.assertEquals(response.getStatusCode(), 200);
            response.then().assertThat().body("message", equalTo("Quotation deleted successfully."));
        }
    } 
	
	//Quotation Detail
	@Test(dependsOnMethods="Login")
    public void QuotationDetail() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file ="{\r\n"
        		+ "	    \"pageNumber\": 1,\r\n"
        		+ "	    \"pageSize\": \"\",\r\n"
        		+ "	    \"id\": \"75\"\r\n"
        		+ "	}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/getQuotations")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of quotation detail :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Record found successfully."));
    } 
	
	//Download quotation
	@Test(dependsOnMethods="Login")
    public void DownloadQuotation() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file ="{\r\n"
        		+ "    \"quotation_id\": \"75\"\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/downloadQuote")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of download quote :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Record found successfully."));
    } 
	
	//Convert to Order
	@Test(dependsOnMethods="Login")
    public void ConvertToOrder() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String filePath = "C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageQuotation\\Image.png";
        File file = Paths.get(filePath).toFile();
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        
        String filePath1 = "C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageQuotation\\Screenshot (26).png";
        File file1 = Paths.get(filePath1).toFile();
        byte[] fileContent1 = FileUtils.readFileToByteArray(file1);
        
        Response response = given()
                .baseUri("https://mytyles.website:3133/api/v1")
                .header("Authorization", "Bearer " + authToken)
                .basePath("/orderDispatched")
                .multiPart("id", 57) 
                .multiPart("comment", "New comment") 
                .multiPart("links", "abc.com") 
                .multiPart("proof", file, "image/png") 
                .multiPart("delivery_type", "Pick Up") 
                .multiPart("delivery_date",2024-06-13)
                .multiPart("sales_person",50)
                .multiPart("direct_ready_for_pickup",true)
                .multiPart("quote_order_amount",450)
                .multiPart("customer_phone_number","2345678910")
                .multiPart("sale_order_no",10056)
               .multiPart("instructions", file1, "image/png")
                .multiPart("order_type","shortage_order")
                .header("Accept", "*/*")
                .header("Content-Type", "multipart/form-data")
                .when()
                .post();

        String res = response.getBody().asString();
        System.out.println("Response body of convert to order :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Order converted successfully"));
    } 
	
	// Generating random phone number
    private String generateRandomIndianPhoneNumber() {
        // Ensure it generates a 10-digit number
        return "9" + faker.number().digits(9); // Start with '9' to ensure a valid 10-digit Indian number
    }
   
   //ADD LEAD
   @Test(dependsOnMethods = "Login")
   public void AddLead() throws IOException
   {
       RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       
       String payload="  {\r\n"
       + "    \"full_name\": \""+randomName+"\",\r\n"  
       + "    \"primary_phone\": \""+randomPhoneNumber+"\",\r\n"
       + "    \"secondary_phone\": \""+secondaryPhone+"\",\r\n"
       + "    \"email\": \""+email+"\",\r\n"
       + "    \"lead_type\": \"Walkin\",\r\n"
       + "    \"lead_source\": \"Offline\",\r\n"
       + "    \"notes\": \""+randomText+"\",\r\n"
       + "    \"requirements\": [\r\n"
       + "        1\r\n"
       + "    ],\r\n"
       + "    \"lead_owner_id\": 2,\r\n"
       + "    \"billing_address\": {\r\n"
       + "        \"city\": \"\",\r\n"
       + "        \"state\": \"\",\r\n"
       + "        \"pincode\": \"\",\r\n"
       + "        \"address_line_1\": \"\",\r\n"
       + "        \"address_line_2\": \"\",\r\n"
       + "        \"landmark\": \"\",\r\n"
       + "        \"gst_number\": \"\"\r\n"
       + "    }\r\n"
       + "}";
       
       
       Response response = RestAssured.given()
               .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
               .body(payload)
               .when()
               .post("/createLead")
               .andReturn();
       String exp = response.getBody().asString();
       //String newData=response.getBody().data;
       this.leadId = JsonPath.from(exp).get("data.lead_id");
       System.out.println(leadId);
       Assert.assertEquals(response.statusCode(),200);
   }
   
	//Add billing address
	@Test(dependsOnMethods="AddLead")
    public void AddBillingAddress() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        Response getResponse = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/getBillingAddress/" + leadId)
                .andReturn();
        System.out.println("hgugujhgj"+leadId);

        if (getResponse.getStatusCode() == 200 && getResponse.getBody().asString().contains("Please add billing address")) { //No address found
            // Proceed to add new billing address
            String file = "{\r\n"
                    + "    \"lead_id\": \"" + leadId + "\",\r\n"
                    + "    \"address_line_1\": \"njn\",\r\n"
                    + "    \"address_line_2\": \"ytuyt\",\r\n"
                    + "    \"gst_number\": \"\",\r\n"
                    + "    \"state_id\": 25,\r\n"
                    + "    \"city_id\": 114,\r\n"
                    + "    \"pincode\": \"787698\"\r\n"
                    + "}";
            
            Response postResponse = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + authToken)
                    .body(file)
                    .when()
                    .post("/addBillingAddress")
                    .andReturn();

            String res = postResponse.getBody().asString();
            System.out.println("Response body of add billing address: " + res);
            Assert.assertEquals(postResponse.getStatusCode(), 200);
            postResponse.then().assertThat().body("message", equalTo("Address added successfully."));
        } else {
            System.out.println("Address already added to this lead");
        }
    }
    
	//Update billing address
	@Test(dependsOnMethods="Login")
    public void UpdateBillingAddress() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file ="{\r\n"
        		+ "    \"lead_id\": 137,\r\n"
        		+ "    \"address_line_1\": \"new address\",\r\n"
        		+ "    \"address_line_2\": \"new address line\",\r\n"
        		+ "    \"gst_number\": \"\",\r\n"
        		+ "    \"state_id\": 25,\r\n"
        		+ "    \"city_id\": 114,\r\n"
        		+ "    \"pincode\": 787698\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/updateBillingAddress")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of update billing address :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Address updated successfully."));
    } 
	
	//Add Shipping Address
	@Test(dependsOnMethods="Login")
    public void AddShippingAddress() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file ="{\r\n"
        		+ "    \"lead_id\": 60,\r\n"
        		+ "    \"shipping_addresses\": [\r\n"
        		+ "        {\r\n"
        		+ "            \"id\": \"\",\r\n"
        		+ "            \"address_line_1\": \"sdfds\",\r\n"
        		+ "            \"address_line_2\": \"wewe\",\r\n"
        		+ "            \"state_id\": 25,\r\n"
        		+ "            \"city_id\": 114,\r\n"
        		+ "            \"pincode\": \"768686\",\r\n"
        		+ "            \"gst_number\": \"\",\r\n"
        		+ "            \"site_in_charge_mobile_number\": \"\",\r\n"
        		+ "            \"landmark\": \"\"\r\n"
        		+ "        }\r\n"
        		+ "    ]\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/addMultipleShippingAddresses")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of add shipping address :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Address added successfully."));
    }
	
	//Update Shipping address
	@Test(dependsOnMethods="Login")
    public void UpdateShippingAddress() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file ="{\r\n"
        		+ "    \"lead_id\": 60,\r\n"
        		+ "    \"shipping_addresses\": [\r\n"
        		+ "        {\r\n"
        		+ "            \"id\": \"12\",\r\n"
        		+ "            \"address_line_1\": \"sdfds\",\r\n"
        		+ "            \"address_line_2\": \"wewe\",\r\n"
        		+ "            \"state_id\": 25,\r\n"
        		+ "            \"city_id\": 114,\r\n"
        		+ "            \"pincode\": \"768686\",\r\n"
        		+ "            \"gst_number\": \"\",\r\n"
        		+ "            \"site_in_charge_mobile_number\": \"\",\r\n"
        		+ "            \"landmark\": \"\"\r\n"
        		+ "        }\r\n"
        		+ "    ]\r\n"
        		+ "}";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/addMultipleShippingAddresses")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of update shipping address :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Address added successfully."));
    }   
	
	//RECHECK STOCK
	//@Test(dependsOnMethods="Login")
    public void RecheckStock() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file ="{\r\n"
        		+ "    \"quotation_id\": \"88\",\r\n"
        		+ "    \"id\": [\r\n"
        		+ "        \"123\"\r\n"
        		+ "    ]\r\n"
        		+ "}";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/reStockCheck")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of recheck stock :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Quotation status updated successfully."));
    }  
}	
