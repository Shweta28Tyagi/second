package APITestCase;

import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ManageInquiry {
	String authToken;
	 
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
        System.out.println("        ******** MANAGE INQUIRY ********");
        Assert.assertEquals(response.getStatusCode(), 200);

        String res = response.getBody().asString();
        this.authToken = JsonPath.from(res).get("data.token");
        response.then().assertThat().body("message", equalTo("Login successfully"));
    } 
    
    //Get all inquiry
    @Test(dependsOnMethods="Login")
    public void GetVendorInquiry() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       String[] inquiryTab= {"new_inquiry","responded_inquiry","order_placed_inquiry"};   
       String selectedValue=inquiryTab[1];
       
        String file="{\r\n"
        		+ "    \"pageNumber\": 1,\r\n"
        		+ "    \"pageSize\": 10,\r\n"
        		+ "    \"enquiry_tab\": \""+selectedValue+"\",\r\n"
        		+ "    \"search\": \"\",\r\n"
        		+ "    \"sort\": \"createdDateDesc\",\r\n"
        		+ "    \"dateRange\": {\r\n"
        		+ "        \"type\": \"all\",\r\n"
        		+ "        \"startDate\": \"\",\r\n"
        		+ "        \"endDate\": \"\"\r\n"
        		+ "    }\r\n"
        		+ "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/getVendorQuotations")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of get vendor inquiry :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        
     // Add assertions based on the selected value
        if ("new_inquiry".equals(selectedValue)) {
        	System.out.println("Response  : "+res);	 
        	response.then().assertThat().body("message", equalTo("Record found successfully."));
        } else if ("responded_inquiry".equals(selectedValue)) {
        	System.out.println("Response body : "+res);	    
            response.then().assertThat().body("message", equalTo("Record found successfully."));
        }
        else if ("order_placed_inquiry".equals(selectedValue)) {
        	System.out.println("Response body : "+res);	    
            response.then().assertThat().body("message", equalTo("Record found successfully."));
        }
    } 
    
    //Update Inquiry status
    @Test(dependsOnMethods="Login")
    public void UpdateInquiryStatus() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       String[] inquiryTab= {"Not Available","Available Multiple Batches","Available"};   
       String selectedValue=inquiryTab[2];
       
        String file="{\r\n"
        		+ "    \"id\": 118,\r\n"
        		+ "    \"stock_check_status\": \""+selectedValue+"\",\r\n"
        		+ "    \"not_available_quantity\": \"\",\r\n"
        		+ "    \"not_available_in_days\": \"\"\r\n"
        		+ "}";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/updateQuotationProductStatus")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of update inquiry status :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        
     // Add assertions based on the selected value
        if ("Not Available".equals(selectedValue)) {
        	System.out.println("Response  : "+res);	 
        	response.then().assertThat().body("message", equalTo("Updated successfully."));
        } else if ("Available".equals(selectedValue)) {
        	System.out.println("Response body : "+res);	    
            response.then().assertThat().body("message", equalTo("Updated successfully."));
        }
        else if ("Available Multiple Batches".equals(selectedValue)) {
        	System.out.println("Response body : "+res);	    
            response.then().assertThat().body("message", equalTo("Updated successfully."));
        }	
    } 
    
    //EXPORT
    @Test(dependsOnMethods="Login")
    public void ExportInquiry() throws IOException
    {
        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
       String[] inquiryTab= {"new_inquiry", "order_placed_inquiry", "responded_inquiry"};
       String selectedValue=inquiryTab[0];
        String file="{\r\n"
        		+ "    \"dateRange\": {\r\n"
        		+ "        \"type\": \"all\",\r\n"
        		+ "        \"startDate\": \"\",\r\n"
        		+ "        \"endDate\": \"\"\r\n"
        		+ "    },\r\n"
        		+ "    \"enquiry_tab\": \""+selectedValue+"\",\r\n"
        		+ "    \"search\": \"\"\r\n"
        		+ "}";
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(file)
                .when()
                .post("/exportVendorQuotations")
                .andReturn();

        String res = response.getBody().asString();
        System.out.println("Response body of export inquiry :"+res);
        Assert.assertEquals(response.getStatusCode(), 200);
        
     // Add assertions based on the selected value   
        if ("new_inquiry".equals(selectedValue)) {
        	System.out.println("Response  : "+res);	 
        	response.then().assertThat().body("message", equalTo("Record found successfully."));
        } else if ("responded_inquiry".equals(selectedValue)) {
        	System.out.println("Response body : "+res);	    
            response.then().assertThat().body("message", equalTo("Record found successfully."));
        }
        else if ("order_placed_inquiry".equals(selectedValue)) {
        	System.out.println("Response body : "+res);	    
            response.then().assertThat().body("message", equalTo("Record found successfully."));
        }
    } 
}
