import java.awt.*;
import java.util.HashMap;

public class Territory extends Landscape{

    private String territoryname;
    private Point capital;
    private int counterLandscape;
    private HashMap<String, Territory> territoryNeighbours = new HashMap<String, Territory>();
    private HashMap<Integer, Landscape> territoryLandscapes = new HashMap<Integer, Landscape>();


    private String ownedby; // NONE or Playername
    private Color fillcolor; // Player's Color
    private int armycount;

    public Territory (String name){
        territoryname = name;
        ownedby = "NONE";
        counterLandscape = 0;
        fillcolor = Color.DARK_GRAY;
    }

    public Territory() {
        territoryname = "UNKNOWN TERRITORY";
        ownedby = "NONE";
        counterLandscape = 0;
        fillcolor = Color.DARK_GRAY;
    }

    public String getTerritoryname(){
        return territoryname;
    }

    public Color getTerritoryColor(){
        return fillcolor;
    }

    public Integer getTerritoryArmy(){
        return armycount;
    }
    
    public void addLandscapeToTerritory(Landscape ls)
    {
        territoryLandscapes.put(counterLandscape, ls);
    }

    public void addNeighbourToTerritory(Territory te)
    {
        territoryNeighbours.put(te.territoryname, te);
    }

    public void setTerritoryCapital(Point p)
    {
        capital = new Point((int)p.getX(), (int)p.getY());
    }

    public Point getTerritoryCapital(){
        return capital;
    }
    
    public HashMap<Integer, Landscape> getLandscapes(){
        return territoryLandscapes;
    }

}
