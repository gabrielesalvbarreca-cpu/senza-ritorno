package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GalassiaTest {

    private final Luogo terra = new Luogo("terra", "Terra", "Casa");
    private final Luogo alfa = new Luogo("alfa", "Stazione Alfa", "Snodo");
    private final Luogo confine = new Luogo("confine", "Confine", "La fine");

    @Test
    void unaRottaSiPercorreNeiDueSensi() {
        Galassia galassia = new Galassia(terra, confine);

        galassia.collega(terra, alfa);

        assertTrue(galassia.sonoCollegati(terra, alfa));
        assertTrue(galassia.sonoCollegati(alfa, terra));
        assertFalse(galassia.sonoCollegati(terra, confine));
        assertEquals(List.of(alfa), galassia.vicini(terra));
    }

    @Test
    void partenzaEDestinazioneFinaleFannoGiaParteDellaGalassia() {
        Galassia galassia = new Galassia(terra, confine);

        assertTrue(galassia.contiene(terra));
        assertTrue(galassia.contiene(confine));
        assertFalse(galassia.contiene(alfa));
        assertEquals(List.of(terra, confine), galassia.luoghi());
    }

    @Test
    void iLuoghiSiRitrovanoPerId() {
        Galassia galassia = new Galassia(terra, confine);
        galassia.collega(terra, alfa);

        assertEquals(Optional.of(alfa), galassia.cerca("alfa"));
        assertTrue(galassia.cerca("sconosciuto").isEmpty());
    }

    @Test
    void unLuogoNonSiPuoCollegareASeStesso() {
        Galassia galassia = new Galassia(terra, confine);

        assertThrows(IllegalArgumentException.class, () -> galassia.collega(terra, terra));
    }

    @Test
    void dueLuoghiDiversiNonPossonoAvereLoStessoId() {
        Galassia galassia = new Galassia(terra, confine);
        Luogo altraTerra = new Luogo("terra", "Seconda Terra", "Un doppione");

        assertThrows(IllegalArgumentException.class, () -> galassia.collega(altraTerra, alfa));
    }
}
