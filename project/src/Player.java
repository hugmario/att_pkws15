import java.awt.*;

public class Player {

    private String playername;
    private Color playercolor;
<<<<<<< HEAD
    private int ownedterritories;
=======
    private int reinforcement;
>>>>>>> b6bebd3b422e1ffc58ff3ab122b7f6c6fa4edf5f

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
