import java.awt.*;

public class Player {

    private String playername;
    private Color playercolor;
    private int numberOwnedTerritories;
    private int reinforcement;

    public Player(String name, Color color) {
        playername = name;
        playercolor = color;
        reinforcement = 0;
    }

    public String getPlayername() {
        return playername;
    }

    public Color getPlayerColor() {
        return playercolor;
    }

    public Integer getNumberOwnedTerritories(){return numberOwnedTerritories; }

    public boolean hasReinforcement() {
        return reinforcement > 0;
    }

    public int getReinforcement() {
        return reinforcement;
    }

    public void setReinforcement(int a) {
        this.reinforcement = a;
    }

}
