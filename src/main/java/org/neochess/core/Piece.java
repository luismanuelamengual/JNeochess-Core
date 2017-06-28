
package org.neochess.core;

import static org.neochess.core.Figure.*;
import static org.neochess.core.Side.*;

public enum Piece {
    WHITE_PAWN(WHITE, PAWN),
    WHITE_KNIGHT(WHITE, KNIGHT),
    WHITE_BISHOP(WHITE, BISHOP),
    WHITE_ROOK(WHITE, ROOK),
    WHITE_QUEEN(WHITE, QUEEN),
    WHITE_KING(WHITE, KING),
    BLACK_PAWN(BLACK, PAWN),
    BLACK_KNIGHT(BLACK, KNIGHT),
    BLACK_BISHOP(BLACK, BISHOP),
    BLACK_ROOK(BLACK, ROOK),
    BLACK_QUEEN(BLACK, QUEEN),
    BLACK_KING(BLACK, KING);

    private static final Piece[][] piecesCache;

    static {
        piecesCache = new Piece[2][6];
        for (Piece piece : values()) {
            piecesCache[piece.getSide().ordinal()][piece.getFigure().ordinal()] = piece;
        }
    }

    private final Figure figure;
    private final Side side;

    Piece(Side side, Figure figure) {
        this.figure = figure;
        this.side = side;
    }

    public Figure getFigure() {
        return figure;
    }

    public Side getSide() {
        return side;
    }

    public static Piece getPiece(Side side, Figure figure) {
        return piecesCache[side.ordinal()][figure.ordinal()];
    }

    public String getSan() {
        String san = getFigure().getSan();
        if (getSide().equals(BLACK)) {
            san = san.toLowerCase();
        }
        return san;
    }

    public static Piece fromSan (String san) {
        Side sanSide = (san == san.toUpperCase())? WHITE : BLACK;
        Figure sanFigure = Figure.fromSan(san);
        return getPiece(sanSide, sanFigure);
    }
}

