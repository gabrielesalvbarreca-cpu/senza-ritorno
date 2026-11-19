package it.unicam.cs.mpgc.rpg122629.senzaritorno.view;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.Combattimento;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Personaggio;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Lo scontro: la salute dei due combattenti, il diario dei colpi e le quattro mosse.
 *
 * <p>Ogni pulsante chiede una mossa al {@link Combattimento}, scrive nel diario le
 * righe del turno e ridisegna lo stato. A scontro finito le mosse si spengono e
 * resta solo "Continua", così si può leggere com'è andata prima di proseguire.</p>
 */
public final class SchermataCombattimento {

    private final Combattimento combattimento;
    private final VBox radice;

    private final ProgressBar barraNemico = new ProgressBar();
    private final ProgressBar barraGiocatore = new ProgressBar();
    private final Label saluteNemico = new Label();
    private final Label saluteGiocatore = new Label();
    private final Label energia = new Label();
    private final TextArea diario = new TextArea();

    private final Button attacca = new Button("Attacca");
    private final Button colpoPotente =
            new Button("Colpo potente (" + Combattimento.COSTO_COLPO_POTENTE + " EN)");
    private final Button difenditi = new Button("Difenditi");
    private final Button usaOggetto = new Button("Usa oggetto");
    private final Button continua = new Button("Continua");

    public SchermataCombattimento(Combattimento combattimento, Navigazione navigazione) {
        this.combattimento = combattimento;
        Giocatore giocatore = combattimento.giocatore();

        attacca.setOnAction(e -> mostraTurno(combattimento.attacca()));
        colpoPotente.setOnAction(e -> mostraTurno(combattimento.colpoPotente()));
        difenditi.setOnAction(e -> mostraTurno(combattimento.difenditi()));
        usaOggetto.setOnAction(e -> giocatore.inventario().primoConsumabile()
                .ifPresent(oggetto -> mostraTurno(combattimento.usaOggetto(oggetto))));
        continua.setOnAction(e -> navigazione.mostraLuogoCorrente());

        barraNemico.getStyleClass().add("barra-salute-nemico");
        barraGiocatore.getStyleClass().add("barra-salute");
        diario.setEditable(false);
        diario.getStyleClass().add("diario");
        diario.appendText(combattimento.nemico().nome() + " ti sbarra la strada!\n");
        VBox.setVgrow(diario, Priority.ALWAYS);

        VBox stato = new VBox(4,
                riga(combattimento.nemico(), barraNemico, saluteNemico),
                riga(giocatore, barraGiocatore, saluteGiocatore),
                energia);
        stato.getStyleClass().add("pannello");
        stato.setPadding(new Insets(12));

        radice = new VBox(10, stato, diario,
                new HBox(10, attacca, colpoPotente, difenditi, usaOggetto, continua));
        radice.setPadding(new Insets(24));
        aggiornaStato();
    }

    public Parent radice() {
        return radice;
    }

    private void mostraTurno(List<String> righe) {
        righe.forEach(riga -> diario.appendText(riga + "\n"));
        aggiornaStato();
    }

    private void aggiornaStato() {
        Giocatore giocatore = combattimento.giocatore();
        aggiornaSalute(combattimento.nemico(), barraNemico, saluteNemico);
        aggiornaSalute(giocatore, barraGiocatore, saluteGiocatore);
        energia.setText("Energia: " + giocatore.energia());

        boolean finito = combattimento.isFinito();
        attacca.setDisable(finito);
        difenditi.setDisable(finito);
        colpoPotente.setDisable(finito || !combattimento.puoColpoPotente());
        usaOggetto.setDisable(finito || giocatore.inventario().primoConsumabile().isEmpty());
        continua.setDisable(!finito);
    }

    private static HBox riga(Personaggio personaggio, ProgressBar barra, Label salute) {
        Label nome = new Label(personaggio.nome());
        nome.setMinWidth(180);
        barra.setPrefWidth(300);
        return new HBox(8, nome, barra, salute);
    }

    private static void aggiornaSalute(Personaggio personaggio, ProgressBar barra, Label salute) {
        barra.setProgress((double) personaggio.salute() / personaggio.saluteMassima());
        salute.setText(personaggio.salute() + "/" + personaggio.saluteMassima());
    }
}
