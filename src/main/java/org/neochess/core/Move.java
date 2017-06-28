
package org.neochess.core;

import java.util.EnumMap;

public class Move {

    private Square fromSquare;
    private Square toSquare;

    private Piece promotionPiece;
    private Piece movingPiece;
    private Piece capturedPiece;
    private EnumMap<Side, CastleRights> castleRights;
    private Square enPassantSquare;
    private int halfMoveCounter;

    protected Move(Square fromSquare, Square toSquare) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    public Square getFromSquare() {
        return fromSquare;
    }

    public void setFromSquare(Square fromSquare) {
        this.fromSquare = fromSquare;
    }

    public Square getToSquare() {
        return toSquare;
    }

    public void setToSquare(Square toSquare) {
        this.toSquare = toSquare;
    }

    protected Piece getPromotionPiece() {
        return promotionPiece;
    }

    protected void setPromotionPiece(Piece promotionPiece) {
        this.promotionPiece = promotionPiece;
    }

    protected Piece getMovingPiece() {
        return movingPiece;
    }

    protected void setMovingPiece(Piece movingPiece) {
        this.movingPiece = movingPiece;
    }

    protected Piece getCapturedPiece() {
        return capturedPiece;
    }

    protected void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    protected EnumMap<Side, CastleRights> getCastleRights() {
        return castleRights;
    }

    protected void setCastleRights(EnumMap<Side, CastleRights> castleRights) {
        this.castleRights = castleRights;
    }

    protected Square getEnPassantSquare() {
        return enPassantSquare;
    }

    protected void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    protected int getHalfMoveCounter() {
        return halfMoveCounter;
    }

    protected void setHalfMoveCounter(int halfMoveCounter) {
        this.halfMoveCounter = halfMoveCounter;
    }

    @Override
    public String toString() {
        return fromSquare.toString() + toSquare.toString();
    }
}
