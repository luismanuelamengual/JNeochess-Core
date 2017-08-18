
package org.neochess.core.searchengine;

public class BoardUtils {

    public static long NULLBITBOARD = 0x0000000000000000L;
    public static long[] squareBit;
    public static long[] squareBitX;
    public static long[] squareBit90;
    public static long[] squareBit45;
    public static long[] squareBit315;
    public static long[] squareBitX90;
    public static long[] squareBitX45;
    public static long[] squareBitX315;
    public static long[] rankBits;
    public static long[] fileBits;
    public static long[] stoneWall;
    public static long[] rings;
    public static long[] boxes;
    public static long[] boardHalf;
    public static long[] boardSide;
    public static long[][] fromtoRay;
    public static long[][] moveArray;
    public static long[][] rook00Atak;
    public static long[][] rook90Atak;
    public static long[][] bishop45Atak;
    public static long[][] bishop315Atak;
    public static long[][] ray;
    public static long[][] distMap;

    public static int[][] distance;
    public static int[][] taxicab;
    public static int[][] directions;

    public static final int ndir[] = { 2, 8, 4, 4, 8, 8, 2 };
    public static final int range[] = { 0, 0, 1, 1, 1, 0, 0 };
    public static final int slider[] = { 0, 0, 1, 1, 1, 0, 0 };
    public static final int sliderX[] = { 1, 0, 1, 1, 1, 0 };
    public static final int raybeg[] = { 0, 0, 0, 4, 0, 0 };
    public static final int rayend[] = { 0, 0, 4, 8, 8, 0 };
    public static final int dirpos[] = { 1, 1, 0, 0, 1, 1, 0, 0 };

    private static int[] lzArray;
    private static int[] bitCount;

    public static final int dir[][] = {
        {   9,  11,   0,  0, 0,  0,  0,  0 },
        { -21, -19, -12, -8, 8, 12, 19, 21 },
        { -11,  -9,   9, 11, 0,  0,  0,  0 },
        { -10,  -1,   1, 10, 0,  0,  0,  0 },
        { -11, -10,  -9, -1, 1,  9, 10, 11 },
        { -11, -10,  -9, -1, 1,  9, 10, 11 },
        {  -9, -11,   0,  0, 0,  0,  0,  0 } };

    public static final int map[] = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1,  0,  1,  2,  3,  4,  5,  6,  7, -1,
        -1,  8,  9, 10, 11, 12, 13, 14, 15, -1,
        -1, 16, 17, 18, 19, 20, 21, 22, 23, -1,
        -1, 24, 25, 26, 27, 28, 29, 30, 31, -1,
        -1, 32, 33, 34, 35, 36, 37, 38, 39, -1,
        -1, 40, 41, 42, 43, 44, 45, 46, 47, -1,
        -1, 48, 49, 50, 51, 52, 53, 54, 55, -1,
        -1, 56, 57, 58, 59, 60, 61, 62, 63, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1  };

    public static final int shift00[] = {
        56, 56, 56, 56, 56, 56, 56, 56,
        48, 48, 48, 48, 48, 48, 48, 48,
        40, 40, 40, 40, 40, 40, 40, 40,
        32, 32, 32, 32, 32, 32, 32, 32,
        24, 24, 24, 24, 24, 24, 24, 24,
        16, 16, 16, 16, 16, 16, 16, 16,
        8,  8,  8,  8,  8,  8,  8,  8,
        0,  0,  0,  0,  0,  0,  0,  0 };

    public static final int shift90[] = {
        0, 8, 16, 24, 32, 40, 48, 56,
        0, 8, 16, 24, 32, 40, 48, 56,
        0, 8, 16, 24, 32, 40, 48, 56,
        0, 8, 16, 24, 32, 40, 48, 56,
        0, 8, 16, 24, 32, 40, 48, 56,
        0, 8, 16, 24, 32, 40, 48, 56,
        0, 8, 16, 24, 32, 40, 48, 56,
        0, 8, 16, 24, 32, 40, 48, 56 };

    public static final int shift45[] = {
        28, 36, 43, 49, 54, 58, 61, 63,
        21, 28, 36, 43, 49, 54, 58, 61,
        15, 21, 28, 36, 43, 49, 54, 58,
        10, 15, 21, 28, 36, 43, 49, 54,
        6, 10, 15, 21, 28, 36, 43, 49,
        3,  6, 10, 15, 21, 28, 36, 43,
        1,  3,  6, 10, 15, 21, 28, 36,
        0,  1,  3,  6, 10, 15, 21, 28 };

    public static final int shift315[] = {
        63, 61, 58, 54, 49, 43, 36, 28,
        61, 58, 54, 49, 43, 36, 28, 21,
        58, 54, 49, 43, 36, 28, 21, 15,
        54, 49, 43, 36, 28, 21, 15, 10,
        49, 43, 36, 28, 21, 15, 10,  6,
        43, 36, 28, 21, 15, 10,  6,  3,
        36, 28, 21, 15, 10,  6,  3,  1,
        28, 21, 15, 10,  6,  3,  1,  0 };

    public static final int mask45[] = {
        0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03, 0x01,
        0x7F, 0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03,
        0x3F, 0x7F, 0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07,
        0x1F, 0x3F, 0x7F, 0xFF, 0x7F, 0x3F, 0x1F, 0x0F,
        0x0F, 0x1F, 0x3F, 0x7F, 0xFF, 0x7F, 0x3F, 0x1F,
        0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF, 0x7F, 0x3F,
        0x03, 0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF, 0x7F,
        0x01, 0x03, 0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF };

    public static final int mask315[] = {
        0x01, 0x03, 0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF,
        0x03, 0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF, 0x7F,
        0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF, 0x7F, 0x3F,
        0x0F, 0x1F, 0x3F, 0x7F, 0xFF, 0x7F, 0x3F, 0x1F,
        0x1F, 0x3F, 0x7F, 0xFF, 0x7F, 0x3F, 0x1F, 0x0F,
        0x3F, 0x7F, 0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07,
        0x7F, 0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03,
        0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03, 0x01 };

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

    private static void initSquareBitArrays () {

        squareBit = new long[64];
        squareBitX = new long[64];
        squareBit90 = new long[64];
        squareBit45 = new long[64];
        squareBit315 = new long[64];
        squareBitX90 = new long[64];
        squareBitX45 = new long[64];
        squareBitX315 = new long[64];
        long b = (long)1;
        for (int i = 63; i >= 0; i--, b <<= 1) {
            squareBit[i] = b;
            squareBitX[i] = ~b;
        }
        for (int i = 0; i < 64; i++) {
            squareBit90[i] = squareBit[r90[i]];
            squareBit45[i] = squareBit[r45[i]];
            squareBit315[i] = squareBit[r315[i]];
            squareBitX90[i] = squareBitX[r90[i]];
            squareBitX45[i] = squareBitX[r45[i]];
            squareBitX315[i] = squareBitX[r315[i]];
        }
    }

    private static void initLzArray () {

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

    private static void initBitCount () {

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

    private static void initRankFileArrays () {

        rankBits = new long[8];
        fileBits = new long[8];
        int i = 8;
        long b = (long)255;
        while (i-- != 0) {
            rankBits[i] = b;
            b <<= 8;
        }
        i = 8;
        b = 0x0101010101010101L;
        while (i-- != 0) {
            fileBits[i] = b;
            b <<= 1;
        }
    }

    private static void initMoveArray () {

        int pieceRef, from, to, f, t, n;
        moveArray = new long[7][64];
        for (pieceRef = Board.PAWN; pieceRef <= Board.BPAWN; pieceRef++) {
            for (from = 0; from < 120; from++) {
                if ((f = map[from]) == -1) continue;
                moveArray[pieceRef][f] = BoardUtils.NULLBITBOARD;
                for (n = 0; n < ndir[pieceRef]; n++) {
                    to = from;
                    do {
                        to += dir[pieceRef][n];
                        if ((t = map[to]) != -1)
                            moveArray[pieceRef][f] |= BoardUtils.squareBit[t];
                    }
                    while ((range[pieceRef] > 0) && t != -1);
                }
            }
        }
    }

    private static void initFromtoRay () {

        int pieceRef, from, to, f, t, n;
        fromtoRay = new long[64][64];
        long moves;
        for (pieceRef = Board.BISHOP; pieceRef <= Board.ROOK; pieceRef++) {
            for (from = 0; from < 120; from++) {
                if ((f = map[from]) == -1) continue;
                for (n = 0; n < ndir[pieceRef]; n++) {
                    to = from;
                    t = map[to];
                    do {
                        moves = fromtoRay[f][t];
                        to += dir[pieceRef][n];
                        if ((t = map[to]) != -1) {
                            fromtoRay[f][t] |= BoardUtils.squareBit[t];
                            fromtoRay[f][t] |= moves;
                        }
                    }
                    while (t != -1);
                }
            }
        }
    }

    private static void initRotatedAtacks () {

        int sq, map, sq1, sq2;
        int cmap[] = { 128, 64, 32, 16, 8, 4, 2, 1 };
        int rot1[] = { Board.A1, Board.A2, Board.A3, Board.A4, Board.A5, Board.A6, Board.A7, Board.A8 };
        int rot2[] = { Board.A1, Board.B2, Board.C3, Board.D4, Board.E5, Board.F6, Board.G7, Board.H8 };
        int rot3[] = { Board.A8, Board.B7, Board.C6, Board.D5, Board.E4, Board.F3, Board.G2, Board.H1 };
        rook00Atak = new long[64][256];
        rook90Atak = new long[64][256];
        bishop45Atak = new long[64][256];
        bishop315Atak = new long[64][256];
        for (sq = Board.A1; sq <= Board.H1; sq++) {
            for (map = 0; map < 256; map++) {
                rook00Atak[sq][map] = 0;
                rook90Atak[sq][map] = 0;
                bishop45Atak[sq][map] = 0;
                bishop315Atak[sq][map] = 0;
                sq1 = sq2 = sq;
                while (sq1 > 0) { if ( ( cmap[--sq1] & map ) > 0 ) break; }
                while (sq2 < 7) { if ( ( cmap[++sq2] & map ) > 0 ) break; }
                rook00Atak[sq][map] = fromtoRay[sq][sq1] | fromtoRay[sq][sq2];
                rook90Atak[rot1[sq]][map] = fromtoRay[rot1[sq]][rot1[sq1]] | fromtoRay[rot1[sq]][rot1[sq2]];
                bishop45Atak[rot2[sq]][map] = fromtoRay[rot2[sq]][rot2[sq1]] | fromtoRay[rot2[sq]][rot2[sq2]];
                bishop315Atak[rot3[sq]][map] = fromtoRay[rot3[sq]][rot3[sq1]] | fromtoRay[rot3[sq]][rot3[sq2]];
            }
        }

        for (map = 0; map < 256; map++) {
            for (sq = Board.A2; sq <= Board.H8; sq++)
                rook00Atak[sq][map] = rook00Atak[sq-8][map] >>> 8;

            for (sq1 = Board.FILE_B; sq1 <= Board.FILE_H; sq1++) {
                for (sq2 = Board.A1; sq2 <= Board.H8; sq2+=8) {
                    sq = sq2 + sq1;
                    rook90Atak[sq][map] = rook90Atak[sq-1][map] >>> 1;
                }
            }
            for (sq1 = Board.B1, sq2 = Board.H7; sq1 <= Board.H1; sq1++, sq2-=8)
                for (sq = sq1; sq <= sq2; sq += 9)
                    bishop45Atak[sq][map] = bishop45Atak[sq+8][map] << 8;
            for (sq1 = Board.A2, sq2 = Board.G8; sq1 <= Board.A8; sq1+=8, sq2--)
                for (sq = sq1; sq <= sq2; sq += 9)
                    bishop45Atak[sq][map] = (bishop45Atak[sq+1][map] & BoardUtils.squareBitX[sq1-8]) << 1;
            for (sq1 = Board.H2, sq2 = Board.B8; sq1 <= Board.H8; sq1+=8, sq2++)
                for (sq = sq1; sq <= sq2; sq += 7)
                    bishop315Atak[sq][map] = bishop315Atak[sq-8][map] >>> 8;
            for (sq1 = Board.G1, sq2 = Board.A7; sq1 >= Board.A1; sq1--, sq2-=8)
                for (sq = sq1; sq <= sq2; sq += 7)
                    bishop315Atak[sq][map] = (bishop315Atak[sq+1][map] & BoardUtils.squareBitX[sq2+8]) << 1;
        }
    }

    private static void initRayArray () {

        int piece, fsq, tsq, f, t, n, tempray;
        directions = new int[64][64];
        ray = new long[64][8];
        for (f = Board.A1; f <= Board.H8; f++)
            for (t = Board.A1; t <= Board.H8; t++)
                directions[f][t] = -1;

        for (fsq = 0; fsq < 120; fsq++) {
            if ((f = map[fsq]) == -1) continue;
            tempray = -1;
            for (piece = Board.BISHOP; piece <= Board.ROOK; piece++) {
                for (n = 0; n < ndir[piece]; n++) {
                    tempray += 1;
                    ray[f][tempray] = BoardUtils.NULLBITBOARD;
                    tsq = fsq;
                    do {
                        tsq += dir[piece][n];
                        if ((t = map[tsq]) != -1) {
                            ray[f][tempray] |= BoardUtils.squareBit[t];
                            directions[f][t] = tempray;
                        }
                    } while (t != -1);
                }
            }
        }
    }

    private static void initDistanceArrays () {

        int f, t, j;
        int d1, d2;
        distance = new int[64][64];
        taxicab = new int[64][64];
        distMap = new long[64][8];
        for (f = 0; f < 64; f++) {

            for (t = f; t < 64; t++) {

                d1 = (t & 0x07) - (f & 0x07);
                if (d1 < 0) d1 = -d1;
                d2 = (t >> 3) - (f >> 3);
                if (d2 < 0) d2 = -d2;
                distance[f][t] = Math.max(d1, d2);
                distance[t][f] = Math.max(d1, d2);
                taxicab[f][t] = d1 + d2;
                taxicab[t][f] = d1 + d2;
            }
        }

        for (f = 0; f < 64; f++)
            for (t = 0; t < 64; t++)
                distMap[f][distance[t][f]] |= BoardUtils.squareBit[t];
        for (f = 0; f < 64; f++)
            for (t = 0; t < 8; t++)
                for (j = 0; j < t; j++)
                    distMap[f][t] |= distMap[f][j];
    }

    private static void initBoardVars () {

        stoneWall = new long[2];
        rings = new long[4];
        boxes = new long[2];
        boardHalf = new long[2];
        boardSide = new long[2];
        stoneWall[Board.WHITE] = BoardUtils.squareBit[Board.D4] | BoardUtils.squareBit[Board.E3] | BoardUtils.squareBit[Board.F4];
        stoneWall[Board.BLACK] = BoardUtils.squareBit[Board.D5] | BoardUtils.squareBit[Board.E6] | BoardUtils.squareBit[Board.F5];
        rings[0] = 0x0000001818000000L;
        rings[1] = 0x00003C24243C0000L;
        rings[2] = 0x007E424242427E00L;
        rings[3] = 0xFF818181818181FFL;
        boxes[0] = 0x00003C3C3C3C0000L;
        boxes[1] = 0x007E7E7E7E7E7E00L;
        boardHalf[Board.WHITE] = rankBits[0]|rankBits[1]|rankBits[2]|rankBits[3];
        boardHalf[Board.BLACK] = rankBits[4]|rankBits[5]|rankBits[6]|rankBits[7];
        boardSide[0] = fileBits[4]|fileBits[5]|fileBits[6]|fileBits[7];
        boardSide[1] = fileBits[0]|fileBits[1]|fileBits[2]|fileBits[3];
    }

    static {
        initSquareBitArrays ();
        initLzArray ();
        initBitCount ();
        initRankFileArrays ();
        initFromtoRay ();
        initMoveArray ();
        initRotatedAtacks ();
        initDistanceArrays ();
        initRayArray ();
        initBoardVars ();
    }

    public static int getLeastSignificantBit (long bitboard) {

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

    public static int getMostSignificantBit (long bitboard) {

        return getLeastSignificantBit(bitboard & ~ ((bitboard)&(bitboard-1)));
    }

    public static int getBitCount (long bitboard) {

        return bitCount[(int)(bitboard>>>48)] + bitCount[(int)((bitboard>>>32) & 0xffff)] + bitCount[(int)((bitboard>>>16) & 0xffff)] + bitCount[(int)(bitboard & 0xffff)];
    }
}
