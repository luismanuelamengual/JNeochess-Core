package org.neochess.core.searchengine;

public class BitBoard {

    public static long NULLBITBOARD = 0x0000000000000000L;
    public static long[] squareBit;
    public static long[] squareBitX;
    public static long[] squareBit90;
    public static long[] squareBit45;
    public static long[] squareBit315;
    public static long[] squareBitX90;
    public static long[] squareBitX45;
    public static long[] squareBitX315;

    private static int[] lzArray;
    private static int[] bitCount;
    private static int r90[] = {
        56,  48, 40, 32, 24, 16, 8,  0,
        57,  49, 41, 33, 25, 17, 9,  1,
        58,  50, 42, 34, 26, 18, 10, 2,
        59,  51, 43, 35, 27, 19, 11, 3,
        60,  52, 44, 36, 28, 20, 12, 4,
        61,  53, 45, 37, 29, 21, 13, 5,
        62,  54, 46, 38, 30, 22, 14, 6,
        63,  55, 47, 39, 31, 23, 15, 7
    };

    private static int r45[] = {
        28, 21, 15, 10,  6,  3,  1,  0,
        36, 29, 22, 16, 11,  7,  4,  2,
        43, 37, 30, 23, 17, 12,  8,  5,
        49, 44, 38, 31, 24, 18, 13,  9,
        54, 50, 45, 39, 32, 25, 19, 14,
        58, 55, 51, 46, 40, 33, 26, 20,
        61, 59, 56, 52, 47, 41, 34, 27,
        63, 62, 60, 57, 53, 48, 42, 35
    };

    private static int r315[] = {
         0,  2,  5,  9, 14, 20, 27, 35,
         1,  4,  8, 13, 19, 26, 34, 42,
         3,  7, 12, 18, 25, 33, 41, 48,
         6, 11, 17, 24, 32, 40, 47, 53,
        10, 16, 23, 31, 39, 46, 52, 57,
        15, 22, 30, 38, 45, 51, 56, 60,
        21, 29, 37, 44, 50, 55, 59, 62,
        28, 36, 43, 49, 54, 58, 61, 63
    };

    public static int getLeastSignificantBit (long bitboard)
    {
        if ((bitboard >>> 48) > 0) {
            return lzArray[(int) (bitboard >>> 48)];
        }
        if ((bitboard >>> 32) > 0) {
            return lzArray[(int) (bitboard >>> 32)] + 16;
        }
        if ((bitboard >>> 16) > 0) {
            return lzArray[(int) (bitboard >>> 16)] + 32;
        }
        return lzArray[(int)(bitboard)] + 48;
    }

    public static int getMostSignificantBit (long bitboard)
    {
        return getLeastSignificantBit(bitboard & ~ ((bitboard)&(bitboard-1)));
    }

    public static int getBitCount (long bitboard)
    {
        return bitCount[(int)(bitboard>>>48)] + bitCount[(int)((bitboard>>>32) & 0xffff)] + bitCount[(int)((bitboard>>>16) & 0xffff)] + bitCount[(int)(bitboard & 0xffff)];
    }

    private static void initSquareBitArrays ()
    {
        squareBit = new long[64];
        squareBitX = new long[64];
        squareBit90 = new long[64];
        squareBit45 = new long[64];
        squareBit315 = new long[64];
        squareBitX90 = new long[64];
        squareBitX45 = new long[64];
        squareBitX315 = new long[64];
        long b = (long)1;
        for (int i = 63; i >= 0; i--, b <<= 1)
        {
            squareBit[i] = b;
            squareBitX[i] = ~b;
        }
        for (int i = 0; i < 64; i++)
        {
            squareBit90[i] = squareBit[r90[i]];
            squareBit45[i] = squareBit[r45[i]];
            squareBit315[i] = squareBit[r315[i]];
            squareBitX90[i] = squareBitX[r90[i]];
            squareBitX45[i] = squareBitX[r45[i]];
            squareBitX315[i] = squareBitX[r315[i]];
        }
    }

    private static void initLzArray ()
    {
        lzArray = new int[65536];
        int i, j, s, n;
        s = n = 1;
        for (i = 0; i < 16; i++)
        {
            for (j = s; j < s + n; j++)
                lzArray[j] = 16 - 1 - i;
            s += n;
            n += n;
        }
    }

    private static void initBitCount ()
    {
        bitCount = new int[65536];
        int i, j, n;
        bitCount[0] = 0;
        bitCount[1] = 1;
        i = 1;
        for (n = 2; n <= 16; n++)
        {
            i <<= 1;
            for (j = i; j <= i + (i-1); j++)
                bitCount[j] = 1 + bitCount[j - i];
        }
    }

    static {
        initSquareBitArrays ();
        initLzArray ();
        initBitCount ();
    }
}
