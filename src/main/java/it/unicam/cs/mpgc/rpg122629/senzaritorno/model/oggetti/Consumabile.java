package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Personaggio;

/**
 * Un {@link Oggetto} che si usa una volta sola: produce il suo effetto e poi
 * sparisce dall'inventario.
 *
 * <p>Il combattimento conosce solo questa interfaccia e non le classi concrete,
 * quindi un consumabile nuovo funziona in battaglia senza modificare il
 * combattimento.</p>
 */
public interface Consumabile extends Oggetto {

    /** Applica l'effetto dell'oggetto al personaggio indicato. */
    void usaSu(Personaggio bersaglio);
}
