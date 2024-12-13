package Project;

public class getSession {
    private  static getSession instance;
    private String name;

    public static getSession getInstance(){
        if(instance == null){
            instance = new getSession();}
        return instance;}

    public void setName(String name){
        this.name = name;}

    public String getName(){
        return name;}}