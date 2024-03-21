package CURD;

import java.util.List;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import Base.ApiHelper;
import Base.ApiHelper;
import Base.BaseTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import requestPojo.CreateRequestPojo;
import requestPojo.UpdateRequestPojo;
import responsePojo.CreateResponsePojo;
import responsePojo.CreateResponsePojoWithExistingName;
import responsePojo.DeleteResponseErrorPojo;
import responsePojo.GetResponsePojo;
import responsePojo.GetResponsePojoWithNonExistingRepoName;
import responsePojo.UpdateResponsePojo;
import utils.ExtentReportsUtility;
import utils.JsonSchemaValidate;
import utils.TestDataUtils;

public class gitHubFunctions extends BaseTest {
	ExtentReportsUtility report=ExtentReportsUtility.getInstance();
	ApiHelper apiHelper;
    String id,node_id,name,full_name,description,homepage;
    boolean Private;
    private Faker faker;
    String newRepoName = "";
    String repoName = "";
    
    @BeforeClass
    public void beforeClass() {
        faker = new Faker();
        apiHelper = new ApiHelper();
        repoName = "Hello-World-" + faker.number().numberBetween(1000, 2000);
        newRepoName = repoName + "-" + faker.number().numberBetween(10000, 20000); 
    }
    
    @Test(priority = 2, description = "validate Single repo in getfunction ")
    public void validateSingleRepoInGetData() { 
        Response singleRepo = apiHelper.getSingleRepo(repoName);
        GetResponsePojo getResponse = singleRepo.getBody().as(new TypeRef<GetResponsePojo>() {
        });
        Assert.assertEquals(singleRepo.getStatusCode(), HttpStatus.SC_OK, "Response code is not matching for get data.");
        report.logTestInfo("successfull , with statuscode 200");
        
        Assert.assertEquals(getResponse.getName(),repoName ,"GetRepo is not working , given repo is not in gitHub");
        JsonSchemaValidate.validateSchemaInClassPath(singleRepo, "ExpectedJsonSchema/getResponseSchema.json");
        report.logTestInfo("singleGetRepo is validated against expected schema successfully");
    }
    
    @Test(priority = 3, description = "validate Single repo in non existing repo name in getfunction ")
    public void validateSingleRepoWithNonExistingRepoNameInGetData() {
        Response singleRepoWithNonExistingRepoName = apiHelper.getSingleRepo(repoName + "-nonExist");
        GetResponsePojoWithNonExistingRepoName getResponse = singleRepoWithNonExistingRepoName.getBody().as(new TypeRef<GetResponsePojoWithNonExistingRepoName>() {
        });
        Assert.assertEquals(singleRepoWithNonExistingRepoName.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Response code is not matching for get data.");
        report.logTestInfo("successfull , with statuscode 404");
        
        Assert.assertEquals(getResponse.getMessage(),"Not Found","GetRepo is not working , given repo is not in gitHub");
        JsonSchemaValidate.validateSchemaInClassPath(singleRepoWithNonExistingRepoName, "ExpectedJsonSchema/getResponseSchemaWithNonExistingRepoName.json");
        report.logTestInfo("singleGetRepoWithNonExistingRepoName is validated against expected schema successfully");
    }
    
    @Test(priority = 4, description = "validate All repo in getfunction ")
    public void validateGetAllRepoInGetData() {
        Response AllRepo = apiHelper.getAllRepo();
        List<GetResponsePojo> getResponselist = AllRepo.getBody().as(new TypeRef<List<GetResponsePojo>>() {
        });
        GetResponsePojo Response = null;
       
        try {
        	Response = returnTheMatchingGetDataResponse(repoName, getResponselist);
        } catch (NullPointerException e) {
            Assert.fail("Added data is not available in the get data response");
        }
        Assert.assertEquals(AllRepo.getStatusCode(), HttpStatus.SC_OK, "Response code is not matching for get data.");
        report.logTestInfo("successfull , with statuscode 200");
        
        Assert.assertEquals(Response.getName(), repoName ,"actual value ["+Response.getName()+"] is not matched with Expected Value ["+name+"]");

    }
    
    @Test(priority = 0, description = "validate create repo in create function ")
    public void validateCreateRepo() {
    	name = repoName;
    	description = "This is your firstrepo";
    	homepage = "https://github.com";
    	Private = false;
    	CreateRequestPojo createRepoReq = CreateRequestPojo.builder()
    										.name(name)
    										.description(description)
    										.homepage(homepage)
    										.Private(Private)
    										.build();
    	Response response =apiHelper.createRepo(createRepoReq);
    	CreateResponsePojo createResponse = response.getBody().as(new TypeRef<CreateResponsePojo>() {});
    	Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "Add data functionality is not working as expected.");
    	report.logTestInfo("successfull , with statuscode 201");
    	Assert.assertEquals(createResponse.getName(),createRepoReq.getName() , "given repo name is created");
        JsonSchemaValidate.validateSchemaInClassPath(response, "ExpectedJsonSchema/createResponseSchema.json");
        report.logTestInfo("create repo is validated against expected schema successfully");
    }
    
    @Test(priority = 1, description = "validate create existing repo in create function ")
    public void validateCreateRepoExistingName() {
    	name = repoName;
    	description = "This is your firstrepo";
    	homepage = "https://github.com";
    	Private = false;
    	CreateRequestPojo createRepoReq = CreateRequestPojo.builder()
    										.name(name)
    										.description(description)
    										.homepage(homepage)
    										.Private(Private)
    										.build();
    	Response response =apiHelper.createRepo(createRepoReq);
    	CreateResponsePojoWithExistingName createResponse = response.getBody().as(new TypeRef<CreateResponsePojoWithExistingName>() {});
    	Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY, "Created repo with existing repo name.");
    	report.logTestInfo("successfull , with statuscode 422");
        Assert.assertEquals(createResponse.getMessage(),"Repository creation failed." , "given repo name is not created");
        JsonSchemaValidate.validateSchemaInClassPath(response, "ExpectedJsonSchema/createExsistingResponseSchema.json");
        report.logTestInfo("create existing repo is validated against expected schema successfully");
    }
    
    @Test(priority = 5, description = "validate update repo in update function ")
    public void validateUpdateRepo() {
    	name = newRepoName;
    	description = "This is your firstrepo";
    	homepage = "https://github.com";
    	Private = false;
    	UpdateRequestPojo req = UpdateRequestPojo.builder()
    										.name(name)
    										.description(description)
    										.Private(Private)
    										.build();
    	Response response =apiHelper.UpdateRepo(repoName, req);
    	UpdateResponsePojo updateResponse = response.getBody().as(new TypeRef<UpdateResponsePojo>() {});
    	Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "failed to update repo name.");
    	report.logTestInfo("successfull , with statuscode 200");
        Assert.assertEquals(updateResponse.getName(),req.getName() , "given repo name is not updated");
        JsonSchemaValidate.validateSchemaInClassPath(response, "ExpectedJsonSchema/updateResponseSchema.json");
        report.logTestInfo("updateRepo is validated against expected schema successfully");
    }
    
    @Test(priority = 6, description = "validate delete repo ")
    public void deleteRepo() {
    Response deleteRepo = apiHelper.deleteRepo(newRepoName);
    
    Assert.assertEquals(deleteRepo.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Response code is not matching for delete data.");
    report.logTestInfo("Deleted repo successfully , with statuscode 204");
  }
    
    @Test(priority = 7, description = "validate delete non existing repo ")
    public void deleteRepoWithNonexistingName() {
    Response deleteRepo = apiHelper.deleteRepo(newRepoName);
    DeleteResponseErrorPojo deleteResponse = deleteRepo.getBody().as(new TypeRef<DeleteResponseErrorPojo>() {
    });
    Assert.assertEquals(deleteRepo.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Response code is not matching for deleteing the existing repo.");
    report.logTestInfo("successfull , with statuscode 404");
    
    Assert.assertEquals(deleteResponse.getMessage() ,"Not Found" ,"deleteRepo is not working , Invalid message return instead of [Not Found]");
    JsonSchemaValidate.validateSchemaInClassPath(deleteRepo, "ExpectedJsonSchema/deleteResponseSchema.json");
    report.logTestInfo("deleteRepo is validated against expected schema successfully");
    
    }
    
	private GetResponsePojo returnTheMatchingGetDataResponse(String name, List<GetResponsePojo> getResponselist) {
		for(GetResponsePojo response:getResponselist ) {
			if(response.getName().equals(name)) {
				return response;
			}	
		}	
		return null;
	}    
}
