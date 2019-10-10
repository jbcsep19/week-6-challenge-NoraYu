package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface CarRepository extends CrudRepository<Car, Long> {

    ArrayList<Car> findByCarNameContainingIgnoreCaseOrColorContainingIgnoreCaseOrMakeContainingIgnoreCase(String s1,String s2,String s3);

}


