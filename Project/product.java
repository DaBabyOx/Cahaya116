package Project;

public class product {
    private String id, name, description;
    private int price;

    public product(String id, String name, String description, int price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;}

    public String getId(){
        return id;}

    public String getName(){
        return name;}

    public String getDescription(){
        return description;}

    public int getPrice(){
        return price;}

    public void setId(String id){
        this.id = id;}

    public void setName(String name){
        this.name = name;}

    public void setDescription(String description){
        this.description = description;}

    public void setPrice(int price){
        this.price = price;}}