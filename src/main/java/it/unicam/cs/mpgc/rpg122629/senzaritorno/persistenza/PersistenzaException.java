package it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza;

/**
 * Una partita non si è potuta salvare o caricare (file mancante, illeggibile o
 * corrotto).
 *
 * <p>È un'eccezione controllata perché è un problema che chi chiama deve gestire,
 * per esempio mostrando un messaggio, e non un errore di programmazione. Nasconde
 * la causa tecnica ({@code IOException}, errori di Gson), che resta disponibile con
 * {@link #getCause()}: il resto del programma non dipende dalla tecnologia usata
 * per salvare.</p>
 */
public class PersistenzaException extends Exception {

    public PersistenzaException(String messaggio) {
        super(messaggio);
    }

    public PersistenzaException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}
