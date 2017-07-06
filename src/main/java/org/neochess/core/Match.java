
package org.neochess.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Match {

    private final Board board;
    private final List<Move> moves;

    public Match() {
        board = new Board();
        board.setInitialPosition();
        moves = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public int getPly() {
        return moves.size();
    }

    public List<Move> getHistoryMoves() {
        return Collections.unmodifiableList(moves);
    }

    public Move getHistoryMove (int ply) {
        return moves.get(ply);
    }

    public Board getHistoryBoard (int ply) {

        Board board;
        if (ply >= moves.size()) {
            board = this.board;
        }
        else {
            board = moves.get(ply).getBoard();
        }
        return board;
    }

    public Move makeMove(Square fromSquare, Square toSquare) {
        return makeMove(fromSquare, toSquare, null);
    }

    public Move makeMove(Square fromSquare, Square toSquare, Figure promotionFigure) {
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
            board.makeMove(move);
            this.moves.add(move);
        }
        return move;
    }

    public Move makeMove(String sanMove) {
        Move move = null;
        List<Move> moves = board.getLegalMoves();
        for (Move testMove : moves) {
            String testMoveSan = testMove.getSan();
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
            board.makeMove(move);
            this.moves.add(move);
        }
        return move;
    }

    public Move unmakeMove() {
        Move move = null;
        if (!moves.isEmpty()) {
            move = moves.remove(moves.size()-1);
            board.unmakeMove(move);
        }
        return move;
    }
}
