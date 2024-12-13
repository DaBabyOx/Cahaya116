package Project;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class menumanagement {
    private TableView<product> productTable;
    private String id;
    public menumanagement(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Cahaya 116");

        MenuBar menuBar = MenuBar.createMenuBar(primaryStage, "menumanagement");
        productTable = createTable();
        productTable.setMaxWidth(500);
        productTable.setMaxHeight(800);
        productTable.setMinHeight(800);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        productTable.setItems(DBConnect.getProducts());

        Label nameLabel = new Label("Menu Name");
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label priceLabel = new Label("Menu Price");
        priceLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label descLabel = new Label("Menu Description");
        descLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setMinWidth(150);
        nameField.setMaxWidth(150);

        TextField priceField = new TextField();
        priceField.setMinWidth(150);
        priceField.setMaxWidth(150);

        TextArea descField = new TextArea();
        descField.setWrapText(true);
        descField.setMinWidth(150);
        descField.setMaxWidth(150);

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                id = newSelection.getId();
                nameField.setText(newSelection.getName());
                priceField.setText(String.valueOf(newSelection.getPrice()));
                descField.setText(newSelection.getDescription());}});

        Button addButton = new Button("Add Menu");
        addButton.setMinWidth(150);
        addButton.setMinHeight(75);
        addButton.setOnAction(e -> {
            if(nameField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "name cannot be empty");}
            else if(priceField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "price cannot be empty");}
            else if(descField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "description cannot be empty");}
            else {
                try {
                    if(DBConnect.checkMenu(nameField.getText())){
                        showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "Name must be unique");}
                    else if(Integer.parseInt(priceField.getText()) < 5000 || Integer.parseInt(priceField.getText()) > 1000000){
                        showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "price must range from 5000 - 10000000");}
                    else if(descField.getText().length() < 5 || descField.getText().length() > 255){
                        showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "Menu description must range from 5 - 255 characters");}
                    else{
                        try {
                            DBConnect.addProduct(nameField.getText(), Integer.parseInt(priceField.getText()), descField.getText());
                            productTable.setItems(DBConnect.getProducts());
                            productTable.refresh();
                            showInfo(Alert.AlertType.INFORMATION, "Success", "Item has been added to the menu", "");
                            nameField.clear();
                            priceField.clear();
                            descField.clear();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);}}
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);}}});

        Button updateButton = new Button("Update Menu");
        updateButton.setMinWidth(150);
        updateButton.setMinHeight(75);
        updateButton.setOnAction(e -> {
            if(nameField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "name cannot be empty");}
            else if(priceField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "price cannot be empty");}
            else if(descField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "description cannot be empty");}
            else {
                try {
                    if(DBConnect.checkMenu(nameField.getText())){
                        showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "Name must be unique");}
                    else if(Integer.parseInt(priceField.getText()) < 5000 || Integer.parseInt(priceField.getText()) > 1000000){
                        showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "price must range from 5000 - 10000000");}
                    else if(descField.getText().length() < 5 || descField.getText().length() > 255){
                        showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "Menu description must range from 5 - 255 characters");}
                    else{
                        try {
                            DBConnect.updateProduct(id, nameField.getText(), Integer.parseInt(priceField.getText()), descField.getText());
                            productTable.setItems(DBConnect.getProducts());
                            productTable.refresh();
                            nameField.clear();
                            priceField.clear();
                            descField.clear();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);}}
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);}}});

        Button deleteButton = new Button("Delete Menu");
        deleteButton.setMinWidth(150);
        deleteButton.setMinHeight(75);
        deleteButton.setOnAction(e -> {
            if(nameField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "name cannot be empty");}
            else if(priceField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "price cannot be empty");}
            else if(descField.getText().isEmpty()){
                showInfo(Alert.AlertType.ERROR, "Error", "Menu Management Error", "description cannot be empty");}
            else {
                try {
                    DBConnect.deleteProduct(id);
                    productTable.setItems(DBConnect.getProducts());
                    productTable.refresh();
                    nameField.clear();
                    priceField.clear();
                    descField.clear();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);}}});

        VBox buttonLayout = new VBox(5, addButton, updateButton, deleteButton);
        buttonLayout.setAlignment(Pos.CENTER);

        VBox formLayout = new VBox(5, nameLabel, nameField, priceLabel, priceField, descLabel, descField);
        formLayout.setAlignment(Pos.CENTER_LEFT);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(formLayout);
        borderPane.setCenter(productTable);
        borderPane.setRight(buttonLayout);
        borderPane.setTop(menuBar);
        BorderPane.setAlignment(formLayout, Pos.CENTER);
        BorderPane.setAlignment(productTable, Pos.CENTER);
        BorderPane.setAlignment(buttonLayout, Pos.CENTER);

        Scene scene = new Scene(borderPane, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();}

    private TableView<product> createTable(){
        TableView<product> table = new TableView<>();
        TableColumn<product, String> name = new TableColumn<>("Menu Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<product, String> price = new TableColumn<>("Menu Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(name, price);

        for (TableColumn<?, ?> column : table.getColumns()) {
            column.setPrefWidth(150);}
        return table;}

    private void showInfo(Alert.AlertType here, String title, String header, String content){
        Alert alert = new Alert(here);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();}
}