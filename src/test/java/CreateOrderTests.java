import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static models.Endpoints.*;
import static models.Steps.*;

public class CreateOrderTests {
    public UserRequest userRequest = new UserRequest(EMAIL,PASSWORD,NAME);
    public  String accessToken;
    @Before
    public void setUp(){
        RestAssured.baseURI = MAIN_PAGE;
    }
    @Test
    public void createOrderTestWithAuth(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser,200);
        UserResponse tokenForDeleter = getUserResponse(createUser);
        accessToken = tokenForDeleter.getAccessToken();
        Response response = sendPostRequestAuthUser(new UserRequest(EMAIL,PASSWORD));
        compareStatusCodeUser(response, 200);

        Ingredients ingredients = sendGetIngredients();
        validateResponseIsNotNull(ingredients);
        String ingredientFirst = ingredients.getData().get(0).get_id();
        String ingredientSecond = ingredients.getData().get(1).get_id();
        String[] order = new String[]{ingredientFirst, ingredientSecond};
        CreateOrderResponse createOrder = new CreateOrderResponse(order);

        CreateOrderResponse createNewOrder = sendPostCreateOrder(accessToken, createOrder, 200);
        Assert.assertTrue(createNewOrder.getSuccess());
        Assert.assertNotNull(createNewOrder.getName());
        Assert.assertTrue(createNewOrder.getOrder().getNumber() > 0);
    }

    @Test
    public void createOrderTestWithoutAuth() {
        Ingredients ingredients = sendGetIngredients();
        validateResponseIsNotNull(ingredients);
        String ingredientFirst = ingredients.getData().get(0).get_id();
        String ingredientSecond = ingredients.getData().get(1).get_id();
        String[] order = new String[]{ingredientFirst, ingredientSecond};
        CreateOrderResponse createOrder = new CreateOrderResponse(order);

        CreateOrderResponse createNewOrder = sendPostCreateOrderWithoutAuth(createOrder, 200);
        Assert.assertTrue(createNewOrder.getSuccess());
        Assert.assertNotNull(createNewOrder.getName());
        Assert.assertTrue(createNewOrder.getOrder().getNumber() > 0);
    }
    @Test
    public void createOrderTestWithoutIngredients(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser,200);
        UserResponse tokenForDeleter = getUserResponse(createUser);
        accessToken = tokenForDeleter.getAccessToken();
        Response response = sendPostRequestAuthUser(new UserRequest(EMAIL,PASSWORD));
        compareStatusCodeUser(response, 200);

        String[] order = new String[]{};
        CreateOrderResponse createOrder = new CreateOrderResponse(order);
        CreateOrderResponse createNewOrder = sendPostCreateOrder(accessToken, createOrder, 400);

        String expected = "Ingredient ids must be provided";
        Assert.assertFalse(createNewOrder.getSuccess());
        Assert.assertEquals(expected,createNewOrder.getMessage());
    }
    @Test
    public void createOrderTestWrongIngredients(){
        Response createUser = sendPostRequestCreateUser(userRequest);
        compareStatusCodeUser(createUser,200);
        UserResponse tokenForDeleter = getUserResponse(createUser);
        accessToken = tokenForDeleter.getAccessToken();
        Response response = sendPostRequestAuthUser(new UserRequest(EMAIL,PASSWORD));
        compareStatusCodeUser(response, 200);

        String[] order = new String[]{null, null};
        CreateOrderResponse createOrder = new CreateOrderResponse(order);
        CreateOrderResponse createNewOrder = sendPostCreateOrder(accessToken, createOrder, 400);

        Assert.assertFalse(createNewOrder.getSuccess());
        Assert.assertEquals("One or more ids provided are incorrect",createNewOrder.getMessage());
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
