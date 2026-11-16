package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Arma;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Nemico;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Statistiche;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LuogoTest {

    @Test
    void unaStoriaLettaNonSiRipresenta() {
        Luogo terra = new Luogo("terra", "Terra", "Casa",
                new Storia("Partenza", "Si parte.", new Arma("Pistola Laser", 8)));
        assertTrue(terra.storia().isPresent());

        terra.completa();

        assertTrue(terra.isCompletato());
        assertTrue(terra.storia().isEmpty());
    }

    @Test
    void unNemicoSconfittoNonSorvegliaPiuIlLuogo() {
        Luogo alfa = new Luogo("alfa", "Stazione Alfa", "Snodo",
                new Nemico("Pirata", new Statistiche(70, 14, 8)));
        assertTrue(alfa.nemico().isPresent());

        alfa.completa();

        assertTrue(alfa.nemico().isEmpty());
    }

    @Test
    void unLuogoTranquilloNonHaNeStoriaNeNemico() {
        Luogo vuoto = new Luogo("vuoto", "Vuoto", "Niente");

        assertTrue(vuoto.storia().isEmpty());
        assertTrue(vuoto.nemico().isEmpty());
        assertFalse(vuoto.isCompletato());
    }

    @Test
    void idENomeVuotiVengonoRifiutati() {
        assertThrows(IllegalArgumentException.class, () -> new Luogo(" ", "Terra", "Casa"));
        assertThrows(IllegalArgumentException.class, () -> new Luogo("terra", "", "Casa"));
    }
}
