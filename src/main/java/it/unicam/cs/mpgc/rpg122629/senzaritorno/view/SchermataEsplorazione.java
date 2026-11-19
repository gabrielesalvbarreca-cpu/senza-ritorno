package it.unicam.cs.mpgc.rpg122629.senzaritorno.view;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.Partita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * L'esplorazione: il luogo corrente, i luoghi raggiungibili, lo stato del giocatore
 * e i comandi per l'inventario e il salvataggio.
 *
 * <p>Un clic su una destinazione chiede il viaggio alla {@link Partita}, che
 * controlla che sia lecito, e poi chiede di mostrare il nuovo luogo: cosa ci si
 * trova (uno scontro, una storia) lo decide la navigazione, non questa schermata.</p>
 */
public final class SchermataEsplorazione {

    private final BorderPane radice = new BorderPane();

    public SchermataEsplorazione(Partita partita, Navigazione navigazione, ComandiPartita comandi) {
        Luogo qui = partita.luogoCorrente();
        Label nome = new Label(qui.nome());
        nome.getStyleClass().add("titolo");
        Label descrizione = new Label(qui.descrizione());
        descrizione.setWrapText(true);

        VBox destinazioni = new VBox(8, new Label("Viaggia verso:"));
        for (Luogo destinazione : partita.destinazioni()) {
            Button vai = new Button(destinazione.nome());
            vai.setMaxWidth(Double.MAX_VALUE);
            vai.setOnAction(e -> {
                partita.viaggiaVerso(destinazione);
                navigazione.mostraLuogoCorrente();
            });
            destinazioni.getChildren().add(vai);
        }

        Button inventario = new Button("Inventario");
        inventario.setOnAction(e -> navigazione.mostraInventario());
        Button salva = new Button("Salva");
        salva.setOnAction(e -> comandi.salvaPartita());

        radice.setPadding(new Insets(24));
        radice.setTop(new VBox(6, nome, descrizione));
        radice.setCenter(destinazioni);
        radice.setRight(pannelloGiocatore(partita.giocatore()));
        radice.setBottom(new HBox(10, inventario, salva));
        BorderPane.setMargin(destinazioni, new Insets(16, 16, 16, 0));
    }

    public Parent radice() {
        return radice;
    }

    private static VBox pannelloGiocatore(Giocatore giocatore) {
        Label intestazione = new Label(giocatore.nome() + " — " + giocatore.classe());
        intestazione.getStyleClass().add("sottotitolo");
        VBox pannello = new VBox(4,
                intestazione,
                new Label("Salute: " + giocatore.salute() + "/" + giocatore.saluteMassima()),
                new Label("Energia: " + giocatore.energia()),
                new Label("Attacco: " + giocatore.attacco()),
                new Label("Difesa: " + giocatore.difesa()));
        pannello.getStyleClass().add("pannello");
        pannello.setPadding(new Insets(12));
        return pannello;
    }
}
