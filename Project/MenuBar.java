package Project;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import java.sql.SQLException;

public class MenuBar extends javafx.scene.control.MenuBar {
    public static MenuBar createMenuBar(Stage primaryStage, String page) {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        if(page.equals("home") || page.equals("cart")){
            MenuItem home = new MenuItem("Home");
            MenuItem cart = new MenuItem("Cart");
            MenuItem logout = new MenuItem("Logout");

            home.setOnAction(e ->{
                try{
                    new home(primaryStage);
                }catch(SQLException ex){
                    throw new RuntimeException(ex);}});
            cart.setOnAction(e ->{
                try{
                    new cart(primaryStage);
                }catch(SQLException ex){
                    throw new RuntimeException(ex);}});
            logout.setOnAction(e -> new login(primaryStage));

            menu.getItems().addAll(home, cart, logout);}

        else if(page.equals("menumanagement")){
            MenuItem menumanagement = new MenuItem("Menu Management");
            MenuItem logout = new MenuItem("Logout");

            menumanagement.setOnAction(e ->{
                try{
                    new menumanagement(primaryStage);
                }catch(SQLException ex){
                    throw new RuntimeException(ex);}});
            logout.setOnAction(e -> new login(primaryStage));

            menu.getItems().addAll(menumanagement, logout);}

        menuBar.getMenus().add(menu);
        return menuBar;}}