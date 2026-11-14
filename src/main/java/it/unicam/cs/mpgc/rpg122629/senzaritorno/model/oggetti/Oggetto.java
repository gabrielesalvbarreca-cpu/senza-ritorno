package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti;

/**
 * Qualsiasi cosa il giocatore possa portare nell'{@link Inventario}.
 *
 * <p>Ogni tipo di oggetto decide da sé cosa fa: un'{@link Arma} rinforza l'attacco
 * finché la si porta, un {@link Consumabile} agisce quando lo si usa. Un tipo nuovo
 * si aggiunge scrivendo una classe nuova, senza cambiare quelle che ci sono
 * (Open/Closed).</p>
 */
public interface Oggetto {

    /** @return il nome mostrato all'utente. */
    String nome();

    /** @return una frase che spiega a cosa serve l'oggetto. */
    String descrizione();
}
