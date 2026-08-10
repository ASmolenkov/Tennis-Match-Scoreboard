package io.github.asmolenkov.tennismatchscoreboard.model;

import io.github.asmolenkov.tennismatchscoreboard.exception.addPoint.AddPointMatchScoreException;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




@Getter
public class MatchScore {
    private static final int SETS_TO_WIN = 2;
    private static final String CANNOT_ADD_POINT = "Cannot add point: Match is already finished";

    private final List<SetScore> sets = new ArrayList<>();
    private SetScore currentSet;

    public MatchScore() {
        this.currentSet = new SetScore();
        this.sets.add(this.currentSet);
    }


    public void addPoint(PlayerSide playerSide) {
        if (isMatchFinished()) {
            throw new AddPointMatchScoreException(CANNOT_ADD_POINT);
        }

        currentSet.addPoint(playerSide);

        if(currentSet.isSetFinished()){
            handleSetFinished();
        }
    }


    public boolean isMatchFinished() {
        return getWinner().isPresent();
    }

    public Optional<PlayerSide> getWinner(){
        int playerOneSets = countWonGames(PlayerSide.ONE);
        int playerSecondSets = countWonGames(PlayerSide.TWO);

        if(playerOneSets >= SETS_TO_WIN){
            return Optional.of(PlayerSide.ONE);
        }
        if(playerSecondSets >= SETS_TO_WIN){
            return Optional.of(PlayerSide.TWO);
        }
        return Optional.empty();
    }

    private void handleSetFinished(){
        if(isMatchFinished()){
            return;
        }
        this.currentSet = new SetScore();
        this.sets.add(currentSet);
    }

    private int countWonGames(PlayerSide playerSide) {
        return (int) sets.stream().filter(set -> set.getWinner().orElse(null) == playerSide).count();
    }


}
