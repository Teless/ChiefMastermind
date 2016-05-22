<h1>ChiefMastermind</h1>
<b>Summary</b>
- <a href="#creatingGame">Creating a game</a>
  - <a href="#quickGame">Quick Game</a>
  - <a href="#quickMultiplayerGame">Quick Multiplayer Game</a>
  - <a href="#joinGame">Join Game/a>
  - <a href="#gameStatus">Game Status</a>
- <a href="#startGame">Start the Game</a>
- <a href="#gameStatus">Find game status</a>
- <a href="#playingTheGame">Playing the game</a>

This an Mastermind API implentation in Java. Don't know how the game works? Check the <a href="https://en.wikipedia.org/wiki/Mastermind_(board_game)#Gameplay_and_rules">Rules<a/>

<h2 id="creatingGame">Creating a game</h2>

This API allows you to create a game with the configurations of your choice:
- Limit of players
- Limit of rounds 
- Number of positions
- Secrect size
- Number of colors

But if you want to quickly create a new the API exports two shorcuts methos

<h3 id="quickGame">Quick Game</h3>
| Parameters  | POST /game/quickGame |
| ------------- | ------------- |
| userName  | name of the player  |

```
{
  "id": "{for each game is generated a unique id}",
  "gameKey": "{for each game is generate a key (visible by the creator of the game) used to start the game}",
  "status": "MASTER_MINDING",
  "playersLimit": 1,
  "playersCount": 1,
  "roundsLimit": 8,
  "round": 0,
  "roundGuesses": 0,
  "positions": 8,
  "players": [
    "{userName}"
  ]
}
```

<h3 id="quickMultiplayerGame">Quick Multiplayer Game</h3>
| Parameters  | POST /game/quickMultiplayerGame |
| ------------- | ------------- |
| userName  | name of the player  |

```
{
  "id": "{for each game is generated a unique id}",
  "gameKey": "{for each game is generate a key (visible by the creator of the game) used to start the game}",
  "status": "WAITING",
  "playersLimit": 2,
  "playersCount": 1,
  "roundsLimit": 8,
  "round": 0,
  "roundGuesses": 0,
  "positions": 8,
  "players": [
    "{userName}"
  ]
}
```

<h3 id="customGame">Create a custom Game</h3>
| Parameters  | POST /game/newGame |
| ------------- | ------------- |
| userName  | name of the player  |
| playersLimit  | limit of players |
| roundsLimit  | limit of rounds  |
| positions  | number os positions |
| secretSize  | secrect size  |
| colorsCount  | number of colors  |

```
{
  "id": "{for each game is generated a unique id}",
  "gameKey": "{for each game is generate a key (visible by the creator of the game) used to start the game}",
  "status": "WAITING",
  "playersLimit": {playersLimit},
  "playersCount": 1,
  "roundsLimit": {roundsLimit},
  "round": 0,
  "roundGuesses": 0,
  "positions": {positions},
  "players": [
    "{userName}"
  ]
}
```

<h3 id="joinGame">Join Game</h3>
| Parameters  | POST /game/join |
| ------------- | ------------- |
| userName  | name of the player  |
| gameId  | game id |


```
{Status}
```

Join a game response contains the following states:
- SUCCESS: The player joinned the game with success
- GAME_NOT_FOUND: The game was not found
- GAME_FULL: The game is full
- GAME_WAS_NOT_ON_WAIT: The game was not waiting for more players (is running, solved or finished)

<h3 id="gameStatus">Game Status</h3>
A game can have 4 states:
- WAITING: the game is waiting for the "owner" to start the game
- MASTER_MINDING: the game is running
- SOLVED: a player solved the secret in the current round, the other players that didn't guess in the round still can try
- FINISHED: at least on player found the solution or the game hit the rounds limit

<h2 id="startGame">Start the Game</h2>

<h2 id="gameStatus">Find game status</h2>

<h2 id="playingTheGame">Playing the game</h2>
	