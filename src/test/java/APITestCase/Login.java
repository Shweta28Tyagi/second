package APITestCase;

import org.hamcrest.core.Is;
import com.github.javafaker.Faker;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.testng.Assert;
import static io.restassured.RestAssured.*;

import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.text.ParseException;
public class Login 
{
   public static String authToken;
    String otpToken;
    int userID;
    String userName;
    String nameInCamelCase;
    Faker faker=new Faker();
    String newPassword;

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
        System.out.println("        ******** PRE LOGIN ********");
        Assert.assertEquals(response.getStatusCode(), 200);

        String res = response.getBody().asString();
        this.authToken = JsonPath.from(res).get("data.token");
        response.then().assertThat().body("message", equalTo("Login successfully"));
 
        System.out.println("LOGIN TOKEN : " + authToken);
       String[] chunks = authToken. split("\\.");      
       Base64.Decoder decoder = Base64.getUrlDecoder();

//       String header = new String(decoder.decode(chunks[0])); 
//       System.out.println(header)
       String payload = new String(decoder.decode(chunks[1]));
       this.userID=JsonPath.from(payload).get("userId");
       System.out.println("User Id of logged in user : " +userID);
       
       userName=JsonPath.from(payload).get("firstName");
      
       nameInCamelCase = userName.substring(0, 1).toUpperCase() + userName.substring(1);  //convert name with first alphabet capital
       System.out.println("Name of user logged in : "+nameInCamelCase);
       
       Assert.assertEquals(response.getStatusCode(), 200);  
       response.then().assertThat().body("message", equalTo("Login successfully")); 
    } 
    
    //LOGIN WITH OTP
    @Test(priority=1)
    public void LoginWithOtp() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        File file = new File("C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\PreLogin\\OtpLogin.json");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .post("/login")
                .andReturn();

        String exp = response.getBody().asString();
        System.out.println("Login with OTP :" + exp);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("OTP sent successfully")); 
    }
    
    //OTP VERIFICATION
    @Test(priority=2)
    public void OtpVerification() throws IOException
    {
    	RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file = "{\r\n" +
                "    \"phone_number\": \"9999999999\",\r\n" +
                "    \"verify_otp\": \"444444\",\r\n" +
                "    \"verification_type\": \"login\"\r\n" + // Removed the trailing comma and added closing brace
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .post("/otpVerification")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of login with OTP: " + res);

        // Check if the response is valid JSON and contains the expected key
        try {
            JsonPath jsonPath = JsonPath.from(res);
            this.otpToken = jsonPath.getString("data.token");
        } catch (Exception e) {
            System.out.println("Failed to parse JSON response: " + e.getMessage());
        }

        if (res.contains("Otp must be a 6 digit number")) {
            Assert.assertEquals(response.getStatusCode(), 403);
            response.then().assertThat().body("message", equalTo("Otp must be a 6 digit number"));
        } else if (res.contains("please register your account or provided otp is not correct")) {
            Assert.assertEquals(response.getStatusCode(), 403);
            response.then().assertThat().body("message", equalTo("please register your account or provided otp is not correct"));
        } else if (res.contains("Login successfully")) {
            Assert.assertEquals(response.getStatusCode(), 200);
            response.then().assertThat().body("message", equalTo("Login successfully"));
        } else {
            // Handle unexpected response
            Assert.fail("Unexpected response: " + res);
        }
    }   
    
  //GENERATE UNIQUE PASSWORD
    private String generateUniquePassword() {
        String basePassword = "Test" +"@";
        Random random = new Random();
        int randomNumber = 100 + random.nextInt(900); // Generate a random number between 100 and 999
        return basePassword + randomNumber;
    }
    
    //RESET PASSWORD
    @Test(priority=3)
    public void ResetPassword() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        this.newPassword = generateUniquePassword();
       
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
    
    @Test(dependsOnMethods="ResetPassword")
    public void CheckResetPassword() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file="{\r\n"
        		+ "    \"phone_number\": \"9999999999\",\r\n"
        		+ "    \"password\": \""+newPassword+"\",\r\n"
        		+ "    \"login_type\": \"password\"\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .post("/login")
                .andReturn();
        
        String res = response.getBody().asString();
        System.out.println("Response body to check login after reset password"+res);
       
       Assert.assertEquals(response.getStatusCode(), 200);  
       response.then().assertThat().body("message", equalTo("Login successfully")); 
    } 
    
    // Logged in user profile
    @Test(priority=4,dependsOnMethods = "Login")
    public void UserProfile() throws IOException
    {
    	RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
     
        String userLoginProfile="{\r\n" +
                "    \"id\":\"" + userID + "\"\r\n" +
                "}";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(userLoginProfile)
                .when()
                .post("/getUserProfile")
                .andReturn();
    
        String body = response.getBody().asString();
        System.out.println("Response body of user : " +body);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Record found successfully")); 
    }
    
    //LOGOUT
    @Test
    public void Logout() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
                
        String payl="{\r\n"
        		+ "    \"users_device_token\":\"authToken\"\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payl)
                .when()
                .post("/logout")
                .andReturn();

        String exp = response.getBody().asString();
        System.out.println("Response body of logout : "+exp);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Logout successfully"));   
    }
}

