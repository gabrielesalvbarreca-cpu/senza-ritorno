package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Inventario;

import java.util.Objects;

/**
 * Il personaggio guidato dall'utente.
 *
 * <p>Rispetto a un {@link Nemico} ha una {@link ClassePersonaggio}, un inventario e
 * l'energia, che si spende per i colpi potenti e non si ricarica. Il suo attacco
 * comprende il bonus dell'arma migliore che porta con sé.</p>
 */
public final class Giocatore extends Personaggio {

    private final ClassePersonaggio classe;
    private final Inventario inventario = new Inventario();
    private int energia;

    /** Crea un giocatore nuovo, in piena forma e con l'inventario vuoto. */
    public Giocatore(String nome, ClassePersonaggio classe) {
        this(nome, classe, classe.statistiche().saluteMassima(), classe.energia());
    }

    /**
     * Ricrea un giocatore a viaggio iniziato, per esempio da un salvataggio.
     *
     * @throws IllegalArgumentException se la salute è fuori dai limiti o l'energia è negativa
     */
    public Giocatore(String nome, ClassePersonaggio classe, int salute, int energia) {
        super(nome, Objects.requireNonNull(classe, "classe").statistiche(), salute);
        if (energia < 0) {
            throw new IllegalArgumentException("L'energia non può essere negativa");
        }
        this.classe = classe;
        this.energia = energia;
    }

    public ClassePersonaggio classe() {
        return classe;
    }

    public Inventario inventario() {
        return inventario;
    }

    public int energia() {
        return energia;
    }

    /**
     * Consuma energia per un'azione speciale.
     *
     * @throws IllegalStateException se l'energia non basta: chi chiama deve
     *                               controllare prima {@link #energia()}
     */
    public void spendiEnergia(int quantita) {
        if (quantita < 0) {
            throw new IllegalArgumentException("La quantità non può essere negativa");
        }
        if (quantita > energia) {
            throw new IllegalStateException("Energia insufficiente: servono " + quantita
                    + ", disponibili " + energia);
        }
        energia -= quantita;
    }

    /** @return l'attacco della classe più il bonus dell'arma migliore nell'inventario. */
    @Override
    public int attacco() {
        return super.attacco() + inventario.bonusArmaMigliore();
    }
}
