
package org.neochess.core;

import java.util.EnumMap;

import static org.neochess.core.Side.BLACK;
import static org.neochess.core.Side.WHITE;

public class Move {

    private final Square fromSquare;
    private final Square toSquare;
    private final String san;
    private final Piece promotionPiece;
    private final Piece movingPiece;
    private final Piece capturedPiece;
    private final EnumMap<Side, CastleRights> castleRights;
    private final Square enPassantSquare;
    private final int halfMoveCounter;

    protected Move(Board board, Square fromSquare, Square toSquare, boolean generateSan) {
        this(board, fromSquare, toSquare, null, generateSan);
    }

    protected Move(Board board, Square fromSquare, Square toSquare, Figure promotionFigure, boolean generateSan) {

        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        castleRights = new EnumMap<>(Side.class);
        castleRights.put(WHITE, board.getCastleRights(WHITE).clone());
        castleRights.put(BLACK, board.getCastleRights(BLACK).clone());
        promotionPiece = promotionFigure != null? Piece.getPiece(board.getSideToMove(), promotionFigure) : null;
        movingPiece = board.getPiece(fromSquare);
        capturedPiece = board.getPiece(toSquare);
        enPassantSquare = board.getEnPassantSquare();
        halfMoveCounter = board.getHalfMoveCounter();
        san = "";
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

    @Override
    public String toString() {
        return fromSquare.toString() + toSquare.toString();
    }
}
