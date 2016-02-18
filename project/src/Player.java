import java.awt.*;

public class Player {

    private String playername;
    private Color playercolor;

    public Player(String name, Color color){
        playername = name;
        playercolor = color;
    }
    public String getPlayername(){
        return playername;
    }

    public Color getPlayerColor(){
        return playercolor;
    }

}
