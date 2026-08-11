package io.github.asmolenkov.tennismatchscoreboard.mapper;

import io.github.asmolenkov.tennismatchscoreboard.dto.PlayerDto;
import io.github.asmolenkov.tennismatchscoreboard.entity.Player;
import io.github.asmolenkov.tennismatchscoreboard.model.PlayerModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerMapper {
    public static PlayerDto toDto(Player entity){
        if(entity == null){
            return null;
        }
        return new PlayerDto(entity.getId(), entity.getName());
    }

    public static PlayerModel toModel(PlayerDto entity){
        if(entity == null){
            return null;
        }
        return new PlayerModel(entity.id(), entity.name());
    }
}
