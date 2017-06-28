
package org.neochess.core;

public enum Rank {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT;

    private static final Rank[] ranks = values();

    public Rank getOffsetRank (int rankOffset) {
        return Rank.getOffsetRank(this, rankOffset);
    }

    public static Rank getOffsetRank (Rank rank, int rankOffset) {
        int offsetOrdinal = rank.ordinal() + rankOffset;
        return (offsetOrdinal >= 0 && offsetOrdinal <= 7)? ranks[offsetOrdinal] : null;
    }

    public String getSan() {
        return String.valueOf(ordinal() + 1);
    }

    public static Rank fromSan (String san) {
        return Rank.values()[Integer.parseInt(san)-1];
    }
}
