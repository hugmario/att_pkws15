import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;


public class Game extends JPanel implements MouseListener, MouseMotionListener{

    private String worldfile = "";
    private Player[] players = new Player[2];
    private WorldLoader map;
    private Point mouseClicked;
    private boolean initdraw = true;
    private int phase = 0; // 0, alles generieren; 1, Starte mit Landerwerb; 2, Angriff; 3, Armeen verschieben
    private int PlayerOnTurn = 0; // 0, gar keiner; 1, .. usw.

    public Game(String worldfile){
        this.worldfile = worldfile;

        // Mapfile einlesen und Territorien generieren
        map = new WorldLoader(worldfile);

        // Spieler erstellen
        players[0] = new Player("Mario", Color.green);
        players[1] = new Player("Daniel", Color.blue);

        // Mausevents erstellen
        addMouseListener(this);
        addMouseMotionListener(this);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //System.out.println("DEBUG Game: Draw firsttime map");
        HashMap<String, Continent> continentsDraw = new HashMap<String, Continent>();
        HashMap<String, Territory> territoriesDraw = new HashMap<String, Territory>();
        HashMap<Integer, Landscape> landscapesDraw = new HashMap<Integer, Landscape>();

        continentsDraw = map.getContinentHashMap();
        territoriesDraw = map.getTerritoryHashMap();
        //System.out.println("DEBUG Game: before first loop");
        for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
            String key = entry.getKey();
            Territory ter = entry.getValue();
            landscapesDraw = ter.getLandscapes();

            // Zeichne Landscapes
            for (Map.Entry<Integer, Landscape> entryl : landscapesDraw.entrySet()) {
                Integer keyl = entryl.getKey();
                Landscape landsc = entryl.getValue();

                Polygon p = landsc.getPolygon();
                g.setColor(landsc.getColor());
                g.drawPolygon(p);
            }

            // Zeichne Hauptstädte mit Armeenzahl
            g.setColor(Color.black);
            g.drawString(ter.getTerritoryArmy().toString(), (int)ter.getTerritoryCapital().getX(), (int)ter.getTerritoryCapital().getY());
        }

       /* g.setColor(Color.blue);
        g.fillRect(5, 5, 50, 50);*/
    }

    public void mouseEvent(){}

    // checks if the clicked territory is free, if yes occupies it
    // moves the game to the next phase if all territories are occupied.
    private void occupyTerritories(){}

    private void placeReinforcements(){}

    private int calculateReinforcements(){return 0;}

    private void attackTerritories(){}

    private void mooveTroops(){}

    /*public Territory getRandomTerritory(){
        return ;
    }*/

    public int gameOver(){return 0;}

    public void RunGame(){

        // 1.Phase Landerwerb
        // Spieler / PC wï¿½hlen abwechselnd
        // - wechsel auf Spieler
        // - Spieler klickt auf ein Territorium
        //   - noch nicht besetzt? -> Spieler besetzt es
        //   - schon besetzt? -> passiert nichts, Spieler muss neu wï¿½hlen
        // - wechsel auf PC
        // - PC sucht nï¿½chstmï¿½gliches Territorium (nach Regel: benachbarte Territorium zu besetzen)
        //   - noch nicht besetzt? -> PC besetzt es
        //   - schon besetzt? -> passiert nichts, PC sucht nï¿½chstes Territorium
        // - prï¿½fen, ob es noch offene Territorien gibt - falls ja, dann wiederholen, sonst Phase 2

        // 2.Phase Eroberungen
        // VERTEILEN DER VERSTï¿½RKUNGEN
        // Spieler ist an der Reihe
        // - mitzählen, wieviele Territorien der Spieler besitzt (pro 3 Territorien, 1 Armee + Bonusarmeen der Kontinente)
        // - Spieler setzt die Armeen in Territorien
        //   - prï¿½fen, ob dieses Territorium den Spieler gehï¿½rt
        //     - ja, dann nach Anzahl fragen, wieviele gesetzt werden sollen (von maximal erlaubten)
        //     - nein -> neu klicken
        // - hat der Spieler noch Armeen zum platzieren? - wenn ja, dann wiederholen, wenn nein
        // PC ist an der Reihe
        // - zï¿½hlen, wieviele Territorien der Spieler besitzt (pro 3 Territorien, 1 Armee + Bonusarmeen der Kontinente)
        // - PC setzt Armeen in Territoren (zuerst passendes suchen)
        //   - PC setzt Hï¿½he an Armeen an durchschnittlicher Armeenzahl benachbarter feindlicher Territorien
        // - wiederholen, falls noch nicht alle Armeen platziert wurden

        // 2.Phase
        // ANGRIFF
        // Spieler ist an der Reihe
        // - Spieler klickt in ein eigenes Territorium
        //   - prï¿½fen ob es seines ist, und mindestens 2 Armeen vorhanden sind (da 1 im Territorium bleiben muss)
        //   - wenn nicht, neu klicken lassen
        // - gï¿½ltiges Territorium: Spieler klickt auf benachbartes feindliches Territorium
        //   - prï¿½fen ob es ein feindliches ist
        //   - wenn nicht, neu klicken lassen
        // - Angriff mit max. 3 Armeen (wird immer hï¿½chstmï¿½gliche Anzahl ausgewï¿½hlt)
        //
        // - Spieler schickt Armee #1 rein - Angreifendes Territorium verteidigt sich mit Armee #1
        // - beide Seiten wï¿½rfeln:
        // - Spieler hat hï¿½here Augenzahl als PC?
        //   - Spieler Armee ï¿½berlebt, PC Armee wird vernichtet
        //   - sonst PC Armee ï¿½berlebt, Spieler Armee wird vernichtet
        // - war es die letzte Armee des PC Territoriums (Spieler hat das PCTerritorium ï¿½bernommen)?
        //   - ja, Territoriumsbesitz auf Spieler umschreiben und ï¿½berlebte Armeen eintragen
        //     - erlaube Armeen vom eigenen angreifenden Territorium ins ï¿½bernommene Territorium nachziehen zu lassen
        //         (wenigstens 1 Armee muss im angreifenden Territorium zurï¿½ckbleiben)
        // - nein, war es die letzte Armee des Spieler Territoriums (PC hat Spielerterritorium ï¿½bernommen)?
        //   - ja, Territoriumsbesitz auf PC umschreiben und ï¿½berlebte Armeen eintragen
        //   - nein, gibt es noch nï¿½chste Armeen? (bis zu 2 weitere)
        //     - ja, dann nï¿½chste Armeen antreten lassen
        //     - nein, keine Territoriumsï¿½nderung (laut Aufgabenstellung soll das aber niemals eintreten...) [DISCUSS]
        //


        // 2.Phase
        // BEWEGEN VON ARMEEN
        // Spieler klickt auf ein eigenes Territorium
        // - nicht seines?, neu klicken
        // Spieler klickt auf ein benachtbartes eigene Territorium
        // - nicht seines?, neu klicken
        // pro Linksklick aufs Zielterritorium wird 1 Armee verschoben
        // pro Rechtsklick aufs Zielterritorium wird 1 Armee wieder zurï¿½ckverschoben


        // 3.Phase
        // RUNDENABSCHLUSS
        // wenn der Spieler mit dem Verschieben fertig ist, dann die Runde beenden und nï¿½chster ist an der Reihe (PC)

        // Prüfen ob Spielende
        //

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mouseClicked = mouseEvent.getPoint(); // wo wurde mit der Maus geklickt?
        System.out.println("DEBUG Game-mouseClicked: " + mouseClicked.getX() + ", "+ mouseClicked.getY());

    }

    // folgende werden nicht benötigt
    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
