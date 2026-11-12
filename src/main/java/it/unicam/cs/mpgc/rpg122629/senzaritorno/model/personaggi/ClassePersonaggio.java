package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi;

/**
 * Il ruolo scelto alla creazione del personaggio. Ogni classe ha le proprie
 * {@link Statistiche} e la propria energia di partenza.
 *
 * <p>È un {@code enum} perché le classi sono un insieme chiuso e noto. Aggiungerne
 * una è una riga in più qui: la schermata di creazione elenca da sola tutti i
 * valori dell'enum.</p>
 */
public enum ClassePersonaggio {

    PILOTA("Veloce ed evasivo, corazza leggera.", new Statistiche(90, 14, 10), 110),
    INGEGNERE("Equilibrato, si affida ai suoi congegni.", new Statistiche(95, 12, 14), 100),
    SOLDATO("Resistente e forte, poca energia.", new Statistiche(120, 16, 16), 80),
    HACKER("Molto danno e molta energia, ma fragile.", new Statistiche(80, 18, 8), 130);

    private final String descrizione;
    private final Statistiche statistiche;
    private final int energia;

    ClassePersonaggio(String descrizione, Statistiche statistiche, int energia) {
        this.descrizione = descrizione;
        this.statistiche = statistiche;
        this.energia = energia;
    }

    public String descrizione() {
        return descrizione;
    }

    public Statistiche statistiche() {
        return statistiche;
    }

    /** @return l'energia con cui parte un giocatore di questa classe. */
    public int energia() {
        return energia;
    }

    /** @return il nome leggibile della classe (per esempio "Pilota"), usato dall'interfaccia. */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
