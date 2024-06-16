import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.UserRequest;
import models.UserResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static models.Steps.*;
import static models.Endpoints.*;

public class AuthUserTests {
    public UserRequest userRequest = new UserRequest(EMAIL, PASSWORD, NAME);
    public String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = MAIN_PAGE;
    }

    @Test
    public void authUserTest(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser, 200);
        UserResponse actualToken = getUserResponse(createUser);
        accessToken = actualToken.getAccessToken();
        Response response = sendPostRequestAuthUser(userRequest);
        compareStatusCodeUser(response, 200);
        UserResponse actualBody = getUserResponse(response);
        validateResponseIsNotNull(actualBody);
        UserResponse expectedBody = new UserResponse(true, "", new UserRequest(EMAIL, PASSWORD, NAME), "","");
        compareUserResponse(actualBody, expectedBody);

    }

    @Test
    public void authUserNegativeEmail(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser, 200);
        UserResponse actualToken = getUserResponse(createUser);
        accessToken = actualToken.getAccessToken();
        Response response = sendPostRequestAuthUser(new UserRequest("wrong_email",PASSWORD));
        compareStatusCodeUser(response, 401);
        UserResponse actualBody = getUserResponse(response);
        validateResponseIsNotNull(actualBody);
        UserResponse expectedBody = new UserResponse(false,"email or password are incorrect");
        compareFailedUserResponse(actualBody,expectedBody);

    }

    @Test
    public void authUserNegativePassword() {
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser, 200);
        UserResponse actualToken = getUserResponse(createUser);
        accessToken = actualToken.getAccessToken();
        Response response = sendPostRequestAuthUser(new UserRequest(EMAIL, "wrong_password"));
        compareStatusCodeUser(response, 401);
        UserResponse actualBody = getUserResponse(response);
        validateResponseIsNotNull(actualBody);
        UserResponse expectedBody = new UserResponse(false, "email or password are incorrect");
        compareFailedUserResponse(actualBody, expectedBody);

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
