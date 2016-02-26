import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Game extends JPanel implements MouseListener, MouseMotionListener {

    private String playerOnTurnMessage = "";
    private String systemmessage = "initializing";
    private Player[] players = new Player[2];
    private WorldLoader map;
    private Point mouseClickedPoint;
    private boolean gameover = false;
    private int phase = 0;                  // 0, alles generieren; 1, Starte mit Landerwerb; 2, Angriff; 3, Armeen verschieben
    private int subphase2 = 0;              // Unterphase von Phase 2
    private int playerOnTurn = -1;          // Index: -1, gar keiner; 0, .. usw.
    private int capitalClickOffset = 25;
    private int territoriesInitCounter;     // wenn max, Territorien, dann Phase 1 beendet

    private int playerArmysToSet = 0;       // wieviele Armeen darf der Spieler noch setzen

    private Territory startTerritory;       // eigenes Territorium
    private Territory targetTerritory;      // fremd Territorium
    private int armysForFight;              // wieviele Armeen sind zum K�mpfen verf�gbar?
    private int enemyArmsToFight;           // wieviele Armeen sind zu Besiegen?

    private Random r;
    private Territory tempTerritory;

    HashMap<String, Continent> continentsDraw = new HashMap<String, Continent>();
    HashMap<String, Territory> territoriesDraw = new HashMap<String, Territory>();
    HashMap<Integer, Landscape> landscapesDraw = new HashMap<Integer, Landscape>();


    public Game(String worldfile) {

        // Mapfile einlesen und Territorien generieren
        map = new WorldLoader(worldfile);

        continentsDraw = map.getContinentHashMap();
        territoriesDraw = map.getTerritoryHashMap();

        // Spieler erstellen
        players[0] = new Player("Human", Color.green);
        players[1] = new Player("Computer", Color.blue);

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

        // Zeichne Nachbarn
        for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
            Territory ter = entry.getValue();

            // Zeichne Landscapes eines Territoriums
            for (Map.Entry<String, Territory> entryl : ter.getTerritoryNeighbours().entrySet()) {
                g.setColor(Color.black);
                g.drawLine(ter.getTerritoryCapital().x, ter.getTerritoryCapital().y, entryl.getValue().getTerritoryCapital().x, entryl.getValue().getTerritoryCapital().y);
            }
        }

        for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
            Territory ter = entry.getValue();
            landscapesDraw = ter.getLandscapes();

            // Zeichne Landscapes eines Territoriums
            for (Map.Entry<Integer, Landscape> entryl : landscapesDraw.entrySet()) {
                Landscape landsc = entryl.getValue();

                Polygon p = landsc.getPolygon();
                g.setColor(landsc.getColor());
                g.fillPolygon(p);
                g.setColor(Color.black);
                g.drawPolygon(p);
            }

            // Zeichne Hauptstädte mit Armeenzahl
            if (players[1].getPlayername().equals(ter.getTerritoryOwner())) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.black);
            }
            g.drawString(ter.getTerritoryArmy().toString(), (int) ter.getTerritoryCapital().getX(), (int) ter.getTerritoryCapital().getY());
        }

        g.setColor(Color.black);
        playerOnTurnMessage = "Current Player: " + players[playerOnTurn].getPlayername();
        g.drawString(playerOnTurnMessage, 20, 580);  // Player on turn
        g.drawString(systemmessage, 20, 600);        // Systemmeldung ausgeben


    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        boolean runAgain;
        do {
            // Wenn Computer seinen Zug sofort machen soll, wird 'runAgain' auf 'true' gesetzt
            runAgain = false;
            mouseClickedPoint = mouseEvent.getPoint(); // wo wurde mit der Maus geklickt?
            System.out.println("DEBUG Game-mouseClicked: " + mouseClickedPoint.getX() + ", " + mouseClickedPoint.getY());
            if (gameover) {
                systemmessage = "Player " + players[playerOnTurn].getPlayername() + " has won. :D (restart complete for a new game)";
            } else {
                switch (phase) {
                    case 1:
                        // Wenn Computer an der Reihe, Click auf ein freies Territorium simulieren
                        simulateComputer("NONE", null);
                        // Suche nach gültigen Klick (in der N�he der Hauptstadt)
                        for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                            String key = entry.getKey();
                            Territory ter = entry.getValue();
                            if (checkTerritoryClick(ter, "NONE")) {
                                // nur, wenn es noch von keinem besetzt ist
                                ter.setTerritoryOwner(players[playerOnTurn].getPlayername());
                                ter.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                                ter.setTerritoryArmyInit();
                                players[playerOnTurn].addOneToOwnedTerritories();
                                territoriesInitCounter++;
                                // Computer auch gleich wählen lassen
                                runAgain = true;
                                setNextPlayer();
                            }
                        }

                        // War es das letzte Territorium?
                        System.out.println("DEBUG Game: territoriesInitCounter: " + territoriesInitCounter);
                        if (territoriesInitCounter >= territoriesDraw.size()) {
                            phase = 2;
                            playerOnTurn = 0; // Spieler muss ja Phase 2 beginnen
                            systemmessage = "all territories owned, Phase 2 is next. (please click).";
                        } else {
                            systemmessage = "Occupie an territory (by clicking on a capital).";
                        }

                        break;
                    case 2:
                        switch (subphase2) {
                            case 0:
                                boolean allContinentTerritoriesOwned = true;
                                // Ermittle Anzahl der Verstärkungen zum Platzieren (nur einmal zum Phasenstart pro Spieler)
                                for (Map.Entry<String, Continent> entryc : continentsDraw.entrySet()) {
                                    Continent cont = entryc.getValue();
                                    HashMap<String, Territory> continentsTerritory = new HashMap<String, Territory>();
                                    continentsTerritory = cont.getTerritories();
                                    for (Map.Entry<String, Territory> entryt : continentsTerritory.entrySet()) {
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
                                subphase2 = 1;
                                break;
                            case 1:
                                // Setzen der Verstärkungen
                                if (playerArmysToSet > 0) {
                                    // Wenn Computer an der Reihe, Click auf ein eigenes Territorium simulieren
                                    simulateComputer(players[playerOnTurn].getPlayername(), null);
                                    // Suche gültigen Klick
                                    for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                                        String key = entry.getKey();
                                        Territory ter = entry.getValue();
                                        if (checkTerritoryClick(ter, players[playerOnTurn].getPlayername())) {
                                            // nur sein eigenes Territorium
                                            ter.changeTerritoryArmy(1); // eine pro Links-Klick setzen
                                            playerArmysToSet -= 1;
                                            if (playerArmysToSet > 0) {
                                                // Computer auch gleich wählen lassen
                                                runAgain = true;
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
                                // Wenn Computer an der Reihe, Click auf ein eigenes Territorium simulieren
                                simulateComputer(players[playerOnTurn].getPlayername(), "2.2");
                                for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                                    String key = entry.getKey();
                                    Territory ter = entry.getValue();
                                    if (checkTerritoryClick(ter, players[playerOnTurn].getPlayername())) {
                                        if (checkNeighbors(ter)) {
                                            // nur sein eigenes Territorium
                                            if (ter.getTerritoryArmy() >= 2) {
                                                startTerritory = ter;
                                                startTerritory.setTerritoryColor(Color.red);

                                                // Anzahl der Armeen ermitteln
                                                armysForFight = startTerritory.getTerritoryArmy() - 1;
                                                if (armysForFight > 3) {
                                                    armysForFight = 3;
                                                }
                                                startTerritory.changeTerritoryArmy(-armysForFight);

                                                if (playerOnTurn == 0) {
                                                    systemmessage = "Now choose a territory you want to fight.";
                                                } else {
                                                    systemmessage = players[playerOnTurn].getPlayername() + " chooses a target territory.";
                                                }
                                                subphase2 = 3;
                                            } else {
                                                systemmessage = "Not enough armys at this territory to fight for.";
                                            }
                                        } else {
                                            systemmessage = "Not a valid starting Territory.";
                                        }
                                    }
                                }
                                break;
                            case 3: // Ziel Angriffsland auswählen
                                // Wenn Computer an der Reihe, Click auf ein benachbartes fremdes Territorium simulieren
                                simulateComputer(players[(playerOnTurn + 1) % 2].getPlayername(), "2.3");
                                for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                                    Territory ter = entry.getValue();
                                    if (checkTerritoryClick(ter, null)) {
                                        if (!ter.getTerritoryOwner().equals(players[playerOnTurn].getPlayername())) { // nur ein fremdes Territorium
                                            if (startTerritory.getTerritoryNeighbours().containsKey(ter.getTerritoryName())) {
                                                targetTerritory = ter;

                                                // Anzahl der feindlichen Armeen ermitteln
                                                enemyArmsToFight = Math.min(targetTerritory.getTerritoryArmy(), 2);
                                                targetTerritory.changeTerritoryArmy(-enemyArmsToFight);
                                                systemmessage = "FIGHT.";
                                                System.out.println("DEBUG Phase 2d: Start fight.");

                                                // ANGRIFF
                                                // Würfeln
                                                r = new Random();

                                                // Höchste und zweithöchste Würfelzahl aller beteiligten Armeen ausrechnen
                                                int highestDiceOwn = 0;
                                                int secHighestDiceOwn = 0;
                                                int highestDiceEnemy = 0;
                                                int secHighestDiceEnemy = 0;
                                                for (int i = 0; i < armysForFight; i++) {
                                                    int dice = r.nextInt(6) + 1;
                                                    if (dice > highestDiceOwn) {
                                                        secHighestDiceOwn = highestDiceOwn;
                                                        highestDiceOwn = dice;
                                                    } else if (dice > secHighestDiceOwn) {
                                                        secHighestDiceOwn = dice;
                                                    }
                                                }
                                                for (int i = 0; i < enemyArmsToFight; i++) {
                                                    int dice = r.nextInt(6) + 1;
                                                    if (dice > highestDiceEnemy) {
                                                        secHighestDiceEnemy = highestDiceEnemy;
                                                        highestDiceEnemy = dice;
                                                    } else if (dice > secHighestDiceEnemy) {
                                                        secHighestDiceEnemy = dice;
                                                    }
                                                }

                                                int backup = enemyArmsToFight;
                                                if (highestDiceOwn > highestDiceEnemy) {
                                                    enemyArmsToFight--;
                                                } else {
                                                    armysForFight--;
                                                }
                                                // Wenn zweite Armee verteidigt auch zweithöchsten Wert anschauen
                                                if (backup == 2) {
                                                    if (secHighestDiceOwn > secHighestDiceEnemy) {
                                                        enemyArmsToFight--;
                                                    } else {
                                                        armysForFight--;
                                                    }
                                                }

                                                targetTerritory.changeTerritoryArmy(enemyArmsToFight); // Überlebte Verteidiger eintragen
                                                if (targetTerritory.getTerritoryArmy() >= 1) {
                                                    // Verteidiger hat gewonnen
                                                    startTerritory.changeTerritoryArmy(armysForFight); // Überlebte Angreifer eintragen
                                                    System.out.println(players[playerOnTurn].getPlayername() + " lost.");
                                                    systemmessage = players[playerOnTurn].getPlayername() + " lost. Click to end this phase.";
                                                    startTerritory.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                                                } else {
                                                    // Spieler hat gewonnen
                                                    players[getPlayerIndex(targetTerritory.getTerritoryName())].removeOneToOwnedTerritories();
                                                    players[playerOnTurn].addOneToOwnedTerritories();                           // erobertes Territorium hinzuz�hlen
                                                    targetTerritory.setTerritoryOwner(players[playerOnTurn].getPlayername());   // Besitzer umschreiben
                                                    targetTerritory.setTerritoryArmy(armysForFight);                            // �berlebte Armeen eintragen
                                                    targetTerritory.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                                                    System.out.println(players[playerOnTurn].getPlayername() + " won.");
                                                    systemmessage = players[playerOnTurn].getPlayername() + " won. Left-Click at target territory to move armys (1 per click) or right-click to end this phase.";
                                                    startTerritory.setTerritoryColor(Color.red);
                                                    targetTerritory.setTerritoryColor(Color.yellow);
                                                }
                                                subphase2 = 4;
                                            } else {
                                                systemmessage = "Not a valid target Territory.";
                                            }
                                        } else {
                                            systemmessage = "That's not a foreign territory.";
                                        }
                                    }
                                }
                                break;
                            case 4: // Armeen nachziehen
                                // Wenn Computer an der Reihe, zufällige Anzahl nachziehen
                                if (playerOnTurn == 1) {
                                    int n = 0;
                                    if (targetTerritory.getTerritoryOwner().equals(players[playerOnTurn].getPlayername()) && startTerritory.getTerritoryArmy() > 1) { // hat der Computer gewonnen
                                        r = new Random();
                                        n = r.nextInt(startTerritory.getTerritoryArmy());
                                        startTerritory.changeTerritoryArmy(-n);
                                        targetTerritory.changeTerritoryArmy(n);
                                    }
                                    phase = 3;
                                    startTerritory.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                                    targetTerritory.setTerritoryColor(players[getPlayerIndex(targetTerritory.getTerritoryName())].getPlayerColor());
                                    startTerritory = null;
                                    targetTerritory = null;
                                    systemmessage = "Computer moved " + n + " armys after attack.";
                                } else if (SwingUtilities.isLeftMouseButton(mouseEvent)) { // Links klick erlaubt 1 Armee verschieben (von Start auf Ziel Territorium), wenn genug vorhanden sind
                                    if (targetTerritory.getTerritoryOwner().equals(players[playerOnTurn].getPlayername()) && startTerritory.getTerritoryArmy() > 1) { // hat der Spieler gewonnen
                                        startTerritory.changeTerritoryArmy(-1);
                                        targetTerritory.changeTerritoryArmy(1);
                                    } else { // hat der Spieler verloren, dann gleich weiter
                                        phase = 3;
                                        startTerritory.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                                        targetTerritory.setTerritoryColor(players[getPlayerIndex(targetTerritory.getTerritoryName())].getPlayerColor());
                                        startTerritory = null;
                                        targetTerritory = null;
                                        systemmessage = "Choose start territory to move armys from with left-click (or middle-click to end this round.)";
                                    }
                                } else if (SwingUtilities.isRightMouseButton(mouseEvent)) { // fertig mit dem verschieben?
                                    phase = 3;
                                    startTerritory.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                                    targetTerritory.setTerritoryColor(players[getPlayerIndex(targetTerritory.getTerritoryName())].getPlayerColor());
                                    startTerritory = null;
                                    targetTerritory = null;
                                    systemmessage = "Choose start territory to move armys from with left-click (or middle-click to end this round.)";
                                } else { // ungültige Maustaste?
                                    systemmessage = "Left-click at target territory or right-click to end this phase.";
                                }
                                break;
                        }
                        break;
                    case 3: // Verschieben der Armeen von ein Territorium in ein anderes.
                        if (SwingUtilities.isMiddleMouseButton(mouseEvent)) { // Mittlerer Maustastenklick, um zu beenden
                            phase = 4;
                        } else {
                            boolean fakeLeft = false;
                            boolean fakeRight = false;
                            if (playerOnTurn == 1) {
                                // Wenn Computer an der Reihe, Click auf ein eigenes Territorium simulieren
                                if (startTerritory == null) {
                                    simulateComputer(players[playerOnTurn].getPlayername(), "3");
                                    fakeLeft = true;
                                } else if (targetTerritory == null) {
                                    simulateComputer(players[playerOnTurn].getPlayername(), "2.3");
                                    fakeRight = true;
                                } else if (startTerritory.getTerritoryArmy() > 1) {
                                    r = new Random();
                                    int n = r.nextInt(startTerritory.getTerritoryArmy());
                                    startTerritory.changeTerritoryArmy(-n);
                                    targetTerritory.changeTerritoryArmy(n);
                                    systemmessage = "Computer moved " + n + " armys.";
                                    phase = 4;
                                    break;
                                }
                            }
                            // Territorium ermitteln
                            for (Map.Entry<String, Territory> entry : territoriesDraw.entrySet()) {
                                Territory ter = entry.getValue();
                                if (checkTerritoryClick(ter, players[playerOnTurn].getPlayername())) {
                                    // nur sein eigenes Territorium
                                    if (fakeLeft || SwingUtilities.isLeftMouseButton(mouseEvent)) { // erstmalig ein Startterritorium wählen
                                        if (startTerritory == null) {
                                            if (ter.getTerritoryArmy() > 1 && checkNeighborsOwn(ter)) {
                                                startTerritory = ter;
                                                startTerritory.setTerritoryColor(Color.red);
                                                systemmessage = "Choose target territory to move armys to with left-click (or middle-click to end this round.)";
                                            }
                                        } else if (targetTerritory == null) {
                                            if (startTerritory.getTerritoryNeighbours().containsKey(ter.getTerritoryName())) {
                                                targetTerritory = ter;
                                                targetTerritory.setTerritoryColor(Color.yellow);
                                                systemmessage = "Right-click to move 1 army to target, left-click to switch. (or middle-click to end this round.)";
                                            }
                                        } else {
                                            tempTerritory = targetTerritory;
                                            targetTerritory = startTerritory;
                                            startTerritory = tempTerritory;
                                            startTerritory.setTerritoryColor(Color.red);
                                            targetTerritory.setTerritoryColor(Color.yellow);
                                        }
                                    } else if (fakeRight || SwingUtilities.isRightMouseButton(mouseEvent)) { // erstmalig ein Zielterritorium wählen
                                        if (targetTerritory != null) {
                                            if (startTerritory.getTerritoryArmy() >= 2) {
                                                targetTerritory.changeTerritoryArmy(1);
                                                startTerritory.changeTerritoryArmy(-1);
                                            } else {
                                                systemmessage = "not enough armys to move";
                                                phase = 4;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        break;
                    case 4:
                        // Variablenreset für den nächsten Spieler
                        phase = 2;
                        if (playerOnTurn == 0) {
                            subphase2 = 2;
                        } else {
                            subphase2 = 0;
                        }
                        startTerritory.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                        targetTerritory.setTerritoryColor(players[playerOnTurn].getPlayerColor());
                        startTerritory = null;
                        targetTerritory = null;
                        setNextPlayer();
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
        } while (runAgain && playerOnTurn == 1);
    }

    private void simulateComputer(String territoryOwner, String phase) {
        if (playerOnTurn == 1) {
            r = new Random();
            int i = 0;
            List<Territory> list = new ArrayList<Territory>();
            for (Territory ter : territoriesDraw.values()) {
                // Wenn der Eigentümer egal ist oder gleich dem übergebenen
                if ((territoryOwner == null || territoryOwner.equals(ter.getTerritoryOwner()))) {
                    // Wenn ein Start-Territorium ausgewählt werden soll, genug Armeen und mindestens ein Feind als Nachbar prüfen
                    if (!"2.2".equals(phase) || ter.getTerritoryArmy() >= 2 && checkNeighbors(ter)) {
                        // Wenn ein benachbartes Ziel-Territorium ausgewählt werden soll
                        if (!"2.3".equals(phase) || startTerritory.getTerritoryNeighbours().containsKey(ter.getTerritoryName())) {
                            if (!"3".equals(phase) || ter.getTerritoryArmy() > 1 && checkNeighborsOwn(ter)) {
                                list.add(ter);
                            }
                        }
                    }
                }
            }
            if (list.size() == 0) {
                mouseClickedPoint = null;
                systemmessage = players[playerOnTurn].getPlayername() + " can not find a move.";
            } else {
                int randomTerIndex = r.nextInt(list.size());
                mouseClickedPoint = list.get(randomTerIndex).getTerritoryCapital();
            }
        }
    }

    private boolean checkNeighbors(Territory territory) {
        for (Territory ter : territory.getTerritoryNeighbours().values()) {
            if (!ter.getTerritoryOwner().equals(territory.getTerritoryOwner())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkNeighborsOwn(Territory territory) {
        for (Territory ter : territory.getTerritoryNeighbours().values()) {
            if (ter.getTerritoryOwner().equals(territory.getTerritoryOwner())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTerritoryClick(Territory ter, String owner) {
        if (mouseClickedPoint != null) {
            if (owner == null || owner.equals(ter.getTerritoryOwner())) {
                if (mouseClickedPoint.getX() >= ter.getTerritoryCapital().getX() - capitalClickOffset && mouseClickedPoint.getX() <= ter.getTerritoryCapital().getX() + capitalClickOffset &&
                        mouseClickedPoint.getY() >= ter.getTerritoryCapital().getY() - capitalClickOffset && mouseClickedPoint.getY() <= ter.getTerritoryCapital().getY() + capitalClickOffset) {
                    return true;
                }
                for (Landscape l : ter.getLandscapes().values()) {
                    if (l.getPolygon().contains(mouseClickedPoint)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkGameOver() {
        for (int i = 0; i < players.length; ++i) {
            if (players[playerOnTurn].getNumberOwnedTerritories() == territoriesDraw.size()) {
                systemmessage = "Player " + players[playerOnTurn].getPlayername() + " has won. :D";
                return true;
            }
        }
        return false;
    }

    public void setNextPlayer() {
        if (playerOnTurn + 1 >= players.length) {
            playerOnTurn = 0;
        } else {
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
