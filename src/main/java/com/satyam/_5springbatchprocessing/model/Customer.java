package com.satyam._5springbatchprocessing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Customer {

    @Id
    private Integer customerId;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}