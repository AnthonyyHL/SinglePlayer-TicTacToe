/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.singleplayertictactoe.model;

import ec.edu.espol.singleplayertictactoe.constants.GameTurns;
import java.util.List;

/**
 *
 * @author samir
 */

/**
 * Implementa el árbol de decisión para el algoritmo minimax del juego TicTacToe.
 * Maneja la lógica de la IA para encontrar el mejor movimiento posible.
 */
public class GameTree {
    // Constantes para la evaluación del tablero
    private static final int VICTORIA_IA = 10;
    private static final int VICTORIA_HUMANO = -10;
    private static final int EMPATE = 0;
    private static final int PROFUNDIDAD_MAXIMA = 6;
    
    private final GameTreeNode root;

   
    public GameTree(char[][] initialBoard, char humanTurn) {
        Player humanPlayer = new Player(humanTurn);
        Player AIPlayer = new Player(humanTurn == GameTurns.X_TURNS ? 
                GameTurns.O_TURNS : GameTurns.X_TURNS);
        this.root = new GameTreeNode(initialBoard, humanPlayer, AIPlayer);
    }

    /**
     * Encuentra el mejor movimiento posible para la IA
     * @return coordenadas [fila, columna] del mejor movimiento
     */
    public int[] findBestMove() {
        int[] bestMove = {-1, -1};
        int bestScore = Integer.MIN_VALUE;
        char[][] board = root.getBoard();
        
        // Verificar victoria inmediata
        int[] winningMove = encontrarMovimientoGanador(board, root.getPlayerTurn().getTurn());
        if (winningMove != null) {
            return winningMove;
        }
        
        // Verificar necesidad de bloqueo
        int[] blockingMove = encontrarMovimientoGanador(board, root.getOpponentTurn().getTurn());
        if (blockingMove != null) {
            return blockingMove;
        }
        
        // Aplicar minimax para encontrar el mejor movimiento
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = root.getPlayerTurn().getTurn();
                    int score = minimax(board, 0, false);
                    board[i][j] = ' ';
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        
        return bestMove;
    }

    /**
     * Implementación del algoritmo minimax con poda alfa-beta
     */
    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        // Verificar estados terminales
        int puntaje = evaluarTablero(board);
        if (puntaje != 0) return puntaje;
        if (isBoardFull(board)) return EMPATE;
        if (depth >= PROFUNDIDAD_MAXIMA) return evaluarPosicion(board);
        
        if (isMaximizing) {
            return maximizar(board, depth);
        } else {
            return minimizar(board, depth);
        }
    }

    
    private int maximizar(char[][] board, int depth) {
        int maxEval = Integer.MIN_VALUE;
        for (int[] move : getPossibleMoves(board)) {
            board[move[0]][move[1]] = root.getPlayerTurn().getTurn();
            int eval = minimax(board, depth + 1, false);
            board[move[0]][move[1]] = ' ';
            maxEval = Math.max(maxEval, eval);
        }
        return maxEval;
    }

   
    private int minimizar(char[][] board, int depth) {
        int minEval = Integer.MAX_VALUE;
        for (int[] move : getPossibleMoves(board)) {
            board[move[0]][move[1]] = root.getOpponentTurn().getTurn();
            int eval = minimax(board, depth + 1, true);
            board[move[0]][move[1]] = ' ';
            minEval = Math.min(minEval, eval);
        }
        return minEval;
    }

    private int evaluarTablero(char[][] board) {
        if (checkWinningMove(board, root.getPlayerTurn().getTurn())) {
            return VICTORIA_IA;
        }
        if (checkWinningMove(board, root.getOpponentTurn().getTurn())) {
            return VICTORIA_HUMANO;
        }
        return 0;
    }

    private int evaluarPosicion(char[][] board) {
        int score = 0;
        // Evaluar centro
        if (board[1][1] == root.getPlayerTurn().getTurn()) score += 3;
        // Evaluar esquinas
        score += evaluarEsquinas(board);
        return score;
    }

    
    private int evaluarEsquinas(char[][] board) {
        int score = 0;
        int[][] esquinas = {{0,0}, {0,2}, {2,0}, {2,2}};
        for (int[] esquina : esquinas) {
            if (board[esquina[0]][esquina[1]] == root.getPlayerTurn().getTurn()) {
                score += 2;
            }
        }
        return score;
    }

    
    private List<int[]> getPossibleMoves(char[][] board) {
        java.util.List<int[]> moves = new java.util.ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }

    
    private int[] encontrarMovimientoGanador(char[][] board, char player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = player;
                    if (checkWinningMove(board, player)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }
        return null;
    }

    
    private boolean checkWinningMove(char[][] board, char player) {
        // Verificar filas
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        
        // Verificar columnas
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        
        // Verificar diagonales
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}