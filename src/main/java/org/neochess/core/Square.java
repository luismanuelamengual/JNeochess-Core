
package org.neochess.core;

public enum Square {

    A1(File.A, Rank.ONE),
    B1(File.B, Rank.ONE),
    C1(File.C, Rank.ONE),
    D1(File.D, Rank.ONE),
    E1(File.E, Rank.ONE),
    F1(File.F, Rank.ONE),
    G1(File.G, Rank.ONE),
    H1(File.H, Rank.ONE),

    A2(File.A, Rank.TWO),
    B2(File.B, Rank.TWO),
    C2(File.C, Rank.TWO),
    D2(File.D, Rank.TWO),
    E2(File.E, Rank.TWO),
    F2(File.F, Rank.TWO),
    G2(File.G, Rank.TWO),
    H2(File.H, Rank.TWO),

    A3(File.A, Rank.THREE),
    B3(File.B, Rank.THREE),
    C3(File.C, Rank.THREE),
    D3(File.D, Rank.THREE),
    E3(File.E, Rank.THREE),
    F3(File.F, Rank.THREE),
    G3(File.G, Rank.THREE),
    H3(File.H, Rank.THREE),

    A4(File.A, Rank.FOUR),
    B4(File.B, Rank.FOUR),
    C4(File.C, Rank.FOUR),
    D4(File.D, Rank.FOUR),
    E4(File.E, Rank.FOUR),
    F4(File.F, Rank.FOUR),
    G4(File.G, Rank.FOUR),
    H4(File.H, Rank.FOUR),

    A5(File.A, Rank.FIVE),
    B5(File.B, Rank.FIVE),
    C5(File.C, Rank.FIVE),
    D5(File.D, Rank.FIVE),
    E5(File.E, Rank.FIVE),
    F5(File.F, Rank.FIVE),
    G5(File.G, Rank.FIVE),
    H5(File.H, Rank.FIVE),

    A6(File.A, Rank.SIX),
    B6(File.B, Rank.SIX),
    C6(File.C, Rank.SIX),
    D6(File.D, Rank.SIX),
    E6(File.E, Rank.SIX),
    F6(File.F, Rank.SIX),
    G6(File.G, Rank.SIX),
    H6(File.H, Rank.SIX),

    A7(File.A, Rank.SEVEN),
    B7(File.B, Rank.SEVEN),
    C7(File.C, Rank.SEVEN),
    D7(File.D, Rank.SEVEN),
    E7(File.E, Rank.SEVEN),
    F7(File.F, Rank.SEVEN),
    G7(File.G, Rank.SEVEN),
    H7(File.H, Rank.SEVEN),

    A8(File.A, Rank.EIGHT),
    B8(File.B, Rank.EIGHT),
    C8(File.C, Rank.EIGHT),
    D8(File.D, Rank.EIGHT),
    E8(File.E, Rank.EIGHT),
    F8(File.F, Rank.EIGHT),
    G8(File.G, Rank.EIGHT),
    H8(File.H, Rank.EIGHT);

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
}
