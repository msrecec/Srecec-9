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
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Virus;
import main.java.sample.covidportal.model.Zupanija;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PretragaVirusiController implements Initializable {

    private static ObservableList<Bolest> observableListaVirusa;

    @FXML
    private TableView tablicaVirusa ;
    @FXML
    private TableColumn<Bolest, String> nazivStupac;
    @FXML
    private TableColumn<Set<Simptom>, String> simptomiStupac;
    @FXML
    private TableColumn<Long, String> idStupac;
    @FXML
    private TextField unosNazivaVirusa;

    public void pretraga() throws IOException {
        String uneseniNazivVirusa = unosNazivaVirusa.getText().toLowerCase();

        Optional<List<Bolest>> filtriranaBolest = Optional.ofNullable(
                Main.bolesti
                .stream()
                .filter(z -> ((z instanceof Virus)) && z.getNaziv().toLowerCase().contains(uneseniNazivVirusa))
                .collect(Collectors.toList())
        );
//        System.out.println(filtriranaBolest.get(0).getNaziv());

        if(filtriranaBolest.isPresent()) {
//            nazivStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
//            simptomiStupac.setCellValueFactory(new PropertyValueFactory<Set<Simptom>, String>("simptomi"));

            tablicaVirusa.getItems().setAll(filtriranaBolest.get());
        }


//        tablicaZupanija.setItems(FXCollections.observableArrayList(filtriraneZupanije));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListaVirusa = FXCollections.observableArrayList();
        observableListaVirusa.addAll(Main.bolesti.stream().filter(z -> ((z instanceof Virus))).collect(Collectors.toList()));

        nazivStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        simptomiStupac.setCellValueFactory(new PropertyValueFactory<Set<Simptom>, String>("simptomi"));
        idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

        tablicaVirusa.setItems(observableListaVirusa);

    }

    public static ObservableList<Bolest> getObservableListaVirusa() {
        return observableListaVirusa;
    }

    public static void setObservableListaVirusa(ObservableList<Bolest> observableListaVirusa) {
        PretragaVirusiController.observableListaVirusa = observableListaVirusa;
    }
}
