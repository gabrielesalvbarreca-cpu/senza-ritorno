package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.mondo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * La mappa del gioco: un grafo di {@link Luogo} collegati da rotte percorribili nei
 * due sensi, con un luogo di partenza e una destinazione finale.
 *
 * <p>Il grafo è una mappa di adiacenza: per ogni luogo, l'insieme dei luoghi che si
 * raggiungono con un solo viaggio. Una seconda mappa ritrova i luoghi per
 * {@linkplain Luogo#id() id} in tempo costante. La galassia descrive solo la
 * struttura del mondo: dove si trova il giocatore lo sa la partita.</p>
 */
public final class Galassia {

    private final Map<String, Luogo> perId = new LinkedHashMap<>();
    private final Map<Luogo, Set<Luogo>> rotte = new HashMap<>();
    private final Luogo partenza;
    private final Luogo destinazioneFinale;

    /**
     * @param partenza           dove comincia ogni viaggio
     * @param destinazioneFinale il luogo in cui si vince la partita
     */
    public Galassia(Luogo partenza, Luogo destinazioneFinale) {
        this.partenza = Objects.requireNonNull(partenza, "partenza");
        this.destinazioneFinale = Objects.requireNonNull(destinazioneFinale, "destinazioneFinale");
        aggiungi(partenza);
        aggiungi(destinazioneFinale);
    }

    /**
     * Collega due luoghi nei due sensi, aggiungendoli alla galassia se serve.
     *
     * @throws IllegalArgumentException se i due luoghi coincidono o se nella
     *                                  galassia c'è già un altro luogo con lo stesso id
     */
    public void collega(Luogo a, Luogo b) {
        if (a == b) {
            throw new IllegalArgumentException("Un luogo non può essere collegato a se stesso");
        }
        aggiungi(a);
        aggiungi(b);
        rotte.get(a).add(b);
        rotte.get(b).add(a);
    }

    private void aggiungi(Luogo luogo) {
        Objects.requireNonNull(luogo, "luogo");
        Luogo giaPresente = perId.putIfAbsent(luogo.id(), luogo);
        if (giaPresente != null && giaPresente != luogo) {
            throw new IllegalArgumentException("Un altro luogo usa già l'id '" + luogo.id() + "'");
        }
        rotte.putIfAbsent(luogo, new LinkedHashSet<>());
    }

    public Luogo partenza() {
        return partenza;
    }

    public Luogo destinazioneFinale() {
        return destinazioneFinale;
    }

    /** @return il luogo con l'id indicato, se fa parte della galassia. */
    public Optional<Luogo> cerca(String id) {
        return Optional.ofNullable(perId.get(id));
    }

    /** @return {@code true} se proprio questo luogo fa parte della galassia. */
    public boolean contiene(Luogo luogo) {
        return luogo != null && perId.get(luogo.id()) == luogo;
    }

    /** @return i luoghi raggiungibili da {@code luogo} con un solo viaggio. */
    public List<Luogo> vicini(Luogo luogo) {
        return List.copyOf(rotte.getOrDefault(luogo, Set.of()));
    }

    public boolean sonoCollegati(Luogo a, Luogo b) {
        return rotte.getOrDefault(a, Set.of()).contains(b);
    }

    /** @return tutti i luoghi, nell'ordine in cui sono stati aggiunti. */
    public List<Luogo> luoghi() {
        return List.copyOf(perId.values());
    }
}
