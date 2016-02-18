

public class Continent extends Territory{

    private String continentname;
    private int armybonus;
    private Territory[] continentterritories;

    private int continent_minX;
    private int continent_minY;
    private int continent_maxX;
    private int continent_maxY;

    public Continent(String name){
        continentname = name;
    }
}
