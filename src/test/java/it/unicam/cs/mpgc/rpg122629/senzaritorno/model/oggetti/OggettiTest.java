package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Nemico;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Statistiche;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OggettiTest {

    @Test
    void unOggettoCuraRestituisceLaSuaSalute() {
        Nemico ferito = new Nemico("Bersaglio", new Statistiche(50, 0, 0));
        ferito.subisciDanno(30);

        new OggettoCura("Medikit", 20).usaSu(ferito);

        assertEquals(40, ferito.salute());
    }

    @Test
    void valoriNonValidiVengonoRifiutati() {
        assertThrows(IllegalArgumentException.class, () -> new OggettoCura("Medikit", 0));
        assertThrows(IllegalArgumentException.class, () -> new Arma("Pistola", -1));
        assertThrows(IllegalArgumentException.class, () -> new Arma(" ", 5));
    }

    @Test
    void ogniOggettoSiDescriveDaSolo() {
        assertEquals("Arma: +8 all'attacco", new Arma("Pistola Laser", 8).descrizione());
        assertEquals("Cura: ripristina 30 punti salute", new OggettoCura("Medikit", 30).descrizione());
    }
}
