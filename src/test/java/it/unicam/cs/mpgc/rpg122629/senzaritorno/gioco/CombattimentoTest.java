package it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.OggettoCura;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Nemico;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Statistiche;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Il giocatore è un Soldato senza armi (salute 120, attacco 16, difesa 16, energia
 * 80); il pirata ha attacco 20 e difesa 6. Quindi il giocatore fa 10 danni a colpo e
 * il pirata 4.
 */
class CombattimentoTest {

    private final Giocatore giocatore = new Giocatore("Nova", ClassePersonaggio.SOLDATO);

    private static Luogo luogoCon(Nemico nemico) {
        return new Luogo("alfa", "Stazione Alfa", "Snodo", nemico);
    }

    private static Nemico pirata(int salute) {
        return new Nemico("Pirata", new Statistiche(salute, 20, 6));
    }

    @Test
    void attaccareFaAttaccoMenoDifesaEIlNemicoRisponde() {
        Combattimento scontro = new Combattimento(giocatore, luogoCon(pirata(50)));

        List<String> turno = scontro.attacca();

        assertEquals(40, scontro.nemico().salute());
        assertEquals(116, giocatore.salute());
        assertEquals(List.of("Nova colpisce Pirata: 10 danni.", "Pirata colpisce Nova: 4 danni."), turno);
        assertFalse(scontro.isFinito());
    }

    @Test
    void ogniColpoFaAlmenoUnDanno() {
        Nemico corazzato = new Nemico("Corazzato", new Statistiche(50, 5, 30));
        Combattimento scontro = new Combattimento(giocatore, luogoCon(corazzato));

        scontro.attacca();

        assertEquals(49, corazzato.salute());
        assertEquals(119, giocatore.salute());
    }

    @Test
    void ilColpoPotenteFaDannoDoppioEConsumaEnergia() {
        Combattimento scontro = new Combattimento(giocatore, luogoCon(pirata(50)));

        scontro.colpoPotente();

        assertEquals(30, scontro.nemico().salute());
        assertEquals(80 - Combattimento.COSTO_COLPO_POTENTE, giocatore.energia());
    }

    @Test
    void senzaEnergiaIlColpoPotenteVieneRifiutato() {
        Giocatore stanco = new Giocatore("Nova", ClassePersonaggio.SOLDATO, 120, 10);
        Combattimento scontro = new Combattimento(stanco, luogoCon(pirata(50)));

        assertFalse(scontro.puoColpoPotente());
        assertThrows(IllegalStateException.class, scontro::colpoPotente);
        assertEquals(50, scontro.nemico().salute());
    }

    @Test
    void difendersiDimezzaIlColpoDelNemico() {
        Combattimento scontro = new Combattimento(giocatore, luogoCon(pirata(50)));

        scontro.difenditi();

        assertEquals(118, giocatore.salute());
        assertEquals(50, scontro.nemico().salute());
    }

    @Test
    void usareUnOggettoCuraLoConsumaEIlNemicoRispondeLoStesso() {
        Giocatore ferito = new Giocatore("Nova", ClassePersonaggio.SOLDATO, 100, 80);
        OggettoCura medikit = new OggettoCura("Medikit", 30);
        ferito.inventario().aggiungi(medikit);
        Combattimento scontro = new Combattimento(ferito, luogoCon(pirata(50)));

        scontro.usaOggetto(medikit);

        assertEquals(120 - 4, ferito.salute());
        assertFalse(ferito.inventario().contiene(medikit));
    }

    @Test
    void nonSiPuoUsareUnOggettoCheNonSiPossiede() {
        Combattimento scontro = new Combattimento(giocatore, luogoCon(pirata(50)));

        assertThrows(IllegalArgumentException.class, () -> scontro.usaOggetto(new OggettoCura("Medikit", 30)));
        assertEquals(120, giocatore.salute());
    }

    @Test
    void vincereCompletaIlLuogoEIlNemicoNonRisponde() {
        Luogo alfa = luogoCon(pirata(10));
        Combattimento scontro = new Combattimento(giocatore, alfa);

        List<String> turno = scontro.attacca();

        assertTrue(scontro.isFinito());
        assertTrue(scontro.giocatoreHaVinto());
        assertEquals(120, giocatore.salute());
        assertEquals("Pirata è sconfitto.", turno.getLast());
        assertTrue(alfa.isCompletato());
    }

    @Test
    void perdereLasciaIlNemicoAlSuoPosto() {
        Giocatore alloStremo = new Giocatore("Nova", ClassePersonaggio.SOLDATO, 4, 80);
        Luogo alfa = luogoCon(pirata(50));
        Combattimento scontro = new Combattimento(alloStremo, alfa);

        List<String> turno = scontro.attacca();

        assertTrue(scontro.isFinito());
        assertFalse(scontro.giocatoreHaVinto());
        assertEquals("Nova cade in battaglia.", turno.getLast());
        assertTrue(alfa.nemico().isPresent());
    }

    @Test
    void aScontroFinitoNonSiAgiscePiu() {
        Combattimento scontro = new Combattimento(giocatore, luogoCon(pirata(10)));
        scontro.attacca();

        assertThrows(IllegalStateException.class, scontro::attacca);
        assertThrows(IllegalStateException.class, scontro::difenditi);
    }

    @Test
    void senzaNemicoNonSiApreUnoScontro() {
        Luogo tranquillo = new Luogo("terra", "Terra", "Casa");

        assertThrows(IllegalStateException.class, () -> new Combattimento(giocatore, tranquillo));
    }
}
