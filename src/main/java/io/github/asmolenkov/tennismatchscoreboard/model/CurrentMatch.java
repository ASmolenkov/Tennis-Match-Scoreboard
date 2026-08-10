package io.github.asmolenkov.tennismatchscoreboard.model;


import io.github.asmolenkov.tennismatchscoreboard.exception.addPoint.AddPointCurrentMatchException;
import lombok.*;

import java.util.Optional;
import java.util.UUID;

@Getter
public class CurrentMatch {
    private static final String CANNOT_ADD_POINT = "Cannot add point: Match is already finished";
    private UUID uuid;
    private final PlayerModel playerOne;
    private final PlayerModel playerSecond;
    private final MatchScore matchScore;


    public CurrentMatch(UUID matchUuid, PlayerModel playerOne, PlayerModel playerSecond) {
        this.uuid = matchUuid;
        this.playerOne = playerOne;
        this.playerSecond = playerSecond;
        this.matchScore = new MatchScore();
    }

    public CurrentMatch(PlayerModel playerOne, PlayerModel playerSecond) {
        this.playerOne = playerOne;
        this.playerSecond = playerSecond;
        this.matchScore = new MatchScore();
    }

    public void addPoint(PlayerSide playerSide){
        if(isMatchFinished()){
            throw new AddPointCurrentMatchException(CANNOT_ADD_POINT);
        }
        matchScore.addPoint(playerSide);

    }

    public boolean isMatchFinished(){
        return matchScore.isMatchFinished();
    }

    public Optional<PlayerModel> getWinner(){
        return matchScore.getWinner().map(winnerSide -> winnerSide == PlayerSide.ONE ? playerOne : playerSecond);
    }




}
