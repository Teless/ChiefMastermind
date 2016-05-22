<h1>ChiefMastermind</h1>
<b>Summary</b>
- <a href="#creatingGame">Creating a game</a>
  - <a href="#quickGame">Quick Game</a>
  - <a href="#quickMultiplayerGame">Quick Multiplayer Game</a>
  - <a href="#joinGame">Join Game</a>
  - <a href="#gameStatus">Game Status</a>
- <a href="#startGame">Start the Game</a>
- <a href="#gameStatus">Find game status</a>
- <a href="#playingTheGame">Playing the game</a>
  - <a href="#guessStatus">Guess Status</a>
  - <a href="#secrectCode">Secret Code</a>
- <a href="#install">Install</a>
  - <a href="#dbConfig">DB Configuration</a>

This an Mastermind API implentation in Java. Don't know how the game works? Check the <a href="https://en.wikipedia.org/wiki/Mastermind_(board_game)#Gameplay_and_rules">Rules<a/>

<h2 id="creatingGame">Creating a game</h2>

This API allows you to create a game with the configurations of your choice:
- Limit of players
- Limit of rounds 
- Number of positions
- Secret size
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
| secretSize  | secret size  |
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

Join a game method's response contains the following states:
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

| Parameters  | POST /game/start |
| ------------- | ------------- |
| gameKey  | key of the game |
| gameId  | game id |


```
{Status}
```

Join a game response contains the following states:
- SUCCESS: The player joinned the game with success
- GAME_NOT_FOUND: The game was not found
- WRONG_GAME_KEY: The game was found, but gameKey is wrong
- GAME_WAS_NOT_ON_WAIT: The game was not waiting for more players (is running, solved or finished)


<h2 id="gameStatus">Find game status</h2>

| Parameters  | POST /game/status |
| ------------- | ------------- |
| gameId  | game id  |

```
{
  "id": "{for each game is generated a unique id}",
  "gameKey": "{for each game is generate a key (visible by the creator of the game) used to start the game}",
  "status": "{status}",
  "playersLimit": {playersLimit},
  "playersCount": {count of players in the game},
  "roundsLimit": {roundsLimit},
  "round": {current round of the game},
  "roundGuesses": {numbers of guesses in the round},
  "positions": {positions},
  "players": [list of players name]
}
```

<h2 id="playingTheGame">Playing the game</h2>
| Parameters  | POST /game/guess |
| ------------- | ------------- |
| code  | guess code  |
| userName  | user name  |
| gameId  | game id  |

```
{
  "near": {number of nears},
  "exact": {number of exact maches},
  "code": "{submited guess code}",
  "status": "{status}"
}
```

<h3 id="guessStatus">Guess status</h3>
A guess can have the following status:
- VALID_GUESS: the player made a valid guess but didn't solve the secret
- WAITING_OTHER_PLAYERS_GUESSES: the player has to wait for the other players guesses of the round
- NOT_IN_THE_GAME: the player is not in the game
- INVALID_GUESS: the guess contains invalid characters or don't have the size of the secret
- GAME_IS_WAITING_FOR_MORE: the game was not started wet
- GAME_HAS_ENDED: the game has ended
- SOLVED: the guess solved the secret

<h3 id="secrectCode">Secret Code</h3>

The secret code is generated base on the "secretSize" parameter (if a quick game is created the secretSize and "numbers of color" will be set to 8), a random String is created with the size of the "secretSize" parameter filled with caracter '0' -> 'secreSize' (exclusive), ex:

- secretSize = 4
- output = "1032"

The number can repeat inside the secret

<h2 id="install">Install</h2>
To run the server you need:
- JDK8 installed
- Apache tomcat 7.x
- MongoDB 3.x

To run the server deploy the war file "chiefmastermind-1.0.war" in your tomcat server

<h3 id="dbConfig">DB Configuration</h3>
The API uses a mongoDB server, by default the applications connects to "localhost:27017" and creates the db "ChiefMastermind". If you need to change the connection you can edit the file "src/main/resources/development.properties" and change the configuration

* The system was set up to run in two possible configurations: development and production, if you want to change the system state you need to edit the file "src/main/webapp/WEB-INF/web.xml" and change the property "br.com.caelum.vraptor.environment" to production. Doing that the system will load the production.properties instead of development.properties

