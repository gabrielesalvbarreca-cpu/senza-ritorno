package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Arma;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GiocatoreTest {

    @Test
    void unGiocatoreNuovoPartiInPienaFormaConLeStatisticheDellaClasse() {
        Giocatore giocatore = new Giocatore("Nova", ClassePersonaggio.HACKER);

        assertEquals(ClassePersonaggio.HACKER.statistiche(), giocatore.statistiche());
        assertEquals(80, giocatore.salute());
        assertEquals(130, giocatore.energia());
        assertTrue(giocatore.inventario().oggetti().isEmpty());
    }

    @Test
    void unNomeVuotoVieneRifiutato() {
        assertThrows(IllegalArgumentException.class, () -> new Giocatore("  ", ClassePersonaggio.PILOTA));
    }

    @Test
    void ilDannoRiduceLaSaluteSenzaScendereSottoZero() {
        Giocatore giocatore = new Giocatore("Nova", ClassePersonaggio.SOLDATO);

        giocatore.subisciDanno(20);
        assertEquals(100, giocatore.salute());

        giocatore.subisciDanno(500);
        assertEquals(0, giocatore.salute());
        assertFalse(giocatore.isVivo());
    }

    @Test
    void laCuraNonSuperaLaSaluteMassima() {
        Giocatore giocatore = new Giocatore("Nova", ClassePersonaggio.SOLDATO);
        giocatore.subisciDanno(30);

        giocatore.cura(1000);

        assertEquals(giocatore.saluteMassima(), giocatore.salute());
    }

    @Test
    void spendereEnergiaLaRiduceERifiutaUnaSpesaScoperta() {
        Giocatore giocatore = new Giocatore("Nova", ClassePersonaggio.SOLDATO);

        giocatore.spendiEnergia(30);

        assertEquals(50, giocatore.energia());
        assertThrows(IllegalStateException.class, () -> giocatore.spendiEnergia(51));
        assertEquals(50, giocatore.energia());
    }

    @Test
    void lAttaccoComprendeSoloLArmaMigliore() {
        Giocatore giocatore = new Giocatore("Nova", ClassePersonaggio.SOLDATO);
        giocatore.inventario().aggiungi(new Arma("Coltello", 3));
        giocatore.inventario().aggiungi(new Arma("Pistola Laser", 8));

        assertEquals(16 + 8, giocatore.attacco());
    }

    @Test
    void unGiocatoreRipresoDaUnSalvataggioConservaSaluteEdEnergia() {
        Giocatore giocatore = new Giocatore("Nova", ClassePersonaggio.PILOTA, 45, 20);

        assertEquals(45, giocatore.salute());
        assertEquals(20, giocatore.energia());
        assertEquals(90, giocatore.saluteMassima());
    }

    @Test
    void unaSaluteOltreIlMassimoVieneRifiutata() {
        assertThrows(IllegalArgumentException.class,
                () -> new Giocatore("Nova", ClassePersonaggio.PILOTA, 91, 20));
    }
}
