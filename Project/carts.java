package Project;

public class carts {
    private String name;
    private int price, quantity, total;

    public carts(String name, int quantity, int price){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.total = getPrice()*getQuantity();}

    public String getName(){
        return name;}

    public int getQuantity(){
        return quantity;}

    public void setName(String name){
        this.name = name;}

    public void setQuantity(int quantity){
        this.quantity = quantity;
        calTotal();}

    public int getPrice(){
        return price;}

    public void setPrice(int price){
        this.price = price;
        calTotal();}

    public int getTotal(){
        return total;}

    private void calTotal(){
        total = getPrice()*getQuantity();}}