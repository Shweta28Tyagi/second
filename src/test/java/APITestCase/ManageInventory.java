package APITestCase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ManageInventory {
	String authToken;
	int productId;
	 Faker faker = new Faker();
	  int randomCode = 10000000 + faker.number().numberBetween(0, 90000000); // Ensures the number is 6 digits
      String randomSKU= faker.lorem().characters(15, true, true);
      String randomProductName = faker.commerce().productName();	 
	
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
	        System.out.println("        ******** MANAGE INVENTORY ********");
	        Assert.assertEquals(response.getStatusCode(), 200);

	        String res = response.getBody().asString();
	        this.authToken = JsonPath.from(res).get("data.token");
	        response.then().assertThat().body("message", equalTo("Login successfully"));
	    } 
	  
	 //ADD INVENTORY
	 @Test(dependsOnMethods="Login")
	    public void AddInventory() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";       
	        
	        String file="{\r\n"
	        		+ "    \"product_name\": \""+randomProductName+"\",\r\n"
	        		+ "    \"product_sku\": \""+randomSKU+"\",\r\n"
	        		+ "    \"brand_id\": \"1\",\r\n"
	        		+ "    \"unit_of_measurement\": \"Set\",\r\n"
	        		+ "    \"country_of_manufacture\": \"2\",\r\n"
	        		+ "    \"product_uses\": [\r\n"
	        		+ "        1\r\n"
	        		+ "    ],\r\n"
	        		+ "    \"product_category\": [\r\n"
	        		+ "        1\r\n"
	        		+ "    ],\r\n"
	        		+ "    \"product_code\": \""+randomCode+"\",\r\n"
	        		+ "    \"product_hsn_code\": \"23432\",\r\n"
	        		+ "    \"product_packing\": \"wd\",\r\n"
	        		+ "    \"vendor_company_name\": \"2\",\r\n"
	        		+ "    \"product_size\": \"4\",\r\n"
	        		+ "    \"product_finish\": \"1\",\r\n"
	        		+ "    \"product_material_type\": \"2\",\r\n"
	        		+ "    \"product_weight\": \"12.00\",\r\n"
	        		+ "    \"product_quality\": \"1\",\r\n"
	        		+ "    \"web_url\": \"abc.com\",\r\n"
	        		+ "    \"final_price\": \"1200\",\r\n"
	        		+ "    \"mrp\": \"2000\",\r\n"
	        		+ "    \"discount\": 15.254,\r\n"
	        		+ "    \"gst_rate\": \"18\",\r\n"
	        		+ "    \"purchase_rate\": \"3000\",\r\n"
	        		+ "    \"inventory\": \"12\",\r\n"
	        		+ "    \"coverage\": \"2\",\r\n"
	        		+ "    \"delivery_time\": \"7\",\r\n"
	        		+ "    \"product_images\": [],\r\n"
	        		+ "    \"product_image_previews\": []\r\n"
	        		+ "}";
	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(file)
	                .when()
	                .post("/addProductInventory")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of add product :"+res);
	        this.productId = JsonPath.from(res).get("data.product_id");
	        System.out.println("Product Id :"+productId);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Product Added Successfully"));
	    } 
	 
	 //GET INVENTORY

	 @Test(dependsOnMethods="Login")
	    public void GetInventory() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	        String file="{\r\n"
	        		+ "    \"pageNumber\": 1,\r\n"
	        		+ "    \"pageSize\": 10,\r\n"
	        		+ "    \"search\": \"\",\r\n"
	        		+ "    \"sort\": \"\",\r\n"
	        		+ "    \"product_status\": [],\r\n"
	        		+ "    \"brand\": [],\r\n"
	        		+ "    \"product_material_type\": [],\r\n"
	        		+ "    \"product_quality\": [],\r\n"
	        		+ "    \"product_finish\": [],\r\n"
	        		+ "    \"product_category\": [],\r\n"
	        		+ "    \"product_uses\": [],\r\n"
	        		+ "    \"country_of_manufacture\": [],\r\n"
	        		+ "    \"vendor_company_name\": [],\r\n"
	        		+ "    \"unit_of_measurement\": [],\r\n"
	        		+ "    \"gst_rate\": []\r\n"
	        		+ "}";
	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(file)
	                .when()
	                .post("/getProductsInventory")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of product list :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Record found successfully"));
	    } 
	 
	 //Product Image
	 @Test(dependsOnMethods="Login")
	    public void AddProductImage() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	       
	        String filePath = "C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\tile1.jpg";
	        File file = Paths.get(filePath).toFile();
	        byte[] fileContent = FileUtils.readFileToByteArray(file);
	        
	        String filepath1="C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageUsers\\Image.jpg";
	        File file1 = Paths.get(filepath1).toFile();
	        byte[] fileContent1 = FileUtils.readFileToByteArray(file1);
	        
	        // Construct the multipart form data request
	        Response response = given()
	                .baseUri("https://mytyles.website:3133/api/v1")
	                .header("Authorization", "Bearer " + authToken)
	                .basePath("/addUploadMultipleImage")
	                .multiPart("id", productId) // Replace with actual id
	                .multiPart("file", file, "image/jpeg")
	                .multiPart("file", file1, "image/jpeg")
	                .header("Accept", "*/*")
	                .header("Content-Type", "multipart/form-data")
	                .when()
	                .post();

	        // Parse the response body as JSON
	        //JSONObject responseBody = new JSONObject(response.getBody().asString());
	        String res = response.getBody().asString();
	        System.out.println("Response body of add product image: " + res);

	        // Validate the response status
	        Assert.assertEquals(response.getStatusCode(),200);
	    } 
	 
	 //UPDATE PRODUCT
	 @Test(dependsOnMethods="Login")
	    public void UpdateProduct() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	        String file="{\r\n"
	        		+ "    \"id\": \""+productId+"\",\r\n"
	        		+ "    \"product_name\": \"Newtiles\",\r\n"
	        		+ "    \"product_sku\": \""+randomSKU+"\",\r\n"
	        		+ "    \"brand_id\": 1,\r\n"
	        		+ "    \"unit_of_measurement\": \"Set\",\r\n"
	        		+ "    \"country_of_manufacture\": 2,\r\n"
	        		+ "    \"product_uses\": [\r\n"
	        		+ "        1\r\n"
	        		+ "    ],\r\n"
	        		+ "    \"product_category\": [\r\n"
	        		+ "        1\r\n"
	        		+ "    ],\r\n"
	        		+ "    \"product_code\": \""+randomCode+"\",\r\n"      
	        		+ "    \"product_hsn_code\": 23432,\r\n"
	        		+ "    \"product_packing\": \"wd\",\r\n"
	        		+ "    \"vendor_company_name\": 2,\r\n"
	        		+ "    \"product_size\": 4,\r\n"
	        		+ "    \"product_finish\": 1,\r\n"
	        		+ "    \"product_material_type\": 2,\r\n"
	        		+ "    \"product_weight\": \"12.00\",\r\n"
	        		+ "    \"product_quality\": 1,\r\n"
	        		+ "    \"web_url\": \"abc.com\",\r\n"
	        		+ "    \"final_price\": 1200,\r\n"
	        		+ "    \"mrp\": 2000,\r\n"
	        		+ "    \"discount\": 15.254,\r\n"
	        		+ "    \"gst_rate\": \"18\",\r\n"
	        		+ "    \"purchase_rate\": 3000,\r\n"
	        		+ "    \"inventory\": 12,\r\n"
	        		+ "    \"coverage\": \"2\",\r\n"
	        		+ "    \"delivery_time\": 7,\r\n"
	        		+ "    \"product_status\": \"added\"\r\n"
	        		+ "}";
	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(file)
	                .when()
	                .post("/updateProductsInventory")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of update product :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Product Updated Successfully"));
	    } 	 
	 
	 //Update product status
	 @Test( dependsOnMethods="AddInventory")
	    public void UpdateProductStatus() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	        String[] statuses = {"active", "inactive"};        
	        String selectedStatus=statuses[1];
	            
	        String requestBody="{\r\n"
	        		+ "    \"id\":\""+productId+"\",\r\n" 
	        		+ "    \"product_status\":\""+selectedStatus+"\"\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(requestBody)
	                .when()
	                .post("/updateProductStatus")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of update product status :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        
	        // Add assertions based on the selected status
	        if ("active".equals(selectedStatus)) {
	            response.then().assertThat().body("message", equalTo("Product Activated Successfully"));
	        } else if ("inactive".equals(selectedStatus)) {
	            response.then().assertThat().body("message", equalTo("Product Deactivated Successfully"));
	        }
	    } 	 
	 
	 //Get product details  DROPDOWN
	 @Test( dependsOnMethods="Login")
	    public void AddProductDropdownValues() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	        String[] statuses = {"brand", "country_of_manufacture", "uses", "category", "finish", "material", "quality", "delivery_time", "vendor_company_name", "product_size"};        
	        String selectedValue=statuses[0];
	            
	        String requestBody="{\r\n"
	        		+ "    \"dropdown_data\":\""+selectedValue+"\", \r\n"
	        		+ "    \"search\":\"\"\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(requestBody)
	                .when()
	                .post("/getProductDetails")
	                .andReturn();

	        String res = response.getBody().asString();
	       // System.out.println("Response body of selected value :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        
	        // Add assertions based on the selected value
	        if ("brand".equals(selectedValue)) {
	        	//System.out.println("Response body : "+res);	 
	        	//String old = JsonPath.from(res).get("data.records");
	        	System.out.println("Response  : "+res);	 
	        	response.then().assertThat().body("message", equalTo("Record found successfully"));
	        } else if ("uses".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	        else if ("country_of_manufacture".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	        else if ("category".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	        else if ("finish".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	        else if ("material".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	        else if ("quality".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	        else if ("delivery_time".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	        else if ("vendor_company_name".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	        else if ("product_size".equals(selectedValue)) {
	        	System.out.println("Response body : "+res);	    
	            response.then().assertThat().body("message", equalTo("Record found successfully"));
	        }
	    } 
	 
	 //Delete product
	 @Test( dependsOnMethods="AddInventory")
	    public void DeleteProduct() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	            
	        String requestBody="\r\n"
	        		+ "{\r\n"
	        		+ "    \"product_ids\": [133]\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(requestBody)
	                .when()
	                .post("/deleteProducts")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of delete product :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Product Deleted Successfully"));
	    }
	 
	 //GET INVENTORY FILTER VALUES
	 @Test( dependsOnMethods="Login")
	    public void GetInventoryFilterValues() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	        
	        String[] filterValues= {"brand", "country_of_manufacture", "uses", "category", "finish", "material", "quality", "delivery_time", "vendor_company_name", "product_size"};
	        String selectedValue=filterValues[0];
            
	        String requestBody="{\r\n"
	        		+ "    \"filter_value\":\""+selectedValue+"\",\r\n"
	        		+ "    \"status\":[]\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(requestBody)
	                .when()
	                .post("/getInventoryFilterValue")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of after filter applied :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Record found successfully"));
	    } 
	 
	 //BULK EDIT
	 @Test( dependsOnMethods="Login")
	    public void BulkEdit() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	      
	        String requestBody="{\r\n"
	        		+ "  \"product_ids\": [128],\r\n"
	        		+ "    \"updated_fields\": {\r\n"
	        		+ "        \"country_of_manufacture\": \"\",\r\n"
	        		+ "        \"unit_of_measurement\":\"\",\r\n"
	        		+ "        \"product_uses\": [1],\r\n"
	        		+ "        \"product_category\": [1],\r\n"
	        		+ "        \"vendor_company_name\": \"\",\r\n"
	        		+ "        \"product_packing\": \"\",\r\n"
	        		+ "        \"brand_id\": \"\",\r\n"
	        		+ "        \"product_size\": \"\",\r\n"
	        		+ "        \"product_finish\": \"\",\r\n"
	        		+ "        \"product_weight\": \"\",\r\n"
	        		+ "        \"product_material_type\": \"\",\r\n"
	        		+ "        \"product_quality\": \"\",\r\n"
	        		+ "        \"final_price\": \"\",\r\n"
	        		+ "        \"mrp\": \"\",\r\n"
	        		+ "        \"discount\": \"\",\r\n"
	        		+ "        \"gst_rate\": \"1\",\r\n"
	        		+ "        \"purchase_rate\": \"\",\r\n"
	        		+ "        \"inventory\": \"\",\r\n"
	        		+ "        \"coverage\": \"\",\r\n"
	        		+ "        \"delivery_time\": \"\"\r\n"
	        		+ "    }\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(requestBody)
	                .when()
	                .post("/updateMultipleProductsInventory")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of bulk edit :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Bulk Edit Successfully"));
	    } 
	 
	 //BULK DELETE
	 @Test( dependsOnMethods="Login")
	    public void BulkDelete() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	            
	        String requestBody="\r\n"
	        		+ "{\r\n"
	        		+ "    \"product_ids\": [133,134]\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(requestBody)
	                .when()
	                .post("/deleteProducts")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of bulk delete :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Products Deleted Successfully"));
	    }
	 
	 //EXPORT
	 @Test( dependsOnMethods="Login")
	    public void Export() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	            
	        String requestBody="{\r\n"
	        		+ "    \"product_id\":[133]\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(requestBody)
	                .when()
	                .post("/exportProductsInventory")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of export :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Record found successfully"));
	    }
	 
	 // Get Inventory filter count
	 @Test( dependsOnMethods="Login")
	    public void GetInventoryFilterCount() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	            
	        String requestBody="{\r\n"
	        		+ "  \"brand\":[],\r\n"
	        		+ "  \"delivery_time\": [],\r\n"
	        		+ "  \"unit_of_measurement\": [],\r\n"
	        		+ "  \"product_material_type\": [],\r\n"
	        		+ "  \"product_category\": [],\r\n"
	        		+ "  \"product_uses\": [],\r\n"
	        		+ "  \"product_size\": [],\r\n"
	        		+ "  \"final_price\": \r\n"
	        		+ "{\r\n"
	        		+ "    \"min_final_price\": 1,\r\n"
	        		+ "    \"max_final_price\": 100\r\n"
	        		+ "    },\r\n"
	        		+ "  \"inventory\": {\r\n"
	        		+ "    \"min_inventory\": 5,\r\n"
	        		+ "    \"max_inventory\": 50\r\n"
	        		+ "},\r\n"
	        		+ "  \"mrp\": \r\n"
	        		+ "{\r\n"
	        		+ "    \"min_mrp\": 100,\r\n"
	        		+ "    \"max_mrp\": 10000\r\n"
	        		+ "},\r\n"
	        		+ "  \"vendor_company_name\": [],\r\n"
	        		+ "  \"coverage\": \"\",\r\n"
	        		+ "  \"purchase_rate\": \"\",\r\n"
	        		+ "  \"gst_rate\": [],\r\n"
	        		+ "  \"product_status\":[],\r\n"
	        		+ "  \"product_finish\": [],\r\n"
	        		+ "  \"product_quality\": [],\r\n"
	        		+ "  \"country_of_manufacture\": []\r\n"
	        		+ "}";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(requestBody)
	                .when()
	                .post("/getInventoryFilterCount")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of inventory filter count :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Record found successfully"));
	    }
	 
	 //Download excel for import
	 @Test( dependsOnMethods="Login")
	    public void DownloadImportExcel() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";

	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .when()
	                .post("/DownloadExcel")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of download import template :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Record found successfully"));
	    }
	 
	 //Import product
	 @Test( dependsOnMethods="Login")
	    public void ImportProduct() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";
	        
	        String filePath = "C:\\Users\\Admin\\eclipse-workspace\\REST ASSURED GOAL\\src\\main\\java\\ManageInventory\\inventory.xlsx";
	        File file = Paths.get(filePath).toFile();
	        byte[] fileContent = FileUtils.readFileToByteArray(file);

	        Response response = given()
	                .baseUri("https://mytyles.website:3133/api/v1")
	                .header("Authorization", "Bearer " + authToken)
	                .basePath("/importProductsInventory")
	                .multiPart("file", file, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	                .header("Accept", "*/*")
	                .header("Content-Type", "multipart/form-data")
	                .when()
	                .post();

	        // Parse the response body as JSON
	        //JSONObject responseBody = new JSONObject(response.getBody().asString());
	        String res = response.getBody().asString();
	        System.out.println("Response body of import product: " + res);

	        // Validate the response status
	        Assert.assertEquals(response.getStatusCode(),200);
	        response.then().assertThat().body("message", equalTo("Product Added Successfully"));
	    }
	 
	 //Bulk Print label
	 @Test( dependsOnMethods="Login")
	    public void BulkPrintLabel() throws IOException
	    {
	        RestAssured.baseURI = "https://mytyles.website:3133/api/v1";

	        String payload="{\r\n"
	        		+ "    \"product_ids\": [\r\n"
	        		+ "        141\r\n"
	        		+ "    ]\r\n"
	        		+ "}";
	        Response response = RestAssured.given()
	                .contentType(ContentType.JSON)
	                .header("Authorization", "Bearer " + authToken)
	                .body(payload)
	                .when()
	                .post("/bulkPrintProduct")
	                .andReturn();

	        String res = response.getBody().asString();
	        System.out.println("Response body of bulk print label :"+res);
	        Assert.assertEquals(response.getStatusCode(), 200);
	        response.then().assertThat().body("message", equalTo("Record found successfully"));
	    }
}

