import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldLoader extends JPanel {


    private HashMap<String, Continent> continentHashMap = new HashMap<String, Continent>();
    private HashMap<String, Territory> territoryHashMap = new HashMap<String, Territory>();


    public WorldLoader(String worldfile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(worldfile));
            String line = "";
            String subline = "";
            String affectedTerritory = "";
            String[] affectedTerritories;
            Polygon poly;
            Point point;
            Territory t, t2;
            Landscape l;
            String splitline[];
            String affectedContinent = "";
            Continent c;
            int bonusarmys;

            while ((line = br.readLine()) != null) { // solangs noch Zeilen gibt
                if (!line.equals("")) {
                    if (line.startsWith("patch-of")) { // eine Landfläche, die zu einem Territorium zugewiesen wird

                        subline = line.substring(8); // erst direkt nach dem patch-of und dem Leerzeichen beginnen
                        subline = subline.trim();
                        affectedTerritory = getAffectedRegion(subline); // betroffenes Territorium rausfinden

                        poly = new Polygon();
                        poly = getCoordinates(subline); // Landscape, welches zum betroffenen Territorium hinzugef�gt wird
                        l = new Landscape(poly); // Polygon dem Landscape geben

                        if (territoryHashMap.containsKey(affectedTerritory)) {
                            // Territorium existiert schon
                            t = territoryHashMap.get(affectedTerritory);
                            t.addLandscapeToTerritory(l);
                            //territoryHashMap.replace(affectedTerritory, t);
                        } else {
                            // Territorium existiert noch nicht
                            t = new Territory(affectedTerritory);
                            t.addLandscapeToTerritory(l);
                            territoryHashMap.put(affectedTerritory, t);
                        }

                    } else if (line.startsWith("capital-of")) { // Hauptstadt des Territoriums
                        subline = line.substring(10); // erst direkt nach dem capital-of beginnen
                        subline = subline.trim();
                        affectedTerritory = getAffectedRegion(subline); // betroffenes Territorium rausfinden

                        point = new Point();
                        point = getCoordinatesCapital(subline); // Landscape, welches zum betroffenen Territorium hinzugef�gt wird

                        // Territorium muss bereits existieren
                        t = territoryHashMap.get(affectedTerritory);
                        t.setTerritoryCapital(point);

                    } else if (line.startsWith("neighbors-of")) { // alle Nachbarn des Territoriums
                        splitline = line.split(":"); // erster Split (Bsp: neighbors-of Island A UND Island B - Island C)
                        splitline[0] = splitline[0].substring(12); //neighbors-of wegschneiden
                        subline = splitline[1]; // Zwischenspeichern des restlichen Strings (also alle Nachbarn von)
                        affectedTerritory = getAffectedRegion(splitline[0]); // welches Territorium wird Nachbarn bekommen?
                        affectedTerritory = affectedTerritory.trim();

                        // Territorium muss bereits existieren
                        t = territoryHashMap.get(affectedTerritory);

                        // Ermitteln der Nachbarn
                        splitline = subline.split("-");
                        for (int i = 0; i < splitline.length; ++i) {
                            splitline[i] = splitline[i].trim(); //etwaige Leerzeichen (davor/danach) entfernen
                            t2 = territoryHashMap.get(splitline[i]);
                            t.addNeighbourToTerritory(t2); // Nachbar eintragen
                            t2.addNeighbourToTerritory(t); // allerdings genauso umgekehrt
                        }

                    } else if (line.startsWith("continent")) { // ein Kontinent mit Bonus und zugeh�riger Territorien
                        // BUG, world.map liest das falsch ein
                        splitline = line.split(":"); // erster Split (Bsp: continent America 3 UND Western America - Eastern America - Southern America)
                        splitline[0] = splitline[0].substring(9); //continent wegschneiden
                        subline = splitline[1]; // Zwischenspeichern des restlichen Strings (also alle Territoriennamen des Continents)

                        splitline = splitline[0].split(" "); //f�r bonusArmy Suche / das America UND 3
                        //affectedContinent = splitline[1];

                        affectedContinent = getAffectedRegion(subline);
                        affectedContinent = affectedContinent.trim();
                        bonusarmys = Integer.parseInt(splitline[splitline.length - 1]); // den letzten Index, da steht fix die Ziffer drinn


                        // Continent immer erstellen (den gibts ja nur einmal)
                        c = new Continent(affectedContinent, bonusarmys);

                        // Ermitteln der zugeh�rigen Territorien
                        splitline = subline.split("-");
                        for (int i = 0; i < splitline.length; ++i) {
                            splitline[i] = splitline[i].trim(); //etwaige Leerzeichen (davor/danach) entfernen
                            t = territoryHashMap.get(splitline[i]);
                            c.addTerritoryToContinent(splitline[i], t);
                        }
                        continentHashMap.put(affectedContinent, c);
                    } else { //ung�ltiger Wert, da sollte man nie hinkommen

                    }
                } else {
                    // leere Zeile

                }
            }
            System.out.println("DEBUG WorldLoader: " + continentHashMap.size() + " Continents with " + territoryHashMap.size() + " Territories total.");


        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public HashMap<String, Continent> getContinentHashMap() {
        return continentHashMap;
    }

    public HashMap<String, Territory> getTerritoryHashMap() {
        return territoryHashMap;
    }

    public String getAffectedRegion(String line) {
        // Pr�fe auf W�rter mit Leerzeichen (zuerst das patch-of usw. wegschneiden, dann klappts)
        Matcher match = Pattern.compile("[a-zA-Z ]+[a-zA-Z]").matcher(line);
        while (match.find()) {
            return match.group();
        }
        throw new IllegalArgumentException();
    }

    public Polygon getCoordinates(String line) {
        String[] stringData = line.split(" ");

        Polygon p = new Polygon();

        for (int i = 0; i < stringData.length; i++) {
            if (isNumber(stringData[i]) && isNumber(stringData[i + 1])) {
                p.addPoint(Integer.parseInt(stringData[i]),
                        Integer.parseInt(stringData[i + 1]));
                i++; //jump over next index
            }
        }
        return p;
    }

    public Point getCoordinatesCapital(String line) {
        String[] stringData = line.split(" ");

        Point p = new Point();

        for (int i = 0; i < stringData.length; i++) {
            if (isNumber(stringData[i]) && isNumber(stringData[i + 1])) {
                p.setLocation(Integer.parseInt(stringData[i]),
                        Integer.parseInt(stringData[i + 1]));
                i++; //jump over next index
            }
        }
        return p;
    }


    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
