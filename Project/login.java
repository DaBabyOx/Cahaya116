package Project;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class login {
    public login(Stage primaryStage){
        primaryStage.setTitle("Cahaya 116");

        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 48px;");

        Label uname = new Label("Username");
        uname.setStyle("-fx-font-size: 18px;");

        Label pass = new Label("Password");
        pass.setStyle("-fx-font-size: 18px;");

        TextField unameField = new TextField();
        unameField.setMinHeight(30);
        unameField.setMinWidth(500);
        unameField.setMaxWidth(500);

        PasswordField passField = new PasswordField();
        passField.setMinHeight(30);
        passField.setMinWidth(500);
        passField.setMaxWidth(500);

        VBox unamelayout = new VBox(5, uname, unameField);
        VBox passlayout = new VBox(5, pass, passField);

        VBox upperlayout = new VBox(10, title, unamelayout, passlayout);
        upperlayout.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setMinWidth(100);
        loginButton.setMinHeight(50);
        loginButton.setMaxHeight(50);
        loginButton.setOnAction(e ->{
            String name = unameField.getText();
            String password = passField.getText();

            if(name.isEmpty()){
                showError("Error", "Login Error", "please fill out your username");}
            else if(password.isEmpty()){
                showError("Error", "Login Error", "please fill out your password");}
            else{
                try{
                    if(DBConnect.validateLogin(name, password)){
                        String userRole = DBConnect.getURole(name);
                        getSession.getInstance().setName(name);
                        if(userRole != null){
                            if(userRole.equals("Admin")){
                                new menumanagement(primaryStage);}
                            else if(userRole.equals("User")){
                                new home(primaryStage);}}
                        else{
                            showError("Error", "Login Error", "Credentials must match!");}}
                }catch (SQLException ex){
                    ex.printStackTrace();}}
        });

        Hyperlink registerLink = new Hyperlink("Don't have an account yet? Register Here!");
        registerLink.setStyle("-fx-font-size: 18px;");
        registerLink.setOnAction(e -> new register(primaryStage));

        VBox lowerlayout = new VBox(10, loginButton, registerLink);
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
        alert.showAndWait();}}