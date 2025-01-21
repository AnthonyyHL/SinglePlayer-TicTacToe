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
    private static final int VICTORIA_IA = 1;
    private static final int VICTORIA_HUMANO = -1;
    private static final int EMPATE = 0;
    private static final int MAX = 9;
    
    private final GameTreeNode root;

   
    public GameTree(char[][] initialBoard, char humanTurn) {
        Player humanPlayer = new Player(humanTurn);
        Player AIPlayer = new Player(humanTurn == GameTurns.X_TURNS ? 
                GameTurns.O_TURNS : GameTurns.X_TURNS);
        this.root = new GameTreeNode(initialBoard, humanPlayer, AIPlayer);
    }

    public GameTreeNode getRoot() {
        return root;
    }

    /**
     * Construye el árbol de decisiones a partir del nodo raíz.
     */
//    private void buildTree(GameTreeNode node) {
//        if (node.isTerminalState()) {
//            node.calculateUtility(); // Calcular la utilidad en el estado terminal
//            return;
//        }
//
//        // Obtener movimientos posibles desde el estado actual
//        List<int[]> possibleMoves = node.getPossibleMoves();
//        for (int[] move : possibleMoves) {
//            // Crear un nuevo nodo para el siguiente estado
//            char[][] newBoard = node.copyBoard();
//            newBoard[move[0]][move[1]] = node.getPlayerTurn().getTurn(); // Realizar el movimiento
//            GameTree childTreeNode = new GameTree(newBoard, node.getOpponentTurn().getTurn());
//            childTreeNode.getRoot().setLastMove(move[0], move[1]);
//            node.addChild(childTreeNode); // Añadir el nodo hijo
//            buildTree(childTreeNode.getRoot()); // Llamar recursivamente para construir el árbol desde el nodo hijo
//        }
//    }
    
    private void buildTree(GameTreeNode node, int depth, boolean isMaximizing) {
        if (isTerminalNode(node) || depth >= MAX) {
            // Primera verificación de las condiciones ganadoras
            if (checkWin(node.getBoard(), node.getPlayerTurn().getTurn())) {
                node.setUtility(VICTORIA_IA);
                return;
            }
            if (checkWin(node.getBoard(), node.getOpponentTurn().getTurn())) {
                node.setUtility(VICTORIA_HUMANO);
                return;
            }
            if (isBoardFull(node.getBoard())) {
                node.setUtility(EMPATE);
                return;
            }

            // Si no es terminal por victoria o empate, calcula en función de las posibilidades
            int playerPossibilities = node.calculatePossibilities(node.getPlayerTurn());
            int opponentPossibilities = node.calculatePossibilities(node.getOpponentTurn());
            
            // Heavily weight positions that block opponent wins
            int utility = (playerPossibilities * 2) - opponentPossibilities;
            
            // Posiciones de mucho peso que bloquean las victorias del oponente
            utility += evaluatePosition(node.getBoard(), node.getPlayerTurn().getTurn());
            
            node.setUtility(utility);
            return;
        }

        List<int[]> moves = node.getPossibleMoves();
        int bestValue = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int[] move : moves) {
            GameTreeNode child = createChildNode(node, move, isMaximizing);
            buildTree(child, depth + 1, !isMaximizing);
            
            if (isMaximizing) {
                bestValue = Math.max(bestValue, child.getUtility());
            } else {
                bestValue = Math.min(bestValue, child.getUtility());
            }
        }
        
        node.setUtility(bestValue);
    }
    private GameTreeNode createChildNode(GameTreeNode parent, int[] move, boolean isMaximizing) {
        char[][] newBoard = parent.copyBoard();
        newBoard[move[0]][move[1]] = isMaximizing ? 
            parent.getPlayerTurn().getTurn() : 
            parent.getOpponentTurn().getTurn();
        
        GameTreeNode child = new GameTreeNode(
            newBoard, 
            parent.getPlayerTurn(), 
            parent.getOpponentTurn()
        );
        child.setLastMove(move[0], move[1]);
        return child;
    }
    private int evaluatePosition(char[][] board, char aiSymbol) {
        int score = 0;
        
        // El control central es crucial
        if (board[1][1] == aiSymbol) {
            score += 30;
        }
        
        // Las esquinas también son importantes
        if (board[0][0] == aiSymbol) score += 15;
        if (board[0][2] == aiSymbol) score += 15;
        if (board[2][0] == aiSymbol) score += 15;
        if (board[2][2] == aiSymbol) score += 15;
        
        return score;
    }

    public int[] findBestMove() {
        buildTree(root, 0, true);
        List<int[]> moves = root.getPossibleMoves();
        int[] bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        // Prioriza los movimientos ganadores
        for (int[] move : moves) {
            char[][] newBoard = root.copyBoard();
            newBoard[move[0]][move[1]] = root.getPlayerTurn().getTurn();
            
            // Comprueba si este movimiento conduce a la victoria inmediata
            if (checkWin(newBoard, root.getPlayerTurn().getTurn())) {
                return move;
            }
            
            GameTreeNode child = new GameTreeNode(newBoard, root.getPlayerTurn(), root.getOpponentTurn());
            buildTree(child, 1, false);
            
            if (child.getUtility() > bestValue) {
                bestValue = child.getUtility();
                bestMove = move;
            }
        }

        return bestMove;
    }
    
    private boolean isTerminalNode(GameTreeNode node) {
        return checkWin(node.getBoard(), node.getPlayerTurn().getTurn()) ||
               checkWin(node.getBoard(), node.getOpponentTurn().getTurn()) ||
               isBoardFull(node.getBoard());
    }


    /**
     * Encuentra el mejor movimiento posible para la IA
     * @return coordenadas [fila, columna] del mejor movimiento
     */
//    public int[] findBestMove() {
//        // Aquí puedes implementar la lógica para seleccionar el mejor movimiento
//        // utilizando el árbol de decisiones construido.
//        return selectBestMove(root);
//    }
     

//    public int[] selectBestMove(GameTreeNode root) {
//        buildTree(root);
//
//        int[] winningMove = encontrarMovimientoGanador(root.getBoard(), root.getPlayerTurn().getTurn());
//        if (winningMove != null) {
//            return winningMove;
//        }
//
//        // Verificar necesidad de bloqueo
//        int[] blockingMove = encontrarMovimientoGanador(root.getBoard(), root.getOpponentTurn().getTurn());
//        if (blockingMove != null) {
//            return blockingMove;
//        }
//
//        GameTreeNode bestChild = null;
//        int maxUtility = Integer.MIN_VALUE;
//
//        for (GameTree child : getRoot().getChildren()) {
//            GameTreeNode childNode = child.getRoot();
//            if (childNode.getUtility() > maxUtility) {
//                maxUtility = childNode.getUtility();
//                bestChild = childNode;
//            }
//        }
//
//        return bestChild != null ? bestChild.getLastMove() : null;
//    }

    /**
//     * Implementación del algoritmo minimax con poda alfa-beta
//     */
//    private int minimax(char[][] board, int depth, boolean isMaximizing) {
//        // Verificar estados terminales
//        int puntaje = evaluarTablero(board);
//        if (puntaje != 0) return puntaje;
//        if (isBoardFull(board)) return EMPATE;
//        if (depth >= PROFUNDIDAD_MAXIMA) return evaluarPosicion(board);
//        
//        if (isMaximizing) {
//            return maximizar(board, depth);
//        } else {
//            return minimizar(board, depth);
//        }
//    }
//
//    
//    private int maximizar(char[][] board, int depth) {
//        int maxEval = Integer.MIN_VALUE;
//        for (int[] move : getPossibleMoves(board)) {
//            board[move[0]][move[1]] = root.getPlayerTurn().getTurn();
//            int eval = minimax(board, depth + 1, false);
//            board[move[0]][move[1]] = ' ';
//            maxEval = Math.max(maxEval, eval);
//        }
//        return maxEval;
//    }
//
//   
//    private int minimizar(char[][] board, int depth) {
//        int minEval = Integer.MAX_VALUE;
//        for (int[] move : getPossibleMoves(board)) {
//            board[move[0]][move[1]] = root.getOpponentTurn().getTurn();
//            int eval = minimax(board, depth + 1, true);
//            board[move[0]][move[1]] = ' ';
//            minEval = Math.min(minEval, eval);
//        }
//        return minEval;
//    }
//
//    private int evaluarTablero(char[][] board) {
//        if (checkWinningMove(board, root.getPlayerTurn().getTurn())) {
//            return VICTORIA_IA;
//        }
//        if (checkWinningMove(board, root.getOpponentTurn().getTurn())) {
//            return VICTORIA_HUMANO;
//        }
//        return 0;
//    }
//
//    private int evaluarPosicion(char[][] board) {
//        int score = 0;
//        // Evaluar centro
//        if (board[1][1] == root.getPlayerTurn().getTurn()) score += 3;
//        // Evaluar esquinas
//        score += evaluarEsquinas(board);
//        return score;
//    }
//
//    
//    private int evaluarEsquinas(char[][] board) {
//        int score = 0;
//        int[][] esquinas = {{0,0}, {0,2}, {2,0}, {2,2}};
//        for (int[] esquina : esquinas) {
//            if (board[esquina[0]][esquina[1]] == root.getPlayerTurn().getTurn()) {
//                score += 2;
//            }
//        }
//        return score;
//    }
//
//    
//    private List<int[]> getPossibleMoves(char[][] board) {
//        List<int[]> moves = new java.util.ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (board[i][j] == ' ') {
//                    moves.add(new int[]{i, j});
//                }
//            }
//        }
//        return moves;
//    }
//
//    
//    private int[] encontrarMovimientoGanador(char[][] board, char player) {
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (board[i][j] == ' ') {
//                    board[i][j] = player;
//                    if (checkWinningMove(board, player)) {
//                        board[i][j] = ' ';
//                        return new int[]{i, j};
//                    }
//                    board[i][j] = ' ';
//                }
//            }
//        }
//        return null;
//    }
//
//    
//    private boolean checkWinningMove(char[][] board, char player) {
//        // Verificar filas
//        for (int i = 0; i < 3; i++) {
//            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
//                return true;
//            }
//        }
//        
//        // Verificar columnas
//        for (int j = 0; j < 3; j++) {
//            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
//                return true;
//            }
//        }
//        
//        // Verificar diagonales
//        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
//               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
//    }
    private boolean checkWin(char[][] board, char symbol) {
            // Verifica filas y columnas
            for (int i = 0; i < 3; i++) {
                if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                    (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) {
                    return true;
                }
            }

            // Verifica diagonales
            return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                   (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
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
