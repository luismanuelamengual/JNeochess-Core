
package org.neochess.core;

import java.util.EnumMap;

import static org.neochess.core.Figure.*;
import static org.neochess.core.Piece.*;
import static org.neochess.core.Side.*;
import static org.neochess.core.Square.*;

public class Move {

    private Square fromSquare;
    private Square toSquare;
    private String san;
    private boolean isLegal;

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

    public Square getToSquare() {
        return toSquare;
    }

    public String getSan() {
        return san;
    }

    public boolean isLegal() {
        return isLegal;
    }

    protected Piece getPromotionPiece() {
        return promotionPiece;
    }

    protected Piece getMovingPiece() {
        return movingPiece;
    }

    protected Piece getCapturedPiece() {
        return capturedPiece;
    }

    protected EnumMap<Side, CastleRights> getCastleRights() {
        return castleRights;
    }

    protected Square getEnPassantSquare() {
        return enPassantSquare;
    }

    protected int getHalfMoveCounter() {
        return halfMoveCounter;
    }

    protected void setFromSquare(Square fromSquare) {
        this.fromSquare = fromSquare;
    }

    protected void setToSquare(Square toSquare) {
        this.toSquare = toSquare;
    }

    protected void setSan(String san) {
        this.san = san;
    }

    protected void setPromotionPiece(Piece promotionPiece) {
        this.promotionPiece = promotionPiece;
    }

    protected void setMovingPiece(Piece movingPiece) {
        this.movingPiece = movingPiece;
    }

    protected void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    protected void setCastleRights(EnumMap<Side, CastleRights> castleRights) {
        this.castleRights = castleRights;
    }

    protected void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    protected void setHalfMoveCounter(int halfMoveCounter) {
        this.halfMoveCounter = halfMoveCounter;
    }

    protected void setLegal(boolean legal) {
        isLegal = legal;
    }

    @Override
    public String toString() {
        return fromSquare.toString() + toSquare.toString();
    }
}
