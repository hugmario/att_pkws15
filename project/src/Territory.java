

public class Territory extends Landscape{

    private String territoryname;
    private int capitalX;
    private int capitalY;
    private Landscape[] territoryslandshape;

    private String ownedby; // NONE or Playername
    private int armycount;

    public Territory (String name){
        territoryname = name;
        ownedby = "NONE";
    }

    public void setTerritoryCapital(int x, int y){
        capitalX = x;
        capitalY = y;
    }

}
