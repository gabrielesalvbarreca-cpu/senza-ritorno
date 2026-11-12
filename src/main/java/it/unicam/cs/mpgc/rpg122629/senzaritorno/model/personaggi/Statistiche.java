package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi;

/**
 * I valori fissi che dicono quanto è forte un personaggio: salute massima, attacco
 * e difesa.
 *
 * <p>È un {@code record} immutabile. La salute che cala durante uno scontro non sta
 * qui ma nel {@link Personaggio}: così le stesse statistiche si possono condividere,
 * per esempio fra tutti i giocatori della stessa {@link ClassePersonaggio}.</p>
 *
 * @param saluteMassima punti salute a piena forma (positivi)
 * @param attacco       potenza dei colpi inflitti (non negativa)
 * @param difesa        quanto si riduce il danno subito (non negativa)
 */
public record Statistiche(int saluteMassima, int attacco, int difesa) {

    public Statistiche {
        if (saluteMassima <= 0) {
            throw new IllegalArgumentException("La salute massima deve essere positiva");
        }
        if (attacco < 0 || difesa < 0) {
            throw new IllegalArgumentException("Attacco e difesa non possono essere negativi");
        }
    }
}
