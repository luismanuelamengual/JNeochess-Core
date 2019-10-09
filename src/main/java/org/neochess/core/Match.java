
package org.neochess.core;

import java.util.ArrayList;
import java.util.List;

public class Match {

    private final Board board;
    private final List<MatchHistorySlot> historySlots;

    public Match() {
        board = new Board();
        board.setInitialPosition();
        historySlots = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public int getMovesCount() {
        return historySlots.size();
    }

    public List<Move> getMoves() {
        List<Move> moves = new ArrayList<>();
        for (MatchHistorySlot historySlot : historySlots) {
            moves.add(historySlot.getMove());
        }
        return moves;
    }

    public Move getMove (int ply) {
        return historySlots.get(ply).getMove();
    }

    public Board getBoard (int ply) {
        Board board;
        if (ply >= historySlots.size()) {
            board = this.board;
        }
        else {
            board = historySlots.get(ply).getBoard();
        }
        return board;
    }

    public boolean makeMove(Square fromSquare, Square toSquare) {
        return makeMove(fromSquare, toSquare, null);
    }

    public boolean makeMove(Square fromSquare, Square toSquare, Figure promotionFigure) {
        Move move = null;
        List<Move> moves = board.getLegalMoves();
        for (Move testMove : moves) {
            if (testMove.getFromSquare().equals(fromSquare) && testMove.getToSquare().equals(toSquare)) {
                if (promotionFigure == null || promotionFigure == testMove.getPromotionFigure()) {
                    move = testMove;
                    break;
                }
            }
        }
        if (move != null) {
            Board moveBoard = board.clone();
            board.makeMove(move);
            this.historySlots.add(new MatchHistorySlot(moveBoard, move));
        }
        return move != null;
    }

    public boolean makeMove(String sanMove) {
        Move move = null;
        List<Move> moves = board.getLegalMoves();
        for (Move testMove : moves) {
            String testMoveSan = testMove.toSAN(board);
            if (sanMove.equals(testMoveSan)) {
                move = testMove;
                break;
            }
            else if (testMoveSan.endsWith("+") || testMoveSan.endsWith("#")) {
                testMoveSan = testMoveSan.substring(0, testMoveSan.length()-1);
                if (sanMove.equals(testMoveSan)) {
                    move = testMove;
                    break;
                }
            }
        }
        if (move != null) {
            Board moveBoard = board.clone();
            board.makeMove(move);
            this.historySlots.add(new MatchHistorySlot(moveBoard, move));
        }
        return move != null;
    }

    public void unmakeMove() {
        if (!historySlots.isEmpty()) {
            MatchHistorySlot historySlot = historySlots.remove(historySlots.size()-1);
            board.setFrom(historySlot.getBoard());
        }
    }
}
