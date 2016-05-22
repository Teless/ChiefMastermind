package com.business;

import com.business.dao.GameDaoImpl;
import com.business.dao.PlayerDaoImpl;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.Player;
import com.exception.InvalidGameConfigurationException;
import com.mock.DatastoreFactoryMock;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mongodb.morphia.Datastore;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(CdiRunner.class)
@AdditionalClasses({PlayerDaoImpl.class, GameDaoImpl.class, DatastoreFactoryMock.class})
public class GameCreatorTest {

    @Mock
    @Produces
    private SecretGenerator secretGeneratorMock;

    @Mock
    @Produces
    private GameKeyGenerator gameKeyGeneratorMock;

    @Inject
    private GameCreator gameCreator;

    @Inject
    private Datastore datastore;

    @Before
    public void setUp() throws Exception {
        datastore.delete(datastore.createQuery(Game.class));
        datastore.delete(datastore.createQuery(Player.class));
    }

    @Test
    public void testCreateQuickGame() throws Exception {
        when(gameKeyGeneratorMock.generateGamekey()).thenReturn("gameKey");
        when(secretGeneratorMock.newSecret(Game.DEFAULT_COLORS_COUNT, Game.DEFAULT_SECRET_SIZE))
                .thenReturn("secret");

        Game game = gameCreator.createQuickGame("Rafael");

        Player expectedPlayer = new Player("Rafael");
        expectedPlayer.setId(game.getPlayers().get(0).getId());
        expectedPlayer.setGame(game);

        Game expectedFinalGame = new Game();
        expectedFinalGame.setId(game.getId());
        expectedFinalGame.setGameKey("gameKey");
        expectedFinalGame.setPlayersLimit(1);
        expectedFinalGame.setRoundsLimit(Game.DEFAULT_ROUNDS_LIMIT);
        expectedFinalGame.setPositions(Game.DEFAULT_POSITIONS);
        expectedFinalGame.setStatus(GameStatus.MASTER_MINDING);
        expectedFinalGame.setPlayersCount(1);
        expectedFinalGame.setSecret("secret");
        expectedFinalGame.setPlayers(Collections.singletonList(expectedPlayer));

        assertNotNull(game.getId());
        assertEquals(expectedFinalGame, game);
    }

    @Test
    public void testCreateQuickMultiplayerGame() throws Exception {
        when(gameKeyGeneratorMock.generateGamekey()).thenReturn("gameKey");
        when(secretGeneratorMock.newSecret(Game.DEFAULT_COLORS_COUNT, Game.DEFAULT_SECRET_SIZE))
                .thenReturn("secret");

        Game game = gameCreator.createQuickMultiplayerGame("Rafael");

        Player expectedPlayer = new Player("Rafael");
        expectedPlayer.setId(game.getPlayers().get(0).getId());
        expectedPlayer.setGame(game);

        Game expectedFinalGame = new Game();
        expectedFinalGame.setId(game.getId());
        expectedFinalGame.setGameKey("gameKey");
        expectedFinalGame.setPlayersLimit(Game.DEFAULT_MULTI_PLAYER_LIMIT);
        expectedFinalGame.setRoundsLimit(Game.DEFAULT_ROUNDS_LIMIT);
        expectedFinalGame.setPositions(Game.DEFAULT_POSITIONS);
        expectedFinalGame.setStatus(GameStatus.WAITING);
        expectedFinalGame.setPlayersCount(1);
        expectedFinalGame.setSecret("secret");
        expectedFinalGame.setPlayers(Collections.singletonList(expectedPlayer));

        assertNotNull(game.getId());
        assertEquals(expectedFinalGame, game);
    }

    @Test
    public void testCreateNewGame() throws Exception {
        when(gameKeyGeneratorMock.generateGamekey()).thenReturn("gameKey");
        when(secretGeneratorMock.newSecret(11, 9)).thenReturn("secret");

        Game game = gameCreator.createNewGame("Rafael", 3, 5, 7, 9, 11, GameStatus.WAITING);

        Player expectedPlayer = new Player("Rafael");
        expectedPlayer.setId(game.getPlayers().get(0).getId());
        expectedPlayer.setGame(game);

        Game expectedFinalGame = new Game();
        expectedFinalGame.setId(game.getId());
        expectedFinalGame.setGameKey("gameKey");
        expectedFinalGame.setPlayersLimit(3);
        expectedFinalGame.setRoundsLimit(5);
        expectedFinalGame.setPositions(7);
        expectedFinalGame.setStatus(GameStatus.WAITING);
        expectedFinalGame.setPlayersCount(1);
        expectedFinalGame.setSecret("secret");
        expectedFinalGame.setPlayers(Collections.singletonList(expectedPlayer));

        assertNotNull(game.getId());
        assertEquals(expectedFinalGame, game);
    }

    @Test
    public void testCreateNewGameValidRunning() throws Exception {
        when(gameKeyGeneratorMock.generateGamekey()).thenReturn("gameKey");
        when(secretGeneratorMock.newSecret(11, 9)).thenReturn("secret");

        Game game = gameCreator.createNewGame("Rafael", 1, 5, 7, 9, 11, GameStatus.MASTER_MINDING);

        Player expectedPlayer = new Player("Rafael");
        expectedPlayer.setId(game.getPlayers().get(0).getId());
        expectedPlayer.setGame(game);

        Game expectedFinalGame = new Game();
        expectedFinalGame.setId(game.getId());
        expectedFinalGame.setGameKey("gameKey");
        expectedFinalGame.setPlayersLimit(1);
        expectedFinalGame.setRoundsLimit(5);
        expectedFinalGame.setPositions(7);
        expectedFinalGame.setStatus(GameStatus.MASTER_MINDING);
        expectedFinalGame.setPlayersCount(1);
        expectedFinalGame.setSecret("secret");
        expectedFinalGame.setPlayers(Collections.singletonList(expectedPlayer));

        assertNotNull(game.getId());
        assertEquals(expectedFinalGame, game);
    }

    @Test(expected = InvalidGameConfigurationException.class)
    public void testCreateNewGameInvalidPlayerLimit() throws Exception {
        try {
            gameCreator.createNewGame("Rafael", 0, 5, 7, 9, 11, GameStatus.WAITING);
        } catch (InvalidGameConfigurationException ex) {
            Map<String, String> expected = Collections.singletonMap("playersLimit", "0");
            assertEquals(expected, ex.getInvalidParameters());

            throw ex;
        }
    }

    @Test(expected = InvalidGameConfigurationException.class)
    public void testCreateNewGameInvalidRoundsLimit() throws Exception {
        try {
            gameCreator.createNewGame("Rafael", 3, 0, 7, 9, 11, GameStatus.WAITING);
        } catch (InvalidGameConfigurationException ex) {
            Map<String, String> expected = Collections.singletonMap("roundsLimit", "0");
            assertEquals(expected, ex.getInvalidParameters());

            throw ex;
        }
    }

    @Test(expected = InvalidGameConfigurationException.class)
    public void testCreateNewGameInvalidPositions() throws Exception {
        try {
            gameCreator.createNewGame("Rafael", 3, 5, 0, 9, 11, GameStatus.WAITING);
        } catch (InvalidGameConfigurationException ex) {
            Map<String, String> expected = Collections.singletonMap("positions", "0");
            assertEquals(expected, ex.getInvalidParameters());

            throw ex;
        }
    }

    @Test(expected = InvalidGameConfigurationException.class)
    public void testCreateNewGameInvalidSecretSize() throws Exception {
        try {
            gameCreator.createNewGame("Rafael", 3, 5, 7, 0, 11, GameStatus.WAITING);
        } catch (InvalidGameConfigurationException ex) {
            Map<String, String> expected = Collections.singletonMap("secretSize", "0");
            assertEquals(expected, ex.getInvalidParameters());

            throw ex;
        }
    }

    @Test(expected = InvalidGameConfigurationException.class)
    public void testCreateNewGameInvalidColorsCount() throws Exception {
        try {
            gameCreator.createNewGame("Rafael", 3, 5, 7, 9, 0, GameStatus.WAITING);
        } catch (InvalidGameConfigurationException ex) {
            Map<String, String> expected = Collections.singletonMap("colorsCount", "0");
            assertEquals(expected, ex.getInvalidParameters());

            throw ex;
        }
    }

    @Test(expected = InvalidGameConfigurationException.class)
    public void testCreateNewGameInvalidColorsInvalidStatusRunning() throws Exception {
        try {
            gameCreator.createNewGame("Rafael", 3, 5, 7, 9, 11, GameStatus.MASTER_MINDING);
        } catch (InvalidGameConfigurationException ex) {
            Map<String, String> expected = Collections.singletonMap("status", "MASTER_MINDING");
            assertEquals(expected, ex.getInvalidParameters());

            throw ex;
        }
    }

    @Test(expected = InvalidGameConfigurationException.class)
    public void testCreateNewGameInvalidColorsInvalidStatus() throws Exception {
        try {
            gameCreator.createNewGame("Rafael", 3, 5, 7, 9, 11, GameStatus.FINISHED);
        } catch (InvalidGameConfigurationException ex) {
            Map<String, String> expected = Collections.singletonMap("status", "FINISHED");
            assertEquals(expected, ex.getInvalidParameters());

            throw ex;
        }
    }

}