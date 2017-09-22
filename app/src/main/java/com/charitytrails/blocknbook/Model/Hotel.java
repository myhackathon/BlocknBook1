package com.charitytrails.blocknbook.Model;

import com.charitytrails.blocknbook.Utils.Geocoordinates;

import java.io.Serializable;


/**
 * Created by ghassen.ati on 25/05/2016.
 */

public class Hotel implements Serializable{

    private int id;
    private String name;
    private Integer starsRating;
    private Double price;
    private Double discountPrice;
    private String photo;

    public Hotel() {
        super();
    }

    public Hotel(int id, String location, String name, Integer starsRating, Double price, Double discountPrice, Integer score, String photo, Geocoordinates geocoordinates) {
        this.id = id;
        this.name = name;
        this.starsRating = starsRating;
        this.price = price;
        this.discountPrice = discountPrice;
        this.photo = photo;
    }

    public Hotel( String name, Integer starsRating, Double price) {
        this.name = name;
        this.starsRating = starsRating;
        this.price = price;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStarsRating() {
        return starsRating;
    }

    public void setStarsRating(Integer starsRating) {
        this.starsRating = starsRating;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }



    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



    @Override
    public String toString() {
        return "Name: "+this.name + " | "+this.starsRating+" Price: "+ this.price + " | Discount Price: "+this.discountPrice;
    }

}
