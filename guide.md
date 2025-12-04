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

## 5. Concrete User Cases example 
Each case lists actors, prerequisites, triggers, the main happy path, and notable alternate flows.

UC1: Start the System
Actor: Store Staff
Precondition: The guitar data is stored in some persistent storage.
Basic Flow:
1. The user starts the system.
2. The system displays “Welcome to Rick’s Guitar Inventory!”
3. The system displays the Main Screen, which contains the list of guitars in the inventory, and
options to “Add a Guitar”, “Remove a Guitar”, “Search for Guitars”, “Generate Inventory
Report”, and “System Backup”.

UC2: Add a Guitar
Actor: Store Staff
Precondition: The system is on the Main Screen.
Basic Flow:
1. User chooses to add a new guitar to the inventory.
2. System prompts the user for the following information: serial number, price, builder, model,
type of the guitar, type of back wood, and type of top wood.
3. User provides all the required information.
4. System checks if the serial number already exists in the inventory.
5. If the serial number is unique, system adds the guitar to the inventory.
6. System confirms the successful addition of the guitar.
Exception Flow A:
5.a If the serial number is not unique, system informs the user that the guitar cannot be added due to a
duplicate serial number.
6. Resume at Step 2.
Exception Flow B:
4.a If the user provides incomplete information, system prompts the user to complete all required fields.
5. Resume at Step 2.
Postcondition:
A new guitar is added to the inventory, or the user is informed of the reason the guitar couldn't be
added.
Special Requirements:
1. If the computer loses power while the program is running, the guitar data should remain as up
to date as possible.
2. All active Guitar Store applications must automatically update to reflect changes whenever a
new guitar is added to the system.


UC3: Remove a Guitar
Actor: Store Staff
Precondition: The system is on the Main Screen.
Basic Flow:
1. User chooses to remove a guitar from the inventory.
2. System prompts the user for the serial number of the guitar.
3. User provides the serial number.
4. System checks if the serial number exists in the inventory.
5. If it does, the system removes the guitar from the inventory.
6. System confirms the successful removal of the guitar to the user.
Exception Flow:
5.a If the serial number does not exist, system informs the user that the guitar cannot be removed
because it does not exist in the inventory.
Postcondition:
A guitar is removed from the inventory, or the user is informed of the reason the guitar couldn't be
removed.
Special Requirements:
1. If the computer loses power while the program is running, the guitar data should remain as up
to date as possible.
2. All active Guitar Store applications must automatically update to reflect changes whenever a
new guitar is added to the system.
UC4: Search for Guitars
Actor: Store Staff
Precondition: The system is on the Main Screen.
Basic Flow:
1. User chooses to search for a guitar in the inventory.
2. System prompts the user to provide at least one of the following values: builder, model, type of
the guitar, type of back wood, or type of top wood.
3. User provides the desired search criteria.
4. System searches the inventory for guitars that match all the provided criteria.
5. System displays the guitars that match the searching criteria to the user.
Alternate Flow:
5.a If no guitars match the search criteria, system informs the user that no guitars match their search
criteria.
Postcondition:
User views the search results or is informed if no matching guitars are found.
Special Requirement:
The search operation should be as fast as possible.

UC5: Update Guitar Information
Actor: Store Staff
Precondition: The system is on the Main Screen.
Basic Flow:
1. User selects a guitar.
2. System displays the guitar information (price, builder, model, type, woods, etc.)
3. User chooses to update the guitar information.
4. The system prompts user to update one or more fields (price, builder, model, type, woods, etc.).
5. User provides updated information.
6. System updates the guitar details in the inventory.
7. System confirms successful update.
Exception Flow:
4.a If the user enters any invalid data, the system displays a message to inform the user.
5. Resume at Step 4.
Postcondition:
The guitar details are updated.