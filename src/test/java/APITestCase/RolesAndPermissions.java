package APITestCase;

import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class RolesAndPermissions {
	String authToken;
	int roleId1;
	 Faker faker = new Faker();
     String randomRoleName=faker.job().title();
	 
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
        System.out.println("        ******** ROLES AND PERMISSIONS ********");
        Assert.assertEquals(response.getStatusCode(), 200);

        String res = response.getBody().asString();
        this.authToken = JsonPath.from(res).get("data.token");
        response.then().assertThat().body("message", equalTo("Login successfully"));
    } 
    
    //GET ROLES
    @Test(dependsOnMethods = "Login")
    public void GetRoles() throws IOException {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/getRoles")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of get roles :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Record found successfully"));
    }
    
    //Create role
    @Test(dependsOnMethods = "Login")
    public void CreateRole() throws IOException {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       
        String file = "{\r\n" +
                "    \"roleName\": \"" + randomRoleName + "\"\r\n" +
                "}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/createRole")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of create role :"+res);
        this.roleId1=JsonPath.from(res).get("data.roleId");
        System.out.println("Role Id :"+roleId1);
        Assert.assertEquals(response.getStatusCode(), 201);
        response.then().assertThat().body("message", equalTo("Role created successfully"));
    }
    
    //EDIT ROLE
    @Test(dependsOnMethods="Login")
    public void EditRoleName() throws IOException
    {
    	RestAssured.baseURI="https://mytyles.website:3133/api/v1";
    	String file="{\r\n"
    			+ "    \"roleId\": \""+roleId1+"\",\r\n"
    			+ "    \"roleName\": \""+randomRoleName+"\"\r\n"   
    			+ "}";
    	Response response=RestAssured.given()
    			 .contentType(ContentType.JSON)
                 .header("Authorization", "Bearer " + authToken)
                 .body(file)
                 .when()
                 .post("/editRoleName")
                 .andReturn();
    	
    	String res=response.getBody().asString();
    	System.out.println("response body of edit role :"+res);
    	Assert.assertEquals(response.statusCode(), 200);
    	response.then().assertThat().body("message", equalTo("Updated successfully"));
    }
    
    //DELETE ROLE(CUSTOM)
    @Test(dependsOnMethods="Login")
    public void DeleteRole() throws IOException
    {
    	RestAssured.baseURI="https://mytyles.website:3133/api/v1";
    	String file="{\r\n"
    			+ "    \"roleId\": 71,\r\n"
    			+ "    \"change_role_id\": \"\"\r\n"
    			+ "}";
    	Response response=RestAssured.given()
    			 .contentType(ContentType.JSON)
                 .header("Authorization", "Bearer " + authToken)
                 .body(file)
                 .when()
                 .post("/deleteRole")
                 .andReturn();
    	
    	String res=response.getBody().asString();
    	System.out.println("Response body of delete role :"+res);
    	
    	 if (res.contains("Please provide valid role id")) {
             // Handle case where the quotation is already deleted
             Assert.assertEquals(response.getStatusCode(), 401);
             response.then().assertThat().body("message", equalTo("Please provide valid role id"));
         } else if (res.contains("Deleted successfully")) { 
             // Normal case where the quotation is deleted successfully
             Assert.assertEquals(response.getStatusCode(), 200);
             response.then().assertThat().body("message", equalTo("Deleted successfully"));
         }else if(res.contains("Please Provide Other role "))
    	 {
        	 Assert.assertEquals(response.getStatusCode(), 404);
             response.then().assertThat().body("message", equalTo("Please Provide Other role to update before deleting this Role"));
    	 }
    }
    
    // Get Role Details
    @Test(dependsOnMethods = "CreateRole")
    public void GetRoleDetail() throws IOException {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file="{\r\n"
        		+ "    \"roleId\":\""+roleId1+"\"\r\n"
        		+ "}";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/getRoleDetails")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of get role details :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Record found successfully"));
    }
    
    //EDIT ROLE
    @Test(dependsOnMethods = "Login")
    public void EditRole() throws IOException {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
        String file="{\r\n"
        		+ "    \"role_name\": \"Principal Marketing Specialist\",\r\n"
        		+ "    \"role_id\": \"95\",\r\n"
        		+ "    \"permissions\": [\r\n"
        		+ "        {\r\n"
        		+ "            \"module_id\": 1,\r\n"
        		+ "            \"modulePermission\": [\r\n"
        		+ "                {\r\n"
        		+ "                    \"name\": \"read\",\r\n"
        		+ "                    \"displayName\": \"Read\",\r\n"
        		+ "                    \"isSelected\": true\r\n"
        		+ "                }\r\n"
        		+ "            ]\r\n"
        		+ "        }\r\n"
        		+ "    ]\r\n"
        		+ "}";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/editRole")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of edit role :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body("message", equalTo("Updated successfully"));
    }
}




