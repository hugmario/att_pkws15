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

    public void RunGame(){

        // 1.Phase Landerwerb
        // Spieler / PC wählen abwechselnd
        // - wechsel auf Spieler
        // - Spieler klickt auf ein Territorium
        //   - noch nicht besetzt? -> Spieler besetzt es
        //   - schon besetzt? -> passiert nichts, Spieler muss neu wählen
        // - wechsel auf PC
        // - PC sucht nächstmögliches Territorium (nach Regel: benachbarte Territorium zu besetzen)
        //   - noch nicht besetzt? -> PC besetzt es
        //   - schon besetzt? -> passiert nichts, PC sucht nächstes Territorium
        // - prüfen, ob es noch offene Territorien gibt - falls ja, dann wiederholen, sonst Phase 2

        // 2.Phase Eroberungen
        // VERTEILEN DER VERSTÄRKUNGEN
        // Spieler ist an der Reihe
        // - mitzählen, wieviele Territorien der Spieler besitzt (pro 3 Territorien, 1 Armee + Bonusarmeen der Kontinente)
        // - Spieler setzt die Armeen in Territorien
        //   - prüfen, ob dieses Territorium den Spieler gehört
        //     - ja, dann nach Anzahl fragen, wieviele gesetzt werden sollen (von maximal erlaubten)
        //     - nein -> neu klicken
        // - hat der Spieler noch Armeen zum platzieren? - wenn ja, dann wiederholen, wenn nein
        // PC ist an der Reihe
        // - zählen, wieviele Territorien der Spieler besitzt (pro 3 Territorien, 1 Armee + Bonusarmeen der Kontinente)
        // - PC setzt Armeen in Territoren (zuerst passendes suchen)
        //   - PC setzt Höhe an Armeen an durchschnittlicher Armeenzahl benachbarter feindlicher Territorien
        // - wiederholen, falls noch nicht alle Armeen platziert wurden

        // 2.Phase
        // ANGRIFF
        // Spieler ist an der Reihe
        // - Spieler klickt in ein eigenes Territorium
        //   - prüfen ob es seines ist, und mindestens 2 Armeen vorhanden sind (da 1 im Territorium bleiben muss)
        //   - wenn nicht, neu klicken lassen
        // - gültiges Territorium: Spieler klickt auf benachbartes feindliches Territorium
        //   - prüfen ob es ein feindliches ist
        //   - wenn nicht, neu klicken lassen
        // - Angriff mit max. 3 Armeen (wird immer höchstmögliche Anzahl ausgewählt)
        //
        // - Spieler schickt Armee #1 rein - Angreifendes Territorium verteidigt sich mit Armee #1
        // - beide Seiten würfeln:
        // - Spieler hat höhere Augenzahl als PC?
        //   - Spieler Armee überlebt, PC Armee wird vernichtet
        //   - sonst PC Armee überlebt, Spieler Armee wird vernichtet
        // - war es die letzte Armee des PC Territoriums (Spieler hat das PCTerritorium übernommen)?
        //   - ja, Territoriumsbesitz auf Spieler umschreiben und überlebte Armeen eintragen
        //     - erlaube Armeen vom eigenen angreifenden Territorium ins übernommene Territorium nachziehen zu lassen
        //         (wenigstens 1 Armee muss im angreifenden Territorium zurückbleiben)
        // - nein, war es die letzte Armee des Spieler Territoriums (PC hat Spielerterritorium übernommen)?
        //   - ja, Territoriumsbesitz auf PC umschreiben und überlebte Armeen eintragen
        //   - nein, gibt es noch nächste Armeen? (bis zu 2 weitere)
        //     - ja, dann nächste Armeen antreten lassen
        //     - nein, keine Territoriumsänderung (laut Aufgabenstellung soll das aber niemals eintreten...) [DISCUSS]
        //


        // 2.Phase
        // BEWEGEN VON ARMEEN
        // Spieler klickt auf ein eigenes Territorium
        // - nicht seines?, neu klicken
        // Spieler klickt auf ein benachtbartes eigene Territorium
        // - nicht seines?, neu klicken
        // pro Linksklick aufs Zielterritorium wird 1 Armee verschoben
        // pro Rechtsklick aufs Zielterritorium wird 1 Armee wieder zurückverschoben


        // 3.Phase
        // RUNDENABSCHLUSS
        // wenn der Spieler mit dem Verschieben fertig ist, dann die Runde beenden und nächster ist an der Reihe (PC)

        // Prüfen ob Spielende
        //

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

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
