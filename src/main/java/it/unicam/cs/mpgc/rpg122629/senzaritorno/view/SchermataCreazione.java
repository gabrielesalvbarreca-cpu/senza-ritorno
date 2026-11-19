package it.unicam.cs.mpgc.rpg122629.senzaritorno.view;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Statistiche;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * La creazione del personaggio: un nome e una {@link ClassePersonaggio}.
 *
 * <p>L'elenco delle classi è preso dall'enum, quindi una classe nuova compare qui
 * senza modificare questa schermata.</p>
 */
public final class SchermataCreazione {

    private final VBox radice;

    public SchermataCreazione(Navigazione navigazione, ComandiPartita comandi) {
        Label titolo = new Label("Nuova recluta");
        titolo.getStyleClass().add("titolo");

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome del personaggio");
        campoNome.setMaxWidth(280);

        ComboBox<ClassePersonaggio> sceltaClasse = new ComboBox<>();
        sceltaClasse.getItems().setAll(ClassePersonaggio.values());
        sceltaClasse.getSelectionModel().selectFirst();

        Label descrizione = new Label(descrivi(sceltaClasse.getValue()));
        sceltaClasse.setOnAction(e -> descrizione.setText(descrivi(sceltaClasse.getValue())));

        Button indietro = new Button("Indietro");
        indietro.setOnAction(e -> navigazione.mostraMenu());
        Button inizia = new Button("Inizia il viaggio");
        inizia.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            if (nome.isEmpty()) {
                descrizione.setText("Inserisci un nome.");
                return;
            }
            comandi.iniziaPartita(nome, sceltaClasse.getValue());
        });
        HBox pulsanti = new HBox(10, indietro, inizia);
        pulsanti.setAlignment(Pos.CENTER);

        radice = new VBox(14, titolo, campoNome, sceltaClasse, descrizione, pulsanti);
        radice.setAlignment(Pos.CENTER);
        radice.setPadding(new Insets(40));
    }

    public Parent radice() {
        return radice;
    }

    private static String descrivi(ClassePersonaggio classe) {
        Statistiche s = classe.statistiche();
        return classe.descrizione() + "\nSalute %d · Energia %d · Attacco %d · Difesa %d"
                .formatted(s.saluteMassima(), classe.energia(), s.attacco(), s.difesa());
    }
}
