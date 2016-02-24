import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Territory extends Landscape{

    private String territoryname;
    private Point capital;
    private HashMap<String, Territory> territoryNeighbours = new HashMap<String, Territory>();
    private LinkedList<Landscape> territoryLandscapes = new LinkedList<Landscape>();


    private String ownedby; // NONE or Playername
    private int armycount;

    public Territory (String name){
        territoryname = name;
        ownedby = "NONE";
    }

    public Territory() {
        territoryname = "UNKNOWN TERRITORY";
        ownedby = "NONE";
    }

    public String getTerritoryname(){
        return territoryname;
    }

    public void addLandscapeToTerritory(Landscape ls){
        territoryLandscapes.add(ls);
    }

    public void addNeighbourToTerritory(Territory te){
        territoryNeighbours.put(te.territoryname, te);
    }

    public void setTerritoryCapital(Point p){
        capital = new Point((int)p.getX(), (int)p.getY());
    }

}
