package models;

public class CreateOrderResponse {
    private String[] ingredients;
    private String name;
    private Order order;
    private Boolean success;
    private String message;
    public class Order {
        private int number;
        public Order(int number) {
            this.number = number;
        }
        public int getNumber() {
            return number;
        }
        public void setNumber(int number) {
            this.number = number;
        }
    }
    public CreateOrderResponse(String[] ingredients) {
        this.ingredients = ingredients;
    }
    public CreateOrderResponse(String[] ingredients, String name, Order order, Boolean success, String message) {
        this.ingredients = ingredients;
        this.name = name;
        this.order = order;
        this.success = success;
        this.message = message;
    }
    public String[] getIngredients() {
        return ingredients;
    }
    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

