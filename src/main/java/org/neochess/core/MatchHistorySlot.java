package org.neochess.core;

public class MatchHistorySlot {

    private final Board board;
    private final Move move;

    public MatchHistorySlot(Board board, Move move) {
        this.board = board;
        this.move = move;
    }

    public Board getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }
}
