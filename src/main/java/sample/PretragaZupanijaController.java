package main.java.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.sort.CovidSorter;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PretragaZupanijaController implements Initializable {

    private static ObservableList<Zupanija> observableListaZupanija;

    @FXML
    private TableView tablicaZupanija ;
    @FXML
    private TableColumn<Zupanija, String> nazivStupac;
    @FXML
    private TableColumn<Zupanija, Integer> stanovniciStupac;
    @FXML
    private TableColumn<Zupanija, Integer> zarazeniStupac;
    @FXML
    private TextField unosNazivaZupanije;
    @FXML
    private TableColumn<Long, String> idStupac;

    public void pretraga() throws IOException {
        String uneseniNazivZupanije = unosNazivaZupanije.getText().toLowerCase();

        Optional<List<Zupanija>> filtriraneZupanije = Optional.ofNullable(
                Main.zupanije
                .stream()
                .filter(z -> z.getNaziv().toLowerCase().contains(uneseniNazivZupanije))
                .collect(Collectors.toList())
        );
//        System.out.println(filtriraneZupanije.get(0).getNaziv());

        if(filtriraneZupanije.isPresent()) {
//            nazivStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
//            stanovniciStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));
//            zarazeniStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));

            tablicaZupanija.getItems().setAll(filtriraneZupanije.get());
        }


//        tablicaZupanija.setItems(FXCollections.observableArrayList(filtriraneZupanije));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListaZupanija = FXCollections.observableArrayList();
        observableListaZupanija.addAll(Main.zupanije);

        nazivStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
        stanovniciStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));
        zarazeniStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));
        idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

        tablicaZupanija.setItems(observableListaZupanija);
    }

    public static ObservableList<Zupanija> getObservableListaZupanija() {
        return observableListaZupanija;
    }

    public static void setObservableListaZupanija(ObservableList<Zupanija> observableListaZupanija) {
        PretragaZupanijaController.observableListaZupanija = observableListaZupanija;
    }
}
