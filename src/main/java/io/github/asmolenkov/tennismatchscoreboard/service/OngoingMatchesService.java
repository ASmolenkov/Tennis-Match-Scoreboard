package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.dto.MatchScoreDto;
import io.github.asmolenkov.tennismatchscoreboard.dto.PlayerDto;
import io.github.asmolenkov.tennismatchscoreboard.exception.FindMatchException;
import io.github.asmolenkov.tennismatchscoreboard.exception.PlayerSideException;
import io.github.asmolenkov.tennismatchscoreboard.mapper.MatchMapper;
import io.github.asmolenkov.tennismatchscoreboard.mapper.PlayerMapper;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;
import io.github.asmolenkov.tennismatchscoreboard.model.PlayerModel;
import io.github.asmolenkov.tennismatchscoreboard.model.PlayerSide;
import io.github.asmolenkov.tennismatchscoreboard.repository.ActiveMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class OngoingMatchesService implements OngoingMatches {
    private final ActiveMatchRepository activeMatchRepository;
    private final FinishedMatchesPersistence finishedMatchesPersistence;
    private final PlayerInterface playerInterface;


    @Override
    public UUID createMatch(String playerOne, String playerSecond) {
        PlayerDto playerDtoOne = playerInterface.createPlayer(playerOne);
        PlayerDto playerDtoSecond = playerInterface.createPlayer(playerSecond);
        PlayerModel playerOneModel = PlayerMapper.toModel(playerDtoOne);
        PlayerModel playerSecondModel = PlayerMapper.toModel(playerDtoSecond);
        CurrentMatch currentMatch = new CurrentMatch(playerOneModel, playerSecondModel);
        log.info("Матч {} - {} - сохранен", playerDtoOne.name(), playerDtoSecond.name());
        return activeMatchRepository.save(currentMatch);
    }

    @Override
    public Optional<CurrentMatch> findMatchByUuid(UUID uuid) {
        return activeMatchRepository.find(uuid);
    }
    @Override
    public synchronized void addPoint(UUID uuid, long playerId) {
        CurrentMatch currentMatch = findMatchByUuidOrThrow(uuid);
        PlayerSide playerSide = resolvePlayerSide(currentMatch,uuid,playerId);

        currentMatch.addPoint(playerSide);

        if(currentMatch.isMatchFinished()){
            endMatch(currentMatch, uuid);
        }
    }
    @Override
    public MatchScoreDto getMatchScore(UUID matchUuid){
        Optional<CurrentMatch> activeMatch = activeMatchRepository.find(matchUuid);

        return activeMatch.map(MatchMapper::toMatchScoreDto)
                          .orElseGet(() -> MatchScoreDto.finishedPlaceholder(matchUuid));


    }

    private CurrentMatch findMatchByUuidOrThrow(UUID matchUuid) {
        return activeMatchRepository.find(matchUuid)
                                    .orElseThrow(() -> new FindMatchException(
                                            "Матч с UUID %s не найден".formatted(matchUuid)));
    }



    private PlayerSide resolvePlayerSide(CurrentMatch currentMatch,UUID matchUuid,long playerId) {
        if (currentMatch.getPlayerOne().id() == playerId) {
            return PlayerSide.ONE;
        }
        if (currentMatch.getPlayerSecond().id() == playerId){
            return PlayerSide.TWO;
        }
        throw new PlayerSideException("Игрок с ID %d не участвует в матче %s".formatted(playerId,matchUuid));
    }

    private void endMatch(CurrentMatch match, UUID matchUuid){
        log.info("Матч {} завершён. Победитель: {}", match, match.getWinner().orElse(null));
        finishedMatchesPersistence.saveMatch(match);

        activeMatchRepository.delete(matchUuid);
    }

}
