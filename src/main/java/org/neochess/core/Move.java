
package org.neochess.core;

import java.util.Iterator;
import java.util.List;

import static org.neochess.core.Figure.*;
import static org.neochess.core.Piece.*;
import static org.neochess.core.Square.*;

public class Move {

    private final Square fromSquare;
    private final Square toSquare;
    private final Figure promotionFigure;

    protected Move(Square fromSquare, Square toSquare) {
        this(fromSquare, toSquare, null);
    }

    protected Move(Square fromSquare, Square toSquare, Figure promotionFigure) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.promotionFigure = promotionFigure;
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

    @Override
    public String toString() {
        return fromSquare.toString() + toSquare.toString();
    }

    public String toSAN(Board board) {
        Board cloneBoard = board.clone();
        boolean producesCheck = false;
        boolean producesCheckmate = false;
        Board backupCloneBoard = cloneBoard.clone();
        cloneBoard.makeMove(this);
        if (cloneBoard.inCheck()) {
            producesCheck = true;
            if (cloneBoard.getLegalMoves().isEmpty()) {
                producesCheckmate = true;
            }
        }
        cloneBoard.setFrom(backupCloneBoard);

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
                    sanBuilder.append(fromSquare.getFile().toString().toLowerCase());
                    sanBuilder.append('x');
                }
                sanBuilder.append(toSquare.toSAN());
                if (promotionFigure != null) {
                    sanBuilder.append("=");
                    switch (promotionFigure) {
                        case KING: sanBuilder.append('K'); break;
                        case QUEEN: sanBuilder.append('Q'); break;
                        case ROOK: sanBuilder.append('R'); break;
                        case BISHOP: sanBuilder.append('B'); break;
                        case KNIGHT: sanBuilder.append('N'); break;
                        case PAWN: sanBuilder.append('P'); break;
                    }
                }
            }
            else {
                switch (movingFigure) {
                    case KING: sanBuilder.append('K'); break;
                    case QUEEN: sanBuilder.append('Q'); break;
                    case ROOK: sanBuilder.append('R'); break;
                    case BISHOP: sanBuilder.append('B'); break;
                    case KNIGHT: sanBuilder.append('N'); break;
                    case PAWN: sanBuilder.append('P'); break;
                }
                List<Square> figureAttackingSquares = board.getAttackingSquares(toSquare, sideToMove);
                Iterator<Square> attackingSquaresIterator = figureAttackingSquares.iterator();
                while(attackingSquaresIterator.hasNext()) {
                    Square attackingSquare = attackingSquaresIterator.next();
                    if (!board.getPiece(attackingSquare).getFigure().equals(movingFigure)) {
                        attackingSquaresIterator.remove();
                    }
                }
                if (figureAttackingSquares.size() > 1) {
                    sanBuilder.append(fromSquare.getFile().toString().toLowerCase());
                    int fileAttakingFigures = 0;
                    for (Square square : figureAttackingSquares) {
                        if (square.getFile().equals(fromSquare.getFile())) {
                            fileAttakingFigures++;
                        }
                    }
                    if (fileAttakingFigures > 1) {
                        sanBuilder.append(fromSquare.getRank().ordinal() + 1);
                    }
                }

                if (capturedPiece != null) {
                    sanBuilder.append("x");
                }
                sanBuilder.append(toSquare.toSAN());
            }

            if (producesCheck) {
                sanBuilder.append(producesCheckmate? "#" : "+");
            }
        }
        return sanBuilder.toString();
    }
}
