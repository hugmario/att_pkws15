import java.awt.*;

public class Player {

    private String playername;
    private Color playercolor;
    private int numberOwnedTerritories;

    public Player(String name, Color color) {
        playername = name;
        playercolor = color;
    }

    public String getPlayername() {
        return playername;
    }

    public Color getPlayerColor() {
        return playercolor;
    }

    public void addOneToOwnedTerritories() {
        numberOwnedTerritories++;
    }

    public void removeOneToOwnedTerritories() {
        numberOwnedTerritories--;
    }

    public Integer getNumberOwnedTerritories() {
        return numberOwnedTerritories;
    }
}
