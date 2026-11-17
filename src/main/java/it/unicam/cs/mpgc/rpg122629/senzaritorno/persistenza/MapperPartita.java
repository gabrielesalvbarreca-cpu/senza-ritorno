package it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.FabbricaPartita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.Partita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Galassia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Arma;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Oggetto;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.OggettoCura;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza.DatiPartita.DatiGiocatore;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza.DatiPartita.DatiOggetto;

import java.util.List;

/**
 * Traduce una {@link Partita} nei {@link DatiPartita} da salvare, e ritorno.
 *
 * <p>Tutta la conversione sta qui: il dominio non sa nulla del file e il file non
 * contiene classi del dominio. Visibile solo nel package, perché la usa soltanto
 * il repository.</p>
 */
final class MapperPartita {

    private MapperPartita() {
    }

    static DatiPartita versoDati(Partita partita) {
        Giocatore giocatore = partita.giocatore();
        List<DatiOggetto> inventario = giocatore.inventario().oggetti().stream()
                .map(MapperPartita::datiOggetto)
                .toList();
        List<String> completati = partita.galassia().luoghi().stream()
                .filter(Luogo::isCompletato)
                .map(Luogo::id)
                .toList();
        DatiGiocatore datiGiocatore = new DatiGiocatore(giocatore.nome(), giocatore.classe().name(),
                giocatore.salute(), giocatore.energia(), inventario);
        return new DatiPartita(datiGiocatore, partita.luogoCorrente().id(), completati);
    }

    /**
     * Ricostruisce la galassia dai contenuti del gioco e ci riapplica l'avanzamento salvato.
     *
     * @throws IllegalArgumentException se i dati nominano un luogo, una classe o un
     *                                  tipo di oggetto che non esiste
     */
    static Partita versoPartita(DatiPartita dati) {
        Galassia galassia = FabbricaPartita.creaGalassia();
        for (String id : dati.idLuoghiCompletati()) {
            cercaLuogo(galassia, id).completa();
        }
        DatiGiocatore datiGiocatore = dati.giocatore();
        Giocatore giocatore = new Giocatore(datiGiocatore.nome(),
                ClassePersonaggio.valueOf(datiGiocatore.classe()),
                datiGiocatore.salute(), datiGiocatore.energia());
        for (DatiOggetto datiOggetto : datiGiocatore.inventario()) {
            giocatore.inventario().aggiungi(oggetto(datiOggetto));
        }
        return new Partita(giocatore, galassia, cercaLuogo(galassia, dati.idLuogoCorrente()));
    }

    private static DatiOggetto datiOggetto(Oggetto oggetto) {
        return switch (oggetto) {
            case Arma arma -> new DatiOggetto("ARMA", arma.nome(), arma.bonusAttacco());
            case OggettoCura cura -> new DatiOggetto("CURA", cura.nome(), cura.puntiCura());
            default -> throw new IllegalArgumentException(
                    "Tipo di oggetto che non si sa salvare: " + oggetto.getClass().getSimpleName());
        };
    }

    private static Oggetto oggetto(DatiOggetto dati) {
        return switch (dati.tipo()) {
            case "ARMA" -> new Arma(dati.nome(), dati.valore());
            case "CURA" -> new OggettoCura(dati.nome(), dati.valore());
            default -> throw new IllegalArgumentException("Tipo di oggetto sconosciuto: " + dati.tipo());
        };
    }

    private static Luogo cercaLuogo(Galassia galassia, String id) {
        return galassia.cerca(id)
                .orElseThrow(() -> new IllegalArgumentException("Luogo sconosciuto: " + id));
    }
}
