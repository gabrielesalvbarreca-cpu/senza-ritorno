package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi;

import java.util.Objects;

/**
 * Ciò che hanno in comune il giocatore e i nemici: un nome, delle
 * {@link Statistiche} e una salute che cala con i colpi e risale con le cure.
 *
 * <p>È astratta perché non esiste un "personaggio generico": si crea sempre un
 * {@link Giocatore} o un {@link Nemico}. Il combattimento lavora con questo tipo,
 * quindi tratta allo stesso modo chi attacca e chi si difende.</p>
 */
public abstract class Personaggio {

    private final String nome;
    private final Statistiche statistiche;
    private int salute;

    /**
     * @param salute la salute di partenza, compresa fra 0 e la salute massima
     * @throws IllegalArgumentException se il nome è vuoto o la salute è fuori dai limiti
     */
    protected Personaggio(String nome, Statistiche statistiche, int salute) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }
        this.nome = nome;
        this.statistiche = Objects.requireNonNull(statistiche, "statistiche");
        if (salute < 0 || salute > statistiche.saluteMassima()) {
            throw new IllegalArgumentException("Salute fuori dai limiti: " + salute);
        }
        this.salute = salute;
    }

    public String nome() {
        return nome;
    }

    public Statistiche statistiche() {
        return statistiche;
    }

    public int salute() {
        return salute;
    }

    public int saluteMassima() {
        return statistiche.saluteMassima();
    }

    public boolean isVivo() {
        return salute > 0;
    }

    /** @return la potenza dei colpi; una sottoclasse può aggiungere dei bonus. */
    public int attacco() {
        return statistiche.attacco();
    }

    public int difesa() {
        return statistiche.difesa();
    }

    /** Toglie salute, senza mai scendere sotto zero. */
    public void subisciDanno(int danno) {
        if (danno < 0) {
            throw new IllegalArgumentException("Il danno non può essere negativo");
        }
        salute = Math.max(0, salute - danno);
    }

    /** Restituisce salute, senza mai superare la salute massima. */
    public void cura(int quantita) {
        if (quantita < 0) {
            throw new IllegalArgumentException("La cura non può essere negativa");
        }
        salute = Math.min(saluteMassima(), salute + quantita);
    }
}
