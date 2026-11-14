package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Personaggio;

import java.util.Objects;

/**
 * Un consumabile che restituisce salute (uno stimolante, un medikit, ...).
 *
 * @param nome      nome mostrato
 * @param puntiCura salute restituita con l'uso (positiva)
 */
public record OggettoCura(String nome, int puntiCura) implements Consumabile {

    public OggettoCura {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }
        if (puntiCura <= 0) {
            throw new IllegalArgumentException("I punti di cura devono essere positivi");
        }
    }

    @Override
    public String descrizione() {
        return "Cura: ripristina " + puntiCura + " punti salute";
    }

    @Override
    public void usaSu(Personaggio bersaglio) {
        Objects.requireNonNull(bersaglio, "bersaglio").cura(puntiCura);
    }
}
