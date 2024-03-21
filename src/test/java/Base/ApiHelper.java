package Base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import requestPojo.CreateRequestPojo;
import requestPojo.DeleteRequestPojo;
import requestPojo.UpdateRequestPojo;

import org.apache.http.HttpStatus;
//import requestPOJO.AddDataRequest;
//import requestPOJO.DeleteDataRequest;
//import requestPOJO.LoginRequest;
//import requestPOJO.UpdateDataRequest;
//import responsePOJO.LoginResponse;
import utils.EnvironmentDetails;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class ApiHelper {
	RequestSpecification reqSpec;

	
	public ApiHelper() {
        RestAssured.baseURI = EnvironmentDetails.getProperty("baseURL");
		//String baseURI = EnvironmentDetails.getProperty("baseURL");
        reqSpec = RestAssured.given();
    }
	
	public HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        //"Authorization: Bearer <YOUR-TOKEN>"
        headers.put("Authorization", "Bearer " + EnvironmentDetails.getProperty("Token"));
        return headers;
    }
	
	public Response getSingleRepo(String repoName) {
		reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders());
        String owner =EnvironmentDetails.getProperty("Owner");
        Response response = null;
        try {
            response = reqSpec.get("/repos/" + owner + "/" + repoName);
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    } 

	public Response getAllRepo() {
		reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders());
        String owner =EnvironmentDetails.getProperty("Owner");
        Response response = null;
        try {
            response = reqSpec.get("/user/repos");
            //response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    } 
	
	public Response createRepo(CreateRequestPojo createRepoRequest) {
		reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders());
       // String owner =EnvironmentDetails.getProperty("Owner");
        Response response = null;
        try {
        	log.info("Adding below data :: " + new ObjectMapper().writeValueAsString(createRepoRequest));
        	reqSpec.body(new ObjectMapper().writeValueAsString(createRepoRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.post("/user/repos");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    } 
	
	public Response UpdateRepo(String RepoName,UpdateRequestPojo updateRepoRequest) {
		reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders());
        Response response = null;
        String owner =EnvironmentDetails.getProperty("Owner");
        try {
            reqSpec.body(new ObjectMapper().writeValueAsString(updateRepoRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.patch("/repos/" + owner + "/" + RepoName);
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Update repo functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }
	
	
	public Response deleteRepo(String RepoName) {
		reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders());
        Response response = null;
        String owner =EnvironmentDetails.getProperty("Owner");
        try {
            //reqSpec.body(new ObjectMapper().writeValueAsString(deleteRepoRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.delete("/repos/" + owner + "/" + RepoName);
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Delete Repo functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }
	
	

}
