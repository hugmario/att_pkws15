import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;


public class Game extends JPanel implements MouseListener, MouseMotionListener{

    private String worldfile = "";
    private String systemmessage = "initializing";
    private Player[] players = new Player[2];
    private WorldLoader map;
    private Point mouseClickedPoint;
    private boolean gameover = false;
    private int phase = 0; // 0, alles generieren; 1, Starte mit Landerwerb; 2, Angriff; 3, Armeen verschieben
    private int playerOnTurn = -1; // Index: -1, gar keiner; 0, .. usw.
    private int capitalClickOffset = 15;

    HashMap<String, Continent> continentsDraw = new HashMap<String, Continent>();
    HashMap<String, Territory> territoriesDraw = new HashMap<String, Territory>();
    HashMap<Integer, Landscape> landscapesDraw = new HashMap<Integer, Landscape>();



    public Game(String worldfile){
        this.worldfile = worldfile;

        // Mapfile einlesen und Territorien generieren
        map = new WorldLoader(worldfile);

        continentsDraw = map.getContinentHashMap();
        territoriesDraw = map.getTerritoryHashMap();

        // Spieler erstellen
        players[0] = new Player("Mario", Color.green);
        players[1] = new Player("Daniel", Color.blue);

        // Mausevents erstellen
        addMouseListener(this);
        addMouseMotionListener(this);

        // Spiel starten
        phase = 1;
        playerOnTurn = 0;
        systemmessage = "Player: " + players[playerOnTurn].getPlayername() + " - occupie an territory (by clicking on a capital).";


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);



        for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
            String key = entry.getKey();
            Territory ter = entry.getValue();
            landscapesDraw = ter.getLandscapes();

            // Zeichne Landscapes eines Territoriums
            for (Map.Entry<Integer, Landscape> entryl : landscapesDraw.entrySet()) {
                Integer keyl = entryl.getKey();
                Landscape landsc = entryl.getValue();

                Polygon p = landsc.getPolygon();
                g.setColor(landsc.getColor());
                g.drawPolygon(p);
                g.fillPolygon(p);
            }

            // Zeichne Hauptst�dte mit Armeenzahl
            g.setColor(Color.black);
            g.drawString(ter.getTerritoryArmy().toString(), (int)ter.getTerritoryCapital().getX(), (int)ter.getTerritoryCapital().getY());
        }

        // Systemmeldung ausgeben
        g.setColor(Color.black);
        g.drawString(systemmessage, 20, 600);

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

    public void RunGame(){

        // 1.Phase Landerwerb
        // Spieler / PC w�hlen abwechselnd
        // - wechsel auf Spieler
        // - Spieler klickt auf ein Territorium
        //   - noch nicht besetzt? -> Spieler besetzt es
        //   - schon besetzt? -> passiert nichts, Spieler muss neu w�hlen
        // - wechsel auf PC
        // - PC sucht n�chstm�gliches Territorium (nach Regel: benachbarte Territorium zu besetzen)
        //   - noch nicht besetzt? -> PC besetzt es
        //   - schon besetzt? -> passiert nichts, PC sucht n�chstes Territorium
        // - pr�fen, ob es noch offene Territorien gibt - falls ja, dann wiederholen, sonst Phase 2

        // 2.Phase Eroberungen
        // VERTEILEN DER VERST�RKUNGEN
        // Spieler ist an der Reihe
        // - mitz�hlen, wieviele Territorien der Spieler besitzt (pro 3 Territorien, 1 Armee + Bonusarmeen der Kontinente)
        // - Spieler setzt die Armeen in Territorien
        //   - pr�fen, ob dieses Territorium den Spieler geh�rt
        //     - ja, dann nach Anzahl fragen, wieviele gesetzt werden sollen (von maximal erlaubten)
        //     - nein -> neu klicken
        // - hat der Spieler noch Armeen zum platzieren? - wenn ja, dann wiederholen, wenn nein
        // PC ist an der Reihe
        // - z�hlen, wieviele Territorien der Spieler besitzt (pro 3 Territorien, 1 Armee + Bonusarmeen der Kontinente)
        // - PC setzt Armeen in Territoren (zuerst passendes suchen)
        //   - PC setzt H�he an Armeen an durchschnittlicher Armeenzahl benachbarter feindlicher Territorien
        // - wiederholen, falls noch nicht alle Armeen platziert wurden

        // 2.Phase
        // ANGRIFF
        // Spieler ist an der Reihe
        // - Spieler klickt in ein eigenes Territorium
        //   - pr�fen ob es seines ist, und mindestens 2 Armeen vorhanden sind (da 1 im Territorium bleiben muss)
        //   - wenn nicht, neu klicken lassen
        // - g�ltiges Territorium: Spieler klickt auf benachbartes feindliches Territorium
        //   - pr�fen ob es ein feindliches ist
        //   - wenn nicht, neu klicken lassen
        // - Angriff mit max. 3 Armeen (wird immer h�chstm�gliche Anzahl ausgew�hlt)
        //
        // - Spieler schickt Armee #1 rein - Angreifendes Territorium verteidigt sich mit Armee #1
        // - beide Seiten w�rfeln:
        // - Spieler hat h�here Augenzahl als PC?
        //   - Spieler Armee �berlebt, PC Armee wird vernichtet
        //   - sonst PC Armee �berlebt, Spieler Armee wird vernichtet
        // - war es die letzte Armee des PC Territoriums (Spieler hat das PCTerritorium �bernommen)?
        //   - ja, Territoriumsbesitz auf Spieler umschreiben und �berlebte Armeen eintragen
        //     - erlaube Armeen vom eigenen angreifenden Territorium ins �bernommene Territorium nachziehen zu lassen
        //         (wenigstens 1 Armee muss im angreifenden Territorium zur�ckbleiben)
        // - nein, war es die letzte Armee des Spieler Territoriums (PC hat Spielerterritorium �bernommen)?
        //   - ja, Territoriumsbesitz auf PC umschreiben und �berlebte Armeen eintragen
        //   - nein, gibt es noch n�chste Armeen? (bis zu 2 weitere)
        //     - ja, dann n�chste Armeen antreten lassen
        //     - nein, keine Territoriums�nderung (laut Aufgabenstellung soll das aber niemals eintreten...) [DISCUSS]
        //


        // 2.Phase
        // BEWEGEN VON ARMEEN
        // Spieler klickt auf ein eigenes Territorium
        // - nicht seines?, neu klicken
        // Spieler klickt auf ein benachtbartes eigene Territorium
        // - nicht seines?, neu klicken
        // pro Linksklick aufs Zielterritorium wird 1 Armee verschoben
        // pro Rechtsklick aufs Zielterritorium wird 1 Armee wieder zur�ckverschoben


        // 3.Phase
        // RUNDENABSCHLUSS
        // wenn der Spieler mit dem Verschieben fertig ist, dann die Runde beenden und n�chster ist an der Reihe (PC)

    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mouseClickedPoint = mouseEvent.getPoint(); // wo wurde mit der Maus geklickt?
        System.out.println("DEBUG Game-mouseClicked: " + mouseClickedPoint.getX() + ", " + mouseClickedPoint.getY());


        switch (phase){
            case 1:
                // Suche nach g�ltigen Klick
                for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                    String key = entry.getKey();
                    Territory ter = entry.getValue();
                    if (mouseClickedPoint.getX() >= ter.getTerritoryCapital().getX()-capitalClickOffset && mouseClickedPoint.getX() <= ter.getTerritoryCapital().getX()+capitalClickOffset&&
                    mouseClickedPoint.getY() >= ter.getTerritoryCapital().getY()-capitalClickOffset && mouseClickedPoint.getY() <= ter.getTerritoryCapital().getY()+capitalClickOffset){
                        //if (ter.getTerritoryOwner().equals("NONE")){ // nur, wenn es noch von keinem besetzt ist
                        ter.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                        System.out.println("DEBUG PHASE 1: IN IF terName: " + ter.getTerritoryName() + " terCX: " + ter.getTerritoryCapital().getX() + ", terCY: " + ter.getTerritoryCapital().getY() + " ");

                        //}
                    }
                    System.out.println("DEBUG PHASE 1: NACH IF terName: " + ter.getTerritoryName() + " terCX: " + ter.getTerritoryCapital().getX() + ", terCY: " + ter.getTerritoryCapital().getY() + " ");

                    //landscapesDraw = ter.getLandscapes();


                }


                break;
            case 2:
                break;
            case 3:
                break;
            default:
                System.out.println("DEBUG Game-mouseClicked: unknown phase.");
                break;
        }

        if (playerOnTurn + 1 >= players.length){
            playerOnTurn = 0;
        }else{
            playerOnTurn++;
        }

        if (checkGameOver()){ // has one user owned all Territories?
            gameover = true;
        }



    }

    public boolean checkGameOver(){
        for (int i = 0; i < players.length; ++i) {
            if (players[playerOnTurn].getNumberOwnedTerritories() == continentsDraw.size()) {
                systemmessage = "Player " + players[playerOnTurn].getPlayername() + " has won. :D";
                return true;
            }
        }
        return false;
    }

    // folgende werden nicht ben�tigt
    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        this.repaint();
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
