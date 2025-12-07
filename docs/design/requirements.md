# Requirements - Use Cases

## UC1: Start Game
**Actor:** Players (2-4 in sharing terminal)

**Precondition:** 
- Application is runable in device
- No game is currently running 

**Basic Flow:**
1. System starts and displays language selection menu (English/Korean)
2. User selects preferred language by entering 1 or 2
3. System displays game variant selection menu (Exploding Kittens/Imploding Kittens/Streaking Kittens)
4. User selects game variant by entering 1, 2, or 3. And for our refactoring version is 1 , the exploding Kittens.
5. System displays player count prompt (2-4 players)
6. User enters number of players
7. System initializes game state:
   - Creates deck with cards appropriate to selected variant
   - Deals one Defuse card to each player
   - Deals 5 cards to each player
   - Inserts Exploding Kitten cards (count = players - 1 for base game, players for Streaking)
   - Shuffles the deck
8. System displays initial game state showing Player 0's hand and turn counter
9. System transitions to turn loop until only one player survive.

**Exception Flow A (Invalid Language):**
- 2.a If user enters invalid input (not 1 or 2), system displays error message
- Resume at Step 2

**Exception Flow B (Invalid Variant):**
- 4.a If user enters invalid input (not 1, 2, or 3), system displays error message
- Resume at Step 4

**Exception Flow C (Invalid Player Count):**
- 6.a If user enters invalid player count (not 2-4), system displays error message
- Resume at Step 6

**Postcondition:**
- Game is initialized with selected variant and given player count
- Each player has 6 cards (1 Defuse + 5 random cards)
- Deck contains appropriate number of bombs
- Player 0's turn begins

**Special Requirements:**
1. Language selection must persist for entire game session
2. Game variant determines deck composition and available card types, and keep same rule for entire game session. (Exploding Kittens for our version)
3. All players must share same terminal for input

---

## UC2: Play Shuffle Card
**Actor:** Current player

**Precondition:** 
- Game is in progress 
- Current player has at least one SHUFFLE card in hand
- It is current player's turn

**Trigger:** Player chooses to play SHUFFLE card from hand

**Basic Flow:**
1. System displays current player's hand with card indices
2. Player selects SHUFFLE card index from their hand
3. System removes SHUFFLE card from player's hand
4. System checks if any other player wants to play NOPE card:
   - For each other player who has NOPE:
     - System prompts: "Player X, play NOPE to cancel? (1=Yes, 2=No)"
     - Player responds
5. If no NOPE played (or even number of NOPEs), system proceeds:
   - System prompts: "How many times to shuffle? (1-100)"
   - Player enters number of shuffles
   - System validates input (1-100)
   - System executes shuffle using ShuffleStrategy 
   - System displays: "Deck shuffled N times!"
6. Player continues turn (may play more cards or end turn)

**Alternate Flow A (NOPE Chain - Odd NOPEs):**
- 4.a One or more players play NOPE cards (odd total count)
- 4.b System displays: "Effect cancelled by NOPE chain!"
- 4.c SHUFFLE effect does not execute
- 4.d SHUFFLE card is already removed from hand (not refunded)
- Resume normal turn (player can play more cards or end turn)

**Alternate Flow B (NOPE Chain - Even NOPEs):**
- 4.a Multiple players play NOPE cards (even total count, including NOPE of NOPE)
- 4.b System displays: "Effect goes through after N NOPEs!"
- Resume at Step 5

**Exception Flow A (Invalid Shuffle Count):**
- 5.a If player enters invalid number (not 1-100), system displays error
- Resume at Step 5

**Postcondition:**
- If not NOPEd: Deck is shuffled, card order randomized
- If NOPEd: Deck remains unchanged
- SHUFFLE card removed from player's hand
- Player may continue turn or end turn


---

## UC3: Play See The Future Card
**Actor:** Current player

**Precondition:**
- Game is in progress (UC1 completed)
- Current player has at least one SEE_THE_FUTURE card in hand
- It is current player's turn
- Deck has at least 1 card remaining

**Trigger:** Player chooses to play SEE_THE_FUTURE card from hand

**Basic Flow:**
1. System displays current player's hand with card indices
2. Player selects SEE_THE_FUTURE card index from their hand
3. System removes SEE_THE_FUTURE card from player's hand
4. System checks if any other player wants to play NOPE card (same as UC2 Step 4)
5. If no NOPE played (or even number of NOPEs), system proceeds:
   - System determines cards to reveal based on game variant:
     - Base game (Exploding Kittens): 2 cards
     - Actual cards shown = min(cards_to_reveal, deck_size)
   - System displays: "Top N cards of the deck:"
   - System reveals card types in order from top to bottom:
     - "Card 1 (top): [CARD_TYPE]"
     - "Card 2: [CARD_TYPE]"
6. Player continues turn (may play more cards or end turn)

**Alternate Flow A (NOPE Chain - Odd NOPEs):**
- 4.a One or more players play NOPE cards (odd total count)
- 4.b System displays: "Effect cancelled by NOPE chain!"
- 4.c SEE_THE_FUTURE effect does not execute, cards not revealed
- Resume normal turn

**Alternate Flow B (NOPE Chain - Even NOPEs):**
- 4.a Multiple players play NOPE cards (even total count)
- 4.b System displays: "Effect goes through after N NOPEs!"
- Resume at Step 5

**Alternate Flow C (Deck Has Fewer Cards):**
- 5.a If deck has fewer cards than variant's default reveal count
- 5.b System only reveals available cards (minimum 1)
- 5.c System displays: "Top N cards of the deck:" (where N = actual available)

**Exception Flow (Empty Deck):**
- This scenario should not occur (game ends when player must draw from empty deck)
- If deck is empty, SEE_THE_FUTURE has no effect

**Postcondition:**
- If not NOPEd: Player gains information about upcoming cards
- Deck order remains unchanged 
- SEE_THE_FUTURE card removed from player's hand
- Player may continue turn or end turn

---

## UC4: Play NOPE Card
**Actor:** Any player with NOPE card (except the one who played the card being NOPEd)

**Precondition:**
- Game is in progress 
- Another player has just played a NOPEable card (SHUFFLE, SEE_THE_FUTURE, ATTACK, etc.)
- Current reacting player has at least one NOPE card in hand
- Current reacting player is not the one who played the card being NOPEd


**Basic Flow:**
1. A player plays a NOPEable card effect
2. System identifies all players with NOPE cards (excluding the player who played the card)
3. For each eligible player in sequential order:
   - System displays: "Player X has a Nope Card."
   - System displays: "Would you like to play it?"
   - System displays: "1. Yes"
   - System displays: "2. No"
   - Player responds with 1 (Yes) or 2 (No)
4. If player chooses 1 (Yes - plays NOPE):
   - System displays: "Player X decided to play a Nope Card."
   - System removes NOPE card from player's hand
   - System displays: "Player X successfully played a Nope Card."
   - System toggles the effect cancellation state
   - System checks all players again for NOPE of NOPE (excluding the player who just played NOPE)
   - Resume at Step 2 with new excluded player
5. If player chooses 2 (No):
   - System displays: "Player X did not play a Nope Card."
   - Continue to next player with NOPE card
6. When all eligible players have responded:
   - If effect is in cancelled state: Original effect is CANCELLED
   - If effect is not cancelled: Original effect EXECUTES

**Alternate Flow A (NOPE Chain):**
- 4.a When a NOPE is played, system recursively checks for NOPE of NOPE
- 4.b The player who just played NOPE is now excluded from the next check
- 4.c All other players (including the original card player) can now NOPE the NOPE
- 4.d Each NOPE toggles the cancellation state (true ↔ false)
- Chain can continue indefinitely until no one plays NOPE

**Alternate Flow B (Multiple NOPEs in Chain):**
- Multiple players may play NOPE in sequence
- Each NOPE negates the previous state
- Final result: cancelled if odd number of total NOPEs, executes if even number

**Exception Flow A (Player Has No NOPE):**
- 3.a Player is skipped in NOPE checking loop
- Continue to next player

**Exception Flow B (Invalid Input):**
- 3.a If player enters invalid input (not 1 or 2)
- 3.b System displays: "Invalid choice. Please enter 1 or 2."
- Resume at Step 3 for same player

**Postcondition:**
- NOPE card(s) removed from player(s) hands who played NOPE
- Original effect either cancelled (odd total NOPEs) or executes (even total NOPEs including 0)
- Game continues with original card player's turn


---

## UC5: Draw Exploding Kitten Card
**Actor:** Current player

**Precondition:**
- Game is in progress 
- Current player's turn is ending (no more cards to play or played turn-ending card)
- At least one Exploding Kitten card exists in the deck
- Current player must draw a card

**Trigger:** Player ends turn and draws from deck

**Basic Flow:**
1. Player completes their card plays and chooses to end turn
2. System draws top card from deck
3. System identifies card as EXPLODING_KITTEN type
4. System executes ExplodingKittenTrigger.onDraw():
   - System displays: "You drew an EXPLODING KITTEN!"
   - System displays: "You have an EXPLODING KITTEN in your hand. Defuse it to live!"
5. System checks if player has DEFUSE card:
   - If no DEFUSE: Go to Exception Flow A (Player Explodes)
   - If has DEFUSE: Continue
6. System executes defuse:
   - Removes DEFUSE card from player's hand
   - Inserts EXPLODING_KITTEN back into deck at specified position 0-deck_size
7. Turn passes to next player

**Exception Flow A (Player Explodes - No Defuse):**
- 5.a System displays: "You have no Defuse card!"
- 5.b System displays: "You exploded!"
- 5.c System sets player status to DEAD
- 5.d System removes player from active players
- 5.e Turn passes to next alive player
- Game continues (or ends if only 1 player remains)

**Alternate Flow A (Invalid Insertion Position):**
- 6.a If player enters position < 0 or > deck_size
- 6.b System displays error message
- Resume at Step 6

**Postcondition:**
- If survived: EXPLODING_KITTEN reinserted into deck, DEFUSE removed from hand
- If exploded: Player marked as DEAD, removed from game
- Turn advances to next player
- Game checks for victory condition (only 1 player alive)