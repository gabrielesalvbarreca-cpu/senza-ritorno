package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti;

/**
 * Un'arma: finché sta nell'inventario aggiunge {@code bonusAttacco} all'attacco del
 * giocatore. Se le armi sono più d'una conta solo la migliore.
 *
 * @param nome         nome mostrato
 * @param bonusAttacco attacco aggiunto (non negativo)
 */
public record Arma(String nome, int bonusAttacco) implements Oggetto {

    public Arma {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }
        if (bonusAttacco < 0) {
            throw new IllegalArgumentException("Il bonus d'attacco non può essere negativo");
        }
    }

    @Override
    public String descrizione() {
        return "Arma: +" + bonusAttacco + " all'attacco";
    }
}
