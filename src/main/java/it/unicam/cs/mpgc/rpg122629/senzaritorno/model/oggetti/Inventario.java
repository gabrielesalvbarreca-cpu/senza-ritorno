package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Gli oggetti che il giocatore porta con sé.
 *
 * <p>Si limita a custodirli e a ritrovarli: cosa fa un oggetto lo decide l'oggetto
 * stesso, quando lo si usa lo decide il combattimento.</p>
 */
public final class Inventario {

    private final List<Oggetto> oggetti = new ArrayList<>();

    public void aggiungi(Oggetto oggetto) {
        oggetti.add(Objects.requireNonNull(oggetto, "oggetto"));
    }

    /** @return {@code true} se l'oggetto c'era ed è stato tolto. */
    public boolean rimuovi(Oggetto oggetto) {
        return oggetti.remove(oggetto);
    }

    public boolean contiene(Oggetto oggetto) {
        return oggetti.contains(oggetto);
    }

    /** @return una copia non modificabile: da fuori non si può alterare l'inventario. */
    public List<Oggetto> oggetti() {
        return List.copyOf(oggetti);
    }

    /** @return il primo consumabile dell'inventario, se ce n'è uno. */
    public Optional<Consumabile> primoConsumabile() {
        return oggetti.stream()
                .filter(Consumabile.class::isInstance)
                .map(Consumabile.class::cast)
                .findFirst();
    }

    /** @return il bonus d'attacco dell'arma migliore, oppure 0 se non ci sono armi. */
    public int bonusArmaMigliore() {
        return oggetti.stream()
                .mapToInt(oggetto -> oggetto instanceof Arma arma ? arma.bonusAttacco() : 0)
                .max()
                .orElse(0);
    }
}
