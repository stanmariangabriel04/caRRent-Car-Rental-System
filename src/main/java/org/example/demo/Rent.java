package org.example.demo;

public class Rent {
    private String customerName;
    private String email;
    private int phone;
    private String carModel;
    private int days;
    private double totalPrice;

    public Rent(String customerName, String email, int phone, String carModel, int days, double totalPrice) {
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.carModel = carModel;
        this.days = days;
        this.totalPrice = totalPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public int getPhone() {
        return phone;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
