package it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco;

/**
 * A che punto è una partita.
 *
 * <p>Un {@code enum} invece di due booleani "vinta" e "persa": così non può esistere
 * una partita vinta e persa nello stesso momento.</p>
 */
public enum StatoPartita {

    /** Il giocatore è vivo e la destinazione finale non è ancora stata superata. */
    IN_CORSO,

    /** Il nemico della destinazione finale è stato sconfitto. */
    VITTORIA,

    /** Il giocatore è caduto in battaglia. */
    SCONFITTA
}
