package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Nemico;

import java.util.Objects;
import java.util.Optional;

/**
 * Una tappa del viaggio: un pianeta o una stazione della {@link Galassia}.
 *
 * <p>Un luogo può essere tranquillo, raccontare una {@link Storia} oppure essere
 * sorvegliato da un {@link Nemico}. Quello che accade in un luogo accade una volta
 * sola: letta la storia o sconfitto il nemico, il luogo è
 * {@linkplain #isCompletato() completato} e da lì in poi resta tranquillo.</p>
 */
public final class Luogo {

    private final String id;
    private final String nome;
    private final String descrizione;
    private final Storia storia;   // null se il luogo non racconta nulla
    private final Nemico nemico;   // null se nessuno sorveglia il luogo
    private boolean completato;

    /** Crea un luogo tranquillo. */
    public Luogo(String id, String nome, String descrizione) {
        this(id, nome, descrizione, null, null);
    }

    /** Crea un luogo che racconta una storia. */
    public Luogo(String id, String nome, String descrizione, Storia storia) {
        this(id, nome, descrizione, Objects.requireNonNull(storia, "storia"), null);
    }

    /** Crea un luogo sorvegliato da un nemico. */
    public Luogo(String id, String nome, String descrizione, Nemico nemico) {
        this(id, nome, descrizione, null, Objects.requireNonNull(nemico, "nemico"));
    }

    private Luogo(String id, String nome, String descrizione, Storia storia, Nemico nemico) {
        if (id == null || id.isBlank() || nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Id e nome del luogo non possono essere vuoti");
        }
        this.id = id;
        this.nome = nome;
        this.descrizione = Objects.requireNonNull(descrizione, "descrizione");
        this.storia = storia;
        this.nemico = nemico;
    }

    /** @return l'identificatore stabile del luogo, usato dal salvataggio. */
    public String id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public String descrizione() {
        return descrizione;
    }

    /** @return la storia ancora da leggere, se c'è. */
    public Optional<Storia> storia() {
        return completato ? Optional.empty() : Optional.ofNullable(storia);
    }

    /** @return il nemico ancora da sconfiggere, se c'è. */
    public Optional<Nemico> nemico() {
        return completato ? Optional.empty() : Optional.ofNullable(nemico);
    }

    public boolean isCompletato() {
        return completato;
    }

    /** Segna come accaduto l'evento del luogo: da ora in poi il luogo è tranquillo. */
    public void completa() {
        completato = true;
    }
}
