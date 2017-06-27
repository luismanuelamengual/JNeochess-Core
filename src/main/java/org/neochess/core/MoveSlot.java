
package org.neochess.core;

import java.util.EnumMap;

import static org.neochess.core.Side.BLACK;
import static org.neochess.core.Side.WHITE;

public class MoveSlot extends Move {

    private Piece movingPiece;
    private Piece capturedPiece;
    private EnumMap<Side, CastleRights> castleRights;
    private Square enPassantSquare;
    private int halfMoveCounter;

    public MoveSlot(Square fromSquare, Square toSquare) {
        super(fromSquare, toSquare);
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

    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    public void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public int getHalfMoveCounter() {
        return halfMoveCounter;
    }

    public void setHalfMoveCounter(int halfMoveCounter) {
        this.halfMoveCounter = halfMoveCounter;
    }
}
