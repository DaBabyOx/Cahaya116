package Project;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.control.window.Window;

import java.sql.SQLException;

public class cart {
    private TableView<carts> cartTable;
    private String username = getSession.getInstance().getName();
    private StackPane sp;
    int checksum;
    private ComboBox<courier> courier;

    public cart(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Cahaya 116");

        MenuBar menuBar = MenuBar.createMenuBar(primaryStage, "cart");

        Label label = new Label(username + "'s Cart");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        cartTable = createTable();
        cartTable.setMaxHeight(800);
        cartTable.setMinHeight(800);
        cartTable.setPrefWidth(500);
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(cartTable, Priority.ALWAYS);
        cartTable.setItems(DBConnect.fetchCart(username));

        Label del = new Label("Delete Item");
        del.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button delete = new Button("Delete Item");
        delete.setMinHeight(75);
        delete.setMinWidth(250);
        delete.setOnAction(e -> {
            carts selectedCart = cartTable.getSelectionModel().getSelectedItem();
            if (selectedCart != null) {
                try {
                    DBConnect.deleteCart(selectedCart.getName(), username);
                    cartTable.setItems(DBConnect.fetchCart(username));
                    cartTable.refresh();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);}}
            else {
                showError("Error", "Deletion Error", "Please select the item to be deleted");}});

        Label cour = new Label("Courier");
        cour.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ObservableList<courier> couriers = DBConnect.fetchCourier();
        courier = new ComboBox<>(couriers);
        courier.setMinWidth(250);

        courier.setConverter(new StringConverter<courier>() {
            @Override
            public String toString(courier c) {
                return (c != null) ? c.getName() : "";}

            @Override
            public courier fromString(String string) {
                return couriers.stream()
                        .filter(c -> c.getName().equals(string))
                        .findFirst()
                        .orElse(null);}});

        Label courPrice = new Label("Courier Price");
        courPrice.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        courier.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                courPrice.setText("Courier Price: " + newVal.getPrice());}
            else {
                courPrice.setText("Courier Price: ");}});

        CheckBox check = new CheckBox("Provide Eating Utensils");

        Label total = new Label("Total Price");
        total.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        cartTable.itemsProperty().addListener((obs, oldItems, newItems) -> updateTotalLabel(total, cartTable, courier.getValue()));
        cartTable.getItems().addListener((Observable observable) -> updateTotalLabel(total, cartTable, courier.getValue()));

        courier.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateTotalLabel(total, cartTable, newVal);});

        Button checkout = new Button("Checkout");
        checkout.setMinHeight(75);
        checkout.setMinWidth(250);
        checkout.setOnAction(e -> {
            if(courier.getValue() == null){
                showError("Error", "Checkout Error", "Please select a courier!");
                return;}
            if(check.isSelected()){
                checksum = 1;}
            else {
                checksum = 0;}
            confirmation(primaryStage);});

        VBox cartLayout = new VBox(10, label, cartTable);
        cartLayout.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(10, del, delete, cour, courier, courPrice, check, total, checkout);
        layout.setAlignment(Pos.CENTER_LEFT);

        HBox finalLayout = new HBox(10, cartLayout, layout);
        finalLayout.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setAlignment(menuBar, Pos.TOP_CENTER);
        borderPane.setCenter(finalLayout);

        sp = new StackPane();
        sp.getChildren().add(borderPane);
        Scene scene = new Scene(sp, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();}

    private TableView<carts> createTable() {
        TableView<carts> table = new TableView<>();
        table.setEditable(false);

        TableColumn<carts, String> name = new TableColumn<>("Menu name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<carts, Double> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<carts, Integer> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<carts, Integer> total = new TableColumn<>("Total");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        table.getColumns().addAll(name, price, quantity, total);

        return table;}

    private void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();}

    private void updateTotalLabel(Label totalLabel, TableView<carts> cartTable, courier selectedCourier) {
        int totalCartPrice = cartTable.getItems().stream()
                .mapToInt(c -> c.getTotal())
                .sum();
        int courierPrice = (selectedCourier != null) ? selectedCourier.getPrice() : 0;
        totalLabel.setText("Total Price: " + (totalCartPrice + courierPrice));}

    private void confirmation(Stage primaryStage){
        Window popUp = new Window("Confirm Checkout");
        popUp.getLeftIcons().clear();
        popUp.setMinSize(primaryStage.getWidth(), primaryStage.getHeight());

        Label confirm = new Label("Are you sure you want to purchase?");
        confirm.setStyle("-fx-font-size: 20px;");
        Button yes = new Button("Yes");
        Button no = new Button("No");
        yes.setPrefSize(100, 50);
        no.setPrefSize(100, 50);

        yes.setOnAction(e -> {
            popUp.close();
            try {
                courier selectedCourier = courier.getValue();
                String courierName = (selectedCourier != null) ? selectedCourier.getName() : "";
                DBConnect.checkout(username, checksum, courierName);
                cartTable.setItems(DBConnect.fetchCart(username));
                cartTable.refresh();}
            catch (SQLException ex) {
                showError("Error", "Checkout Error", "An error occurred during checkout");}});
        no.setOnAction(e -> {
            popUp.close();});

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, confirm, buttons);

        popUp.getContentPane().getChildren().add(layout);
        layout.setAlignment(Pos.CENTER);
        sp.getChildren().add(popUp);
        popUp.toFront();}


}
