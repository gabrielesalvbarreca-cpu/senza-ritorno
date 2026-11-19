package it.unicam.cs.mpgc.rpg122629.senzaritorno.view;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Oggetto;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

/**
 * L'elenco, in sola lettura, degli oggetti che il giocatore porta con sé. Ogni
 * oggetto si descrive da solo, quindi un tipo di oggetto nuovo compare qui senza
 * modifiche.
 */
public final class SchermataInventario {

    private final BorderPane radice = new BorderPane();

    public SchermataInventario(Giocatore giocatore, Navigazione navigazione) {
        Label titolo = new Label("Inventario");
        titolo.getStyleClass().add("titolo");

        ListView<String> elenco = new ListView<>();
        for (Oggetto oggetto : giocatore.inventario().oggetti()) {
            elenco.getItems().add(oggetto.nome() + " — " + oggetto.descrizione());
        }
        if (elenco.getItems().isEmpty()) {
            elenco.getItems().add("(vuoto)");
        }

        Button indietro = new Button("Indietro");
        indietro.setOnAction(e -> navigazione.mostraLuogoCorrente());

        radice.setPadding(new Insets(24));
        radice.setTop(titolo);
        radice.setCenter(elenco);
        radice.setBottom(indietro);
        BorderPane.setMargin(elenco, new Insets(16, 0, 16, 0));
    }

    public Parent radice() {
        return radice;
    }
}
