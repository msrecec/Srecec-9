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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaSimptomaController implements Initializable {
    private static ObservableList<Simptom> observableListaSimptoma;

    @FXML
    private TableView tablicaSimptoma ;
    @FXML
    private TableColumn<Simptom, String> nazivStupac;
    @FXML
    private TableColumn<Simptom, String> vrijednostStupac;
    @FXML
    private TextField unosNazivaSimptoma;
    @FXML
    private TableColumn<Long, String> idStupac;

    public void pretraga() throws IOException {
        String uneseniNazivSimptoma = unosNazivaSimptoma.getText().toLowerCase();

        Optional<List<Simptom>> filtriraniSimptom = Optional.ofNullable(Main.simptomi.stream().filter(z -> z.getNaziv().toLowerCase().contains(uneseniNazivSimptoma)).collect(Collectors.toList()));
//        System.out.println(filtriraniSimptom.get(0).getNaziv());

        if(filtriraniSimptom.isPresent()) {
//            nazivStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
//            vrijednostStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("vrijednost"));

            tablicaSimptoma.getItems().setAll(filtriraniSimptom.get());
        }

//        tablicaZupanija.setItems(FXCollections.observableArrayList(filtriraneZupanije));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListaSimptoma = FXCollections.observableArrayList();
        observableListaSimptoma.addAll(Main.simptomi);

        nazivStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
        vrijednostStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("vrijednost"));
        idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

        tablicaSimptoma.setItems(observableListaSimptoma);
    }

    public static ObservableList<Simptom> getObservableListaSimptoma() {
        return observableListaSimptoma;
    }

    public static void setObservableListaSimptoma(ObservableList<Simptom> observableListaSimptoma) {
        PretragaSimptomaController.observableListaSimptoma = observableListaSimptoma;
    }
}
