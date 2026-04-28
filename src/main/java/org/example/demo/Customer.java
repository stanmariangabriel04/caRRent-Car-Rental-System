package org.example.demo;

public class Customer {
    private String last_name;
    private String first_name;
    private String email;
    private int phone;


    public Customer(String last_name, String first_name, String email, int phone) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone = phone;
    }

    public int getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }
}
