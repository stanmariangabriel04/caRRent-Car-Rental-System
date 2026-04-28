package org.example.demo;

public class Car {
    private int car_id;
    private String brand;
    private String model;
    private int car_year;
    private double price;
    private String location;
    private String imagePath;


    public Car(int car_id, String brand, String model, int car_year, double price, String location, String imagePath) {
        this.car_id = car_id;
        this.brand = brand;
        this.model = model;
        this.car_year = car_year;
        this.price = price;
        this.location = location;
        this.imagePath = imagePath;
    }

    public int getCar_id() {
        return car_id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getCar_year() {
        return car_year;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getImagePath(){
        return imagePath;
    }
}
