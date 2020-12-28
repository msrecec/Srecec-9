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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class DodavanjeNovogVirusaController {


    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNovogVirusaController.class);

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TextField simptomi;

    public void dodajNovuBolest() {
        try {
            String nazivVirusaText = nazivVirusa.getText();
            String simptomiText = simptomi.getText();

            if(nazivVirusaText.isBlank() || simptomiText.isBlank()) {
                throw new PraznoPolje();
            }

            Set<Simptom> simptomi = BazaPodataka.dohvatiSveSimptome();

            PretragaSimptomaController.setObservableListaSimptoma(FXCollections.observableArrayList());

            PretragaSimptomaController.setSimptomi(simptomi);

            Set<Bolest> virusi = BazaPodataka.dohvatiSveBolesti();

            PretragaVirusiController.setObservableListaVirusa(FXCollections.observableArrayList());

            PretragaVirusiController.setVirusi(virusi);


            PretragaBolestiController.getObservableListaBolesti().addAll(PretragaBolestiController.getBolesti().stream().filter(z -> ((z instanceof Virus))).collect(Collectors.toList()));

            Arrays.stream(simptomiText.split(",")).forEach(el -> {

                // Iteracija simptoma po indeksu

                int element = Integer.parseInt(el);

                Simptom simptom;

                Iterator<Simptom> iteratorSimptoma = PretragaSimptomaController.getSimptomi().iterator();
                Simptom pronadeniOdabraniSimptom = null;

                for (int k = 0; k < PretragaSimptomaController.getSimptomi().size() && iteratorSimptoma.hasNext(); ++k) {
                    simptom = iteratorSimptoma.next();
                    if (simptom.getId() == (element)) {
                        pronadeniOdabraniSimptom = simptom;
                        PretragaSimptomaController.getSimptomi().add(pronadeniOdabraniSimptom);
                    }
                }


            } );

            Virus noviVirus = new Virus((long)1, nazivVirusaText, simptomi);

            BazaPodataka.spremiNovuBolest(noviVirus);

            // Provjera da li je unos bolest ili virus i unos u polje bolesti

            if (PretragaBolestiController.getBolesti() == null) {
                PretragaBolestiController.setBolesti(new HashSet<>());
            }
            PretragaBolestiController.getBolesti().add(noviVirus);

            PretragaBolestiController.setObservableListaBolesti(FXCollections.observableArrayList());

            PretragaBolestiController.getObservableListaBolesti().addAll(PretragaBolestiController.getBolesti().stream().filter(z -> ((z instanceof Virus))).collect(Collectors.toList()));

            logger.info("Unesena je bolest: " + noviVirus.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException | PraznoPolje | NumberFormatException | BolestIstihSimptoma | SQLException e) {
            logger.error(e.getMessage());
            PocetniEkranController.neuspjesanUnos(e.getMessage());
        }
    }
}
