package io.github.asmolenkov.tennismatchscoreboard.repository;

import io.github.asmolenkov.tennismatchscoreboard.exception.DeleteActiveMatchException;
import io.github.asmolenkov.tennismatchscoreboard.exception.SaveActiveMatchException;
import io.github.asmolenkov.tennismatchscoreboard.model.CurrentMatch;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class ActiveMatchRepository {

    private static final String LOG_MATCH_SAVE_TEMPLATE = "Match {} - {} saved";
    private static final String CURRENT_MATCH_NULL = "Current match must not be null";
    private static final String MATCH_UUID_NULL = "Match UUID must not be null";
    private static final String MATCH_DELETED_TEMPLATE = "Match with UUID - {} removed";
    private static final String MATCH_NOT_FOUND_UUID_NULL = "No match found, UUID = null";

    private final Map<UUID, CurrentMatch> activeMatches = new ConcurrentHashMap<>();

    public UUID save(CurrentMatch currentMatch){
        if (currentMatch == null) {
            throw new SaveActiveMatchException(CURRENT_MATCH_NULL);
        }
        UUID uuid = UUID.randomUUID();
        CurrentMatch saveCurrentMatch = new CurrentMatch(uuid,currentMatch.getPlayerOne(),currentMatch.getPlayerSecond());
        activeMatches.put(uuid,saveCurrentMatch);
        log.info(LOG_MATCH_SAVE_TEMPLATE,currentMatch.getPlayerOne().name(), currentMatch.getPlayerSecond().name());
        return uuid;
    }

    public Optional<CurrentMatch> find(UUID uuidActiveMatch){
        if(uuidActiveMatch == null){
            log.warn(MATCH_NOT_FOUND_UUID_NULL);
            return Optional.empty();
        }
        return Optional.ofNullable(activeMatches.get(uuidActiveMatch));
    }

    public void delete (UUID finishedMatch){
        if(finishedMatch == null){
            throw new DeleteActiveMatchException(MATCH_UUID_NULL);
        }
        activeMatches.remove(finishedMatch);
        log.info(MATCH_DELETED_TEMPLATE, finishedMatch);
    }

}
