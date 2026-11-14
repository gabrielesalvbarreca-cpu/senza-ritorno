package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventarioTest {

    @Test
    void siPossonoAggiungereERimuovereOggetti() {
        Inventario inventario = new Inventario();
        Oggetto medikit = new OggettoCura("Medikit", 30);

        inventario.aggiungi(medikit);
        assertTrue(inventario.contiene(medikit));

        assertTrue(inventario.rimuovi(medikit));
        assertFalse(inventario.contiene(medikit));
        assertFalse(inventario.rimuovi(medikit));
    }

    @Test
    void laListaDegliOggettiNonModificaLInventario() {
        Inventario inventario = new Inventario();
        inventario.aggiungi(new Arma("Pistola Laser", 8));

        assertThrows(UnsupportedOperationException.class,
                () -> inventario.oggetti().add(new Arma("Fucile", 12)));
        assertEquals(1, inventario.oggetti().size());
    }

    @Test
    void ilPrimoConsumabileSaltaLeArmi() {
        Inventario inventario = new Inventario();
        inventario.aggiungi(new Arma("Pistola Laser", 8));
        inventario.aggiungi(new OggettoCura("Stimolante", 20));

        assertEquals(Optional.of(new OggettoCura("Stimolante", 20)), inventario.primoConsumabile());
    }

    @Test
    void senzaArmiIlBonusDAttaccoEZero() {
        Inventario inventario = new Inventario();
        inventario.aggiungi(new OggettoCura("Stimolante", 20));

        assertEquals(0, inventario.bonusArmaMigliore());
        assertTrue(new Inventario().primoConsumabile().isEmpty());
    }
}
