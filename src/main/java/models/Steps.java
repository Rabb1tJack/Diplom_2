package models;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static models.Endpoints.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class Steps {
    @Step("Send POST request to /api/auth/register")
    public static Response sendPostRequestCreateUser(UserRequest request) {
        return given()
                .header(HEADER_TYPE, HEADER_JSON)
                .and()
                .body(request)
                .post(CREATE_USER);
    }
    @Step("Send POST request to /api/auth/login")
    public static Response sendPostRequestAuthUser(UserRequest request){
        return given()
                .header(HEADER_TYPE, HEADER_JSON)
                .and()
                .body(request)
                .post(AUTH_USER);
    }
    @Step("Send patch request to /api/auth/user")
    public static Response patchUserData(UserRequest request, String token){
            return given()
                    .header(HEADER_TYPE,HEADER_JSON)
                    .header(AUTHORIZATION, token)
                    .and()
                    .body(request)
                    .patch(PATH_USER_INFO);
    }
    @Step("Send patch request to /api/auth/user")
    public static Response patchUserDataWithoutAuth(UserRequest request){
        return given()
                .header(HEADER_TYPE,HEADER_JSON)
                .and()
                .body(request)
                .patch(PATH_USER_INFO);
    }
    @Step("Compare Response.statusCode to something")
    public static void compareStatusCodeUser(Response response, int expectedStatusCode) {
        int actualStatusCode = response.statusCode();
        MatcherAssert.assertThat(actualStatusCode, is(expectedStatusCode));
    }
    @Step("Deserialize Response.body as object type of CreateUserResponse")
    public static UserResponse getUserResponse(Response response) {
        return response.body().as(UserResponse.class);
    }
    @Step("Validate CreateUserResponse object is not null")
    public static void validateResponseIsNotNull(Object actualBody) {
        MatcherAssert.assertThat(actualBody, notNullValue());
    }
    @Step("Compare CreateUserResponse object to something")
    public static void compareUserResponse(UserResponse actualBody, UserResponse expectedBody){
        MatcherAssert.assertThat(actualBody.isSuccess(), is(expectedBody.isSuccess()));
        Assert.assertEquals(actualBody.getUser().getName(), expectedBody.getUser().getName());
        Assert.assertEquals(actualBody.getUser().getEmail(), expectedBody.getUser().getEmail());
    }
    @Step("Compare FailedCreateUserResponse object to something")
    public static void compareFailedUserResponse(UserResponse failedResponseActualBody, UserResponse expectedBody){
        MatcherAssert.assertThat(failedResponseActualBody.isSuccess(), is(expectedBody.isSuccess()));
        MatcherAssert.assertThat(failedResponseActualBody.getMessage(), is(expectedBody.getMessage()));
    }
    @Step("Send GET request get ingredients to /api/ingredients")
    public static Ingredients sendGetIngredients() {
        return given()
            .header(HEADER_TYPE,HEADER_JSON)
            .get(GET_INGREDIENTS)
            .then()
            .statusCode(200)
            .extract().as(Ingredients.class);
    }
    @Step("Send POST request create order to /api/orders")
    public static CreateOrderResponse sendPostCreateOrder(String token, CreateOrderResponse order, int expectedCode) {
        return given()
                .header(HEADER_TYPE,HEADER_JSON)
                .header(AUTHORIZATION, token)
                .body(order)
                .post(CREATE_ORDER)
                .then()
                .statusCode(expectedCode)
                .extract().as(CreateOrderResponse.class);
    }
    @Step("Send POST request create order to /api/orders without accessToken")
    public static CreateOrderResponse sendPostCreateOrderWithoutAuth(CreateOrderResponse order, int expectedCode) {
        return given()
                .header(HEADER_TYPE,HEADER_JSON)
                .body(order)
                .post(CREATE_ORDER)
                .then()
                .statusCode(expectedCode)
                .extract().as(CreateOrderResponse.class);
    }

    @Step("Send GET request get order to /api/orders")
    public static GetOrderResponse sendGetOrder(String token, int expectedCode) {
        return given()
                .header(HEADER_TYPE,HEADER_JSON)
                .header(AUTHORIZATION, token)
                .get(GET_ORDER)
                .then()
                .statusCode(expectedCode)
                .extract().as(GetOrderResponse.class);
    }

    @Step("Send GET request get order to /api/orders without accessToken")
    public static GetOrderResponse sendGetOrderWithoutAuth(int expectedCode) {
        return given()
                .header(HEADER_TYPE,HEADER_JSON)
                .get(GET_ORDER)
                .then()
                .statusCode(expectedCode)
                .extract().as(GetOrderResponse.class);
    }
    @Step("Deserialize Response.body as object type of CreateUserResponse")
    public static GetOrderResponse getOrderResponse(Response response) {
        return response.body().as(GetOrderResponse.class);
    }
}

