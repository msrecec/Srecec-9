package main.java.sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.enumeracija.VrijednostSimptoma;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

public class DodavanjeNovogSimptomaController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNovogSimptomaController.class);

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private TextField vrijednostSimptoma;

    public void dodajNoviSimptom() {
        File unosSimptoma = new File("dat/simptomi.txt");
        try (
                FileWriter filewriter = new FileWriter(unosSimptoma, true);
                BufferedWriter writer = new BufferedWriter(filewriter);
        ) {
            String nazivSimptomaText = nazivSimptoma.getText();
            String vrijednostSimptomaText = vrijednostSimptoma.getText();

            Simptom noviSimptom = new Simptom((long) (Main.simptomi.size() + 1), nazivSimptomaText,
                    vrijednostSimptomaText.equals(VrijednostSimptoma.RIJETKO.getVrijednost()) ?
                            VrijednostSimptoma.RIJETKO :
                            vrijednostSimptoma.equals(VrijednostSimptoma.SREDNJE.getVrijednost()) ?
                                    VrijednostSimptoma.SREDNJE :
                                    VrijednostSimptoma.CESTO
            );

    //        Zupanija novaZupanija = new Zupanija((long) (Main.simptomi.size() + 1), nazivSimptomaText, vrijednostSimptomaText);

            if (Main.simptomi == null) {
                Main.simptomi = new HashSet<>();
            }
            Main.simptomi.add(noviSimptom);

            PretragaSimptomaController.setObservableListaSimptoma(FXCollections.observableArrayList());

            PretragaSimptomaController.getObservableListaSimptoma().addAll(Main.simptomi);

            writer.write(noviSimptom.getId().toString()+"\n");
            writer.write(noviSimptom.getNaziv()+"\n");
            writer.write(noviSimptom.getVrijednost().toString()+"\n");

            logger.info("Unesen je simptom: " + noviSimptom.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException ex) {
            logger.error("Ne mogu pronaci datoteku.", ex);
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        } catch (NumberFormatException exc) {
            PocetniEkranController.neuspjesanUnos(exc.getMessage());
        }
    }
}
