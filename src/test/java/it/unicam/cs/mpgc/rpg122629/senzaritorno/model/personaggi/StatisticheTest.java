package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StatisticheTest {

    @Test
    void laSaluteMassimaDeveEsserePositiva() {
        assertThrows(IllegalArgumentException.class, () -> new Statistiche(0, 10, 10));
    }

    @Test
    void attaccoEDifesaNonPossonoEssereNegativi() {
        assertThrows(IllegalArgumentException.class, () -> new Statistiche(50, -1, 10));
        assertThrows(IllegalArgumentException.class, () -> new Statistiche(50, 10, -1));
    }
}
