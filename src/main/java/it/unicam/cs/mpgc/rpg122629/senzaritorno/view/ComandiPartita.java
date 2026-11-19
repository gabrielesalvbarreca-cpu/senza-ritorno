package it.unicam.cs.mpgc.rpg122629.senzaritorno.view;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;

/**
 * Le richieste sulla partita nel suo insieme: cominciarne una, salvarla, riprenderla.
 *
 * <p>È separata da {@link Navigazione} (Interface Segregation): le schermate di
 * combattimento e di inventario cambiano schermata ma non salvano, quindi non
 * ricevono questi metodi. Nessuna schermata sa dove finisce il salvataggio né come
 * si mostra un errore: lo decide chi realizza l'interfaccia.</p>
 */
public interface ComandiPartita {

    void iniziaPartita(String nomeGiocatore, ClassePersonaggio classe);

    void caricaPartita();

    void salvaPartita();
}
