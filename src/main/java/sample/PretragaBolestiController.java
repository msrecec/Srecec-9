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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class PretragaBolestiController implements Initializable {
    private static ObservableList<Bolest> observableListaBolesti;

    @FXML
    private TableView tablicaBolesti ;
    @FXML
    private TableColumn<Bolest, String> nazivStupac;
    @FXML
    private TableColumn<Set<Simptom>, String> simptomiStupac;
    @FXML
    private TableColumn<Long, String> idStupac;
    @FXML
    private TextField unosNazivaBolesti;

    public void pretraga() throws IOException {
        String uneseniNazivBolesti = unosNazivaBolesti.getText().toLowerCase();

        Optional<List<Bolest>> filtriranaBolest = Optional.ofNullable(
                Main.bolesti
                .stream()
                .filter(z -> (!(z instanceof Virus)) && z.getNaziv().toLowerCase().contains(uneseniNazivBolesti))
                .collect(Collectors.toList())
        );
//        System.out.println(filtriranaBolest.get(0).getNaziv());

        if(filtriranaBolest.isPresent()) {
//            nazivStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
//            simptomiStupac.setCellValueFactory(new PropertyValueFactory<Set<Simptom>, String>("simptomi"));

            tablicaBolesti.getItems().setAll(filtriranaBolest.get());
        }


//        tablicaZupanija.setItems(FXCollections.observableArrayList(filtriraneZupanije));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListaBolesti = FXCollections.observableArrayList();
        observableListaBolesti.addAll(Main.bolesti.stream().filter(z -> (!(z instanceof Virus))).collect(Collectors.toList()));

        nazivStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        simptomiStupac.setCellValueFactory(new PropertyValueFactory<Set<Simptom>, String>("simptomi"));
        idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

        tablicaBolesti.setItems(observableListaBolesti);
    }

    public static ObservableList<Bolest> getObservableListaBolesti() {
        return observableListaBolesti;
    }

    public static void setObservableListaBolesti(ObservableList<Bolest> observableListaBolesti) {
        PretragaBolestiController.observableListaBolesti = observableListaBolesti;
    }
}
