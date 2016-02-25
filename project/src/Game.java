import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Game extends JPanel implements MouseListener, MouseMotionListener{

    private String worldfile = "";
    private String playerOnTurnMessage = "";
    private String systemmessage = "initializing";
    private Player[] players = new Player[2];
    private WorldLoader map;
    private Point mouseClickedPoint;
    private boolean gameover = false;
    private int phase = 0; // 0, alles generieren; 1, Starte mit Landerwerb; 2, Angriff; 3, Armeen verschieben
    private int subphase2 = 0; /*
     -1, noch gar nicht; 0, Errechne zu Platzierende Armeen; 1, Setze die Armeen;
     2, Start Angriffsland; 3, Ziel Angriffsland; 4, Angriff handeln; (weiterer Angriff -> 2, oder): 5, Armeen nachschieben (von Start nach Ziel)
     */
    private int playerOnTurn = -1; // Index: -1, gar keiner; 0, .. usw.
    private int capitalClickOffset = 25;
    private int territoriesInitCounter; // wenn max, Territorien, dann Phase 1 beendet

    private boolean calculatePlayerArmysToPlay = true; // wenn true, dann wird f�r den Spieler die zu setzende Armeenzahl erstellt
    private boolean allContinentTerritoriesOwned = true; // wenn true, dann gibts f�r den Kontinent keinen Armybonus
    private int playerArmysToSet = 0; // wieviele Armeen darf der Spieler noch setzen

    private Territory startTerritory; // eigenes Territorium
    private Territory targetTerritory; // fremd Territorium
    private int armysForFight; // wieviele Armeen sind zum K�mpfen verf�gbar?
    private int enemyArmsToFight; // wieviele Armeen sind zu Besiegen?

    private Random r;
    private int rollTheDiceOwn;
    private int rollTheDiceEnemy;

    private boolean moveArmysToTargetTerritory = true; // wenn wahr, dann wird per Links-Klick das Startterritorium gewählt, und per Rechtsklick das Zielterritorium
    private boolean moveArmysToStartTerritory = true; // wenn wahr, dann wird per Links-Klick das Targetterritorium gewählt, und per Rechtsklick das Startterritorium
    private boolean startTerritoryChosen = false;
    private boolean targetTerritoryChosen = false;
    private Territory tempTerritory;

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
        systemmessage = "Occupie an territory (by clicking on a capital).";


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

            // Zeichne Hauptstädte mit Armeenzahl
            g.setColor(Color.black);
            g.drawString(ter.getTerritoryArmy().toString(), (int)ter.getTerritoryCapital().getX(), (int)ter.getTerritoryCapital().getY());
        }

        g.setColor(Color.black);
        playerOnTurnMessage = "Current Player: " + players[playerOnTurn].getPlayername();
        g.drawString(playerOnTurnMessage, 20, 580); // Player on turn
        g.drawString(systemmessage, 20, 600); // Systemmeldung ausgeben


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

        // 2.Phase
        // ANGRIFF
        // Spieler ist an der Reihe
        // - Spieler klickt in ein eigenes Territorium
        //   - prüfen ob es seines ist, und mindestens 2 Armeen vorhanden sind (da 1 im Territorium bleiben muss)
        //   - wenn nicht, neu klicken lassen
        // - gütiges Territorium: Spieler klickt auf benachbartes feindliches Territorium
        //   - prüfen ob es ein feindliches ist
        //   - wenn nicht, neu klicken lassen
        // - Angriff mit max. 3 Armeen (wird immer höchstmögliche Anzahl ausgewählt)
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
        if (gameover){
            systemmessage = "Player " + players[playerOnTurn].getPlayername() + " has won. :D (restart complete for a new game)";
        }else {
            switch (phase) {
                case 1:
                    // Suche nach gültigen Klick (in der N�he der Hauptstadt)
                    for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                        String key = entry.getKey();
                        Territory ter = entry.getValue();
                        if (mouseClickedPoint.getX() >= ter.getTerritoryCapital().getX() - capitalClickOffset && mouseClickedPoint.getX() <= ter.getTerritoryCapital().getX() + capitalClickOffset &&
                                mouseClickedPoint.getY() >= ter.getTerritoryCapital().getY() - capitalClickOffset && mouseClickedPoint.getY() <= ter.getTerritoryCapital().getY() + capitalClickOffset) {
                            if (ter.getTerritoryOwner().equals("NONE")) { // nur, wenn es noch von keinem besetzt ist
                                ter.setTerritoryOwner(players[playerOnTurn].getPlayername());
                                ter.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                                ter.setTerritoryArmyInit();
                                players[playerOnTurn].addOneToOwnedTerritories();
                                territoriesInitCounter++;
                            }
                        }
                        //System.out.println("DEBUG PHASE 1: TERRSTAT terOwner: " + ter.getTerritoryOwner() + " terName: " + ter.getTerritoryName() + " terCX: " + ter.getTerritoryCapital().getX() + ", terCY: " + ter.getTerritoryCapital().getY() + " ");
                    }

                    // War es das letzte Territorium?

                    if (territoriesInitCounter >= territoriesDraw.size()) {
                        phase = 2;
                        playerOnTurn = 0; // Spieler muss ja Phase 2 beginnen
                        systemmessage = "all territories owned, Phase 2 is next. (please click).";
                    } else {
                        setNextPlayer();
                        systemmessage = "Occupie an territory (by clicking on a capital).";
                    }

                    break;
                case 2:
                    switch (subphase2) {
                        case 0:
                            // Ermittle Anzahl der Verstärkungen zum Platzieren (nur einmal zum Phasenstart pro Spieler)
                            for (Map.Entry<String, Continent> entryc : continentsDraw.entrySet()) {
                                String keyl = entryc.getKey();
                                Continent cont = entryc.getValue();
                                HashMap<String, Territory> continentsTerritory = new HashMap<String, Territory>();
                                continentsTerritory = cont.getTerritories();
                                for (Map.Entry<String, Territory> entryt : continentsTerritory.entrySet()) {
                                    String keyt = entryt.getKey();
                                    Territory tc = entryt.getValue();

                                    if (allContinentTerritoriesOwned) {
                                        if (tc.getTerritoryOwner().equals(players[playerOnTurn].getPlayername())) {
                                            allContinentTerritoriesOwned = true;
                                        } else {
                                            allContinentTerritoriesOwned = false;
                                        }
                                    }


                                }
                                if (allContinentTerritoriesOwned) {
                                    playerArmysToSet += cont.getContinentArmyBonus();
                                } else {
                                    allContinentTerritoriesOwned = true; // reset für den nächsten Continent
                                }
                            }
                            playerArmysToSet += (players[playerOnTurn].getNumberOwnedTerritories() / 3);
                            systemmessage = "Player " + players[playerOnTurn].getPlayername() + " can set: " + playerArmysToSet + " Armys total / Normal Armys are: " + (players[playerOnTurn].getNumberOwnedTerritories() / 3 + " (per left-click on Capital to place 1 Army)");
                            calculatePlayerArmysToPlay = false;
                            subphase2 = 1;
                            break;
                        case 1:
                            // Setzen der Verstärkungen
                            if (playerArmysToSet > 0) {
                                // Suche gültigen Klick
                                for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                                    String key = entry.getKey();
                                    Territory ter = entry.getValue();
                                    if (mouseClickedPoint.getX() >= ter.getTerritoryCapital().getX() - capitalClickOffset && mouseClickedPoint.getX() <= ter.getTerritoryCapital().getX() + capitalClickOffset &&
                                            mouseClickedPoint.getY() >= ter.getTerritoryCapital().getY() - capitalClickOffset && mouseClickedPoint.getY() <= ter.getTerritoryCapital().getY() + capitalClickOffset) {
                                        if (ter.getTerritoryOwner().equals(players[playerOnTurn].getPlayername())) { // nur sein eigenes Territorium
                                            ter.changeTerritoryArmy(1); // eine pro Links-Klick setzen
                                            playerArmysToSet -= 1;
                                            if (playerArmysToSet > 0) {
                                                systemmessage = "You have " + playerArmysToSet + " armies remaining to place. (per left-click on Capital to place 1 Army)";
                                            } else {
                                                systemmessage = "You have no armies remaining to place. (click for next player)";
                                                setNextPlayer();
                                                if (playerOnTurn == 0) { // jeder hat seine Armeen plazieren k�nnen
                                                    subphase2 = 2;
                                                    systemmessage = "Click on Territory with at least 2 armies to fight for.";
                                                } else {
                                                    subphase2 = 0;
                                                }
                                            }
                                        }
                                    }
                                    //System.out.println("DEBUG PHASE 2b: TERRSTAT terOwner: " + ter.getTerritoryOwner() + " terName: " + ter.getTerritoryName() + " terCX: " + ter.getTerritoryCapital().getX() + ", terCY: " + ter.getTerritoryCapital().getY() + " ");
                                }
                            } else {
                                systemmessage = "You have no armies to place. (click for next player).";
                                setNextPlayer();
                                if (playerOnTurn == 0) { // jeder hat seine Armeen plazieren k�nnen
                                    subphase2 = 2;
                                    systemmessage = "Click on Territory with at least 2 armies to fight for.";
                                } else {
                                    subphase2 = 0;
                                }
                            }
                            break;
                        case 2: // Start Angriffsland auswählen
                            for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                                String key = entry.getKey();
                                Territory ter = entry.getValue();
                                if (mouseClickedPoint.getX() >= ter.getTerritoryCapital().getX() - capitalClickOffset && mouseClickedPoint.getX() <= ter.getTerritoryCapital().getX() + capitalClickOffset &&
                                        mouseClickedPoint.getY() >= ter.getTerritoryCapital().getY() - capitalClickOffset && mouseClickedPoint.getY() <= ter.getTerritoryCapital().getY() + capitalClickOffset) {
                                    if (ter.getTerritoryOwner().equals(players[playerOnTurn].getPlayername())) { // nur sein eigenes Territorium
                                        if (ter.getTerritoryArmy() >= 2) {
                                            startTerritory = ter;

                                            // Anzahl der Armeen ermitteln
                                            armysForFight = startTerritory.getTerritoryArmy()-1;
                                            systemmessage = "Now choose a territory you want to fight.";
                                            subphase2 = 3;
                                        }else{
                                            systemmessage = "Not enough armys at this territory to fight for.";
                                        }
                                    }else{
                                        systemmessage = "Not a valid starting Territory.";
                                    }
                                }
                                //System.out.println("DEBUG PHASE 2c: TERstart terOwner: " + startTerritory.getTerritoryOwner() + " terName: " + startTerritory.getTerritoryName() + " ter");
                            }
                            break;
                        case 3: // Ziel Angriffsland auswählen
                            for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                                String key = entry.getKey();
                                Territory ter = entry.getValue();
                                if (mouseClickedPoint.getX() >= ter.getTerritoryCapital().getX() - capitalClickOffset && mouseClickedPoint.getX() <= ter.getTerritoryCapital().getX() + capitalClickOffset &&
                                        mouseClickedPoint.getY() >= ter.getTerritoryCapital().getY() - capitalClickOffset && mouseClickedPoint.getY() <= ter.getTerritoryCapital().getY() + capitalClickOffset) {
                                    if (!ter.getTerritoryOwner().equals(players[playerOnTurn].getPlayername())) { // nur ein fremdes Territorium
                                        targetTerritory = ter;
                                        // Anzahl der feindlichen Armeen ermitteln
                                        enemyArmsToFight = targetTerritory.getTerritoryArmy();
                                        systemmessage = "FIGHT.";
                                        System.out.println("DEBUG Phase 2d: Start fight.");

                                        // ANGRIFF
                                        // Würfeln
                                        r = new Random();

                                        // Würfeln, solange es auf jeder Seite mindestens 1 Armee gibt
                                        while (armysForFight >= 1 && enemyArmsToFight >= 1) {
                                            rollTheDiceOwn = r.nextInt(7);
                                            rollTheDiceEnemy = r.nextInt(7);
                                            System.out.println("DEBUG Phase 2e: You rolled: " + rollTheDiceOwn + " and Enemy rolled: " + rollTheDiceEnemy);
                                            if (rollTheDiceOwn > rollTheDiceEnemy) {
                                                enemyArmsToFight -= 1;
                                            }else{
                                                armysForFight -= 1;
                                            }
                                        }

                                        if (armysForFight >= 1){
                                            // Spieler hat gewonnen
                                            players[getPlayerIndex(targetTerritory.getTerritoryName())].removeOneToOwnedTerritories();
                                            players[playerOnTurn].addOneToOwnedTerritories(); // erobertes Territorium hinzuz�hlen
                                            targetTerritory.setTerritoryOwner(players[playerOnTurn].getPlayername()); // Besitzer umschreiben
                                            targetTerritory.setTerritoryArmy(armysForFight); // �berlebte Armeen eintragen
                                            targetTerritory.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                                            System.out.println("You won.");
                                            systemmessage = "You won. Left-Click at target territory to move armys (1 per click) or right-click to end this phase.";
                                        }else if (enemyArmsToFight >= 1){
                                            // Gegner hat gewonnen
                                            players[getPlayerIndex(targetTerritory.getTerritoryName())].addOneToOwnedTerritories();
                                            players[playerOnTurn].removeOneToOwnedTerritories();
                                            startTerritory.setTerritoryOwner(targetTerritory.getTerritoryOwner()); // Besitzer umschreiben
                                            startTerritory.setTerritoryArmy(enemyArmsToFight); // �berlebte Gegner Armeen eintragen
                                            startTerritory.setTerritoryColor(players[getPlayerIndex(targetTerritory.getTerritoryName())].getPlayerColor());
                                            systemmessage = "You lost. Right-click to end this phase.";

                                        }
                                        subphase2 = 4;
                                    }else{
                                        systemmessage = "That's not a foreign territory.";
                                    }
                                }else{
                                    systemmessage = "Not a valid target Territory.";
                                }
                                //System.out.println("DEBUG PHASE 2d: TERtarget terOwner: " + targetTerritory.getTerritoryOwner() + " terName: " + targetTerritory.getTerritoryName() + " ter");
                            }
                            break;
                        case 4: // Armeen nachziehen
                            if (SwingUtilities.isLeftMouseButton(mouseEvent)) { // Links klick erlaubt 1 Armee verschieben (von Start auf Ziel Territorium), wenn genug vorhanden sind
                                if (armysForFight >= 1 && enemyArmsToFight <= 0) { // hat der Spieler gewonnen
                                    startTerritory.changeTerritoryArmy(-1);
                                    targetTerritory.changeTerritoryArmy(1);
                                } else { // hat der Spieler verloren, dann gleich weiter
                                    phase = 3;
                                }
                            }else if (SwingUtilities.isRightMouseButton(mouseEvent)){ // fertig mit dem verschieben?
                                phase = 3;
                            }else{ // ungültige Maustaste?
                                systemmessage = "Left-click at target territory or right-click to end this phase.";
                            }
                            break;
                    }
                    break;
                case 3: // Verschieben der Armeen von ein Territorium in ein anderes.
                    if (SwingUtilities.isMiddleMouseButton(mouseEvent)){ // Mittlerer Maustastenklick, um zu beenden
                        phase = 4;
                    }else {
                        // Territorium ermitteln
                            for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                                String key = entry.getKey();
                                Territory ter = entry.getValue();
                                if (mouseClickedPoint.getX() >= ter.getTerritoryCapital().getX() - capitalClickOffset && mouseClickedPoint.getX() <= ter.getTerritoryCapital().getX() + capitalClickOffset &&
                                        mouseClickedPoint.getY() >= ter.getTerritoryCapital().getY() - capitalClickOffset && mouseClickedPoint.getY() <= ter.getTerritoryCapital().getY() + capitalClickOffset) {
                                    if (ter.getTerritoryOwner().equals(players[playerOnTurn].getPlayername())) { // nur sein eigenes Territorium
                                        if (!startTerritoryChosen && !targetTerritoryChosen) { // erstmalig ein Startterritorium wählen
                                            startTerritory = ter;
                                            startTerritoryChosen = true;
                                            systemmessage = "Choose target teritorry to move armys to. (or middle-click to end this round.)";
                                            moveArmysToStartTerritory = false;
                                            moveArmysToTargetTerritory = true;
                                        }else if (startTerritoryChosen && !targetTerritoryChosen){ // erstmalig ein Zielterritorium wählen
                                            targetTerritory = ter;
                                            targetTerritoryChosen = true;
                                            systemmessage = "Right-click to move 1 army to TargetTerritory. (or middle-click to end this round.)";
                                        }

                                        if(SwingUtilities.isLeftMouseButton(mouseEvent)){ //Links-Klick
                                            // Entspricht geklicktem Territorium dem target Territory?
                                            
                                        }else if (SwingUtilities.isRightMouseButton(mouseEvent)){ //Rechts-Klick
                                            if (moveArmysToTargetTerritory){ // Start nach Ziel
                                                if (startTerritory.getTerritoryArmy() >= 2) {
                                                    targetTerritory.changeTerritoryArmy(1);
                                                    startTerritory.changeTerritoryArmy(-1);
                                                }else{
                                                    systemmessage = "not enough armys to move";
                                                }
                                            }else{ // Ziel nach Start
                                                if (targetTerritory.getTerritoryArmy() >= 2) {
                                                    targetTerritory.changeTerritoryArmy(-1);
                                                    startTerritory.changeTerritoryArmy(1);
                                                }else{
                                                    systemmessage = "not enough armys to move";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                    }

                    break;
                case 4:
                    // Variablenreset für den nächsten Spieler
                    break;
                default:
                    System.out.println("DEBUG Game-mouseClicked: unknown phase.");
                    break;
            }


            if (checkGameOver()) { // has one user owned all Territories?
                gameover = true;
                this.setBackground(Color.WHITE);
            }
        }
    }

    public boolean checkGameOver(){
        for (int i = 0; i < players.length; ++i) {
            if (players[playerOnTurn].getNumberOwnedTerritories() == territoriesDraw.size()) {
                systemmessage = "Player " + players[playerOnTurn].getPlayername() + " has won. :D";
                return true;
            }
        }
        return false;
    }

    public void setNextPlayer(){
        if (playerOnTurn + 1 >= players.length){
            playerOnTurn = 0;
        }else{
            playerOnTurn++;
        }
    }

    public Integer getPlayerIndex(String player) {
        return players[0].getPlayername().equals(player) ? 0 : 1;
    }

    // folgende werden nicht benötigt
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
