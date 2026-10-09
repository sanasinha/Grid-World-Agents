# CS 440 Lab 1: Grid-World Agents

My first set of game-playing agents for **CS 440: Introduction to Artificial Intelligence** at Boston University (Fall 2026). Each agent controls one or more units on a grid map and decides, turn by turn, which direction every unit moves.

## The agents

### `ScriptedAgent`: coin first, then the exit
- `initializeFromState` finds the single unit it controls and scans every tile for the `COIN`.
- `assignActions` picks a target each turn. The target is the coin until it has been collected (`getNumCoinsCollected() == 0`), then the exit, which is hard-coded at `(2, 6)` for this map.
- It moves **UP** until it reaches the target's row, then **RIGHT** until it reaches the target's column. The script is the rule "up, then right", not a fixed list of moves.

### `ZigZagAgent`: right, up, right, up…
- Finds the unit it controls and locates the `FINISH` tile.
- Uses a `preferRight` flag that flips every turn, so the unit moves **RIGHT, UP, RIGHT, UP…** starting with RIGHT. Nothing in the logic depends on a particular map, so it works on any map where this staircase reaches the exit.

### `ClosestUnitAgent`: move only the unit nearest the exit
- Records every unit it controls (any number of them) and the `FINISH` coordinate.
- Each turn, it computes the Euclidean distance from every unit to the exit:
  $$d(c_1, c_2) = \sqrt{(c_1.x - c_2.x)^2 + (c_1.y - c_2.y)^2}$$
- Only the closest unit moves. It goes **LEFT/RIGHT** until it shares the exit's column, then **UP/DOWN** into the exit. Map size, number of units and starting positions can all vary.

### The agent API

All three extend the course framework's abstract `Agent` and implement two hooks:

- `initializeFromState(StateView)` is a "secondary constructor" that runs once the world exists. It is where the agents discover unit IDs and tile locations.
- `assignActions(StateView)` is called every turn. It returns a `Map<unitId, Direction>` with at most one move per unit.

## Project layout

```
src/labs/lab1/agents/
├── ScriptedAgent.java
├── ZigZagAgent.java
└── ClosestUnitAgent.java
lab1.srcs          # list of source files passed to javac
```

## Running it

You need **Java 21**. The game engine (`labs-lab1-jar`) and `argparse4j` are course-provided jars, so they are not in this repo. Put them in `lib/` before compiling.

```bash
# macOS / Linux, from the repo root
javac -cp "./lib/*:." @lab1.srcs

java -cp "./lib/*:." edu.bu.labs.lab1.EmptyMapMain          src.labs.lab1.agents.ScriptedAgent
java -cp "./lib/*:." edu.bu.labs.lab1.EmptyMapMainWithCoin  src.labs.lab1.agents.ScriptedAgent
java -cp "./lib/*:." edu.bu.labs.lab1.EmptyMapMain          src.labs.lab1.agents.ZigZagAgent
java -cp "./lib/*:." edu.bu.labs.lab1.TwoUnitMapMain        src.labs.lab1.agents.ClosestUnitAgent
```

On Windows, use `;` instead of `:` in the classpath (`-cp ./lib/*;.`).

Useful flags: `--hz 2` slows rendering down so you can watch each turn, `-s` runs without the GUI, and `--seed N` fixes the random seed.

## What I learned

- How a turn-based game loop works, and how to write an agent against a read-only state view (`StateView`).
- How to separate setup that needs world state from per-turn decision making.
- The difference between scripted behaviour (`ScriptedAgent`) and behaviour that generalizes across maps (`ZigZagAgent`, `ClosestUnitAgent`).

---
*Coursework for CS 440 at Boston University. The game engine and starter scaffolding were provided by the course staff. The agent logic is my own.*
