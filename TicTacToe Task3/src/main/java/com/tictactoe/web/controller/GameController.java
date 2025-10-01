package com.tictactoe.web.controller;
import com.tictactoe.domain.model.Game;
import com.tictactoe.domain.service.GameService;
import com.tictactoe.datasource.repository.GameRepository;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameRepository repository;
    private final GameService gameService;

    public GameController(GameRepository repo, GameService service) {
        this.repository = repo;
        this.gameService = service;
    }

    @PostMapping
    public Game createGame() {
        Game game = new Game();
        return repository.save(game);
    }

    @PostMapping("/{gameId}")
    public Game makeMove(@PathVariable UUID gameId, @RequestBody MoveRequest request) {
        Game game = repository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        if (!"a game is underway".equals(game.status)) {
            throw new RuntimeException("Game finished");
        }

        // ПОМЕНЯЙ ЭТИ СТРОКИ:
        int[][] board = game.getBoard(); // получаем доску
        if (board[request.row][request.col] != 0) {
            throw new RuntimeException("Cell occupied");
        }
        board[request.row][request.col] = 1; // X
        game.setBoard(board); // сохраняем доску обратно

        gameService.checkWinner(game);

        if ("a game is underway".equals(game.status)) {
            gameService.makeAIMove(game);
        }

        return repository.save(game);
    }

    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable UUID gameId) {
        return repository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public static class MoveRequest {
        public int row;
        public int col;
    }
}
