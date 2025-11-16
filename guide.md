# Exploding Kitten Console Guide

## 1. What This Project Does
This project is a console implementation of the Exploding Kittens card game and its Streaking/Imploding variants. The system is split between:
- `domain.game` — pure game logic (cards, deck, players, rules, and the `Game` state machine).
- `ui` — the interactive console layer (`GameUI` and `Main`) that walks players through prompts, handles localization (English/Korean), and relays game events.

## 2. How to Run the Game
1. **Install prerequisites**: JDK 11+ and Gradle Wrapper (already checked in).
2. **Compile & run**:
   ```bash
   ./gradlew clean run
   ```
   The `run` task is wired to pass stdin through to the CLI UI, so you can answer prompts interactively.
3. **Execute tests** (optional validation before play):
   ```bash
   ./gradlew test
   ```
4. **Static analysis** (optional):
   ```bash
   ./gradlew checkstyleMain spotbugsMain
   ```

## 3. Gameplay Flow at a Glance
1. **Choose Language** – English or Korean resource bundles drive all prompts.
2. **Pick Game Variant** – Exploding, Imploding, or Streaking Kittens; deck composition changes instantly.
3. **Set Player Count** – 2–4 live participants (validated input); each receives a Defuse card.
4. **Deck Setup** – Deck is built, shuffled, bombs inserted according to variant and player count.
5. **Initial Draw** – Each player draws five cards; game state is shown before every turn.
6. **Turn Loop** – Active player may play single cards or combos, trigger effects, resolve NOPEs, and optionally end the turn by drawing.
7. **Card Resolution** – Game logic handles attacks, skips, curses, special draws, combo steals, and bomb/defuse interactions.
8. **Victory Condition** – Game ends when only one player remains alive; `GameUI` performs wrap-up messaging.

## 4. Complete User Story
> *As a group of friends who want to play Exploding Kittens variants without the physical deck, we launch the console app, pick a shared language, and configure our preferred variant. Each player sits at the same terminal, announcing their inputs when prompted. On every turn we can see our hands (unless cursed), decide whether to play cards or combos, and negotiate reactions like NOPEs while the system enforces rules such as attack chains, reversed order, and bomb handling. We keep drawing and resolving cards until only one player survives, at which point the app announces the winner and we can restart if desired.*

## 5. Concrete User Cases
Each case lists actors, prerequisites, triggers, the main happy path, and notable alternate flows.

### UC1 – Select Language
- **Actors**: All players (shared console).
- **Preconditions**: Application launched via `./gradlew run`.
- **Trigger**: CLI displays the language menu.
- **Flow**: Player enters `1` for English or `2` for Korean. `GameUI` loads the matching `ResourceBundle` so all later prompts use that locale.
- **Alternates**: Invalid numerical input prints an error and re-prompts.
- **Postcondition**: Localized strings ready for the remainder of the session.

### UC2 – Configure Game Variant
- **Actors**: Same shared console operator.
- **Preconditions**: Language chosen.
- **Trigger**: Variant menu appears (`Exploding`, `Imploding`, `Streaking`).
- **Flow**: User selects option 1–3. `Game.retrieveGameMode` validates and updates `Deck` composition (e.g., adds Imploding Kitten cards or Streaking-only actions).
- **Alternates**: Selecting the current game type triggers the "must provide valid game type" exception, so `GameUI` warns and re-prompts.
- **Postcondition**: Deck blueprint and max size configured.

### UC3 – Set Player Count & Deal Opening Hands
- **Actors**: Console operator + all players.
- **Preconditions**: Variant chosen.
- **Trigger**: Player-count prompt is shown.
- **Flow**: Operator chooses 2, 3, or 4. Game injects that number, ensures limits, creates player hands, inserts bombs based on variant, shuffles, and deals five cards per player while showing turn order info.
- **Alternates**: Invalid count prints localized error; input repeats.
- **Postcondition**: All players have baseline hands plus Defuse cards.

### UC4 – Execute a Standard Turn
- **Actors**: Current player.
- **Preconditions**: Game loop running, player alive.
- **Trigger**: `GameUI.startTurn()` announces the hand and remaining actions.
- **Flow**: Player may (a) play no card and choose to end turn, (b) play a single action card (e.g., Skip, Shuffle), or (c) initiate combos via `playSpecialCombo`. End turn draws from deck and may trigger bomb resolution.
- **Alternates**: If a player is cursed, certain cards (Exploding/Streaking) auto-trigger penalties; invalid plays show warnings.
- **Postcondition**: Control passes to next player based on turns remaining.

### UC5 – Resolve Attacks and Targeted Attacks
- **Actors**: Attacking player, targeted players.
- **Preconditions**: At least one Attack or Targeted Attack card is played.
- **Trigger**: Player selects Attack/Super Skip options.
- **Flow**: Card is removed from hand, `startAttackFollowUp` begins; every affected player can chain further attacks or choose targets. The attack queue executes via `Game.startAttackPhase`, incrementing required future turns.
- **Alternates**: Any player can interrupt with NOPE; targeted attacks can redirect to specific players.
- **Postcondition**: Turn counters updated, attack queue emptied.

### UC6 – Handle Bomb, Defuse, Imploding, and Streaking Interactions
- **Actors**: Player drawing a bomb & other players (if effects ripple).
- **Preconditions**: Player ends a turn and draws a card.
- **Trigger**: Drawn card is Exploding Kitten or Imploding Kitten.
- **Flow**: `GameUI.endTurn` detects bomb, checks for Streaking support or Defuse card. If Defuse exists, player chooses where to reinsert the bomb. Imploding cards flip once, then kill the player when drawn face-up.
- **Alternates**: Streaking Kitten owners can optionally keep an Exploding card; lacking defenses kills the player immediately.
- **Postcondition**: Player either survives (and adjusts deck) or is marked dead, possibly ending the game.

### UC7 – Perform Special Combos (Two / Three of a Kind & Feral Cats)
- **Actors**: Current player.
- **Preconditions**: Player possesses duplicates or Feral Cats.
- **Trigger**: Player chooses combo option during their turn.
- **Flow**: `playSpecialCombo` asks for combo size, enforces card counts, supports mixing Feral Cats, and then executes the reward (steal selected card or specific target card type).
- **Alternates**: Insufficient cards triggers localized warnings; cursed players who try invalid combos automatically discard and continue.
- **Postcondition**: Targeted card theft resolves, used cards removed from hand.

### UC8 – Apply Curse of the Cat Butt and Tracking Cursed Players
- **Actors**: Player playing the curse; targeted player.
- **Preconditions**: Card in hand, target alive and not the current player.
- **Trigger**: Player selects the card from hand.
- **Flow**: System prompts for target index, validates, marks them cursed (hand hidden, special discard behavior, forced shuffles upon garbage collection).
- **Alternates**: Invalid target input re-prompts; if target dies, curse is irrelevant.
- **Postcondition**: Target’s hand becomes hidden from everyone (including themselves) until curse is cleared.

### UC9 – Run Garbage Collection & Deck Manipulation Utilities
- **Actors**: Active player and all living players.
- **Preconditions**: Garbage Collection, Alter the Future, See the Future, Swap Top/Bottom cards, or Draw-From-Bottom actions available.
- **Trigger**: Player plays respective card.
- **Flow**: `GameUI` walks through showing other hands (unless cursed), collecting discard choices, optionally letting players inspect or reorder top-of-deck cards, or drawing from the bottom.
- **Alternates**: NOPE reactions cancel the effect; invalid indices re-prompt.
- **Postcondition**: Deck state is updated, and play proceeds according to the card’s rule.

## 6. Tips for Facilitators
- Keep a single note-taker near the terminal to avoid accidental key presses.
- Verbally announce every input so everyone can agree before confirming.
- Encourage players to track their remaining turns when attack chains get deep; the UI shows `playerTurnsMessage` each time to help.
- Re-run `./gradlew run` to start a new session after a winner is announced.

This guide should give new contributors and play-testers enough structure to operate the console UI and understand how user-facing scenarios map to the underlying design.
