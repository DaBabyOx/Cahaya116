package Project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/cahaya116";
    private static final String USER = "root";
    private static final String PASSWORD ="Ayasjago1@";

    public static Connection connect() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);}

    public static boolean validateLogin(String username, String password) throws SQLException{
        String query = "SELECT * FROM MsUser WHERE UserName = ? AND Password = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;}}
        return false;}

    public static String getURole(String name) {
        String query = "SELECT Role FROM MsUser WHERE UserName = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("Role");}}
        catch (SQLException ex){
            throw new RuntimeException(ex);}
        return null;}

    public static boolean checkUsername(String username) throws SQLException{
        String query = "SELECT * FROM MsUser WHERE UserName = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();}}

    public static boolean checkMenu(String menu) throws SQLException{
        String query = "SELECT * FROM MsMenu WHERE MenuName = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, menu);
            ResultSet rs = ps.executeQuery();
            return rs.next();}}

    public static boolean checkEmail(String email) throws SQLException{
        String query = "SELECT * FROM MsUser WHERE Email = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();}}

    public static String getID(String table, String column, String prefix) throws SQLException{
        String query = "SELECT MAX("+column+") FROM "+table;
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
            String lastID = rs.getString(1);
            if(lastID != null){
                int idn = Integer.parseInt(lastID.replace(prefix, ""));
                return prefix + String.format("%03d", idn + 1);}}}
        return prefix + "001";}


    public static void registerUser(String username, String email, String password, String gender, String role, String dob) throws SQLException{
        String id = getID("MsUser", "UserID", "US");
        String query = "INSERT INTO MsUser (UserID, UserName, Email, Password, Gender, Role, DateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, id);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, gender);
            ps.setString(6, role);
            ps.setString(7, dob);
            ps.executeUpdate();}
        catch (SQLException ex){
            throw new RuntimeException(ex);}}

    public static ObservableList<product> getProducts() throws SQLException{
        ObservableList<product> products = FXCollections.observableArrayList();
        String query = "SELECT * FROM MsMenu";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                products.add(new product(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)));}}
        return products;}

    public static String getUID(String name) {
        String query = "SELECT UserID FROM MsUser WHERE UserName = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("UserID");}}
        catch (SQLException ex){
            throw new RuntimeException(ex);}
        return null;}

    public static void addToCart(String email, String productID, int quantity) throws SQLException{
        String query = "INSERT INTO Cart (UserID, MenuID, Quantity) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE Quantity = Quantity + ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, email);
            ps.setString(2, productID);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);
            ps.executeUpdate();}
        catch (SQLException ex){
            throw new RuntimeException(ex);}}

    public static ObservableList<carts> fetchCart(String name){
        ObservableList<carts> carts = FXCollections.observableArrayList();
        String query = "SELECT m.MenuName, m.MenuPrice, c.Quantity FROM Cart c JOIN MsMenu m ON c.MenuID = m.MenuID JOIN MsUser u ON c.UserID = u.UserID WHERE u.UserName = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                carts.add(new carts(rs.getString(1), rs.getInt(2), rs.getInt(3)));}}
        catch (SQLException ex){
            throw new RuntimeException(ex);}
        return carts;}

    public static void deleteCart(String in, String un) throws SQLException {
        String query = "DELETE FROM Cart " +
                "WHERE UserID = (SELECT u.UserID FROM MsUser u WHERE u.UserName = ?) " +
                "AND MenuID = (SELECT m.MenuID FROM MsMenu m WHERE m.MenuName = ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, un);
            ps.setString(2, in);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);}}

    public static ObservableList<courier> fetchCourier() throws SQLException{
        ObservableList<courier> couriers = FXCollections.observableArrayList();
        String query = "SELECT CourierName, CourierPrice FROM MsCourier";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                couriers.add(new courier(rs.getString(1), rs.getInt(2)));}}
        return couriers;}

    public static void checkout(String un, int eatingUtensils, String courierName) throws SQLException {
        String transactionID = getID("TransactionHeader", "TransactionID", "TR");

        String userIDQuery = "SELECT UserID FROM MsUser WHERE UserName = ?";
        String courierIDQuery = "SELECT CourierID FROM MsCourier WHERE CourierName = ?";
        String userID = null;
        String courierID = null;

        try (Connection conn = connect()) {
            try (PreparedStatement ps = conn.prepareStatement(userIDQuery)) {
                ps.setString(1, un);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    userID = rs.getString("UserID");}}

            try (PreparedStatement ps = conn.prepareStatement(courierIDQuery)) {
                ps.setString(1, courierName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    courierID = rs.getString("CourierID");}}

            if (userID == null || courierID == null) {
                throw new SQLException("User or Courier ID not found!");}

            String query1 = "INSERT INTO TransactionHeader (TransactionID, UserID, CourierID, EatingUtensils, TransactionDate) " +
                    "VALUES (?, ?, ?, ?, CURRENT_DATE)";
            try (PreparedStatement ps = conn.prepareStatement(query1)) {
                ps.setString(1, transactionID);
                ps.setString(2, userID);
                ps.setString(3, courierID);
                ps.setInt(4, eatingUtensils);
                ps.executeUpdate();}

            String query2 = "INSERT INTO TransactionDetail (TransactionID, MenuID, Quantity) " +
                    "SELECT ?, c.MenuID, c.Quantity FROM Cart c WHERE c.UserID = ?";
            try (PreparedStatement ps = conn.prepareStatement(query2)) {
                ps.setString(1, transactionID);
                ps.setString(2, userID);
                ps.executeUpdate();}

            String query3 = "DELETE FROM Cart WHERE UserID = ?";
            try (PreparedStatement ps = conn.prepareStatement(query3)) {
                ps.setString(1, userID);
                ps.executeUpdate();}

        } catch (SQLException e) {
            throw new RuntimeException("Checkout failed: " + e.getMessage(), e);}}

    public static void addProduct(String name, int price, String desc) throws SQLException{
        String id = getID("MsMenu", "MenuID", "ME");
        String query = "INSERT INTO MsMenu (MenuID, MenuName, MenuPrice, MenuDescription) VALUES (?, ?, ?, ?)";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setInt(3, price);
            ps.setString(4, desc);
            ps.executeUpdate();}
        catch (SQLException ex){
            throw new RuntimeException(ex);}}

    public static void updateProduct(String id, String name, int price, String desc) throws SQLException {
        String query = "UPDATE MsMenu SET MenuName = ?, MenuPrice = ?, MenuDescription = ? WHERE MenuId = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setString(3, desc);
            ps.setString(4, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);}}

    public static void deleteProduct(String id) throws SQLException {
        String query = "DELETE FROM MsMenu WHERE MenuID = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);}}

}

