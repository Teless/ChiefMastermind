<h1>ChiefMastermind</h1>
<b>Summary</b>
- <a href="#creatingGame">Creating a game</a>
  - <a href="#quickGame">Quick Game</a>
  - <a href="#quickMultiplayerGame">Quick Multiplayer Game</a>
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



<h2 id="playingTheGame">Playing the game</h2>
	
