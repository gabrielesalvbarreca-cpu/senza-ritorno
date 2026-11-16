package it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Galassia;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.OggettoCura;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FabbricaPartitaTest {

    @Test
    void laNuovaPartitaPartiDallaTerraConUnoStimolante() {
        Partita partita = FabbricaPartita.nuovaPartita("Nova", ClassePersonaggio.PILOTA);

        assertEquals("terra", partita.luogoCorrente().id());
        assertEquals(List.of(new OggettoCura("Stimolante", 20)), partita.giocatore().inventario().oggetti());
    }

    @Test
    void ogniLuogoSiRaggiungeDallaPartenza() {
        Galassia galassia = FabbricaPartita.creaGalassia();

        Set<Luogo> raggiunti = new HashSet<>();
        Deque<Luogo> daVisitare = new ArrayDeque<>(List.of(galassia.partenza()));
        while (!daVisitare.isEmpty()) {
            Luogo luogo = daVisitare.pop();
            if (raggiunti.add(luogo)) {
                daVisitare.addAll(galassia.vicini(luogo));
            }
        }

        assertEquals(Set.copyOf(galassia.luoghi()), raggiunti);
    }

    @Test
    void laDestinazioneFinaleESorvegliataDaUnNemico() {
        Galassia galassia = FabbricaPartita.creaGalassia();

        assertTrue(galassia.destinazioneFinale().nemico().isPresent());
    }

    @Test
    void ogniChiamataCreaUnaGalassiaNuova() {
        Galassia prima = FabbricaPartita.creaGalassia();
        prima.partenza().completa();

        assertFalse(FabbricaPartita.creaGalassia().partenza().isCompletato());
    }
}
