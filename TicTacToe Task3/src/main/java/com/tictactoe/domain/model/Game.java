package com.tictactoe.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    @Column(name = "board_state", length = 9)
    public String boardState = "000000000"; // 3x3 как строка

    public String status = "a game is underway";
    public String currentPlayer = "X";

    public Game() {
        this.id = UUID.randomUUID();
    }

    // Методы для работы с доской
    public int[][] getBoard() {
        int[][] board = new int[3][3];
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = Character.getNumericValue(boardState.charAt(i));
        }
        return board;
    }

    public void setBoard(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]);
            }
        }
        this.boardState = sb.toString();
    }
}
