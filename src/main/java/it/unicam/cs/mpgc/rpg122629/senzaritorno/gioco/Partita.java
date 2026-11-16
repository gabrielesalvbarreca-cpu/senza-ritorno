package it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Galassia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Storia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Una partita in corso: il giocatore, la galassia e il luogo in cui si trova.
 *
 * <p>Tiene insieme i pezzi del modello e applica le regole del viaggio: si va solo
 * verso luoghi collegati, una storia si legge una volta sola e lascia la sua
 * ricompensa, la partita si vince sconfiggendo il nemico della destinazione finale
 * e si perde cadendo. Non sa nulla di come viene mostrata né di come viene
 * salvata.</p>
 */
public final class Partita {

    private final Giocatore giocatore;
    private final Galassia galassia;
    private Luogo luogoCorrente;

    /** Comincia una partita nuova dal luogo di partenza della galassia. */
    public Partita(Giocatore giocatore, Galassia galassia) {
        this(giocatore, galassia, galassia.partenza());
    }

    /**
     * Riprende una partita da un luogo qualsiasi, per esempio dopo un caricamento.
     *
     * @throws IllegalArgumentException se il luogo non fa parte della galassia
     */
    public Partita(Giocatore giocatore, Galassia galassia, Luogo luogoCorrente) {
        this.giocatore = Objects.requireNonNull(giocatore, "giocatore");
        this.galassia = Objects.requireNonNull(galassia, "galassia");
        if (!galassia.contiene(luogoCorrente)) {
            throw new IllegalArgumentException("Il luogo corrente non fa parte della galassia");
        }
        this.luogoCorrente = luogoCorrente;
    }

    public Giocatore giocatore() {
        return giocatore;
    }

    public Galassia galassia() {
        return galassia;
    }

    public Luogo luogoCorrente() {
        return luogoCorrente;
    }

    /** @return i luoghi raggiungibili con un viaggio da quello corrente. */
    public List<Luogo> destinazioni() {
        return galassia.vicini(luogoCorrente);
    }

    /**
     * Porta il giocatore in un luogo collegato a quello corrente.
     *
     * @throws IllegalArgumentException se la destinazione non è collegata
     */
    public void viaggiaVerso(Luogo destinazione) {
        if (!galassia.sonoCollegati(luogoCorrente, destinazione)) {
            throw new IllegalArgumentException("Non c'è una rotta verso questo luogo");
        }
        luogoCorrente = destinazione;
    }

    /**
     * Legge la storia del luogo corrente, se ce n'è una ancora da leggere: il
     * giocatore riceve la ricompensa e il luogo diventa completato.
     *
     * @return la storia appena letta, oppure vuoto se qui non c'è niente da leggere
     */
    public Optional<Storia> leggiStoria() {
        Optional<Storia> storia = luogoCorrente.storia();
        storia.ifPresent(letta -> {
            giocatore.inventario().aggiungi(letta.ricompensa());
            luogoCorrente.completa();
        });
        return storia;
    }

    /** @return {@code true} se nel luogo corrente c'è un nemico da affrontare. */
    public boolean nemicoPresente() {
        return luogoCorrente.nemico().isPresent();
    }

    /**
     * Apre lo scontro con il nemico del luogo corrente.
     *
     * @throws IllegalStateException se nel luogo non c'è nessun nemico
     */
    public Combattimento iniziaCombattimento() {
        return new Combattimento(giocatore, luogoCorrente);
    }

    /**
     * L'ordine dei controlli conta: se il giocatore è caduto la partita è persa,
     * qualunque cosa sia successa prima.
     */
    public StatoPartita stato() {
        if (!giocatore.isVivo()) {
            return StatoPartita.SCONFITTA;
        }
        if (galassia.destinazioneFinale().isCompletato()) {
            return StatoPartita.VITTORIA;
        }
        return StatoPartita.IN_CORSO;
    }
}
