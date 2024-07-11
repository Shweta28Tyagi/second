package APITestCase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.io.FileUtils;  //IMAGE UPLOAD
import org.hamcrest.Matchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ManageLead {
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
        System.out.println("        ******** MANAGE LEAD ********");
        Assert.assertEquals(response.getStatusCode(), 200);

        String res = response.getBody().asString();
        this.authToken = JsonPath.from(res).get("data.token");
//        System.out.println("Response body of login : " +res);
//        System.out.println("Login Token : " +authToken);
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
        		+ "        \"city\": \"115\",\r\n"
        		+ "        \"state\": \"25\",\r\n"
        		+ "        \"pincode\": \"879898\",\r\n"
        		+ "        \"address_line_1\": \"aa\",\r\n"
        		+ "        \"address_line_2\": \"ss\",\r\n"
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
        System.out.println("Response body of add lead : " + exp);
        this.leadId = JsonPath.from(exp).get("data.lead_id");
        System.out.println("Added lead id : " + leadId);

        Assert.assertEquals(response.statusCode(),200);
    }

    // GET LEADS
   @Test(dependsOnMethods = "Login")
    public void GetLeads() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\GetLeads.json");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .body(file)
                .when()
                .post("/getLeads")
                .andReturn();
        String exp = response.getBody().asString();
        System.out.println("Response body of get lead : " + exp);

        int count = JsonPath.from(exp).get("data.count");
        System.out.println("Total lead count : "+count);
        
        Assert.assertEquals(response.statusCode(),200);
        //Assert.assertEquals(response.body("message", equals("Record found successfully.")));        
    }
   
   //UPDATE LEAD
   @Test(dependsOnMethods = "Login")
   public void UpdateLead() throws IOException
   {
       RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       
       String payload="{\r\n"
       		+ "    \"requirements\": [\r\n"
       		+ "        4\r\n"
       		+ "    ],\r\n"
       		+ "    \"id\": 104,\r\n"
       		+ "    \"full_name\": \"Rudolf Moen\",\r\n"
       		+ "    \"primary_phone\": 9001035667,\r\n"
       		+ "    \"secondary_phone\": 9641801267,\r\n"
       		+ "    \"email\": \"grady.cruickshank@hotmail.com\",\r\n"
       		+ "    \"created_by\": \"2024-06-04\",\r\n"
       		+ "    \"lead_type\": \"Walkin\",\r\n"
       		+ "    \"lead_source\": \"Offline\",\r\n"
       		+ "    \"lead_owner_id\": 2,\r\n"
       		+ "    \"notes\": \"mLLD12XvZMrx7XO\",\r\n"
       		+ "    \"billing_address\": {\r\n"
       		+ "        \"city_id\": 115,\r\n"
       		+ "        \"state_id\": 25,\r\n"
       		+ "        \"pincode\": 879898,\r\n"
       		+ "        \"address_line_1\": \"aa\",\r\n"
       		+ "        \"address_line_2\": \"ss\",\r\n"
       		+ "        \"gst_number\": \"\"\r\n"
       		+ "    }\r\n"
       		+ "}";
       Response response = RestAssured.given()
               .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
               .body(payload)
               .when()
               .post("/updateLead")
               .andReturn();
       String exp = response.getBody().asString();
//       String leadId = JsonPath.from(exp).get("data.lead_id");
//       System.out.println("Lead Id : "+ leadId);
//       
       Assert.assertEquals(response.statusCode(),200);
   }
   
   //ADD ATTACHMENTS
   @Test(dependsOnMethods = "Login")
   public void UploadLeadAttachment() throws IOException
   {
	   RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	   
	   String filePath = "C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\tile1.jpg";
       File file = Paths.get(filePath).toFile();
       byte[] fileContent = FileUtils.readFileToByteArray(file);

       // Print the image URL from Faker (similar to your original console log)
       String imageUrl = new com.github.javafaker.Faker().internet().avatar();
       System.out.println("Generated image URL: " + imageUrl);

       // Construct the multipart form data request
       Response response = given()
               .baseUri("https://mytyles.website:3133/api/v1")
               .header("Authorization", "Bearer " + authToken)
               .basePath("/addUploadMultipleAttachments")
               .multiPart("id", 104) // Replace with actual id
               .multiPart("file", file, "image/jpeg")
               .header("Accept", "*/*")
               .header("Content-Type", "multipart/form-data")
               .when()
               .post();

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body: " + responseBody.toString(4));

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
       
   }
   
   //EXPORT LEADS
   @Test(dependsOnMethods = "Login")
   public void ExportLeads() throws IOException
   {
	   RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	  
	   File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\exportLead.json");
	   
       Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/exportLeads");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body: " + responseBody.toString(4));

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //Add task
   @Test(dependsOnMethods="Login")
   public void AddTaskForLead() throws IOException
   {
	   RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	  
	   File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\addTask.json");
	   String payload="{\r\n"
	   		+ "    \"lead_id\": 101,\r\n"
	   		+ "    \"follow_up_date\": \"2024-07-03\",\r\n"
	   		+ "    \"follow_up_time\": \"13:20\",\r\n"
	   		+ "    \"subject\": \"NEW TASK\",\r\n"
	   		+ "    \"task_details\": \"average rule\",\r\n"
	   		+ "    \"reminder\": true\r\n"
	   		+ "}";
       Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(payload)
               .when()
               .post("/createLeadTask");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body: " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //GET LEAD TASK
   @Test(dependsOnMethods="Login")
   public void GetLeadTask() throws IOException
   {
	   RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	  
	   File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\getLeadTask.json");
	   
       Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/getLeadTask");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of all lead task : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //GET LEAD HISTORY
   @Test(dependsOnMethods="Login")
   public void GetLeadHistory() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\getLeadHistory.json");
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/getLeadHistory");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of all lead task : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //ADD LEAD ACTIVITY
   @Test(dependsOnMethods="Login")
   public void AddLeadActivity() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\createLeadActivity.json");
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/createLeadActivity");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of add lead activity : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //GET ACTIVITY
   @Test(dependsOnMethods="Login")
   public void GetLeadActivity() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   //File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\getActivityType.json");
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .when()
               .post("/getActivityType");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of get lead activities : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //CITIES
   @Test(dependsOnMethods="Login")
   public void GetCity() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\getCities.json");
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/getCities");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of get cities : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //ALL STATES
   @Test(dependsOnMethods="Login")
   public void GetStates() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .when()
               .post("/getStates");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of get states : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //GET LEAD DETAILS
   @Test(dependsOnMethods="Login")
   public void GetLeadDetails() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\getLeadDetails.json");
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/getLeadDetails");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of lead details : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //CHECK LEAD NUMBERS
   @Test(dependsOnMethods="Login")
   public void CheckLeadNumbers() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageLeads\\checkLeadNumbers.json");
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/checkLeadNumbers");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of checking lead numbers : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //UPDATE LEAD STAGE
   @Test(dependsOnMethods="Login")
   public void UpdateLeadStage() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   String payload="{\r\n"
	   		+ "    \"id\":\""+leadId+"\",\r\n"     
	   		+ "    \"lead_stage\":\"Lost\",\r\n"
	   		+ "    \"comment\":\"\"\r\n"
	   		+ "}";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(payload)
               .when()
               .post("/updateLeadStage");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of update lead stage : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
    
   //GET LEAD STAGE  
   @Test(dependsOnMethods="Login")
   public void GetLeadStage() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .when()
               .post("/getLeadStage");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of get lead stage : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //GET REQUIREMENTS
   @Test(dependsOnMethods="Login")
   public void GetRequirement() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .when()
               .post("/getRequirements");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of get requirements : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //MARK LEAD AS STAR
   @Test(dependsOnMethods="Login")
   public void MarkLeadAsStar() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   String file="{\r\n"
	   		+ "\"lead_id\": 101,\r\n"
	   		+ "\"is_star_marked\":false \r\n"
	   		+ "}";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/markLeadAsStar");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of mark lead as star : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //GET LEAD TASK BY ID
   @Test(dependsOnMethods="Login")
   public void GetTaskByLeadId() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   String file="{\r\n"
	   		+ "    \"lead_id\":101\r\n"
	   		+ "}";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/getLeadTaskByLeadId");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of  get lead task by id : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //Mark Task as Completed
   @Test(dependsOnMethods="Login")
   public void MarkTaskAsCompleted() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   String file="{\r\n"
	   		+ "    \"task_id\":78\r\n"
	   		+ "}\r\n"
	   		+ "";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/markTaskAsCompleted");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body of mark task as completed : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //RESCHEDULE TASK
   @Test(dependsOnMethods="Login")
   public void RescheduleTask() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   String file="{\r\n"
	   		+ "    \"task_id\": 68,\r\n"
	   		+ "    \"subject\": \"Follow up with Mrs. Chantelle Kulas\",\r\n"
	   		+ "    \"follow_up_date\": \"2024-06-14\",\r\n"
	   		+ "    \"follow_up_time\": \"16:50\",\r\n"
	   		+ "    \"task_details\": \"m\",\r\n"
	   		+ "    \"reminder\": true\r\n"
	   		+ "}";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/rescheduleTask");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body reschedule task : " + responseBody);

       // Validate the response status
       Assert.assertEquals(response.getStatusCode(),200);
   }
   
   //DELETE LEAD ATTACHMENT
   @Test(dependsOnMethods="Login")
   public void DeleteAttachment() throws IOException
   {
	   RestAssured.baseURI="https://mytyles.website:3133/api/v1";
	   
	   String file="{\r\n"
	   		+ "    \"id\":[1]\r\n"
	   		+ "}";
	   
	   Response response = given()
    		   .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken)
               .body(file)
               .when()
               .post("/deleteAttachments");

       // Parse the response body as JSON
       JSONObject responseBody = new JSONObject(response.getBody().asString());
       System.out.println("Response body delete attachment : " + responseBody);

       int statusCode = response.getStatusCode();
       if (statusCode == 200) {
           System.out.println("Test passed.");
       } else {
           System.out.println("Test failed with status code: " + statusCode);
       }
       
       // Assert the status code
       Assert.assertEquals(statusCode, 200);
   }
}
