import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static models.Endpoints.*;
import static models.Endpoints.MAIN_PAGE;
import static models.Steps.*;
import static models.Steps.sendPostCreateOrder;

public class GetOrderTests {
    public UserRequest userRequest = new UserRequest(EMAIL,PASSWORD,NAME);
    public  String accessToken;
    @Before
    public void setUp(){
        RestAssured.baseURI = MAIN_PAGE;
    }
    @Test
    public void getOrderTest(){
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
        sendPostCreateOrder(accessToken, createOrder, 200);

        GetOrderResponse getOrderResponse = sendGetOrder(accessToken, 200);
        Assert.assertTrue(getOrderResponse.getSuccess());
        Assert.assertNotNull(getOrderResponse.getOrders().get(0).get_id());
        Assert.assertEquals(ingredientFirst, getOrderResponse.getOrders().get(0).getIngredients().get(0));
        Assert.assertEquals(ingredientSecond, getOrderResponse.getOrders().get(0).getIngredients().get(1));
        Assert.assertNotNull(getOrderResponse.getOrders().get(0).getStatus());
        Assert.assertTrue(getOrderResponse.getOrders().get(0).getNumber() > 0);
        Assert.assertNotNull(getOrderResponse.getOrders().get(0).getCreatedAt());
        Assert.assertNotNull(getOrderResponse.getOrders().get(0).getUpdatedAt());
        Assert.assertTrue(getOrderResponse.getTotal() > 0);
        Assert.assertTrue(getOrderResponse.getTotalToday() > 0);
    }
    @Test
    public void getOrderWithoutAuthTest(){
        GetOrderResponse getOrderResponse = sendGetOrderWithoutAuth(401);
        Assert.assertFalse(getOrderResponse.getSuccess());
        Assert.assertEquals("You should be authorised", getOrderResponse.getMessage());
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
