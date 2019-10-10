package com.example.demo;

import java.io.Serializable;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Car implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long carID;
    private String carName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String posteddate;
    private String color;
    private String make;
    private String pic;
    private double price;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    public long getCarID() {
        return carID;
    }

    public void setCarID(long carID) {
        this.carID = carID;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getPosteddate() {

        return posteddate;
    }


    public void setPosteddate() {
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

        this.posteddate = dateFormat.format(date);
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDefaultPic() {
        String url="https://article.images.consumerreports.org/f_auto/prod/content/dam/CRO%20Images%202018/Magazine/12December/CR-Magazine-Inline-Road-Test-December2018Issue-HondaInsight-10-18";
        this.pic = url.toString();
    }

    public Car() {
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        this.posteddate = dateFormat.format(date);
        this.setDefaultPic();
    }
    public Car(String carName, String color,String make,double price,Category category) {
        this();
        this.carName = carName;
        this.category = category;
        this.color=color;
        this.make=make;
        this.price=price;
    }

    public Car(String carName, String color,String make,String pic,double price, Category category) {
        this();
        this.carName = carName;
        this.category = category;
        this.color=color;
        this.make=make;
        this.pic=pic;
        this.price=price;
    }
}
