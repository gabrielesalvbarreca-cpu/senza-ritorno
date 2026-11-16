package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Oggetto;

import java.util.Objects;

/**
 * Un pezzo di racconto legato a un {@link Luogo}: lo si legge arrivando e lascia
 * al giocatore un oggetto.
 *
 * @param titolo     titolo mostrato
 * @param testo      il racconto
 * @param ricompensa l'oggetto ricevuto leggendo la storia
 */
public record Storia(String titolo, String testo, Oggetto ricompensa) {

    public Storia {
        Objects.requireNonNull(titolo, "titolo");
        Objects.requireNonNull(testo, "testo");
        Objects.requireNonNull(ricompensa, "ricompensa");
    }
}
