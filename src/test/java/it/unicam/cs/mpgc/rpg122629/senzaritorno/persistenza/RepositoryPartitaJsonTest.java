package it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza;

import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.FabbricaPartita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.Partita;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo.Luogo;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.Arma;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.oggetti.OggettoCura;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.ClassePersonaggio;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi.Giocatore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositoryPartitaJsonTest {

    @TempDir
    Path cartella;

    /** Una partita a metà viaggio: storia della Terra letta, giocatore ferito, sul Relè Nexus. */
    private static Partita partitaAvviata() {
        Partita partita = FabbricaPartita.nuovaPartita("Nova", ClassePersonaggio.HACKER);
        partita.leggiStoria();
        Giocatore giocatore = partita.giocatore();
        giocatore.subisciDanno(25);
        giocatore.spendiEnergia(30);
        partita.viaggiaVerso(partita.galassia().cerca("alfa").orElseThrow());
        partita.luogoCorrente().completa();
        partita.viaggiaVerso(partita.galassia().cerca("nexus").orElseThrow());
        return partita;
    }

    @Test
    void salvareERicaricareRipristinaLAvanzamento() throws PersistenzaException {
        RepositoryPartita repository = new RepositoryPartitaJson(cartella.resolve("salvataggio.json"));

        repository.salva(partitaAvviata());
        Partita caricata = repository.carica();

        Giocatore giocatore = caricata.giocatore();
        assertEquals("Nova", giocatore.nome());
        assertEquals(ClassePersonaggio.HACKER, giocatore.classe());
        assertEquals(80 - 25, giocatore.salute());
        assertEquals(130 - 30, giocatore.energia());
        assertEquals(List.of(new OggettoCura("Stimolante", 20), new Arma("Pistola Laser", 8)),
                giocatore.inventario().oggetti());
        assertEquals("nexus", caricata.luogoCorrente().id());
        assertEquals(List.of("terra", "alfa"), caricata.galassia().luoghi().stream()
                .filter(Luogo::isCompletato).map(Luogo::id).toList());
    }

    @Test
    void salvareCreaLeCartelleMancanti() throws PersistenzaException {
        Path file = cartella.resolve("annidata").resolve("salvataggio.json");
        RepositoryPartita repository = new RepositoryPartitaJson(file);
        assertFalse(repository.esisteSalvataggio());

        repository.salva(partitaAvviata());

        assertTrue(repository.esisteSalvataggio());
    }

    @Test
    void caricareSenzaSalvataggioVieneSegnalato() {
        RepositoryPartita repository = new RepositoryPartitaJson(cartella.resolve("salvataggio.json"));

        assertThrows(PersistenzaException.class, repository::carica);
    }

    @Test
    void unFileCheNonEJsonVieneSegnalato() throws Exception {
        Path file = cartella.resolve("salvataggio.json");
        Files.writeString(file, "{ json non valido");

        assertThrows(PersistenzaException.class, () -> new RepositoryPartitaJson(file).carica());
    }

    @Test
    void unSalvataggioConUnLuogoSconosciutoVieneSegnalato() throws Exception {
        Path file = cartella.resolve("salvataggio.json");
        Files.writeString(file, """
                {
                  "giocatore": {"nome": "Nova", "classe": "PILOTA", "salute": 50, "energia": 10, "inventario": []},
                  "idLuogoCorrente": "atlantide",
                  "idLuoghiCompletati": []
                }
                """);

        assertThrows(PersistenzaException.class, () -> new RepositoryPartitaJson(file).carica());
    }
}
