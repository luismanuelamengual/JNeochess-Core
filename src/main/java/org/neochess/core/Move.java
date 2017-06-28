
package org.neochess.core;

import java.util.EnumMap;

import static org.neochess.core.Figure.*;
import static org.neochess.core.Piece.*;
import static org.neochess.core.Side.*;
import static org.neochess.core.Square.*;

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
    private final boolean isLegal;

    protected Move(Board board, Square fromSquare, Square toSquare) {
        this(board, fromSquare, toSquare, null);
    }

    protected Move(Board board, Square fromSquare, Square toSquare, Figure promotionFigure) {

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

        boolean producesCheck = false;
        boolean producesCheckmate = false;
        Side currentSideToMove = board.getSideToMove();
        board.makeMove(this);
        isLegal = !board.isKingSquareAttacked(currentSideToMove);
        if (board.inCheck()) {
            producesCheck = true;
            if (board.isCheckMate()) {
                producesCheckmate = true;
            }
        }
        board.unmakeMove(this);

        StringBuilder sanBuilder = new StringBuilder();
        if ((movingPiece == WHITE_KING && fromSquare == E1 && toSquare == G1) || (movingPiece == BLACK_KING && fromSquare == E8 && toSquare == G8)) {
            sanBuilder.append("O-O");
        }
        else if ((movingPiece == WHITE_KING && fromSquare == E1 && toSquare == C1) || (movingPiece == BLACK_KING && fromSquare == E8 && toSquare == C8)) {
            sanBuilder.append("O-O-O");
        }
        else {
            Figure movingFigure = movingPiece.getFigure();
            if (movingFigure == PAWN) {
                if (capturedPiece != null || toSquare == enPassantSquare) {
                    sanBuilder.append(fromSquare.getFile().getSan());
                    sanBuilder.append('x');
                }
                sanBuilder.append(toSquare.getSan());
                if (promotionPiece != null) {
                    sanBuilder.append("=");
                    sanBuilder.append(promotionPiece.getFigure().getSan());
                }
            }
            else {
                sanBuilder.append(movingFigure.getSan());
                if (capturedPiece != null) {
                    sanBuilder.append("x");
                }
                sanBuilder.append(toSquare.getSan());
            }

            if (producesCheck) {
                sanBuilder.append(producesCheckmate? "#" : "+");
            }

        }
        san = sanBuilder.toString();
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

    protected boolean isLegal() {
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

    @Override
    public String toString() {
        return fromSquare.toString() + toSquare.toString();
    }
}
