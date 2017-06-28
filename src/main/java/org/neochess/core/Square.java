
package org.neochess.core;

import static org.neochess.core.File.*;
import static org.neochess.core.Rank.*;

public enum Square {

    A1(A, ONE),
    B1(B, ONE),
    C1(C, ONE),
    D1(D, ONE),
    E1(E, ONE),
    F1(F, ONE),
    G1(G, ONE),
    H1(H, ONE),

    A2(A, TWO),
    B2(B, TWO),
    C2(C, TWO),
    D2(D, TWO),
    E2(E, TWO),
    F2(F, TWO),
    G2(G, TWO),
    H2(H, TWO),

    A3(A, THREE),
    B3(B, THREE),
    C3(C, THREE),
    D3(D, THREE),
    E3(E, THREE),
    F3(F, THREE),
    G3(G, THREE),
    H3(H, THREE),

    A4(A, FOUR),
    B4(B, FOUR),
    C4(C, FOUR),
    D4(D, FOUR),
    E4(E, FOUR),
    F4(F, FOUR),
    G4(G, FOUR),
    H4(H, FOUR),

    A5(A, FIVE),
    B5(B, FIVE),
    C5(C, FIVE),
    D5(D, FIVE),
    E5(E, FIVE),
    F5(F, FIVE),
    G5(G, FIVE),
    H5(H, FIVE),

    A6(A, SIX),
    B6(B, SIX),
    C6(C, SIX),
    D6(D, SIX),
    E6(E, SIX),
    F6(F, SIX),
    G6(G, SIX),
    H6(H, SIX),

    A7(A, SEVEN),
    B7(B, SEVEN),
    C7(C, SEVEN),
    D7(D, SEVEN),
    E7(E, SEVEN),
    F7(F, SEVEN),
    G7(G, SEVEN),
    H7(H, SEVEN),

    A8(A, EIGHT),
    B8(B, EIGHT),
    C8(C, EIGHT),
    D8(D, EIGHT),
    E8(E, EIGHT),
    F8(F, EIGHT),
    G8(G, EIGHT),
    H8(H, EIGHT);

    private static final Square[][] squareCache;

    static {
        squareCache = new Square[8][8];
        for (Square square : values()) {
            squareCache[square.getFile().ordinal()][square.getRank().ordinal()] = square;
        }
    }

    private final File file;
    private final Rank rank;

    Square(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    public File getFile() {
        return file;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isLight() {
        int ordinal = ordinal();
        int ordinalRank = ordinal / 8;
        return ordinal % 2 == ((ordinalRank % 2 == 1)? 0 : 1);
    }

    public boolean isDark() {
        return !isLight();
    }

    public Square getOffsetSquare(int fileOffset, int rankOffset) {
        return Square.getOffsetSquare(this, fileOffset, rankOffset);
    }

    public static Square getSquare(File file, Rank rank) {
        return squareCache[file.ordinal()][rank.ordinal()];
    }

    public static Square getOffsetSquare(Square square, int fileOffset, int rankOffset) {
        File offsetFile = square.getFile().getOffsetFile(fileOffset);
        Rank offsetRank = square.getRank().getOffsetRank(rankOffset);
        return (offsetFile != null && offsetRank != null)? getSquare(offsetFile, offsetRank) : null;
    }

    public String getSan() {
        return toString().toLowerCase();
    }

    public static Square fromSan (String san) {
        return Square.valueOf(san.toUpperCase());
    }
}
