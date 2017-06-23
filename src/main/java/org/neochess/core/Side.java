
package org.neochess.core;

public enum Side {
    WHITE,
    BLACK;

    public Side getOppositeSide() {
        return this == WHITE? BLACK : WHITE;
    }

    public static Side getOppositeSide (Side side) {
        return side == WHITE? BLACK : WHITE;
    }
}