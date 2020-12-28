package main.java.sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import main.java.sample.covidportal.iznimke.NepostojecaOsoba;
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

            Set<Bolest> bolesti = BazaPodataka.dohvatiSveBolesti();

            SortedSet<Zupanija> zupanije = BazaPodataka.dohvatiSveZupanije();

            List<Osoba> osobe = BazaPodataka.dohvatiSveOsobe();



            String imeOsobeText = imeOsobe.getText();
            String prezimeOsobeText = prezimeOsobe.getText();
            Date datumRodjenjaDate = Date.valueOf(datumRodjenja.getText());
            Zupanija zupanijaOsobeZupanija = null;
            String bolestOsobeText = bolestOsobe.getText();
            String kontaktiraneOsobeText = kontaktiraneOsobe.getText();
            Bolest bolestOsobeBolest = null;

             // odabir zupanije iz seta zupanija po indeksu

            Long odabranaZupanija = Long.parseLong(zupanijaOsobe.getText());

            Iterator<Zupanija> iteratorZupanija = zupanije.iterator();

            for (int j = 0; j < zupanije.size() && iteratorZupanija.hasNext(); ++j) {
                zupanijaOsobeZupanija = iteratorZupanija.next();
                if (zupanijaOsobeZupanija.getId().compareTo(odabranaZupanija) == 0) {
                    break;
                }
            }

            Long odabranaUnesenaBolest = Long.parseLong(bolestOsobeText);

            Iterator<Bolest> iteratorBolesti = bolesti.iterator();

            for (int j = 0; j < bolesti.size() && iteratorBolesti.hasNext(); ++j) {
                bolestOsobeBolest = iteratorBolesti.next();
                if (bolestOsobeBolest.getId().compareTo(odabranaUnesenaBolest) == 0) {
                    break;
                }
            }

            List<Osoba> finalKontaktiraneOsobe = new ArrayList<>();

            Arrays.stream(kontaktiraneOsobeText.split(",")).forEach(el -> {

                List<Osoba> mojeOsobe = osobe;

                for(Osoba o : mojeOsobe) {
                    if(o.getId().compareTo(Long.parseLong(el)) == 0) {
                        finalKontaktiraneOsobe.add(o);
                    }
                }

            });

            if(finalKontaktiraneOsobe.isEmpty()) {
                throw new NepostojecaOsoba();
            }

            List<Osoba> finalKontaktiraneOsobe1 = finalKontaktiraneOsobe.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());

            Osoba novaOsoba = new Osoba.Builder((long) 1)
                    .ime(imeOsobeText)
                    .prezime(prezimeOsobeText)
                    .datumRodjenja(datumRodjenjaDate)
                    .zupanija(zupanijaOsobeZupanija)
                    .zarazenBolescu(bolestOsobeBolest)
                    .kontaktiraneOsobe(finalKontaktiraneOsobe1)
                    .build();

            BazaPodataka.spremiNovuOsobu(novaOsoba);

            logger.info("Unesena je osoba: " + novaOsoba.getIme());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException | PraznoPolje | NumberFormatException | SQLException | DuplikatKontaktiraneOsobe | NepostojecaOsoba ex) {
            logger.error(ex.getMessage());
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        }
    }
}
