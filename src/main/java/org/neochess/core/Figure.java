
package org.neochess.core;

import java.util.HashMap;
import java.util.Map;

public enum Figure {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    private static Map<Figure, String> sanByFigure;
    private static Map<String, Figure> figureBySan;

    static {
        sanByFigure = new HashMap<>();
        sanByFigure.put(PAWN, "P");
        sanByFigure.put(KNIGHT, "N");
        sanByFigure.put(BISHOP, "B");
        sanByFigure.put(ROOK, "R");
        sanByFigure.put(QUEEN, "Q");
        sanByFigure.put(KING, "K");

        figureBySan = new HashMap<>();
        figureBySan.put("P", PAWN);
        figureBySan.put("N", KNIGHT);
        figureBySan.put("B", BISHOP);
        figureBySan.put("R", ROOK);
        figureBySan.put("Q", QUEEN);
        figureBySan.put("K", KING);
    }

    public String getSan() {
        return sanByFigure.get(this);
    }

    public static Figure fromSan (String san) {
        return figureBySan.get(san.toUpperCase());
    }
}
