/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.singleplayertictactoe.model;

import ec.edu.espol.singleplayertictactoe.constants.GameTurns;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author samir
 */
/**
 * Representa un nodo en el árbol de decisión del juego.
 * Cada nodo contiene un estado del tablero y la información de los jugadores.
 */
public class GameTreeNode {
    private final char[][] board;
    private final Player playerTurn;
    private final Player opponentTurn;
    private final List<GameTree> children;
    private int[] lastMove;
    private int utility;

    
    public GameTreeNode(char[][] board, Player playerTurn, Player opponentTurn) {
        this.board = copyBoard(board);
        this.playerTurn = playerTurn;
        this.opponentTurn = opponentTurn;
        this.children = new ArrayList<>();
        this.lastMove = new int[]{-1, -1};
        this.utility = 0;
    }

    public char[][] copyBoard() {
        return copyBoard(board);
    }

    /**
     * Crea una copia profunda del tablero
     */
    private char[][] copyBoard(char[][] original) {
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 3);
        }
        return copy;
    }

    /**
     * Obtiene los movimientos posibles desde este estado
     */
    public List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }

    /**
     * Verifica si este nodo es un estado terminal
     */
    public boolean isTerminalState() {
        return isWinningState(playerTurn.getTurn()) || 
               isWinningState(opponentTurn.getTurn()) || 
               isBoardFull();
    }

   
    private boolean isWinningState(char symbol) {
        // Verificar filas y columnas
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) {
                return true;
            }
        }
        
        // Verificar diagonales
        return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
               (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }

    /**
     * Verifica si el tablero está lleno
     */
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // Getters y setters necesarios
    public char[][] getBoard() {
        return board;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public Player getOpponentTurn() {
        return opponentTurn;
    }

    public List<GameTree> getChildren() {
        return children;
    }

    public void addChild(GameTree child) {
        children.add(child);
    }

    public void setLastMove(int row, int col) {
        this.lastMove = new int[]{row, col};
    }

    public int[] getLastMove() {
        return lastMove;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    /**
     * Calcula la utilidad de este nodo basado en el estado del juego
     */
    public void calculateUtility() {
        int playerPossibilities = calculatePossibilities(playerTurn);
        int opponentPossibilities = calculatePossibilities(opponentTurn);

        this.utility = playerPossibilities - opponentPossibilities;
    }

    public int calculatePossibilities(Player player) {
        char opponentTurn = (player.getTurn() == GameTurns.X_TURNS) ?
                GameTurns.O_TURNS :
                GameTurns.X_TURNS;
        int possibilities = 0;

        // Checking Rows
        for (int i = 0; i < 3; i++) {
            if (
                    board[i][0] != opponentTurn &&
                            board[i][0] != board[i][1] &&
                            board[i][0] != board[i][2]
            ) { possibilities++; }
        }

        // Checking Columns
        for (int j = 0; j < 3; j++) {
            if (
                    board[0][j] != opponentTurn &&
                            board[0][j] != board[1][j] &&
                            board[0][j] != board[2][j]
            ) { possibilities++; }
        }

        // Checking Straight Diagonal
        for (int j = 0; j < 3; j++) {
            if (
                    board[0][0] != opponentTurn &&
                            board[0][0] != board[1][1] &&
                            board[0][0] != board[2][2]
            ) { possibilities++; }
        }

        // Checking Inverse Diagonal
        for (int j = 0; j < 3; j++) {
            if (
                    board[0][2] != opponentTurn &&
                            board[0][2] != board[1][1] &&
                            board[0][2] != board[2][0]
            ) { possibilities++; }
        }

        return possibilities;
    }

    /**
     * Restaura el árbol a un estado específico
     */
    public void restoreChildren(char[][] newBoard) {
        this.children.clear();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(newBoard[i], 0, this.board[i], 0, 3);
        }
    }
}