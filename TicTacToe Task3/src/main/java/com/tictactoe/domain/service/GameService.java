package com.tictactoe.domain.service;
import com.tictactoe.domain.model.Game;
import com.tictactoe.domain.model.Move;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class GameService {

    public Game makeAIMove(Game game) {
        int[][] board = game.getBoard();
        Move bestMove = findBestMove(board);
        if (bestMove != null) {
            board[bestMove.row][bestMove.col] = 2;
            game.setBoard(board);
            game.currentPlayer = "X";
            checkWinner(game);
        }
        return game;
    }

    private Move findBestMove(int[][] board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = 2;
                    int score = minimax(board, 0, false);
                    board[i][j] = 0;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(i, j);
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing) {
        int score = evaluateBoard(board);

        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (!hasEmptyCells(board)) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = 2;
                        int currentScore = minimax(board, depth + 1, false);
                        board[i][j] = 0;
                        bestScore = Math.max(bestScore, currentScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = 1;
                        int currentScore = minimax(board, depth + 1, true);
                        board[i][j] = 0;
                        bestScore = Math.min(bestScore, currentScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private int evaluateBoard(int[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == 2 && board[i][1] == 2 && board[i][2] == 2) return 10;
            if (board[0][i] == 2 && board[1][i] == 2 && board[2][i] == 2) return 10;
        }
        if (board[0][0] == 2 && board[1][1] == 2 && board[2][2] == 2) return 10;
        if (board[0][2] == 2 && board[1][1] == 2 && board[2][0] == 2) return 10;

        for (int i = 0; i < 3; i++) {
            if (board[i][0] == 1 && board[i][1] == 1 && board[i][2] == 1) return -10;
            if (board[0][i] == 1 && board[1][i] == 1 && board[2][i] == 1) return -10;
        }
        if (board[0][0] == 1 && board[1][1] == 1 && board[2][2] == 1) return -10;
        if (board[0][2] == 1 && board[1][1] == 1 && board[2][0] == 1) return -10;

        return 0;
    }

    private boolean hasEmptyCells(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) return true;
            }
        }
        return false;
    }

    public void checkWinner(Game game) {
        int[][] b = game.getBoard();
        for (int i = 0; i < 3; i++) {
            if (b[i][0] != 0 && b[i][0] == b[i][1] && b[i][1] == b[i][2]) {
                game.status = b[i][0] == 1 ? "X-Won!" : "O-Won!";
                return;
            }
            if (b[0][i] != 0 && b[0][i] == b[1][i] && b[1][i] == b[2][i]) {
                game.status = b[0][i] == 1 ? "X-Won!" : "O-Won!";
                return;
            }
        }
        if (b[0][0] != 0 && b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
            game.status = b[0][0] == 1 ? "X-Won!" : "O-Won!";
            return;
        }
        if (b[0][2] != 0 && b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
            game.status = b[0][2] == 1 ? "X-Won!" : "O-Won!";
            return;
        }
        boolean hasEmpty = false;
        for (int[] row : b) for (int cell : row) if (cell == 0) hasEmpty = true;
        if (!hasEmpty) game.status = "Draw";
        game.setBoard(b);
    }
}
