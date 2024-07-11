package APITestCase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ManageProfile {
	String authToken;
	 String Doj;
	
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
	        System.out.println("        ******** MANAGE PROFILE ********");
	        Assert.assertEquals(response.getStatusCode(), 200);

	        String res = response.getBody().asString();
	        this.authToken = JsonPath.from(res).get("data.token");
	        response.then().assertThat().body("message", equalTo("Login successfully"));
	    } 
	 
	 //Get User profile
	 @Test(dependsOnMethods = "Login")
	    public void GetUserProfile() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	        String file="{\r\n"
	        		+ "    \"id\": 1\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(file)
	                .when()
	                .post("/getUserProfile")
	                .andReturn();
	        
	        String res = response.getBody().asString();
	        JsonPath jsonPath = response.jsonPath();
	       // String dateOfJoining = jsonPath.getString("data.user.date_of_joining");
	        this.Doj = jsonPath.getString("data.date_of_joining");
	 
	        System.out.println("Response body of loggedIn User :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        
	    } 
	 
	 //Upload Image
	 @Test(dependsOnMethods = "Login")
	   public void UploadProfileImage() throws IOException
	   {
		   RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
		   
		   String filePath = "C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\tile1.jpg";
	       File file = Paths.get(filePath).toFile();
	       byte[] fileContent = FileUtils.readFileToByteArray(file);

	       // Construct the multipart form data request
	       Response response = given()
	               .baseUri("https://mytyles.website:3133/api/v1")
	               .header("Authorization", "Bearer " + authToken)
	               .basePath("/uploadProfileImage")
	               .multiPart("id", 1) // Replace with actual id
	               .multiPart("file", file, "image/jpeg")
	               .header("Accept", "*/*")
	               .header("Content-Type", "multipart/form-data")
	               .when()
	               .post();

	       // Parse the response body as JSON
	       JSONObject responseBody = new JSONObject(response.getBody().asString());
	       System.out.println("Response body of upload profile image: " + responseBody.toString(4));

	       // Validate the response status
	       Assert.assertEquals(response.getStatusCode(),200);   
	   }
	 
	 //Remove profile image
	 @Test(dependsOnMethods = "Login")
	   public void RemoveProfileImage() throws IOException
	   {
		   RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
		
		   String file="{\r\n"
		   		+ "    \"id\": 1\r\n"
		   		+ "}";

		   Response response = given()
	    		   .contentType(ContentType.JSON)
	               .header("Authorization", "Bearer " + authToken)
	               .body(file)
	               .when()
	               .post("/RemoveProfileImage");
		   
		   //JSONObject responseBody = new JSONObject(response.getBody().asString());  //JSON object
		   String res = response.getBody().asString();
	       System.out.println("Response body of remove profile image: " + res);

	       // Validate the response status
	       Assert.assertEquals(response.getStatusCode(),200);
	       response.then().assertThat().body("message", equalTo("Profile Image removed successfully."));   
	   }    
	 
	 //UPDATE USER PROFILE
	 @Test(dependsOnMethods = "GetUserProfile")
	   public void UpdateUserProfile() throws IOException
	   {
		   RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
		   Faker faker=new Faker();
		   String randomFirstName = faker.name().firstName();
		   String randomLastName = faker.name().lastName();
		   String email=faker.internet().emailAddress();
		   
		   String file="{\r\n"
		   		+ "    \"id\": 1,\r\n"
		   		+ "    \"first_name\": \""+randomFirstName+"\",\r\n"
		   		+ "    \"last_name\": \""+randomLastName+"\",\r\n"
		   		+ "    \"user_email\": \""+email+"\",\r\n"
		   		+ "    \"date_of_birth\": \"2013-01-01\",\r\n"
		   		+ "    \"date_of_joining\":\"2048-01-01\",\r\n"
		   		+ "    \"phone_number\": 9999999999,\r\n"
		   		+ "    \"change_password\": {}\r\n"
		   		+ "}";
		 

		   Response response = given()
	    		   .contentType(ContentType.JSON)
	               .header("Authorization", "Bearer " + authToken)
	               .body(file)
	               .when()
	               .post("/updateUserProfile");
		   
		   //JSONObject responseBody = new JSONObject(response.getBody().asString());  //JSON object
		   String res = response.getBody().asString();
	       System.out.println("Response body of edit user profile : " + res);

	       // Validate the response status
	       Assert.assertEquals(response.getStatusCode(),200);
	       response.then().assertThat().body("message", equalTo("User Updated Successfully"));   
	   } 
	 
	//GENERATE UNIQUE PASSWORD
	    private String generateUniquePassword() {
	        String basePassword = "Test" +"@";
	        Random random = new Random();
	        int randomNumber = 100 + random.nextInt(900); // Generate a random number between 100 and 999
	        return basePassword + randomNumber;
	    }
	 
	 //Change password
	 @Test(dependsOnMethods = "Login")
	   public void ChangePassword() throws IOException
	   {
		   RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
		   Faker faker=new Faker();
		   String randomFirstName = faker.name().firstName();
		   String randomLastName = faker.name().lastName();
		   String email=faker.internet().emailAddress();
		   String newPassword = generateUniquePassword();
		   
		   String file="{\r\n"
		   		+ "    \"id\":1,\r\n"
		   		+ "    \"first_name\": \""+randomFirstName+"\",\r\n"
		   		+ "    \"last_name\": \""+randomLastName+"\",\r\n"
		   		+ "    \"user_email\": \""+email+"\",\r\n"
		   		+ "    \"date_of_birth\":\"\", \r\n"
		   		+ "    \"date_of_joining\":\"\",\r\n"
		   		+ "    \"change_password\":{\r\n"
		   		+ "        \"new_password\":\""+newPassword+"\",\r\n"
		   		+ "        \"confirm_password\":\""+newPassword+"\"\r\n"
		   		+ "    }\r\n"
		   		+ "}\r\n"
		   		+ "";

		   Response response = given()
	    		   .contentType(ContentType.JSON)
	               .header("Authorization", "Bearer " + authToken)
	               .body(file)
	               .when()
	               .post("/updateUserProfile");
		   
		   //JSONObject responseBody = new JSONObject(response.getBody().asString());  //JSON object
		   String res = response.getBody().asString();
	       System.out.println("Response body of change password : " + res);

	       // Validate the response status
	       Assert.assertEquals(response.getStatusCode(),200);
	       response.then().assertThat().body("message", equalTo("User Updated Successfully"));   
	   } 
}
