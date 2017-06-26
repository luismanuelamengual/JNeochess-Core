
package org.neochess.core;

import java.util.EnumMap;

public class MoveHistorySlot {

    private Move move;
    private Piece movingPiece;
    private Piece capturedPiece;
    private EnumMap<Side, CastleRights> castleRights;
    private Square epSquare;
    private int halfMoveCounter;

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public void setMovingPiece(Piece movingPiece) {
        this.movingPiece = movingPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public EnumMap<Side, CastleRights> getCastleRights() {
        return castleRights;
    }

    public void setCastleRights(EnumMap<Side, CastleRights> castleRights) {
        this.castleRights = castleRights;
    }

    public Square getEpSquare() {
        return epSquare;
    }

    public void setEpSquare(Square epSquare) {
        this.epSquare = epSquare;
    }

    public int getHalfMoveCounter() {
        return halfMoveCounter;
    }

    public void setHalfMoveCounter(int halfMoveCounter) {
        this.halfMoveCounter = halfMoveCounter;
    }
}
