package it.unicam.cs.mpgc.rpg122629.senzaritorno.model.personaggi;

/**
 * Un avversario che sorveglia un luogo. Comincia ogni scontro in piena forma e non
 * ha altro che le sue {@link Statistiche}: tutta la logica comune sta in
 * {@link Personaggio}.
 */
public final class Nemico extends Personaggio {

    public Nemico(String nome, Statistiche statistiche) {
        super(nome, statistiche, statistiche.saluteMassima());
    }
}
