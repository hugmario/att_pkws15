import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Game extends JPanel implements MouseListener, MouseMotionListener{

    private String worldfile = "";
    private Player[] players = new Player[2];
    private WorldLoader map;

    public Game(String worldfile){
        this.worldfile = worldfile;

        // Mapfile einlesen und Territorien generieren
        map = new WorldLoader(worldfile);

        // Zeichnen der Mapfile

        // Spieler erstellen
        players[0] = new Player("Mario", Color.green);
        players[1] = new Player("Daniel", Color.blue);

    }

    public void mouseEvent(){}

    // checks if the clicked territory is free, if yes occupies it
    // moves the game to the next phase if all territories are occupied.
    private void occupyTerritories(){}

    private void placeReinforcements(){}

    private int calculateReinforcements(){return 0;}

    private void attackTerritories(){}

    private void mooveTroops(){}

    public Territory getRandomTerritory(){}

    public int gameOver(){return 0;}

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
<<<<<<< HEAD
        // - mitz�hlen, wieviele Territorien der Spieler besitzt (pro 3 Territorien, 1 Armee + Bonusarmeen der Kontinente)
=======
        // - z�hlen, wieviele Territorien der Spieler besitzt (pro 3 Territorien, 1 Armee + Bonusarmeen der Kontinente)
>>>>>>> b6bebd3b422e1ffc58ff3ab122b7f6c6fa4edf5f
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

        // Pr�fen ob Spielende
        //

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    // folgende werden nicht ben�tigt
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
