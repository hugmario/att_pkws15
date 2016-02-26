import javax.swing.*;

public class PrepareGame extends JFrame {
    public PrepareGame(String mapfile) {

        // Eigenschaften des Fensters
        this.add(new Game(mapfile));
        this.setSize(1250, 650);
        this.setTitle("All those Territories");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
