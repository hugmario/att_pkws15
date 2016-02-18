import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class World {

    private HashMap<String, Territory> contients;
    private HashMap<String, Landscape[]> territories;

    public World (String worldfile){
        try {
            BufferedReader br = new BufferedReader(new FileReader(worldfile));
            String line = "";
            String splitline[];
            while ((line = br.readLine()) != null) { // solangs noch Zeilen gibt
                if (!line.equals("")) {
                    if (line.startsWith("patch-of")){ // eine Landfläche, die zu einem Territorium zugewiesen wird
                        splitline = line.split(" ");

                    }else if (line.startsWith("capital-of")){ // Hauptstadt des Territoriums
                        splitline = line.split(" ");

                    }else if (line.startsWith("neighnors-of")){ // alle Nachbarn des Territoriums
                        splitline = line.split(" ");

                    }else if (line.startsWith("continent")){ // ein Kontinent mit Bonus und zugehöriger Territorien
                        splitline = line.split(" ");

                    }else{ //ungültiger Wert, da sollte man nie hinkommen

                    }
                }else{
                    // leere Zeile
                }
            }


        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

}
