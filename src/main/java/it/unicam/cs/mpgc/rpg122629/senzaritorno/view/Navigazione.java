package it.unicam.cs.mpgc.rpg122629.senzaritorno.view;

/**
 * Le schermate fra cui l'utente si sposta.
 *
 * <p>Le schermate chiedono di passare a un'altra schermata attraverso questa
 * interfaccia, senza conoscere chi la realizza ({@code App}): ognuna sa cosa
 * mostrare, non come si arriva alla successiva (Dependency Inversion).</p>
 */
public interface Navigazione {

    void mostraMenu();

    void mostraCreazionePersonaggio();

    /**
     * Mostra cosa trova il giocatore dove si trova adesso: lo scontro se c'è un
     * nemico, altrimenti l'esplorazione, con la storia del luogo se ce n'è una da
     * leggere. A partita finita mostra invece com'è andata.
     */
    void mostraLuogoCorrente();

    void mostraInventario();
}
