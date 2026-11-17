package it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza;

import java.util.List;

/**
 * Il formato del file di salvataggio: una fotografia di ciò che cambia durante una
 * partita (DTO, Data Transfer Object).
 *
 * <p>Contiene solo l'avanzamento: il giocatore, dove si trova e quali luoghi ha già
 * completato. I contenuti (la galassia con i suoi nemici e le sue storie) non si
 * salvano: al caricamento si ricostruiscono da {@code FabbricaPartita}. Così il file
 * resta piccolo e leggibile, e il grafo dei luoghi, che ha dei cicli, non va
 * serializzato.</p>
 *
 * <p>Sono {@code record} con soli tipi semplici (stringhe, numeri, liste), che Gson
 * scrive e legge senza configurazione. Il dominio non conosce questo formato: la
 * traduzione sta tutta in {@link MapperPartita}.</p>
 *
 * @param giocatore          lo stato del giocatore
 * @param idLuogoCorrente    dove si trova il giocatore
 * @param idLuoghiCompletati i luoghi di cui è già stata letta la storia o sconfitto il nemico
 */
public record DatiPartita(DatiGiocatore giocatore, String idLuogoCorrente, List<String> idLuoghiCompletati) {

    /** @param classe il nome della costante di {@code ClassePersonaggio} */
    public record DatiGiocatore(String nome, String classe, int salute, int energia,
                                List<DatiOggetto> inventario) {
    }

    /**
     * @param tipo   {@code ARMA} oppure {@code CURA}
     * @param valore il bonus d'attacco di un'arma o i punti di cura di un oggetto curativo
     */
    public record DatiOggetto(String tipo, String nome, int valore) {
    }
}
