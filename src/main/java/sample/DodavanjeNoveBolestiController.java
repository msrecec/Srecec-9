package main.java.sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.BolestIstihSimptoma;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Virus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DodavanjeNoveBolestiController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveBolestiController.class);

    @FXML
    private TextField nazivBolesti;

    @FXML
    private TextField simptomi;

    public void dodajNovuBolest() {
        try {
            String nazivBolestiText = nazivBolesti.getText();
            String simptomiText = simptomi.getText();

            if(nazivBolestiText.isBlank() || simptomiText.isBlank()) {
                throw new PraznoPolje();
            }

            Set<Simptom> simptomi = BazaPodataka.dohvatiSveSimptome();

            PretragaSimptomaController.setObservableListaSimptoma(FXCollections.observableArrayList());

            PretragaSimptomaController.setSimptomi(simptomi);

            Set<Bolest> bolesti = BazaPodataka.dohvatiSveBolesti();

            PretragaBolestiController.setObservableListaBolesti(FXCollections.observableArrayList());

            PretragaBolestiController.setBolesti(bolesti);

            PretragaBolestiController.getObservableListaBolesti().addAll(PretragaBolestiController.getBolesti());

            List<Long> indexList =  Arrays.stream(simptomiText.split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toList());

            for(Long i : indexList) {
                simptomi.add(BazaPodataka.dohvatiSimptom(i).get());
            }

            Bolest novaBolest = new Bolest((long)1, nazivBolestiText, simptomi);

            BazaPodataka.spremiNovuBolest(novaBolest);

            // Provjera da li je unos bolest ili virus i unos u polje bolesti

            if (PretragaBolestiController.getBolesti() == null) {
                PretragaBolestiController.setBolesti(new HashSet<>());
            }
            PretragaBolestiController.getBolesti().add(novaBolest);

            PretragaBolestiController.setObservableListaBolesti(FXCollections.observableArrayList());

            PretragaBolestiController.getObservableListaBolesti().addAll(PretragaBolestiController.getBolesti().stream().filter(z -> (!(z instanceof Virus))).collect(Collectors.toList()));

            logger.info("Unesena je bolest: " + novaBolest.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException | PraznoPolje | NumberFormatException | BolestIstihSimptoma | SQLException e) {
            logger.error(e.getMessage());
            PocetniEkranController.neuspjesanUnos(e.getMessage());
        }
    }
}
