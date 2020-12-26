package main.java.sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Osoba;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DodavanjeNoveOsobeController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveZupanijeController.class);

    @FXML
    private TextField imeOsobe;
    @FXML
    private TextField prezimeOsobe;
    @FXML
    private TextField starostOsobe;
    @FXML
    private TextField zupanijaOsobe;
    @FXML
    private TextField bolestOsobe;
    @FXML
    private TextField kontaktiraneOsobe;

    public void dodajNovuOsobu() {
        File unosOsoba = new File("dat/osobe.txt");
        try (
                FileWriter filewriter = new FileWriter(unosOsoba, true);
                BufferedWriter writer = new BufferedWriter(filewriter);
        ) {
            String imeOsobeText = imeOsobe.getText();
            String prezimeOsobeText = prezimeOsobe.getText();
            Integer starostOsobeInteger = Integer.parseInt(starostOsobe.getText());
            Zupanija zupanijaOsobeZupanija = null;
            String bolestOsobeText = bolestOsobe.getText();
            String kontaktiraneOsobeText = kontaktiraneOsobe.getText();
            Bolest bolestOsobeBolest = null;

             // odabir zupanije iz seta zupanija po indeksu

            Long odabranaZupanija = Long.parseLong(zupanijaOsobe.getText());

            Iterator<Zupanija> iteratorZupanija = Main.zupanije.iterator();

            for (int j = 0; j < Main.zupanije.size() && iteratorZupanija.hasNext(); ++j) {
                zupanijaOsobeZupanija = iteratorZupanija.next();
                if (zupanijaOsobeZupanija.getId().compareTo(odabranaZupanija) == 0) {
                    break;
                }
            }

            Long odabranaUnesenaBolest = Long.parseLong(bolestOsobeText);

            Iterator<Bolest> iteratorBolesti = Main.bolesti.iterator();

            for (int j = 0; j < Main.bolesti.size() && iteratorBolesti.hasNext(); ++j) {
                bolestOsobeBolest = iteratorBolesti.next();
                if (bolestOsobeBolest.getId().compareTo(odabranaUnesenaBolest) == 0) {
                    break;
                }
            }

            List<Osoba> finalKontaktiraneOsobe = new ArrayList<>();

            Arrays.stream(kontaktiraneOsobeText.split(",")).forEach(el -> {

                for(Osoba o : Main.osobe) {
                    if(o.getId().compareTo(Long.parseLong(el)) == 0) {
                        finalKontaktiraneOsobe.add(o);
                    }
                }

            });

            Osoba novaOsoba = new Osoba.Builder((long) Main.osobe.size()+1)
                    .ime(imeOsobeText)
                    .prezime(prezimeOsobeText)
                    .starost(starostOsobeInteger)
                    .zupanija(zupanijaOsobeZupanija)
                    .zarazenBolescu(bolestOsobeBolest)
                    .kontaktiraneOsobe(finalKontaktiraneOsobe)
                    .build();

            if (Main.osobe == null) {
                Main.osobe = new ArrayList<>();
            }
            Main.osobe.add(novaOsoba);

            PretragaOsobaController.setObservableListaOsoba(FXCollections.observableArrayList());

            PretragaOsobaController.getObservableListaOsoba().addAll(Main.osobe);


            writer.write(novaOsoba.getId().toString()+"\n");
            writer.write(novaOsoba.getIme()+"\n");
            writer.write(novaOsoba.getPrezime()+"\n");
            writer.write(novaOsoba.getStarost()+"\n");
            writer.write(novaOsoba.getZupanija().getId().toString()+"\n");
            writer.write(novaOsoba.getZarazenBolescu().getId().toString()+"\n");
            writer.write(String.join(",", novaOsoba
                    .getKontaktiraneOsobe()
                    .stream()
                    .map(osoba -> osoba.getId().toString())
                    .collect(Collectors.toList()))+"\n");

            logger.info("Unesena je osoba: " + novaOsoba.getIme());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException ex) {
            logger.error("Ne mogu pronaci datoteku.", ex);
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        } catch (NumberFormatException exc) {
            logger.error("Ne mogu pretvoriti vrijednost: ", exc);
            PocetniEkranController.neuspjesanUnos(exc.getMessage());
        }
    }
}
