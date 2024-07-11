package APITestCase;


import java.io.File;
import java.util.Iterator;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import com.github.javafaker.Faker;

import APITestCase.Login;

public class ManageUser {
	String authToken;
	Faker faker=new Faker();
	 String randomFirstName = faker.name().firstName();
	 int userId;
	 int id;
	 int getUserId;
	 String branchName;
	 Iterator<Integer> iterator;
	 
	 
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
        System.out.println("        ******** MANAGE USER ********");
        Assert.assertEquals(response.getStatusCode(), 200);

        String res = response.getBody().asString();
        this.authToken = JsonPath.from(res).get("data.token");
        response.then().assertThat().body("message", equalTo("Login successfully"));
    } 
    
    
   // Generating random phone number
    private String generateRandomIndianPhoneNumber() {
        // Ensure it generates a 10-digit number
        return "9" + faker.number().digits(9); // Start with '9' to ensure a valid 10-digit Indian number
    }

    //ADD USER
   @Test(dependsOnMethods = "Login")
   public void AddUser() throws IOException {
       RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
      
       String randomFirstName = faker.name().firstName();
       String randomLastName = faker.name().lastName();
       String randomPhoneNumber = generateRandomIndianPhoneNumber();
       
       String payload = "{\r\n"
               + "   \"first_name\": \"" + randomFirstName + "\",\r\n"
               + "   \"last_name\": \"" + randomLastName + "\",\r\n"
               + "   \"user_email\": \"\",\r\n"
               + "   \"phone_number\": \"" + randomPhoneNumber + "\",\r\n"
               + "   \"date_of_birth\": \"\",\r\n"
               + "   \"date_of_joining\": \"\",\r\n"
               + "   \"role\":\"2\",\r\n"
               + "   \"branch\": \"\",\r\n"
               + "   \"vendor_company_name\":\"\",\r\n"
               + "   \"vendor_company_admin_id\":\"\"\r\n"
               + "}";
       
       Response response = RestAssured.given()
               .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
               .body(payload)
               .when()
               .post("/createUser")
               .andReturn();
       
       String exp = response.getBody().asString();
       System.out.println("Response body of Add User : " + exp);
       this.userId = JsonPath.from(exp).get("data.user_id");
       System.out.println("After adding user id :"+userId);
       
       Assert.assertEquals(response.statusCode(), 200);
       response.then().assertThat().body("message", equalTo("User Added Successfully"));
   }

   //UPDATE USER
   @Test(dependsOnMethods = "Login")
   public void UpdateUser() throws IOException {
       RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       
       String randomFirstName = faker.name().firstName();
       String randomLastName = faker.name().lastName();
       String randomPhoneNumber = generateRandomIndianPhoneNumber();
      
       String payload = "{\r\n"
               + "    \"id\":\"123\",\r\n"
               + "    \"first_name\":\"" + randomFirstName + "\",\r\n"
               + "    \"last_name\":\"" + randomLastName + "\",\r\n"
               + "    \"user_email\":\"\",\r\n"
               + "    \"phone_number\":\"" + randomPhoneNumber + "\",\r\n"
               + "    \"date_of_birth\":\"\",\r\n"
               + "    \"date_of_joining\":\"\",\r\n"
               + "    \"role\":\"5\",\r\n"
               + "    \"vendor_company_name\":\"\",\r\n"
               + "    \"branch\":\"2\",\r\n"
               + "    \"profile_image\":\"\"\r\n"
               + "}";

       Response response = RestAssured.given()
               .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
               .body(payload)
               .when()
               .post("/updateUser")
               .andReturn();

       String exp = response.getBody().asString();
       System.out.println("Response body of Update User: " + exp);
       Assert.assertEquals(response.statusCode(), 200);
       response.then().assertThat().body("message", equalTo("User Updated Successfully")); 
       
   }
   
   @Test(dependsOnMethods = "AddUser")
   public void GetUser() throws IOException {
       RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\getUser.json");

       Response response = RestAssured.given()
               .contentType(ContentType.JSON)
               .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
               .body(file)
               .when()
               .post("/getUsers")
               .andReturn();

       String responseBody = response.getBody().asString();
       System.out.println("Response body of get User: " + responseBody);

       JsonPath jsonPath = response.jsonPath();
       
       // Fetch IDs
          List<Integer> ids = jsonPath.getList("data.records.id");
          Assert.assertEquals(response.getStatusCode(), 200);
          assertThat("The userId should be present in the array", ids, hasItem(userId));	//to check exist records
          
          List<String> Name= jsonPath.getList("data.records.first_name");
          Assert.assertTrue(Name!=null);	
}


	@Test(dependsOnMethods = "Login")
    public void UsersCount() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\getUser.json");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .body(file)
                .when()
                .post("/getUsers")
                .andReturn();

        String exp = response.getBody().asString();
        int count = JsonPath.from(exp).get("data.count");
        System.out.println("Total Users : "+exp);
        
        String contentType = response.getContentType();
        response.then().assertThat().body("message", equalTo("Record found successfully"));
    }
   
    // UPLOAD IMAGE
    @Test
    public void UploadUserProfileImage() throws IOException {
        // Define the file to be uploaded
        String filePath = "C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\Image.jpg";
        File file = Paths.get(filePath).toFile();
        byte[] fileContent = FileUtils.readFileToByteArray(file);

        // Print the image URL from Faker (similar to your original console log)
        //String imageUrl = new com.github.javafaker.Faker().internet().avatar();
        //System.out.println("Generated image URL: " + imageUrl);

        // Construct the multipart form data request
        Response response = given()
                .baseUri("https://mytyles.website:3133")
                .basePath("/api/v1/uploadProfileImage")
                .multiPart("id", 227) // Replace with actual id
                .multiPart("file", file, "image/jpeg")
                .header("Accept", "*/*")
                .header("Content-Type", "multipart/form-data")
                .when()
                .post();

        // Parse the response body as JSON
        JSONObject responseBody = new JSONObject(response.getBody().asString());
        System.out.println("Response body of upload user profile : " + responseBody.toString(4));

        // Validate the response status
        Assert.assertEquals(response.getStatusCode(),200);
        response.then().assertThat().body("message", equalTo("Added successfully"));
    }
   
    //EXPORT USERS
    @Test(dependsOnMethods = "Login")
    
    public void ExportUser() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\ExportUsers.json");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .body(file)
                .when()
                .post("/exportUsers")
                .andReturn();
        
        String exp = response.getBody().asString();
        System.out.println("Response body of Export User : " + exp);
        Assert.assertEquals(response.statusCode(),200);
        response.then().assertThat().body("message", equalTo("Record found successfully"));
        
    }
    
    //ROLE LIST
    @Test(dependsOnMethods = "Login")
    
    public void RoleList() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\roleList.json");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .body(file)
                .when()
                .post("/roleList")
                .andReturn();
        
        String exp = response.getBody().asString();
        System.out.println("Response body of role list : " + exp);
        Assert.assertEquals(response.statusCode(),200);   
        response.then().assertThat().body("message", equalTo("Record found successfully"));
    }
    
    //GENERATE UNIQUE PASSWORD
    private String generateUniquePassword() {
        String basePassword = randomFirstName +"@0";
        Random random = new Random();
        int randomNumber = 100 + random.nextInt(900); // Generate a random number between 100 and 999
        return basePassword + randomNumber;
    }
    
    //RESET PASSWORD
    @Test(dependsOnMethods = "Login")
    public void ResetPassword() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String newPassword = generateUniquePassword();
        System.out.println(newPassword);
       
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .body("{\r\n"
                        + "    \"phone_number\":\"9999999999\",\r\n"
                        + "    \"new_password\":\"" + newPassword + "\",\r\n"
                        + "    \"confirm_password\":\"" + newPassword + "\"\r\n"
                        + "}")
                .when()
                .post("/resetPassword")
                .andReturn();
        
        String exp = response.getBody().asString();
        System.out.println("Response body of reset password : " + exp);
        Assert.assertEquals(response.statusCode(),200);
        response.then().assertThat().body("message", equalTo("Password changed successfully"));
    }
    
    //RESEND PASSWORD
    @Test(dependsOnMethods = "Login")
    
    public void ResendPassword() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\resendPassword.json");
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .body(file)
                .when()
                .post("/resendPassword")
                .andReturn();
        
        String exp = response.getBody().asString();
        System.out.println("Response body of Resend password : " + exp);
        Assert.assertEquals(response.statusCode(),200);
        response.then().assertThat().body("message", equalTo("Password sent successfully"));
    }
    
    //UPDATE USER STATUS
    @Test(dependsOnMethods = "Login")
    public void UpdateUserStatus() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        
        String[] statuses = {"active", "inactive", "deactivated"};        
        String selectedStatus=statuses[0];
            
        String requestBody="{\r\n"
        		+ "    \"id\":\"180\",\r\n"
        		+ "    \"user_status\":\"" + selectedStatus + "\"\r\n"
        		+ "}";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .body(requestBody)
                .when()
                .post("/updateStatus")
                .andReturn();
        
        String exp = response.getBody().asString();
        System.out.println("Response body of Update User Status : " + exp);
        Assert.assertEquals(response.statusCode(),200);
        
     // Conditional validation based on the selected status
        if (selectedStatus.equals("active")) {
            response.then().assertThat().body("message", equalTo("Activated Successfully"));
        } else if (selectedStatus.equals("inactive")) {
            response.then().assertThat().body("message", equalTo("Deactivated Successfully"));
        } else if (selectedStatus.equals("deactivated")) {
            response.then().assertThat().body("message", equalTo("Deleted Successfully"));
        }else {
            Assert.fail("Unknown status: " + selectedStatus);
        }
    }
    
    //GET BRANCH NAME
    @Test(dependsOnMethods = "Login")
    public void GetBranches() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .when()
                .post("/getBranchName")
                .andReturn();
        
        String res = response.getBody().asString();
        System.out.println("Response body of branches : " + res);
        
        JsonPath jsonPath = response.jsonPath();
        
        // Fetch the list of IDs
        List<Integer> ids = jsonPath.getList("data.id");
        
        // Print each ID using the iterator
        Iterator<Integer> iterator = ids.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        // Fetch the list of branch names
        List<String> branchNames = jsonPath.getList("data.branch_name");
   
        Assert.assertEquals(response.statusCode(), 200);    
        response.then().assertThat().body("message", equalTo("Record found successfully"));
        
        // Loop through IDs and branch names and print them
        for (int i = 0; i < ids.size(); i++) {
            this.id = ids.get(i);
            this.branchName = branchNames.get(i);   
            //System.out.println("ID: " + id + ", Branch Name: " + branchName);

            // Fetch a specific value from JSON using index
            int specificValue = jsonPath.getInt("data[" + i + "].id"); // Replace specificField with your actual field name
            //System.out.println("Specific Value: " + specificValue);
        }
    }     
    
    //GET VENDOR COMPANY NAMES
    @Test(dependsOnMethods = "Login")
    public void GetVendorCompany() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
              
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken) // Pass authToken in the header
                .when()
                .post("/getVendorCompanyNames")
                .andReturn();
        
        String exp = response.getBody().asString();
        System.out.println("Response body of Vendor company : " + exp);
        
        Assert.assertEquals(response.statusCode(),200);
        response.then().assertThat().body("message", equalTo("Record found successfully"));
        
        JsonPath jsonPath = response.jsonPath();
        
        // Fetch the list of IDs
           List<Integer> ids = jsonPath.getList("data.vendor_company_admin_id");
           // Fetch vendor company names
           List<String> vendor = jsonPath.getList("data.vendor_company_name");
           
           for (int i = 0; i < ids.size(); i++) {
               int id = ids.get(i);
               String VendorCompanyName = vendor.get(i);   
               //System.out.println("ID: " + id + ", Vendor Company Name: " + VendorCompanyName);
           }
    }
}





