
package org.neochess.core;

public enum Piece {
    WHITE_PAWN(Side.WHITE, Figure.PAWN),
    WHITE_KNIGHT(Side.WHITE, Figure.KNIGHT),
    WHITE_BISHOP(Side.WHITE, Figure.BISHOP),
    WHITE_ROOK(Side.WHITE, Figure.ROOK),
    WHITE_QUEEN(Side.WHITE, Figure.QUEEN),
    WHITE_KING(Side.WHITE, Figure.KING),
    BLACK_PAWN(Side.BLACK, Figure.PAWN),
    BLACK_KNIGHT(Side.BLACK, Figure.KNIGHT),
    BLACK_BISHOP(Side.BLACK, Figure.BISHOP),
    BLACK_ROOK(Side.BLACK, Figure.ROOK),
    BLACK_QUEEN(Side.BLACK, Figure.QUEEN),
    BLACK_KING(Side.BLACK, Figure.KING);

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
}

