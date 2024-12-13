package Project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class home {
    private TableView<product> productTable;
    private Label nameLabel, descriptionLabel, priceLabel;
    private String username = getSession.getInstance().getName();
    private String uid =  DBConnect.getUID(username);

    public home(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Cahaya 116");
        MenuBar mb = MenuBar.createMenuBar(primaryStage, "home");
        productTable = createTable();
        productTable.setPrefWidth(500);
        productTable.setMaxHeight(800);
        productTable.setMinHeight(800);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        productTable.setItems(DBConnect.getProducts());

        nameLabel = new Label("Menu Name");
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);
        descriptionLabel = new Label("");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-size: 18px;");
        priceLabel = new Label("Price");
        priceLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Spinner<Integer> quantity = new Spinner<>(1, 20, 1);
        quantity.setMinWidth(250);
        Button addToCart = new Button("Add to Cart");
        addToCart.setMinWidth(75);
        addToCart.setOnAction(e ->{
            product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if(selectedProduct != null){
                try{
                    DBConnect.addToCart(uid, selectedProduct.getId(), quantity.getValue());
                    showInfo(Alert.AlertType.INFORMATION, "Success", "Cart Info", "Item Successfully added to cart!");
                }catch(SQLException ex){
                    throw new RuntimeException(ex);}
            }else{
                showInfo(Alert.AlertType.ERROR, "Error", "Cart Error", "Please select a menu to be added");}});

        VBox productLayout = new VBox(10, nameLabel, productTable, descriptionLabel, priceLabel, quantity, addToCart);
        productLayout.setAlignment(Pos.CENTER_LEFT);
        productLayout.setMaxWidth(250);
        productLayout.setMinWidth(250);

        quantity.valueProperty().addListener((obs, oldValue, newValue) -> {
            product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                priceLabel.setText("Price: " + selectedProduct.getPrice() * newValue);}});

        productTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<product>() {
            @Override
            public void changed(ObservableValue<? extends product> observable, product oldValue, product newValue) {
                if(newValue != null){
                    nameLabel.setText(newValue.getName());
                    descriptionLabel.setText(newValue.getDescription());
                    priceLabel.setText("Price: "+ newValue.getPrice() * quantity.getValue());}}});

        HBox finalLayout = new HBox(10, productTable, productLayout);
        finalLayout.setAlignment(Pos.CENTER);
        finalLayout.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setTop(mb);
        BorderPane.setAlignment(mb, Pos.TOP_CENTER);
        layout.setCenter(finalLayout);
        BorderPane.setAlignment(finalLayout, Pos.CENTER);

        Scene scene = new Scene(layout, 1920, 1080);
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