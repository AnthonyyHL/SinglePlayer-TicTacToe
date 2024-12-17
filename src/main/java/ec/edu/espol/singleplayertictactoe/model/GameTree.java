package ec.edu.espol.singleplayertictactoe.model;

import ec.edu.espol.singleplayertictactoe.constants.GameTurns;

import java.util.List;

public class GameTree {
    private GameTreeNode root;
    private Player humanTurn;
    private Player AITurn;

    public GameTree(char[][] initialBoard, char humanTurn) {
        this.humanTurn = new Player(humanTurn);
        this.AITurn = (this.humanTurn.getTurn() == GameTurns.X_TURNS) ?
                new Player(GameTurns.O_TURNS) :
                new Player(GameTurns.X_TURNS);
        this.root = new GameTreeNode(initialBoard, this.humanTurn, AITurn);
    }

    public GameTreeNode getRoot() {
        return root;
    }

    public Player getHumanTurn() {
        return humanTurn;
    }

    public Player getAITurn() {
        return AITurn;
    }

    public void buildTree() {
        buildTurnTree(this, 2);
    }

    public void buildTurnTree(GameTree tree, int depth) {
        GameTreeNode node = tree.getRoot();

        if (depth == 0 || node.isTerminalState()) {
            node.calculateUtility();
            return;
        }

        List<int[]> possibleMoves = node.getPossibleMoves();
        for (int[] move : possibleMoves) {
            char[][] board = node.copyBoard();
            board[move[0]][move[1]] = node.getPlayerTurn().getTurn();

            GameTree child = new GameTree(board,
                    node.getPlayerTurn().getTurn() == GameTurns.X_TURNS ? GameTurns.O_TURNS : GameTurns.X_TURNS);
            child.getRoot().setLastMove(move[0], move[1]);

            tree.getRoot().addChild(child);
            buildTurnTree(child, depth - 1);
        }

        if (node.getPlayerTurn() == getAITurn()) {
            int maxUtility = Integer.MIN_VALUE;
            for (GameTree child : node.getChildren()) {
                maxUtility = Math.max(maxUtility, child.getRoot().getUtility());
            }
            node.setUtility(maxUtility);
        } else {
            int minUtility = Integer.MAX_VALUE;
            for (GameTree child : node.getChildren()) {
                minUtility = Math.min(minUtility, child.getRoot().getUtility());
            }
            node.setUtility(minUtility);
        }
    }

    public int[] findBestMove() {
        buildTree();

        GameTreeNode bestChild = null;
        int maxUtility = Integer.MIN_VALUE;

        for (GameTree child : getRoot().getChildren()) {
            GameTreeNode childNode = child.getRoot();
            if (childNode.getUtility() > maxUtility) {
                maxUtility = childNode.getUtility();
                bestChild = childNode;
            }
        }

        return bestChild != null ? new int[]{bestChild.getRow(), bestChild.getCol()} : null;
    }
}
