
package org.neochess.core;

import java.util.Iterator;
import java.util.List;

import static org.neochess.core.Figure.*;
import static org.neochess.core.Piece.*;
import static org.neochess.core.Square.*;

public class Move {

    private final Board board;
    private final Square fromSquare;
    private final Square toSquare;
    private final Figure promotionFigure;
    private final boolean isLegal;
    private final String san;

    protected Move(Board board, Square fromSquare, Square toSquare) {
        this(board, fromSquare, toSquare, null);
    }

    protected Move(Board board, Square fromSquare, Square toSquare, Figure promotionFigure) {

        this.board = board.clone();
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.promotionFigure = promotionFigure;

        boolean isLegal = false;
        boolean producesCheck = false;
        boolean producesCheckmate = false;
        Side currentSideToMove = board.getSideToMove();
        board.makeMove(this);
        isLegal = !board.isKingSquareAttacked(currentSideToMove);
        if (board.inCheck()) {
            producesCheck = true;
            if (board.getLegalMoves().isEmpty()) {
                producesCheckmate = true;
            }
        }
        board.unmakeMove(this);

        Piece movingPiece = board.getPiece(fromSquare);
        Piece capturedPiece = board.getPiece(toSquare);
        Square enPassantSquare = board.getEnPassantSquare();
        Side sideToMove = board.getSideToMove();

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
                if (promotionFigure != null) {
                    sanBuilder.append("=");
                    sanBuilder.append(promotionFigure.getSan());
                }
            }
            else {
                sanBuilder.append(movingFigure.getSan());
                List<Square> figureAttackingSquares = board.getAttackingSquares(toSquare, sideToMove);
                Iterator<Square> attackingSquaresIterator = figureAttackingSquares.iterator();
                while(attackingSquaresIterator.hasNext()) {
                    Square attackingSquare = attackingSquaresIterator.next();
                    if (!board.getPiece(attackingSquare).getFigure().equals(movingFigure)) {
                        attackingSquaresIterator.remove();
                    }
                }
                if (figureAttackingSquares.size() > 1) {
                    sanBuilder.append(fromSquare.getFile().getSan());
                    int fileAttakingFigures = 0;
                    for (Square square : figureAttackingSquares) {
                        if (square.getFile().equals(fromSquare.getFile())) {
                            fileAttakingFigures++;
                        }
                    }
                    if (fileAttakingFigures > 1) {
                        sanBuilder.append(fromSquare.getRank().getSan());
                    }
                }

                if (capturedPiece != null) {
                    sanBuilder.append("x");
                }
                sanBuilder.append(toSquare.getSan());
            }

            if (producesCheck) {
                sanBuilder.append(producesCheckmate? "#" : "+");
            }
        }
        this.isLegal = isLegal;
        this.san = sanBuilder.toString();
    }

    public Square getFromSquare() {
        return fromSquare;
    }

    public Square getToSquare() {
        return toSquare;
    }

    public Figure getPromotionFigure() {
        return promotionFigure;
    }

    public String getSan() {
        return san;
    }

    protected boolean isLegal() {
        return isLegal;
    }

    protected Board getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return fromSquare.toString() + toSquare.toString();
    }
}
