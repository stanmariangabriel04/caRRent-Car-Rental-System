package org.example.demo;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TextField tf_last_name;
    @FXML
    private TextField tf_first_name;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_car_option;
    @FXML
    private TextField tf_days;
    @FXML
    private TableView<Car> tv_cars;
    @FXML
    private TableColumn<Car, Integer> col_option;
    @FXML
    private TableColumn<Car, String> col_brand;
    @FXML
    private TableColumn<Car, String> col_model;
    @FXML
    private TableColumn<Car, Integer> col_year;
    @FXML
    private TableColumn<Car, Double> col_price;
    @FXML
    private TableColumn<Car, String> col_image;
    @FXML
    private TableColumn<Car, String> col_location;
    @FXML
    private Button btn_rent;
    @FXML
    private VBox vbox_rent;
    @FXML
    private RadioButton rbCash;
    @FXML
    private RadioButton rbCard;
    @FXML
    private RadioButton rbTransfer;
    @FXML
    private Button btnPay;
    private ToggleGroup paymentGroup;
    @FXML
    private VBox vbox_pay;

    private int currentRentId;
    private double currentTotalPrice;

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tv_cars.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        try {
            showCars();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        paymentGroup = new ToggleGroup();
        rbCash.setToggleGroup(paymentGroup);
        rbCard.setToggleGroup(paymentGroup);
        rbTransfer.setToggleGroup((paymentGroup));

        btnPay.setDisable(true);
        vbox_pay.setVisible(false);
    }


    public void setUserInformation(String username) {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/caRRent", "postgres", "DB_PASSWORD");
    }
    public ObservableList<Car> getCarsList() throws SQLException {
        ObservableList<Car> carList = FXCollections.observableArrayList();
        Connection connection = getConnection();


        Statement st;
        ResultSet rs;

        try{
            String query = """
                        SELECT c.car_id, c.brand, c.model, c.car_year, c.price, l.city, l.address, c.image_path
                        FROM car c
                        JOIN location l ON c.location_id = l.location_id;
                        """;
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Car car;

            while(rs.next()){
                String location = rs.getString("city") + ", " + rs.getString("address");
                car = new Car(rs.getInt("car_id"), rs.getString("brand"), rs.getString("model"), rs.getInt("car_year"), rs.getDouble("price"), location, rs.getString("image_path"));
                carList.add(car);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return carList;
    }

    public void showCars() throws SQLException {
        ObservableList<Car> list = getCarsList();

        col_option.setCellValueFactory(new PropertyValueFactory<Car, Integer>("car_id"));
        col_brand.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        col_model.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
        col_year.setCellValueFactory(new PropertyValueFactory<Car, Integer>("car_year"));
        col_price.setCellValueFactory(new PropertyValueFactory<Car, Double>("price"));
        col_location.setCellValueFactory(new PropertyValueFactory<Car, String>("location"));
        col_image.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        col_image.setCellFactory(col -> new TableCell<Car, String>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(178);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                    return;
                }

                URL imageUrl = getClass().getResource("/" + imagePath);

                if (imageUrl == null) {
                    setGraphic(null);
                    return;
                }

                Image image = new Image(imageUrl.toExternalForm());
                imageView.setImage(image);
                setGraphic(imageView);

            }


        });

        tv_cars.setItems(list);
    }

    public static int insertCustomerAndRent(Customer customer, int carId, int days, double pricePerDay) throws SQLException{
        String insertCustomerSQL = "INSERT INTO customer (last_name, first_name, email, phone) VALUES (?, ?, ?, ?) RETURNING customer_id";
        String insertRentSQL = "INSERT INTO rent(customer_id, car_id, days, total_cost, rent_date) VALUES (?, ?, ?, ?, NOW()::date) RETURNING rent_id";

        try(Connection conn = getConnection()){
            conn.setAutoCommit(false);
            int customerId;
            int rentId;

            try(PreparedStatement ps = conn.prepareStatement(insertCustomerSQL)){
                ps.setString(1, customer.getLast_name());
                ps.setString(2, customer.getFirst_name());
                ps.setString(3, customer.getEmail());
                ps.setInt(4, customer.getPhone());

                ResultSet rs = ps.executeQuery();
                rs.next();
                customerId = rs.getInt("customer_id");
            }
            double totalPrice = days * pricePerDay;

            //Insert information into rent
            try(PreparedStatement ps2 = conn.prepareStatement(insertRentSQL)){
                ps2.setInt(1, customerId);
                ps2.setInt(2, carId);
                ps2.setInt(3, days);
                ps2.setDouble(4, totalPrice);

                ResultSet rs2 = ps2.executeQuery();
                rs2.next();
                rentId = rs2.getInt("rent_id");
            }
            conn.commit();
            return rentId;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public Car getCarById(int carId) throws SQLException{
        String query = """
                SELECT c.car_id, c.brand, c.model, c.car_year, c.price, l.city, l.address, c.image_path
                FROM car c
                JOIN location l ON c.location_id = l.location_id
                WHERE c.car_id = ?;
                """;
        try(Connection conn = getConnection()){
            PreparedStatement ps = conn.prepareStatement(query);{
                ps.setInt(1, carId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    String location = rs.getString("city")+ ", " + rs.getString("address");
                    return new Car(
                            rs.getInt("car_id"),
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getInt("car_year"),
                            rs.getDouble("price"),
                            location,
                            rs.getString("image_path")
                    );
                }else{
                    return null;
                }
            }
        }
    }

    @FXML
    public void rent(ActionEvent event) throws SQLException {
        //Read customer data
        String lastName = tf_last_name.getText();
        String firstName = tf_first_name.getText();
        String email = tf_email.getText();
        int phone = Integer.parseInt(tf_phone.getText());

        //Read rent data
        int carOption = Integer.parseInt(tf_car_option.getText());
        int days = Integer.parseInt(tf_days.getText());

        //Read price from table Car
        Car car = getCarById(carOption);
        double pricePerDay = car.getPrice();
        double totalPrice = days * pricePerDay;
        currentTotalPrice = totalPrice;


        Customer customer = new Customer(lastName, firstName, email, phone);

        currentRentId = insertCustomerAndRent(customer, carOption, days, pricePerDay);

        //Show rent details in application
        showRent(customer, car, days, totalPrice);
        vbox_pay.setVisible(true);
        btnPay.setDisable(false);
    }
    public void showRent(Customer customer, Car car, int days, double totalPrice){

        Label labelTitle = new Label("=== Successful rental ===");
        Label labelName = new Label("Name: " + customer.getLast_name() + " " + customer.getFirst_name());
        Label labelEmail = new Label("Email: " + customer.getEmail());
        Label labelPhone = new Label("Phone: " + customer.getPhone());
        Label labelCar = new Label("Car: " + car.getBrand() + " " + car.getModel());
        Label labelDays = new Label("Days: " + days);
        Label labelPrice = new Label("Total price: €" + totalPrice);

        labelTitle.setStyle("-fx-font-size: 20;");
        labelTitle.setStyle("-fx-font-weight: bold;");
        labelPrice.setStyle("-fx-font-weight: bold;");
        labelPrice.setStyle("-fx-font-size:15;");
        labelName.setStyle("-fx-font-size: 15;");
        labelEmail.setStyle("-fx-font-size: 15;");
        labelPhone.setStyle("-fx-font-size: 15;");
        labelCar.setStyle("-fx-font-size: 15;");
        labelDays.setStyle("-fx-font-size: 15;");
        vbox_rent.getChildren().clear();
        vbox_rent.getChildren().addAll(labelTitle, labelName, labelEmail, labelPhone, labelCar, labelDays, labelPrice);

    }

    public void pay(ActionEvent event) throws SQLException{
        RadioButton selected = (RadioButton) paymentGroup.getSelectedToggle();
        if(selected == null){
            System.out.println("Select payment method!");
            return;
        }
        String paymentMethod = selected.getText();

        //Insert payment in DB
        insertPayment(currentRentId, paymentMethod, currentTotalPrice);

        //Show payment method
        Label labelPayment = new Label("Payment method: \n" + paymentMethod);
        labelPayment.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        vbox_rent.getChildren().add(labelPayment);

        //Turn off Pay button after payment
        btnPay.setDisable(true);
    }
    public static void insertPayment(int rentId, String paymentMethod, double totalCost) throws SQLException{

        String sql = "INSERT INTO payment (rent_id, payment_method, total_cost, payment_date) VALUES (?, ?, ?, CURRENT_DATE)";
        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1, rentId);
                ps.setString(2, paymentMethod);
                ps.setDouble(3, totalCost);
                ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void executeQuery(String query) throws SQLException {
        Connection connection = getConnection();
        Statement st;
        try{
            st = connection.createStatement();
            st.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
