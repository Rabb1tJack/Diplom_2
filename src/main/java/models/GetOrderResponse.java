package models;

import java.util.ArrayList;
import java.util.Date;

public class GetOrderResponse {
    private Boolean success;
    private ArrayList<Order> orders;
    private int total;
    private int totalToday;

    private String message;

    public class Order{
        private ArrayList<String> ingredients;
        private String _id;
        private String status;
        private int number;
        private Date createdAt;
        private Date updatedAt;

        public Order(ArrayList<String> ingredients, String _id, String status, int number, Date createdAt, Date updatedAt) {
            this.ingredients = ingredients;
            this._id = _id;
            this.status = status;
            this.number = number;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public ArrayList<String> getIngredients() {
            return ingredients;
        }

        public void setIngredients(ArrayList<String> ingredients) {
            this.ingredients = ingredients;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    public GetOrderResponse(Boolean success, ArrayList<Order> orders, int total, int totalToday, String message) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}