package it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Consumabile;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Nemico;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Personaggio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Uno scontro a turni fra il giocatore e il nemico di un luogo, giocato una mossa
 * alla volta.
 *
 * <p>A ogni mossa del giocatore il nemico, se è ancora in piedi, risponde con un
 * attacco. Un colpo fa {@code attacco - difesa} danni, e almeno 1. Ogni mossa
 * restituisce le righe del diario di quel turno: l'interfaccia le mostra senza
 * conoscere le regole.</p>
 *
 * <p>Quando il nemico cade il luogo diventa completato e lì non si combatte più. Se
 * cade il giocatore, il nemico resta al suo posto.</p>
 */
public final class Combattimento {

    /** Energia consumata da un colpo potente. */
    public static final int COSTO_COLPO_POTENTE = 30;

    private final Giocatore giocatore;
    private final Nemico nemico;
    private final Luogo luogo;

    /**
     * Visibile solo nel package: uno scontro si apre da
     * {@link Partita#iniziaCombattimento()}, legato al luogo in cui avviene.
     *
     * @throws IllegalStateException se nel luogo non c'è nessun nemico
     */
    Combattimento(Giocatore giocatore, Luogo luogo) {
        this.giocatore = Objects.requireNonNull(giocatore, "giocatore");
        this.luogo = Objects.requireNonNull(luogo, "luogo");
        this.nemico = luogo.nemico().orElseThrow(
                () -> new IllegalStateException("In " + luogo.nome() + " non c'è nessuno da combattere"));
    }

    public Giocatore giocatore() {
        return giocatore;
    }

    public Nemico nemico() {
        return nemico;
    }

    /** @return {@code true} se uno dei due è caduto. */
    public boolean isFinito() {
        return !giocatore.isVivo() || !nemico.isVivo();
    }

    public boolean giocatoreHaVinto() {
        return isFinito() && giocatore.isVivo();
    }

    public boolean puoColpoPotente() {
        return giocatore.energia() >= COSTO_COLPO_POTENTE;
    }

    /** Il giocatore attacca, poi il nemico risponde. */
    public List<String> attacca() {
        verificaInCorso();
        List<String> diario = new ArrayList<>();
        diario.add(colpisci(giocatore, nemico, danno(giocatore, nemico)));
        concludiTurno(diario, danno(nemico, giocatore));
        return diario;
    }

    /**
     * Il giocatore spende energia per un colpo a danno doppio, poi il nemico risponde.
     *
     * @throws IllegalStateException se l'energia non basta (vedi {@link #puoColpoPotente()})
     */
    public List<String> colpoPotente() {
        verificaInCorso();
        giocatore.spendiEnergia(COSTO_COLPO_POTENTE);
        List<String> diario = new ArrayList<>();
        diario.add(giocatore.nome() + " carica un colpo potente.");
        diario.add(colpisci(giocatore, nemico, 2 * danno(giocatore, nemico)));
        concludiTurno(diario, danno(nemico, giocatore));
        return diario;
    }

    /** Il giocatore si difende: in questo turno il nemico fa metà danno. */
    public List<String> difenditi() {
        verificaInCorso();
        List<String> diario = new ArrayList<>();
        diario.add(giocatore.nome() + " si mette in difesa.");
        concludiTurno(diario, Math.max(1, danno(nemico, giocatore) / 2));
        return diario;
    }

    /**
     * Il giocatore usa un consumabile invece di attaccare: l'oggetto fa il suo
     * effetto, sparisce dall'inventario e il nemico risponde.
     *
     * @throws IllegalArgumentException se il giocatore non possiede l'oggetto
     */
    public List<String> usaOggetto(Consumabile oggetto) {
        verificaInCorso();
        if (!giocatore.inventario().rimuovi(oggetto)) {
            throw new IllegalArgumentException("Il giocatore non possiede: " + oggetto.nome());
        }
        int salutePrima = giocatore.salute();
        oggetto.usaSu(giocatore);
        List<String> diario = new ArrayList<>();
        diario.add("%s usa %s: salute da %d a %d.".formatted(
                giocatore.nome(), oggetto.nome(), salutePrima, giocatore.salute()));
        concludiTurno(diario, danno(nemico, giocatore));
        return diario;
    }

    /** Il nemico risponde, se è ancora in piedi; poi si annota chi è caduto. */
    private void concludiTurno(List<String> diario, int dannoDelNemico) {
        if (nemico.isVivo()) {
            diario.add(colpisci(nemico, giocatore, dannoDelNemico));
        }
        if (!nemico.isVivo()) {
            luogo.completa();
            diario.add(nemico.nome() + " è sconfitto.");
        } else if (!giocatore.isVivo()) {
            diario.add(giocatore.nome() + " cade in battaglia.");
        }
    }

    private static int danno(Personaggio attaccante, Personaggio difensore) {
        return Math.max(1, attaccante.attacco() - difensore.difesa());
    }

    private static String colpisci(Personaggio attaccante, Personaggio difensore, int danno) {
        difensore.subisciDanno(danno);
        return "%s colpisce %s: %d danni.".formatted(attaccante.nome(), difensore.nome(), danno);
    }

    /** Agire a scontro finito è un errore di chi chiama, non una mossa di gioco. */
    private void verificaInCorso() {
        if (isFinito()) {
            throw new IllegalStateException("Lo scontro è già finito");
        }
    }
}
