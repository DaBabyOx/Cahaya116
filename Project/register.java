package Project;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class register {
    public register(Stage primaryStage){
        primaryStage.setTitle("Cahaya 116");

        Label title = new Label("Register");
        title.setStyle("-fx-font-size: 48px;");

        Label uname = new Label("Name");
        uname.setStyle("-fx-font-size: 18px;");

        Label pass = new Label("Password");
        pass.setStyle("-fx-font-size: 18px;");

        Label email = new Label("Email");
        email.setStyle("-fx-font-size: 18px;");

        Label gender = new Label("Gender");
        gender.setStyle("-fx-font-size: 18px;");

        Label dob = new Label("Date of Birth");
        dob.setStyle("-fx-font-size: 18px;");

        TextField unameField = new TextField();
        unameField.setMinHeight(30);
        unameField.setMinWidth(500);
        unameField.setMaxWidth(500);

        PasswordField passField = new PasswordField();
        passField.setMinHeight(30);
        passField.setMinWidth(500);
        passField.setMaxWidth(500);

        TextField emailField = new TextField();
        emailField.setMinHeight(30);
        emailField.setMinWidth(500);
        emailField.setMaxWidth(500);

        RadioButton mRadBut = new RadioButton("Male");
        RadioButton fRadBut = new RadioButton("Female");

        ToggleGroup genderGroup = new ToggleGroup();
        mRadBut.setToggleGroup(genderGroup);
        fRadBut.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(mRadBut, fRadBut);
        genderBox.setAlignment(Pos.CENTER_LEFT);

        DatePicker dobField = new DatePicker();
        dobField.setMinHeight(30);
        dobField.setMinWidth(500);
        dobField.setMaxWidth(500);

        VBox unamelayout = new VBox(5, uname, unameField);
        VBox passlayout = new VBox(5, pass, passField);
        VBox emaillayout = new VBox(5, email, emailField);
        VBox genderlayout = new VBox(5, gender, genderBox);
        VBox doblayout = new VBox(5, dob, dobField);

        VBox upperlayout = new VBox(10, title, unamelayout, passlayout, emaillayout, genderlayout, doblayout);
        upperlayout.setAlignment(Pos.CENTER);

        Button registerButton = new Button("Register");
        registerButton.setMinWidth(100);
        registerButton.setMinHeight(50);
        registerButton.setMaxHeight(50);
        registerButton.setOnAction(e ->{
            String name = unameField.getText();
            String password = passField.getText();
            String emailAdd = emailField.getText();
            String genderVal = mRadBut.isSelected() ? "Male" : fRadBut.isSelected() ? "Female" : null;
            String dobVal = dobField.getValue().toString();
            String role = name.toLowerCase().contains("admin") ? "Admin" : "User";
            if(name.isEmpty() || password.isEmpty() || emailAdd.isEmpty() || genderVal.isEmpty() || dobVal.isEmpty()){
                showError("Error", "Register Error", "All fields are required.");}

            try {
            if(DBConnect.checkUsername(name)) {
                    showError("Error", "Register Error", "Please choose another username");}
            }catch (SQLException ex){
                ex.printStackTrace();}

           try {
               if(DBConnect.checkEmail(emailAdd)){
                   showError("Error", "Register Error", "Please choose another email");}
           }catch (SQLException ex){
               ex.printStackTrace();}

           if(!emailAdd.endsWith("@gmail.com")){
               showError("Error", "Register Error", "Make sure your email ends with '@gmail.com'");}

           else if(password.length() < 8 || password.length()>15){
               showError("Error", "Register Error", "Make sure your password cannot be less than 8 or more than 15 characters");}

           else if(!alphanumericval(password)){
                showError("Error", "Register Error", "Make sure your password is alphanumeric");}

           else{
               try{
                   DBConnect.registerUser(name, emailAdd, password, genderVal, role, dobVal);
                     new login(primaryStage);
               }catch (SQLException ex){
                   ex.printStackTrace();}}});

        Hyperlink loginLink = new Hyperlink("Already have an account? Click here to login!");
        loginLink.setStyle("-fx-font-size: 18px; -fx-text-fill: black");
        loginLink.setOnAction(e -> new login(primaryStage));

        VBox lowerlayout = new VBox(10, registerButton, loginLink);
        lowerlayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(50, upperlayout, lowerlayout);
        layout.setMaxWidth(400);
        layout.setAlignment(Pos.CENTER);

        BorderPane border = new BorderPane();
        border.setCenter(layout);
        BorderPane.setAlignment(layout, Pos.CENTER);

        Scene scene = new Scene(border, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();}

    private void showError(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();}

    private boolean alphanumericval(String name) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : name.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;}
            else if (Character.isDigit(c)) {
                hasDigit = true;}
            if (hasLetter && hasDigit) {
                return true;}}
        return false;}
}
