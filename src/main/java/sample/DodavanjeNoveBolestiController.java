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
            Optional<Simptom> dohvaceniSimptom;

            String nazivBolestiText = nazivBolesti.getText();
            String simptomiText = simptomi.getText();

            if(nazivBolestiText.isBlank() || simptomiText.isBlank()) {
                throw new PraznoPolje();
            }

            Set<Simptom> simptomi = new HashSet<>();

            List<Long> indexList =  Arrays.stream(simptomiText.split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toSet()).stream().collect(Collectors.toList());

            for(Long i : indexList) {
                dohvaceniSimptom = BazaPodataka.dohvatiSimptom(i);
                if(dohvaceniSimptom.isPresent()) {
                    System.out.println("Index od simptoma: " + i);
                    simptomi.add(dohvaceniSimptom.get());
                }
            }

            Bolest novaBolest = new Bolest((long)1, nazivBolestiText, simptomi);

            BazaPodataka.spremiNovuBolest(novaBolest);

            logger.info("Unesena je bolest: " + novaBolest.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException | PraznoPolje | NumberFormatException | BolestIstihSimptoma | SQLException e) {
            logger.error(e.getMessage());
            PocetniEkranController.neuspjesanUnos(e.getMessage());
        }
    }
}
