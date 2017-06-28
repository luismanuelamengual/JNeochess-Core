
package org.neochess.core;

public enum File {
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H;

    private static final File[] files = values();

    public File getOffsetFile (int fileOffset) {
        return File.getOffsetFile(this, fileOffset);
    }

    public static File getOffsetFile (File file, int fileOffset) {
        int offsetOrdinal = file.ordinal() + fileOffset;
        return (offsetOrdinal >= 0 && offsetOrdinal <= 7)? files[offsetOrdinal] : null;
    }

    public String getSan() {
        return toString().toLowerCase();
    }

    public static File fromSan (String san) {
        return File.valueOf(san.toUpperCase());
    }
}
