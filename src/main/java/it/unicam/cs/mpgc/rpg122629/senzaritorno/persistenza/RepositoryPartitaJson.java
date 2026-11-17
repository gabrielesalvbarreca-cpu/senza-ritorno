package it.unicam.cs.mpgc.rpg122629.senzaritorno.persistenza;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg122629.senzaritorno.gioco.Partita;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * {@link RepositoryPartita} che scrive la partita in un file JSON leggibile, con Gson.
 *
 * <p>Il file arriva dal costruttore: il gioco usa quello nella cartella dell'utente
 * ({@link #nellaCartellaUtente()}), i test un file temporaneo. Niente percorsi
 * assoluti scritti nel codice.</p>
 */
public final class RepositoryPartitaJson implements RepositoryPartita {

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RepositoryPartitaJson(Path file) {
        this.file = Objects.requireNonNull(file, "file");
    }

    /** @return un repository che salva in {@code <cartella utente>/.senza-ritorno/salvataggio.json}. */
    public static RepositoryPartitaJson nellaCartellaUtente() {
        return new RepositoryPartitaJson(
                Path.of(System.getProperty("user.home"), ".senza-ritorno", "salvataggio.json"));
    }

    @Override
    public void salva(Partita partita) throws PersistenzaException {
        String json = gson.toJson(MapperPartita.versoDati(partita));
        try {
            Files.createDirectories(file.toAbsolutePath().getParent());
            Files.writeString(file, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenzaException("Impossibile scrivere il salvataggio: " + file, e);
        }
    }

    @Override
    public Partita carica() throws PersistenzaException {
        String json;
        try {
            json = Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenzaException("Impossibile leggere il salvataggio: " + file, e);
        }
        DatiPartita dati;
        try {
            dati = gson.fromJson(json, DatiPartita.class);
        } catch (RuntimeException e) {
            throw new PersistenzaException("Il salvataggio non è un JSON valido: " + file, e);
        }
        if (dati == null) {
            throw new PersistenzaException("Il salvataggio è vuoto: " + file);
        }
        try {
            return MapperPartita.versoPartita(dati);
        } catch (RuntimeException e) {
            // Campi mancanti o valori sconosciuti: per chi chiama è un salvataggio corrotto.
            throw new PersistenzaException("Il salvataggio è corrotto: " + file, e);
        }
    }

    @Override
    public boolean esisteSalvataggio() {
        return Files.isRegularFile(file);
    }
}
