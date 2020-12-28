package main.java.sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Osoba;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DodavanjeNoveOsobeController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveOsobeController.class);

    @FXML
    private TextField imeOsobe;
    @FXML
    private TextField prezimeOsobe;
    @FXML
    private TextField datumRodjenja;
    @FXML
    private TextField zupanijaOsobe;
    @FXML
    private TextField bolestOsobe;
    @FXML
    private TextField kontaktiraneOsobe;

    public void dodajNovuOsobu() {
        try {
            if (
                imeOsobe.getText().isBlank() ||
                prezimeOsobe.getText().isBlank() ||
                datumRodjenja.getText().isBlank() ||
                bolestOsobe.getText().isBlank() ||
                kontaktiraneOsobe.getText().isBlank()
            ) {
                throw new PraznoPolje();
            }



            Set<Simptom> simptomi = BazaPodataka.dohvatiSveSimptome();

            PretragaSimptomaController.setObservableListaSimptoma(FXCollections.observableArrayList());

            PretragaSimptomaController.setSimptomi(simptomi);

            Set<Bolest> bolesti = BazaPodataka.dohvatiSveBolesti();

            PretragaBolestiController.setObservableListaBolesti(FXCollections.observableArrayList());

            PretragaBolestiController.setBolesti(bolesti);

            SortedSet<Zupanija> zupanije = BazaPodataka.dohvatiSveZupanije();

            PretragaZupanijaController.setObservableListaZupanija(FXCollections.observableArrayList());

            PretragaZupanijaController.setZupanije(zupanije);



            String imeOsobeText = imeOsobe.getText();
            String prezimeOsobeText = prezimeOsobe.getText();
            Date datumRodjenjaDate = Date.valueOf(datumRodjenja.getText());
            Zupanija zupanijaOsobeZupanija = null;
            String bolestOsobeText = bolestOsobe.getText();
            String kontaktiraneOsobeText = kontaktiraneOsobe.getText();
            Bolest bolestOsobeBolest = null;

             // odabir zupanije iz seta zupanija po indeksu

            Long odabranaZupanija = Long.parseLong(zupanijaOsobe.getText());

            Iterator<Zupanija> iteratorZupanija = PretragaZupanijaController.getZupanije().iterator();

            for (int j = 0; j < PretragaZupanijaController.getZupanije().size() && iteratorZupanija.hasNext(); ++j) {
                zupanijaOsobeZupanija = iteratorZupanija.next();
                if (zupanijaOsobeZupanija.getId().compareTo(odabranaZupanija) == 0) {
                    break;
                }
            }

            Long odabranaUnesenaBolest = Long.parseLong(bolestOsobeText);

            Iterator<Bolest> iteratorBolesti = PretragaBolestiController.getBolesti().iterator();

            for (int j = 0; j < PretragaBolestiController.getBolesti().size() && iteratorBolesti.hasNext(); ++j) {
                bolestOsobeBolest = iteratorBolesti.next();
                if (bolestOsobeBolest.getId().compareTo(odabranaUnesenaBolest) == 0) {
                    break;
                }
            }

            List<Osoba> finalKontaktiraneOsobe = new ArrayList<>();

            Arrays.stream(kontaktiraneOsobeText.split(",")).forEach(el -> {

                List<Osoba> mojeOsobe = PretragaOsobaController.getOsobe();

                for(Osoba o : mojeOsobe) {
                    if(o.getId().compareTo(Long.parseLong(el)) == 0) {
                        finalKontaktiraneOsobe.add(o);
                    }
                }

            });

            Osoba novaOsoba = new Osoba.Builder((long) 1)
                    .ime(imeOsobeText)
                    .prezime(prezimeOsobeText)
                    .datumRodjenja(datumRodjenjaDate)
                    .zupanija(zupanijaOsobeZupanija)
                    .zarazenBolescu(bolestOsobeBolest)
                    .kontaktiraneOsobe(finalKontaktiraneOsobe)
                    .build();

            BazaPodataka.spremiNovuOsobu(novaOsoba);

            if (PretragaOsobaController.getOsobe() == null) {
                PretragaOsobaController.setOsobe(new ArrayList<>());
            }
            PretragaOsobaController.getOsobe().add(novaOsoba);

            PretragaOsobaController.setObservableListaOsoba(FXCollections.observableArrayList());

            PretragaOsobaController.getObservableListaOsoba().addAll(PretragaOsobaController.getOsobe());

            logger.info("Unesena je osoba: " + novaOsoba.getIme());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException | PraznoPolje | NumberFormatException | SQLException | DuplikatKontaktiraneOsobe ex) {
            logger.error(ex.getMessage());
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        }
    }
}
