import io.qameta.allure.internal.shadowed.jackson.annotation.JsonTypeInfo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.UserRequest;
import models.UserResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static models.Endpoints.*;
import static models.Steps.*;

public class PatchUserDataTests {
    public UserRequest userRequest = new UserRequest(EMAIL, PASSWORD, NAME);
    public String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = MAIN_PAGE;
    }
    @Test
    public void PatchUserEmailTest(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser, 200);
        UserResponse tokenForDeleter = getUserResponse(createUser);
        accessToken = tokenForDeleter.getAccessToken();

        Response response = sendPostRequestAuthUser(new UserRequest(EMAIL,PASSWORD));
        compareStatusCodeUser(response, 200);
        UserResponse actualToken = getUserResponse(response);

        Response patchUserEmail = patchUserData(new UserRequest("anotheremail@af.ru"), actualToken.getAccessToken());
        UserResponse actualBody = getUserResponse(patchUserEmail);
        validateResponseIsNotNull(actualBody);
        compareStatusCodeUser(patchUserEmail,200);
        String actual = actualBody.getUser().getEmail();
        Assert.assertEquals("anotheremail@af.ru", actual);

    }

    @Test
    public void PatchUserNameTest(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser, 200);
        UserResponse tokenForDeleter = getUserResponse(createUser);
        accessToken = tokenForDeleter.getAccessToken();

        Response response = sendPostRequestAuthUser(new UserRequest(EMAIL,PASSWORD));
        compareStatusCodeUser(response, 200);
        UserResponse actualToken = getUserResponse(response);

        Response patchUserName = patchUserData(new UserRequest(null, null, "anotherName"), actualToken.getAccessToken());
        UserResponse actualBody = getUserResponse(patchUserName);
        validateResponseIsNotNull(actualBody);
        compareStatusCodeUser(patchUserName,200);
        Assert.assertEquals("anotherName", actualBody.getUser().getName());

    }

    @Test
    public void PatchUserPasswordTest(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser, 200);
        UserResponse tokenForDeleter = getUserResponse(createUser);
        accessToken = tokenForDeleter.getAccessToken();

        Response response = sendPostRequestAuthUser(new UserRequest(EMAIL,PASSWORD));
        compareStatusCodeUser(response, 200);
        UserResponse actualToken = getUserResponse(response);

        Response patchUserPassword = patchUserData(new UserRequest(null, "anotherpass", null), actualToken.getAccessToken());
        UserResponse actualBody = getUserResponse(patchUserPassword);
        validateResponseIsNotNull(actualBody);
        compareStatusCodeUser(patchUserPassword,200);


    }

    @Test
    public void PatchUserDataWithoutAuth(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser, 200);
        UserResponse tokenForDeleter = getUserResponse(createUser);
        accessToken = tokenForDeleter.getAccessToken();

        Response patchUserEmail = patchUserDataWithoutAuth(new UserRequest(EMAIL));

        UserResponse actualBody = getUserResponse(patchUserEmail);
        validateResponseIsNotNull(actualBody);
        compareStatusCodeUser(patchUserEmail,401);

        UserResponse expectedBody = new UserResponse(false, "You should be authorised");
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
