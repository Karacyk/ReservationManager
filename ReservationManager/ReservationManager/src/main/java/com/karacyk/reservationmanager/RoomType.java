package com.karacyk.reservationmanager;

public class RoomType {
    String type;
    Integer price;
    //tutaj po prostu roomtype
    public RoomType(String type, Integer price){
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public Integer getPrice() {
        return price;
    }
}
