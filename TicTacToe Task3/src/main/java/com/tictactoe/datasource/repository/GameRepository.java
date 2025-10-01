package com.tictactoe.datasource.repository;

import com.tictactoe.domain.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<Game, UUID> {
}
