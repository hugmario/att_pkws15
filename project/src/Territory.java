import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Territory extends Landscape{

    private String territoryname;
    private Point capital;
    private int counterLandscape;
    private HashMap<String, Territory> territoryNeighbours = new HashMap<String, Territory>();
    private HashMap<Integer, Landscape> territoryLandscapes = new HashMap<Integer, Landscape>();


    private String ownedby; // NONE or Playername
    private Color fillcolor; // Player's Color
    private int armycount;

    public Territory (Territory t){
        territoryname = t.territoryname;
        capital = t.capital;
        counterLandscape = t.counterLandscape;
        territoryNeighbours = t.territoryNeighbours;
        territoryLandscapes = t.territoryLandscapes;
        ownedby = t.ownedby;
        fillcolor = t.fillcolor;
        armycount = t.armycount;


    }

    public Territory (String name){
        territoryname = name;
        ownedby = "NONE";
        counterLandscape = 0;
        fillcolor = Color.LIGHT_GRAY;
    }

    public Territory() {
        territoryname = "UNKNOWN TERRITORY";
        ownedby = "NONE";
        counterLandscape = 0;
        fillcolor = Color.DARK_GRAY;
    }

    public String getTerritoryName(){
        return territoryname;
    }

    public void setTerritoryOwner(String player){
        ownedby = player;
    }

    public String getTerritoryOwner(){
        return ownedby;
    }

    public void setTerritoryColor(Color c){
        fillcolor = c;
        setNewColorToLandscapes();
    }

    public Color getTerritoryColor(){
        return fillcolor;
    }

    public Integer getTerritoryArmy(){
        return armycount;
    }

    public void setTerritoryArmyInit(){
        armycount = 1;
    }

    public void setTerritoryArmy(int army){
        armycount = army;
    }

    public void changeTerritoryArmy(int armys){
        armycount += armys;
    }

    public void setNewColorToLandscapes(){
        for (Map.Entry<Integer, Landscape> entry : territoryLandscapes.entrySet()) {
            Integer keyl = entry.getKey();
            Landscape landsc = entry.getValue();
            landsc.setColorToDraw(fillcolor);
        }
    }

    public void addLandscapeToTerritory(Landscape ls)
    {
        territoryLandscapes.put(counterLandscape++, ls);
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
