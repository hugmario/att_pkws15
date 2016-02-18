// asdasdasd
// asdasdasd
// ein kommentar :D

import javax.swing.*;

// testing  GitHub
public class AllThoseTerritories {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (args.length == 0) {
                    //System.out.println("usage: AllThoseTerritories Worldfile.map");
                    //TESTING:
                    Game g = new Game("maps/world.map");
                } else {
                    Game g = new Game(args[0]);
                    g.setVisible(true);
                }
            }
        });
    }
}
