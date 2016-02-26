import java.util.HashMap;
import java.util.LinkedList;

public class Continent extends Territory {

    private String continentname;
    private int armybonus;
    private HashMap<String, Territory> territoryHashMap = new HashMap<String, Territory>();

    private int continent_minX;
    private int continent_minY;
    private int continent_maxX;
    private int continent_maxY;

    public Continent(String name, int bonus) {
        continentname = name;
        armybonus = bonus;
    }

    public Continent(String name) {
        continentname = name;
    }

    public Continent() {
        continentname = "UNKNOWN";
    }

    public void addTerritoryToContinent(String name, Territory t) {
        territoryHashMap.put(name, t);
    }

    public HashMap<String, Territory> getTerritories() {
        return territoryHashMap;
    }

    public void setContinentArmyBonus(int bonus) {
        armybonus = bonus;
    }

    public Integer getContinentArmyBonus() {
        return armybonus;
    }
}
