package main.java.sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        File unosZupanija = new File("dat/zupanije.txt");
        try (
                FileWriter filewriter = new FileWriter(unosZupanija, true);
                BufferedWriter writer = new BufferedWriter(filewriter);
        ) {
            String nazivZupanijeText = nazivZupanije.getText();
            Integer brojStanovnikaZupanijeNumber = Integer.parseInt(brojStanovnikaZupanije.getText());
            Integer brojZarazenihStanovnikaZupanijeNumber = Integer.parseInt(brojZarazenihStanovnikaZupanije.getText());

            Zupanija novaZupanija = new Zupanija((long) (Main.zupanije.size() + 1), nazivZupanijeText, brojStanovnikaZupanijeNumber, brojZarazenihStanovnikaZupanijeNumber);

            if (Main.zupanije == null) {
                Main.zupanije = new TreeSet<>(new CovidSorter());
            }
            Main.zupanije.add(novaZupanija);

            PretragaZupanijaController.setObservableListaZupanija(FXCollections.observableArrayList());

            PretragaZupanijaController.getObservableListaZupanija().addAll(Main.zupanije);


            writer.write(novaZupanija.getId().toString()+"\n");
            writer.write(novaZupanija.getNaziv()+"\n");
            writer.write(novaZupanija.getBrojStanovnika().toString()+"\n");
            writer.write(novaZupanija.getBrojZarazenih().toString()+"\n");

            logger.info("Unesena je zupanija: " + novaZupanija.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException ex) {
            logger.error("Ne mogu pronaci datoteku.", ex);
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        } catch (NumberFormatException exe) {
            logger.error("Ne mogu pretvoriti datoteku.", exe);
            PocetniEkranController.neuspjesanUnos(exe.getMessage());
        }
    }
}
