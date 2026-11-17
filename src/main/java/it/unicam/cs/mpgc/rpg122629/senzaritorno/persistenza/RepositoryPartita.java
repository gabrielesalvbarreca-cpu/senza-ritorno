package it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.Partita;

/**
 * Salvataggio e caricamento di una {@link Partita}.
 *
 * <p>Chi la usa sa solo che una partita si può salvare e riprendere, non in che
 * formato né dove (Dependency Inversion). Passare da JSON a XML o a un database
 * vuol dire scrivere un'altra implementazione e cambiare una riga in {@code App}.</p>
 */
public interface RepositoryPartita {

    /**
     * Salva la partita, sostituendo l'eventuale salvataggio precedente.
     *
     * @throws PersistenzaException se la partita non si può scrivere
     */
    void salva(Partita partita) throws PersistenzaException;

    /**
     * Riprende la partita salvata.
     *
     * @throws PersistenzaException se non c'è un salvataggio o non si può leggere
     */
    Partita carica() throws PersistenzaException;

    /** @return {@code true} se c'è una partita salvata da riprendere. */
    boolean esisteSalvataggio();
}
