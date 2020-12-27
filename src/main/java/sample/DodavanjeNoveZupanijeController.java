package main.java.sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.ZupanijaIstogNaziva;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TreeSet;

public class DodavanjeNoveZupanijeController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveZupanijeController.class);

    @FXML
    private TextField nazivZupanije;

    @FXML
    private TextField brojStanovnikaZupanije;

    @FXML
    private TextField brojZarazenihStanovnikaZupanije;

    public void dodajNovuZupaniju() {
        try {
            String nazivZupanijeText = nazivZupanije.getText();
            Integer brojStanovnikaZupanijeNumber = Integer.parseInt(brojStanovnikaZupanije.getText());
            Integer brojZarazenihStanovnikaZupanijeNumber = Integer.parseInt(brojZarazenihStanovnikaZupanije.getText());

            Zupanija novaZupanija = new Zupanija((long) 1, nazivZupanijeText, brojStanovnikaZupanijeNumber, brojZarazenihStanovnikaZupanijeNumber);

            BazaPodataka.spremiNovuZupaniju(novaZupanija);

            PretragaZupanijaController.setObservableListaZupanija(FXCollections.observableArrayList());

            PretragaZupanijaController.getObservableListaZupanija().addAll(BazaPodataka.dohvatiSveZupanije());

            logger.info("Unesena je zupanija: " + novaZupanija.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException ex) {
            logger.error(ex.getMessage());
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        } catch (NumberFormatException exe) {
            logger.error(exe.getMessage());
            PocetniEkranController.neuspjesanUnos(exe.getMessage());
        } catch (ZupanijaIstogNaziva zupanijaIstogNaziva) {
            logger.error(zupanijaIstogNaziva.getMessage());
            PocetniEkranController.neuspjesanUnos(zupanijaIstogNaziva.getMessage());
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            PocetniEkranController.neuspjesanUnos(throwables.getMessage());
        }
    }
}
