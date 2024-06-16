import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.UserRequest;
import models.UserResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static models.Endpoints.*;
import static models.Steps.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTests {
    public UserRequest userRequest = new UserRequest(EMAIL, PASSWORD, NAME);
    public UserRequest userRequestDuplicate = new UserRequest(EMAIL, PASSWORD, NAME);
    public String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = MAIN_PAGE;
    }

    @Test
    public void CreateUserPozitiveTest() {
        Response response = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(response, 200);
        UserResponse actualBody = getUserResponse(response);
        accessToken = actualBody.getAccessToken();
        validateResponseIsNotNull(actualBody);
        UserResponse expectedBody = new UserResponse(true, "", new UserRequest(EMAIL, PASSWORD, NAME),"","");
        compareUserResponse(actualBody,expectedBody);
    }
    @Test
    public void CreateUserDuplicateTest(){
        Response response = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(response, 200);
        UserResponse actualBody = getUserResponse(response);
        accessToken = actualBody.getAccessToken();
        validateResponseIsNotNull(actualBody);
        UserResponse expectedBody = new UserResponse(true,"", new UserRequest(EMAIL, PASSWORD, NAME),"","");
        compareUserResponse(actualBody, expectedBody);
        response = sendPostRequestCreateUser(userRequestDuplicate);
        compareStatusCodeUser(response, 403);
        UserResponse failedResponseActualBody = new UserResponse(false, "User already exists");
        validateResponseIsNotNull(failedResponseActualBody);
        compareFailedUserResponse(failedResponseActualBody, new UserResponse(false,"User already exists"));

    }
    @Test
    public void CreateUserWithoutEmailTest(){
        Response response = sendPostRequestCreateUser(new UserRequest(null, PASSWORD, NAME));
        compareStatusCodeUser(response,403);
        UserResponse actualBody = getUserResponse(response);
        validateResponseIsNotNull(actualBody);
        compareFailedUserResponse(actualBody, new UserResponse(false,"Email, password and name are required fields"));
    }

    @Test
    public void CreateUserWithoutNameTest(){
        Response response = sendPostRequestCreateUser(new UserRequest(EMAIL,PASSWORD,null));
        compareStatusCodeUser(response,403);
        UserResponse actualBody = getUserResponse(response);
        validateResponseIsNotNull(actualBody);
        compareFailedUserResponse(actualBody, new UserResponse(false,"Email, password and name are required fields"));
    }

    @Test
    public void CreateUserWithoutPasswordTest(){
        Response response = sendPostRequestCreateUser(new UserRequest(EMAIL, null, NAME));
        compareStatusCodeUser(response,403);
        UserResponse actualBody = getUserResponse(response);
        validateResponseIsNotNull(actualBody);
        compareFailedUserResponse(actualBody, new UserResponse(false,"Email, password and name are required fields"));
    }


    @After
    public void deleteData(){
        if(accessToken != null)
        given()
                .header(HEADER_TYPE,HEADER_JSON)
                .header(AUTHORIZATION, accessToken)
                .delete(DELETE_USER);
    }
}
