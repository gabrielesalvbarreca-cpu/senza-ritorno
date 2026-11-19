package it.unicam.cs.mpgc.rpg122629.senzaritorno.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Il menu principale: nuova partita, carica partita, esci.
 */
public final class SchermataMenu {

    private final VBox radice;

    /**
     * @param salvataggioPresente se è {@code false} il pulsante "Carica partita" è
     *                            spento, perché il caricamento fallirebbe di sicuro
     */
    public SchermataMenu(Navigazione navigazione, ComandiPartita comandi, boolean salvataggioPresente) {
        Label titolo = new Label("SENZA RITORNO");
        titolo.getStyleClass().add("titolo");
        Label sottotitolo = new Label("Un viaggio senza ritorno.");
        sottotitolo.getStyleClass().add("sottotitolo");

        Button nuovaPartita = new Button("Nuova partita");
        nuovaPartita.setOnAction(e -> navigazione.mostraCreazionePersonaggio());
        Button caricaPartita = new Button("Carica partita");
        caricaPartita.setOnAction(e -> comandi.caricaPartita());
        caricaPartita.setDisable(!salvataggioPresente);
        Button esci = new Button("Esci");
        esci.setOnAction(e -> Platform.exit());

        for (Button pulsante : new Button[] {nuovaPartita, caricaPartita, esci}) {
            pulsante.getStyleClass().add("pulsante-menu");
        }

        radice = new VBox(16, titolo, sottotitolo, nuovaPartita, caricaPartita, esci);
        radice.setAlignment(Pos.CENTER);
    }

    public Parent radice() {
        return radice;
    }
}
