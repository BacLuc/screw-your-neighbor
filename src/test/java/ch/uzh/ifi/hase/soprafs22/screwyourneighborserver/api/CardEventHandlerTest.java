package ch.uzh.ifi.hase.soprafs22.screwyourneighborserver.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.uzh.ifi.hase.soprafs22.screwyourneighborserver.entity.*;
import ch.uzh.ifi.hase.soprafs22.screwyourneighborserver.repository.*;
import ch.uzh.ifi.hase.soprafs22.screwyourneighborserver.sideeffects.CardEventHandler;
import ch.uzh.ifi.hase.soprafs22.screwyourneighborserver.sideeffects.ModelFactory;
import ch.uzh.ifi.hase.soprafs22.screwyourneighborserver.util.ClearDBAfterTestListener;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(
    value = {ClearDBAfterTestListener.class},
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class CardEventHandlerTest {

  @Autowired private RoundRepository roundRepository;
  @Autowired private MatchRepository matchRepository;
  @Autowired private CardRepository cardRepository;
  @Autowired private GameRepository gameRepository;
  @Autowired private HandRepository handRepository;
  @Autowired private ParticipationRepository participationRepository;

  private Participation participation1;
  private Participation participation2;
  private Participation participation3;
  private Game game;
  private Match match;
  private Round round;
  private Card card1;
  private Card card2;
  private Card card3;
  private CardEventHandler cardEventHandler;

  @BeforeEach
  void setup() {
    // todo: rework with GameBuilder
    participation1 = new Participation();
    participation2 = new Participation();
    participation3 = new Participation();
    game = new Game();
    match = new Match();
    round = new Round();
    CardRank cardRank = CardRank.ACE;
    CardSuit cardSuit = CardSuit.DIAMOND;
    card1 = new Card(cardRank, cardSuit);
    cardRank = CardRank.EIGHT;
    cardSuit = CardSuit.DIAMOND;
    card2 = new Card(cardRank, cardSuit);
    cardRank = CardRank.EIGHT;
    cardSuit = CardSuit.HEART;
    card3 = new Card(cardRank, cardSuit);
    cardEventHandler =
        new CardEventHandler(new ModelFactory(), gameRepository);
    matchRepository.save(match);
    round.setMatch(match);
    round.setRoundNumber(1);
    roundRepository.save(round);

    participation1.setParticipationNumber(1);
    participation1.setGame(game);
    game.getParticipations().add(participation1);
    participation2.setParticipationNumber(2);
    participation2.setGame(game);
    game.getParticipations().add(participation2);
    participation3.setParticipationNumber(3);
    participation3.setGame(game);
    game.getParticipations().add(participation3);
    match.setGame(game);
    round.setMatch(match);
    round.setRoundNumber(1);
  }

  /*

  @Test
  void play_first_card_no_new_round() {
    card1.setRound(round);
    cardRepository.save(card1);
    // there's a random second card, with no round set (i.e. not played)
    cardRepository.save(card2);
    cardEventHandler.handleAfterSave(card1);
    Collection<Round> savedRounds = roundRepository.findAll();
    assertEquals(1, savedRounds.size());
    assertTrue(savedRounds.stream().anyMatch(r -> r.getRoundNumber() == 1));
    assertFalse(savedRounds.stream().anyMatch(r -> r.getRoundNumber() == 2));
  }

  @Test
  void play_not_last_card_no_new_round() {
    // two cards are already played
    card1.setRound(round);
    cardRepository.save(card1);
    card2.setRound(round);
    cardRepository.save(card2);
    // there's a random third card, with no round set (i.e. not played)
    cardRepository.save(card3);
    cardEventHandler.handleAfterSave(card2);
    Collection<Round> savedRounds = roundRepository.findAll();
    assertEquals(1, savedRounds.size());
    assertTrue(savedRounds.stream().anyMatch(r -> r.getRoundNumber() == 1));
    assertFalse(savedRounds.stream().anyMatch(r -> r.getRoundNumber() == 2));
  }

  @Test
  void play_last_card_new_round() {
    // the cards of all three players are already assigned to the round (i.e. has been played)
    card1.setRound(round);
    cardRepository.save(card1);
    card2.setRound(round);
    cardRepository.save(card2);
    card3.setRound(round);
    cardRepository.save(card3);
    cardEventHandler.handleAfterSave(card3);
    Collection<Round> savedRounds = roundRepository.findAll();
    assertEquals(2, savedRounds.size());
    assertTrue(savedRounds.stream().anyMatch(r -> r.getRoundNumber() == 1));
    assertTrue(savedRounds.stream().anyMatch(r -> r.getRoundNumber() == 2));
  }

   */
}
