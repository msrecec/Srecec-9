package main.java.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Osoba;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Zupanija;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {
    private static ObservableList<Osoba> observableListaOsoba;

    @FXML
    private TableView tablicaOsoba ;
    @FXML
    private TableColumn<Osoba, String> imeStupac;
    @FXML
    private TableColumn<Osoba, String> prezimeStupac;
    @FXML
    private TableColumn<Osoba, Integer> starostStupac;
    @FXML
    private TableColumn<Osoba, Zupanija> zupanijaStupac;
    @FXML
    private TableColumn<Osoba, Bolest> bolestStupac;
    @FXML
    private TableColumn<Osoba, List<Osoba>> kontaktiraneOsobeStupac;
    @FXML
    private TextField unosNazivaOsobe;

    public void pretraga() throws IOException {
        String uneseniNazivOsobe = unosNazivaOsobe.getText().toLowerCase();

        Optional<List<Osoba>> filtriraneOsobePoImenu = Optional.ofNullable(Main.osobe.stream()
                .filter(z -> (z.getIme().toLowerCase().contains(uneseniNazivOsobe)))
                .collect(Collectors.toList()));
        Optional<List<Osoba>> filtriraneOsobePoPrezimenu = Optional.ofNullable(Main.osobe.stream()
                .filter(z -> (z.getPrezime().toLowerCase().contains(uneseniNazivOsobe)))
                .collect(Collectors.toList()));
//        System.out.println(filtriraneZupanije.get(0).getNaziv());

        if(filtriraneOsobePoImenu.isPresent() || filtriraneOsobePoPrezimenu.isPresent()) {
            List<Osoba> filtriraneOsobe = new ArrayList<>();
            if(filtriraneOsobePoImenu.isPresent() && filtriraneOsobePoPrezimenu.isPresent()) {
                filtriraneOsobe.addAll(filtriraneOsobePoImenu.get());
                filtriraneOsobe.addAll(filtriraneOsobePoPrezimenu.get());
            } else if(filtriraneOsobePoImenu.isPresent() && filtriraneOsobePoPrezimenu.isEmpty()) {
                filtriraneOsobe.addAll(filtriraneOsobePoImenu.get());
            } else {
                filtriraneOsobe.addAll(filtriraneOsobePoPrezimenu.get());
            }
            Set<Osoba> setFiltriranihOsoba = new HashSet(filtriraneOsobe);
            filtriraneOsobe = new ArrayList<>();
            filtriraneOsobe.addAll(setFiltriranihOsoba);
//            imeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
//            prezimeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
//            starostStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starost"));
//            zupanijaStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Zupanija>("zupanija"));
//            bolestStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Bolest>("zarazenBolescu"));
//            kontaktiraneOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, List<Osoba>>("kontaktiraneOsobe"));

            tablicaOsoba.getItems().setAll(filtriraneOsobe);
        }


//        tablicaZupanija.setItems(FXCollections.observableArrayList(filtriraneZupanije));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListaOsoba = FXCollections.observableArrayList();
        observableListaOsoba.addAll(Main.osobe);

        imeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
        prezimeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
        starostStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starost"));
        zupanijaStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Zupanija>("zupanija"));
        bolestStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Bolest>("zarazenBolescu"));
        kontaktiraneOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, List<Osoba>>("kontaktiraneOsobe"));

        tablicaOsoba.setItems(observableListaOsoba);
    }

    public static ObservableList<Osoba> getObservableListaOsoba() {
        return observableListaOsoba;
    }

    public static void setObservableListaOsoba(ObservableList<Osoba> observableListaOsoba) {
        PretragaOsobaController.observableListaOsoba = observableListaOsoba;
    }
}
