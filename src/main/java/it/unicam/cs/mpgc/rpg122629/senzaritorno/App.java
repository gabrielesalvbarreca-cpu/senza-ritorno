package it.unicam.cs.mpgc.rpg122629.senzaritorno;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.FabbricaPartita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.Partita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Storia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza.PersistenzaException;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza.RepositoryPartita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza.RepositoryPartitaJson;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.view.ComandiPartita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.view.Navigazione;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.view.SchermataCombattimento;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.view.SchermataCreazione;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.view.SchermataEsplorazione;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.view.SchermataInventario;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.view.SchermataMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * Punto d'ingresso: la finestra JavaFX del gioco.
 *
 * <p>Tiene la partita in corso e decide quale schermata mostrare: realizza
 * {@link Navigazione} e {@link ComandiPartita}, le due interfacce con cui le
 * schermate chiedono qualcosa senza conoscere questa classe. Le regole del gioco
 * stanno in {@link Partita}, i contenuti in {@link FabbricaPartita}, il disegno
 * delle schermate nelle classi del package {@code view}.</p>
 */
public final class App extends Application implements Navigazione, ComandiPartita {

    private static final int LARGHEZZA = 900;
    private static final int ALTEZZA = 600;

    /** Dichiarato col tipo dell'interfaccia: per salvare altrove basta cambiare questa riga. */
    private final RepositoryPartita repository = RepositoryPartitaJson.nellaCartellaUtente();

    private Scene scena;
    private Partita partita;   // null finché si è nel menu

    @Override
    public void start(Stage finestra) {
        scena = new Scene(new StackPane(), LARGHEZZA, ALTEZZA);
        URL foglioDiStile = Objects.requireNonNull(
                App.class.getResource("view/tema.css"), "Manca il foglio di stile tema.css");
        scena.getStylesheets().add(foglioDiStile.toExternalForm());
        finestra.setTitle("Senza Ritorno");
        finestra.setScene(scena);
        mostraMenu();
        finestra.show();
    }

    @Override
    public void mostraMenu() {
        partita = null;
        scena.setRoot(new SchermataMenu(this, this, repository.esisteSalvataggio()).radice());
    }

    @Override
    public void mostraCreazionePersonaggio() {
        scena.setRoot(new SchermataCreazione(this, this).radice());
    }

    /**
     * L'unico punto in cui si controlla se la partita è finita: ci passano tutte le
     * schermate quando tornano al gioco (dopo un viaggio, uno scontro, l'inventario),
     * quindi nessuna deve ricordarsi di farlo.
     */
    @Override
    public void mostraLuogoCorrente() {
        switch (partita.stato()) {
            case VITTORIA -> fineViaggio("Vittoria", "Hai sconfitto l'ultimo nemico. Il tuo viaggio è compiuto.");
            case SCONFITTA -> fineViaggio("Fine partita", "Il tuo viaggio finisce qui.");
            case IN_CORSO -> {
                if (partita.nemicoPresente()) {
                    scena.setRoot(new SchermataCombattimento(partita.iniziaCombattimento(), this).radice());
                    return;
                }
                // La storia si legge prima di disegnare la schermata, così la ricompensa è già nelle statistiche.
                Optional<Storia> storia = partita.leggiStoria();
                scena.setRoot(new SchermataEsplorazione(partita, this, this).radice());
                storia.ifPresent(letta -> avviso(Alert.AlertType.INFORMATION, letta.titolo(),
                        letta.testo() + "\n\nRicevi: " + letta.ricompensa().nome()));
            }
        }
    }

    @Override
    public void mostraInventario() {
        scena.setRoot(new SchermataInventario(partita.giocatore(), this).radice());
    }

    @Override
    public void iniziaPartita(String nomeGiocatore, ClassePersonaggio classe) {
        partita = FabbricaPartita.nuovaPartita(nomeGiocatore, classe);
        mostraLuogoCorrente();
    }

    /** Se il caricamento fallisce si resta nel menu, con un messaggio d'errore. */
    @Override
    public void caricaPartita() {
        try {
            partita = repository.carica();
            mostraLuogoCorrente();
        } catch (PersistenzaException e) {
            avviso(Alert.AlertType.ERROR, "Errore", "Impossibile caricare la partita.\n" + e.getMessage());
        }
    }

    /** Un salvataggio non riuscito non chiude il gioco: si avvisa e si continua a giocare. */
    @Override
    public void salvaPartita() {
        try {
            repository.salva(partita);
            avviso(Alert.AlertType.INFORMATION, "Salvataggio", "I tuoi progressi sono stati salvati.");
        } catch (PersistenzaException e) {
            avviso(Alert.AlertType.ERROR, "Errore", "Impossibile salvare la partita.\n" + e.getMessage());
        }
    }

    private void fineViaggio(String titolo, String messaggio) {
        avviso(Alert.AlertType.INFORMATION, titolo, messaggio);
        mostraMenu();
    }

    private void avviso(Alert.AlertType tipo, String titolo, String messaggio) {
        Alert finestra = new Alert(tipo, messaggio);
        finestra.initOwner(scena.getWindow());
        finestra.setTitle(titolo);
        finestra.setHeaderText(null);
        finestra.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
