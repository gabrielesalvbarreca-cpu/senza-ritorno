package it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Galassia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Storia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Arma;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Nemico;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Statistiche;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Galassia di prova: Terra (storia) — Stazione Alfa — Confine (nemico finale). */
class PartitaTest {

    private final Arma pistola = new Arma("Pistola Laser", 8);
    private final Luogo terra = new Luogo("terra", "Terra", "Casa", new Storia("Partenza", "Si parte.", pistola));
    private final Luogo alfa = new Luogo("alfa", "Stazione Alfa", "Snodo");
    private final Luogo confine = new Luogo("confine", "Confine", "La fine",
            new Nemico("Sentinella", new Statistiche(10, 1, 0)));
    private final Giocatore giocatore = new Giocatore("Nova", ClassePersonaggio.SOLDATO);
    private final Partita partita = new Partita(giocatore, galassiaDiProva());

    private Galassia galassiaDiProva() {
        Galassia galassia = new Galassia(terra, confine);
        galassia.collega(terra, alfa);
        galassia.collega(alfa, confine);
        return galassia;
    }

    @Test
    void unaPartitaNuovaCominciaDallaPartenza() {
        assertSame(terra, partita.luogoCorrente());
        assertEquals(List.of(alfa), partita.destinazioni());
        assertEquals(StatoPartita.IN_CORSO, partita.stato());
    }

    @Test
    void siViaggiaSoloVersoLuoghiCollegati() {
        assertThrows(IllegalArgumentException.class, () -> partita.viaggiaVerso(confine));

        partita.viaggiaVerso(alfa);

        assertSame(alfa, partita.luogoCorrente());
    }

    @Test
    void unaStoriaSiLeggeUnaVoltaSolaELasciaLaRicompensa() {
        assertTrue(partita.leggiStoria().isPresent());
        assertTrue(giocatore.inventario().contiene(pistola));

        assertTrue(partita.leggiStoria().isEmpty());
        assertEquals(1, giocatore.inventario().oggetti().size());
    }

    @Test
    void sconfiggereIlNemicoFinaleVinceLaPartita() {
        partita.viaggiaVerso(alfa);
        assertFalse(partita.nemicoPresente());
        partita.viaggiaVerso(confine);
        assertTrue(partita.nemicoPresente());

        partita.iniziaCombattimento().attacca();

        assertEquals(StatoPartita.VITTORIA, partita.stato());
    }

    @Test
    void cadereFaPerdereLaPartita() {
        giocatore.subisciDanno(giocatore.saluteMassima());

        assertEquals(StatoPartita.SCONFITTA, partita.stato());
    }

    @Test
    void nonSiRiprendeUnaPartitaDaUnLuogoFuoriDallaGalassia() {
        Luogo estraneo = new Luogo("altrove", "Altrove", "Non collegato");

        assertThrows(IllegalArgumentException.class, () -> new Partita(giocatore, galassiaDiProva(), estraneo));
    }
}
