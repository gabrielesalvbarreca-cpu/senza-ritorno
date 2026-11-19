# Senza Ritorno — gioco di ruolo narrativo di fantascienza

Progetto d'esame per il corso di **Metodologie di Programmazione** (A.A. 2025/26, Università di Camerino).

Si parte dalla Terra con un nome, una classe e uno stimolante in tasca, e si attraversa una
piccola galassia di pianeti e stazioni spaziali. Alcune tappe raccontano un pezzo di storia e
lasciano un oggetto, altre aprono un **combattimento a turni**. Si vince sconfiggendo la
Sentinella al Confine del Vuoto, si perde cadendo in battaglia.

- **Autore:** Gabriele Barreca, matricola 122629
- **Package radice:** `it.unicam.cs.mpgc.rpg122629.senzaritorno`
- **Documentazione:** la [Wiki](../../wiki) del repository descrive funzionalità, architettura,
  classi, persistenza ed estendibilità

## Requisiti

- Un JDK 17 o successivo per avviare Gradle. Il progetto usa **Java 25**: se non è installato,
  Gradle lo scarica da solo.
- Nient'altro da installare: Gradle arriva con il wrapper incluso (`gradlew`) e JavaFX lo scarica
  la build.
- Una connessione a Internet alla prima compilazione, per scaricare le dipendenze.

## Come si esegue

```bash
git clone https://github.com/gabrielesalvbarreca-cpu/senza-ritorno.git
cd senza-ritorno
./gradlew build    # compila ed esegue i 52 test
./gradlew run      # avvia il gioco
```

Su Windows, da PowerShell o dal Prompt dei comandi, i comandi sono `.\gradlew.bat build` e
`.\gradlew.bat run`.

## Come si gioca

1. Dal menu si sceglie **Nuova partita**, oppure **Carica partita** per riprendere un viaggio
   salvato.
2. Si sceglie un nome e una classe (**Pilota**, **Ingegnere**, **Soldato**, **Hacker**), ognuna con
   salute, energia, attacco e difesa diversi.
3. Nell'esplorazione si sceglie uno dei luoghi collegati a quello corrente. Una storia letta o un
   nemico sconfitto non si ripresentano.
4. In combattimento si sceglie una mossa a ogni turno: **Attacca**, **Colpo potente** (danno doppio,
   30 di energia), **Difenditi** (dimezza il colpo del nemico) o **Usa oggetto**. A scontro finito
   si prosegue con **Continua**.
5. Con **Inventario** si vedono gli oggetti, con **Salva** si salva la partita. Il salvataggio
   finisce in `<cartella utente>/.senza-ritorno/salvataggio.json`.

Le regole complete sono nella pagina [Funzionalità](../../wiki/Funzionalità) della Wiki.

## Dichiarazione sull'uso di strumenti di Intelligenza Artificiale

Per la realizzazione di questo progetto ho utilizzato Claude, un assistente basato su intelligenza artificiale di Anthropic:

- Confronto sulle scelte progettuali. Ho usato l'assistente come interlocutore per mettere a confronto diverse soluzioni di design.
  Con lui ho ragionato su come organizzare i package, su quali responsabilità dare a ciascuna classe e su come applicare i principi SOLID,
  esaminando le alternative prima di decidere.
  Le principali decisioni architetturali sono nate da questi confronti e le ho poi adottate e verificate personalmente.

Le decisioni sul gioco e sul progetto (regole, contenuti, cosa tenere e cosa togliere) sono mie.
Nessuna parte è rimasta nel progetto senza che l'avessi letta e capita: ho scritto il codice, compilato, scritto ed eseguito i
test e giocato per verificarlo, e so spiegare e motivare ogni scelta. La dichiarazione dettagliata
è nella pagina [Uso dell'AI](../../wiki/Uso-dell'AI) della Wiki.
