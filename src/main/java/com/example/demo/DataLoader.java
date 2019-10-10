package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    CarRepository carRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        Category category = new Category("SUV");
        String bn = "Toyota";
        String col="White";
        String m="C-HR";
        double price=25000.0;
        String pic="https://article.images.consumerreports.org/f_auto/prod/content/dam/CRO%20Images%202017/Cars/March/CR-Cars-Inline-2018-Toyota-C-HR-f-03-17";
        Car car = new Car(bn,col,m,pic,price,category);
        Set<Car> cars = new HashSet<Car>();
        cars.add(car);


        bn = "Honda";
        col="Red";
        m="CR-V";
        price=20000.0;
        pic="https://content.autotrader.com/content/dam/autotrader/articles/Comparisons/2019/2019FordEscapeVS2019NissanAltima/2019%20honda%20CR-V%20(4).jpg";
        car = new Car(bn,col,m,pic,price,category);
        cars.add(car);


        category.setCars(cars);
        categoryRepository.save(category);


        category = new Category("Sport");
        bn = "Honda";
        col="White";
        m="Civic Sport";
        price=23000.0;
        pic="https://www.tracyhonda.com/wp-content/uploads/vehicle-image-SHHFK7H42KU423900-02.jpg";
        car = new Car(bn,col,m,pic,price,category);
        Set<Car> sportcars = new HashSet<Car>();
        sportcars.add(car);

        category.setCars(sportcars);
        categoryRepository.save(category);


    }
}


