package model;

public class Player {
    public String  name;
    public String key;
    public int score;
    public Player(){}

    public Player(String name, int score){
        this.name= name;
        this.score=score;
    }
    public Player(String name, String key){
        this.name= name;
        this.key=key;
    }
}
