package it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Galassia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Storia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Arma;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.OggettoCura;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Nemico;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Statistiche;

/**
 * I contenuti di questa versione del gioco: la galassia da attraversare, con i suoi
 * nemici e le sue storie, e la dotazione con cui parte il giocatore.
 *
 * <p>Tiene i <em>dati</em> del gioco separati dalle <em>regole</em>, che stanno in
 * {@link Partita} e {@link Combattimento}: aggiungere un luogo, un nemico o una
 * storia è una modifica che resta in questa classe. Ha solo metodi statici, quindi
 * non si istanzia.</p>
 */
public final class FabbricaPartita {

    private FabbricaPartita() {
    }

    /** @return una partita nuova: il giocatore con uno stimolante, sulla Terra. */
    public static Partita nuovaPartita(String nomeGiocatore, ClassePersonaggio classe) {
        Giocatore giocatore = new Giocatore(nomeGiocatore, classe);
        giocatore.inventario().aggiungi(new OggettoCura("Stimolante", 20));
        return new Partita(giocatore, creaGalassia());
    }

    /**
     * Crea la galassia di partenza, con tutte le storie da leggere e tutti i nemici
     * in piedi. Ogni chiamata restituisce una galassia nuova.
     *
     * <pre>
     *          Stazione Alfa
     *         /             \
     *   Terra                Relè Nexus --- Confine del Vuoto
     *         \             /
     *            Zeta-7
     * </pre>
     */
    public static Galassia creaGalassia() {
        Luogo terra = new Luogo("terra", "Terra", "I moli di casa. L'ultimo porto sicuro.",
                new Storia("Partenza",
                        "Ti sganci dai moli della Terra e prendi dall'armadietto la vecchia pistola laser. "
                                + "Alle tue spalle le comunicazioni tacciono: non si torna indietro.",
                        new Arma("Pistola Laser", 8)));
        Luogo alfa = new Luogo("alfa", "Stazione Alfa", "Un affollato snodo orbitale.",
                new Nemico("Pirata Spaziale", new Statistiche(70, 14, 8)));
        Luogo zeta = new Luogo("zeta", "Zeta-7", "Un mondo minerario flagellato dalle tempeste.",
                new Nemico("Drone di Pattuglia", new Statistiche(40, 10, 6)));
        Luogo nexus = new Luogo("nexus", "Relè Nexus", "Un relè di comunicazione mezzo abbandonato.",
                new Storia("Recupero",
                        "Tra i rottami del relè trovi un medikit ancora funzionante.",
                        new OggettoCura("Medikit", 30)));
        Luogo confine = new Luogo("confine", "Il Confine del Vuoto", "Dove attende la Sentinella.",
                new Nemico("Sentinella del Vuoto", new Statistiche(110, 18, 12)));

        Galassia galassia = new Galassia(terra, confine);
        galassia.collega(terra, alfa);
        galassia.collega(terra, zeta);
        galassia.collega(alfa, nexus);
        galassia.collega(zeta, nexus);
        galassia.collega(nexus, confine);
        return galassia;
    }
}
