package com.seasapps.shoppingapp.model;

public class OffersModel {

    String price;
    String delivery;
    String seller_name;
    String positive_rating;

    public OffersModel(String price, String delivery, String seller_name, String positive_rating) {
        this.price = price;
        this.delivery = delivery;
        this.seller_name = seller_name;
        this.positive_rating = positive_rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getPositive_rating() {
        return positive_rating;
    }

    public void setPositive_rating(String positive_rating) {
        this.positive_rating = positive_rating;
    }
}
