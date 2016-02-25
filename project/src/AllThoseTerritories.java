import javax.swing.*;

public class AllThoseTerritories {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (args.length == 0) {
                    //System.out.println("usage: AllThoseTerritories Worldfile.map");
                    PrepareGame g = new PrepareGame("maps/three-continents.map");
                    g.setVisible(true);
                } else {
                    PrepareGame g = new PrepareGame(args[0]);
                    g.setVisible(true);
                }
            }
        });
    }
}
