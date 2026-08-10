package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.dto.PlayerDto;
import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.exception.PlayerCreationException;
import io.github.asmolenkov.tennismatchscoreboard.mapper.PlayerMapper;
import io.github.asmolenkov.tennismatchscoreboard.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PlayerService implements PlayerInterface {

    private static final String LOG_PLAYER_EXISTS_TEMPLATE = "Player {} already exists in the database!";
    private static final String LOG_PLAYER_SAVE_TEMPLATE = "Player {} is saved in the database!";
    private static final String ERROR_SAVE_PLAYER_TEMPLATE = "Failed to create player %s";

    private final PlayerRepository playerRepository;
    private final TransactionManager transactionManager;


    public PlayerDto createPlayer(String name)  {
        try{
            return transactionManager.executeInTransaction(() -> {
                Optional<Player> existingPlayer = playerRepository.findByName(name);
                if(existingPlayer.isPresent()){
                    log.info(LOG_PLAYER_EXISTS_TEMPLATE, name);
                    return PlayerMapper.toDto(existingPlayer.get());
                }
                Player newPlayer = new Player(name);
                playerRepository.save(newPlayer);
                log.info(LOG_PLAYER_SAVE_TEMPLATE, name);

                return PlayerMapper.toDto(newPlayer);
            });
        }catch (Exception e){
            throw new PlayerCreationException(ERROR_SAVE_PLAYER_TEMPLATE.formatted(name), e);
        }
    }
}
