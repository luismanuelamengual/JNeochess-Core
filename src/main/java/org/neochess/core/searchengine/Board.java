package org.neochess.core.searchengine;

import org.neochess.core.Move;

import java.util.List;
import java.util.Random;

public class Board {

    public static final byte NOSIDE = 2;
    public static final byte WHITE = 0;
    public static final byte BLACK = 1;

    public static final byte EMPTY = 12;
    public static final byte PAWN = 0;
    public static final byte KNIGHT = 1;
    public static final byte BISHOP = 2;
    public static final byte ROOK = 3;
    public static final byte QUEEN = 4;
    public static final byte KING = 5;
    public static final byte BPAWN = 6;
    public static final byte WHITEPAWN = 0;
    public static final byte WHITEKNIGHT = 1;
    public static final byte WHITEBISHOP = 2;
    public static final byte WHITEROOK = 3;
    public static final byte WHITEQUEEN = 4;
    public static final byte WHITEKING = 5;
    public static final byte BLACKPAWN = 6;
    public static final byte BLACKKNIGHT = 7;
    public static final byte BLACKBISHOP = 8;
    public static final byte BLACKROOK = 9;
    public static final byte BLACKQUEEN = 10;
    public static final byte BLACKKING = 11;

    public static final byte INVALIDSQUARE = 64;
    public static final byte A1 = 0;
    public static final byte B1 = 1;
    public static final byte C1 = 2;
    public static final byte D1 = 3;
    public static final byte E1 = 4;
    public static final byte F1 = 5;
    public static final byte G1 = 6;
    public static final byte H1 = 7;
    public static final byte A2 = 8;
    public static final byte B2 = 9;
    public static final byte C2 = 10;
    public static final byte D2 = 11;
    public static final byte E2 = 12;
    public static final byte F2 = 13;
    public static final byte G2 = 14;
    public static final byte H2 = 15;
    public static final byte A3 = 16;
    public static final byte B3 = 17;
    public static final byte C3 = 18;
    public static final byte D3 = 19;
    public static final byte E3 = 20;
    public static final byte F3 = 21;
    public static final byte G3 = 22;
    public static final byte H3 = 23;
    public static final byte A4 = 24;
    public static final byte B4 = 25;
    public static final byte C4 = 26;
    public static final byte D4 = 27;
    public static final byte E4 = 28;
    public static final byte F4 = 29;
    public static final byte G4 = 30;
    public static final byte H4 = 31;
    public static final byte A5 = 32;
    public static final byte B5 = 33;
    public static final byte C5 = 34;
    public static final byte D5 = 35;
    public static final byte E5 = 36;
    public static final byte F5 = 37;
    public static final byte G5 = 38;
    public static final byte H5 = 39;
    public static final byte A6 = 40;
    public static final byte B6 = 41;
    public static final byte C6 = 42;
    public static final byte D6 = 43;
    public static final byte E6 = 44;
    public static final byte F6 = 45;
    public static final byte G6 = 46;
    public static final byte H6 = 47;
    public static final byte A7 = 48;
    public static final byte B7 = 49;
    public static final byte C7 = 50;
    public static final byte D7 = 51;
    public static final byte E7 = 52;
    public static final byte F7 = 53;
    public static final byte G7 = 54;
    public static final byte H7 = 55;
    public static final byte A8 = 56;
    public static final byte B8 = 57;
    public static final byte C8 = 58;
    public static final byte D8 = 59;
    public static final byte E8 = 60;
    public static final byte F8 = 61;
    public static final byte G8 = 62;
    public static final byte H8 = 63;

    public static final byte FILE_A = 0;
    public static final byte FILE_B = 1;
    public static final byte FILE_C = 2;
    public static final byte FILE_D = 3;
    public static final byte FILE_E = 4;
    public static final byte FILE_F = 5;
    public static final byte FILE_G = 6;
    public static final byte FILE_H = 7;
    public static final byte RANK_1 = 0;
    public static final byte RANK_2 = 1;
    public static final byte RANK_3 = 2;
    public static final byte RANK_4 = 3;
    public static final byte RANK_5 = 4;
    public static final byte RANK_6 = 5;
    public static final byte RANK_7 = 6;
    public static final byte RANK_8 = 7;

    private static final byte[] PIECE_SIDE = {WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, NOSIDE};
    private static final byte[] PIECE_FIGURE = {PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, EMPTY};

    private static final byte NOCASTLE = 0;
    private static final byte WHITECASTLESHORT = 1;
    private static final byte WHITECASTLELONG = 2;
    private static final byte BLACKCASTLESHORT = 4;
    private static final byte BLACKCASTLELONG = 8;
    private static final byte[] CASTLEMASK;

    private static final long HASHPIECE[][] = new long[12][64];
    private static final long HASHSIDE;
    private static final long HASHEP[] = new long[64];
    private static final long HASHCASTLEWS;
    private static final long HASHCASTLEWL;
    private static final long HASHCASTLEBS;
    private static final long HASHCASTLEBL;

    public static final long MOVE_FROM_SQUARE_MASK = 0xFF;
    public static final long MOVE_TO_SQUARE_MASK = 0xFF00;
    public static final long MOVE_PROMOTION_PIECE_MASK = 0xFF0000;
    public static final long MOVE_CAPTURED_PIECE_MASK = 0xFF000000;
    public static final long MOVE_CASTLE_STATE_MASK = 0xFF00000000L;
    public static final long MOVE_EP_SQUARE_MASK = 0xFF0000000000L;
    public static final long MOVE_SCORE_MASK = 0xFFFF000000000000L;

    public static final int MOVE_FROM_SQUARE_OFFSET = Long.numberOfTrailingZeros(MOVE_FROM_SQUARE_MASK);
    public static final int MOVE_TO_SQUARE_OFFSET = Long.numberOfTrailingZeros(MOVE_TO_SQUARE_MASK);
    public static final int MOVE_PROMOTION_PIECE_OFFSET = Long.numberOfTrailingZeros(MOVE_PROMOTION_PIECE_MASK);
    public static final int MOVE_CAPTURED_PIECE_OFFSET = Long.numberOfTrailingZeros(MOVE_CAPTURED_PIECE_MASK);
    public static final int MOVE_CASTLE_STATE_OFFSET = Long.numberOfTrailingZeros(MOVE_CASTLE_STATE_MASK);
    public static final int MOVE_EP_SQUARE_OFFSET = Long.numberOfTrailingZeros(MOVE_EP_SQUARE_MASK);
    public static final int MOVE_SCORE_OFFSET = Long.numberOfTrailingZeros(MOVE_SCORE_MASK);

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

    private byte[] squareSide = new byte[64];
    private byte[] squareFigure = new byte[64];
    private byte epSquare;
    private byte castleState;
    private byte sideToMove;
    private long[][] pieces = new long[2][6];
    private long friends[] = new long[2];
    private long blocker;
    private long blockerr90;
    private long blockerr45;
    private long blockerr315;

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
                moveArray[pieceRef][f] = BitBoard.NULLBITBOARD;
                for (n = 0; n < ndir[pieceRef]; n++) {
                    to = from;
                    do {
                        to += dir[pieceRef][n];
                        if ((t = map[to]) != -1)
                            moveArray[pieceRef][f] |= BitBoard.squareBit[t];
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
                            fromtoRay[f][t] |= BitBoard.squareBit[t];
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
                    bishop45Atak[sq][map] = (bishop45Atak[sq+1][map] & BitBoard.squareBitX[sq1-8]) << 1;
            for (sq1 = Board.H2, sq2 = Board.B8; sq1 <= Board.H8; sq1+=8, sq2++)
                for (sq = sq1; sq <= sq2; sq += 7)
                    bishop315Atak[sq][map] = bishop315Atak[sq-8][map] >>> 8;
            for (sq1 = Board.G1, sq2 = Board.A7; sq1 >= Board.A1; sq1--, sq2-=8)
                for (sq = sq1; sq <= sq2; sq += 7)
                    bishop315Atak[sq][map] = (bishop315Atak[sq+1][map] & BitBoard.squareBitX[sq2+8]) << 1;
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
                    ray[f][tempray] = BitBoard.NULLBITBOARD;
                    tsq = fsq;
                    do {
                        tsq += dir[piece][n];
                        if ((t = map[tsq]) != -1) {
                            ray[f][tempray] |= BitBoard.squareBit[t];
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
                distMap[f][distance[t][f]] |= BitBoard.squareBit[t];
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
        stoneWall[Board.WHITE] = BitBoard.squareBit[Board.D4] | BitBoard.squareBit[Board.E3] | BitBoard.squareBit[Board.F4];
        stoneWall[Board.BLACK] = BitBoard.squareBit[Board.D5] | BitBoard.squareBit[Board.E6] | BitBoard.squareBit[Board.F5];
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

        //Inicializacion de mascaras de enroque
        CASTLEMASK = new byte[64];
        for (byte square = A1; square <= H8; square++) {
            CASTLEMASK[square] = WHITECASTLESHORT | WHITECASTLELONG | BLACKCASTLESHORT | BLACKCASTLELONG;
        }
        CASTLEMASK[A1] = WHITECASTLESHORT | BLACKCASTLESHORT | BLACKCASTLELONG;
        CASTLEMASK[H1] = WHITECASTLELONG | BLACKCASTLESHORT | BLACKCASTLELONG;
        CASTLEMASK[E1] = BLACKCASTLESHORT | BLACKCASTLELONG;
        CASTLEMASK[A8] = WHITECASTLESHORT | WHITECASTLELONG | BLACKCASTLESHORT;
        CASTLEMASK[H8] = WHITECASTLESHORT | WHITECASTLELONG | BLACKCASTLELONG;
        CASTLEMASK[E8] = WHITECASTLESHORT | WHITECASTLELONG;

        //Inicialización de variables de hash
        Random randomGenerator = new Random(0);
        for (byte piece = WHITEPAWN; piece <= BLACKKING; piece++) {
            for (byte square = A1; square <= H8; square++) {
                HASHPIECE[piece][square] = randomGenerator.nextLong();
            }
        }
        for (byte epSquare = 0; epSquare < 64; epSquare++) {
            HASHEP[epSquare] = randomGenerator.nextLong();
        }
        HASHCASTLEWS = randomGenerator.nextLong();
        HASHCASTLEWL = randomGenerator.nextLong();
        HASHCASTLEBS = randomGenerator.nextLong();
        HASHCASTLEBL = randomGenerator.nextLong();
        HASHSIDE = randomGenerator.nextLong();

        initRankFileArrays ();
        initFromtoRay ();
        initMoveArray ();
        initRotatedAtacks ();
        initDistanceArrays ();
        initRayArray ();
        initBoardVars ();
    }

    public static byte getSquare (int file, int rank) {
        return (byte)((rank * 8) + file);
    }

    public static byte getSquareFile (int square) {
        return (byte)(square & 7);
    }

    public static byte getSquareRank (int square) {
        return (byte)(square >> 3);
    }

    public static byte getPieceSide (byte piece) {
        return PIECE_SIDE[piece];
    }

    public static byte getPieceFigure (byte piece) {
        return PIECE_FIGURE[piece];
    }

    public static byte getOppositeSide (byte side) {
        return (byte)(1^side);
    }

    public long getHash () {

        long hashValue = 0L;
        for (byte square = A1; square <= H8; square++)
        {
            byte piece = getPiece(square);
            if (piece != EMPTY)
                hashValue ^= HASHPIECE[piece][square];
        }
        if (epSquare != INVALIDSQUARE) hashValue ^= HASHEP[epSquare];
        if ((castleState & WHITECASTLESHORT) > 0) hashValue ^= HASHCASTLEWS;
        if ((castleState & WHITECASTLELONG) > 0) hashValue ^= HASHCASTLEWL;
        if ((castleState & BLACKCASTLESHORT) > 0) hashValue ^= HASHCASTLEBS;
        if ((castleState & BLACKCASTLELONG) > 0) hashValue ^= HASHCASTLEBL;
        if (sideToMove == BLACK) hashValue ^= HASHSIDE;
        return hashValue;
    }

    public void putPiece (byte square, byte piece) {

        byte pieceSide = getPieceSide(piece);
        byte pieceFigure = getPieceFigure(piece);
        if (squareSide[square] == NOSIDE) {
            blocker |= BitBoard.squareBit[square];
            blockerr90 |= BitBoard.squareBit90[square];
            blockerr45 |= BitBoard.squareBit45[square];
            blockerr315 |= BitBoard.squareBit315[square];
            friends[pieceSide] |= BitBoard.squareBit[square];
        }
        else {
            if (pieceSide != squareSide[square]) {
                friends[squareSide[square]] &= BitBoard.squareBitX[square];
                friends[pieceSide] |= BitBoard.squareBit[square];
            }
            pieces[squareSide[square]][squareFigure[square]] &= BitBoard.squareBitX[square];
        }

        pieces[pieceSide][pieceFigure] |= BitBoard.squareBit[square];
        squareSide[square] = pieceSide;
        squareFigure[square] = pieceFigure;
    }

    public byte getPiece (byte square) {
        return squareSide[square] == NOSIDE? EMPTY : (byte)((squareSide[square]*6) + squareFigure[square]);
    }

    public void removePiece (byte square) {
        blocker &= BitBoard.squareBitX[square];
        blockerr90 &= BitBoard.squareBitX90[square];
        blockerr45 &= BitBoard.squareBitX45[square];
        blockerr315 &= BitBoard.squareBitX315[square];
        friends[squareSide[square]] &= BitBoard.squareBitX[square];
        pieces[squareSide[square]][squareFigure[square]] &= BitBoard.squareBitX[square];
        squareSide[square] = NOSIDE;
        squareFigure[square] = EMPTY;
    }

    public void clear () {

        for (byte square = A1; square <= H8; square++) {
            squareSide[square] = NOSIDE;
            squareFigure[square] = EMPTY;
        }
        for (byte side = WHITE; side <= BLACK; side++) {
            friends[side] = BitBoard.NULLBITBOARD;
            for (byte figure = PAWN; figure <= KING; figure++) {
                pieces[side][figure] = BitBoard.NULLBITBOARD;
            }
        }
        blocker = BitBoard.NULLBITBOARD;
        blockerr90 = BitBoard.NULLBITBOARD;
        blockerr45 = BitBoard.NULLBITBOARD;
        blockerr315 = BitBoard.NULLBITBOARD;
        epSquare = INVALIDSQUARE;
        castleState = NOCASTLE;
        sideToMove = NOSIDE;
    }

    public void setStartupPosition () {
        clear();

        putPiece (A1, WHITEROOK);
        putPiece (B1, WHITEKNIGHT);
        putPiece (C1, WHITEBISHOP);
        putPiece (D1, WHITEQUEEN);
        putPiece (E1, WHITEKING);
        putPiece (F1, WHITEBISHOP);
        putPiece (G1, WHITEKNIGHT);
        putPiece (H1, WHITEROOK);
        putPiece (A2, WHITEPAWN);
        putPiece (B2, WHITEPAWN);
        putPiece (C2, WHITEPAWN);
        putPiece (D2, WHITEPAWN);
        putPiece (E2, WHITEPAWN);
        putPiece (F2, WHITEPAWN);
        putPiece (G2, WHITEPAWN);
        putPiece (H2, WHITEPAWN);

        putPiece (A8, BLACKROOK);
        putPiece (B8, BLACKKNIGHT);
        putPiece (C8, BLACKBISHOP);
        putPiece (D8, BLACKQUEEN);
        putPiece (E8, BLACKKING);
        putPiece (F8, BLACKBISHOP);
        putPiece (G8, BLACKKNIGHT);
        putPiece (H8, BLACKROOK);
        putPiece (A7, BLACKPAWN);
        putPiece (B7, BLACKPAWN);
        putPiece (C7, BLACKPAWN);
        putPiece (D7, BLACKPAWN);
        putPiece (E7, BLACKPAWN);
        putPiece (F7, BLACKPAWN);
        putPiece (G7, BLACKPAWN);
        putPiece (H7, BLACKPAWN);

        sideToMove = WHITE;
        epSquare = INVALIDSQUARE;
        castleState = WHITECASTLESHORT | WHITECASTLELONG | BLACKCASTLESHORT | BLACKCASTLELONG;
    }

    public static long createMove (byte fromSquare, byte toSquare) {
        return (fromSquare << MOVE_FROM_SQUARE_OFFSET) | (toSquare << MOVE_TO_SQUARE_OFFSET);
    }

    public static long createMove (byte fromSquare, byte toSquare, byte promotionPiece) {
        return (fromSquare << MOVE_FROM_SQUARE_OFFSET) | (toSquare << MOVE_TO_SQUARE_OFFSET) | (promotionPiece << MOVE_PROMOTION_PIECE_OFFSET);
    }

    public long makeMove (long move) {

        byte fromSquare = (byte)((move & MOVE_FROM_SQUARE_MASK) >>> MOVE_FROM_SQUARE_OFFSET);
        byte toSquare = (byte)((move & MOVE_TO_SQUARE_MASK) >>> MOVE_TO_SQUARE_OFFSET);
        byte movingPiece = getPiece(fromSquare);
        byte movingFigure = squareFigure[fromSquare];
        byte capturedPiece = getPiece(toSquare);

        long appliedMove = move
                | (capturedPiece << MOVE_CAPTURED_PIECE_OFFSET)
                | ((long)castleState << MOVE_CASTLE_STATE_OFFSET)
                | ((long)epSquare << MOVE_EP_SQUARE_OFFSET);

        if (movingFigure == PAWN) {
            if (sideToMove == WHITE) {
                if (getSquareRank(toSquare) == RANK_8) {
                    byte promotionPiece = (byte)((move & MOVE_PROMOTION_PIECE_MASK) >>> MOVE_PROMOTION_PIECE_OFFSET);
                    movingPiece = promotionPiece != EMPTY ? promotionPiece : WHITEQUEEN;
                }
                else if (toSquare == epSquare) {
                    removePiece((byte) (toSquare - 8));
                }
            }
            else {
                if (getSquareRank(toSquare) == RANK_1) {
                    byte promotionPiece = (byte)((move & MOVE_PROMOTION_PIECE_MASK) >>> MOVE_PROMOTION_PIECE_OFFSET);
                    movingPiece = promotionPiece != EMPTY ? promotionPiece : BLACKQUEEN;
                }
                else if (toSquare == epSquare) {
                    removePiece((byte) (toSquare + 8));
                }
            }
            epSquare = (Math.abs(fromSquare - toSquare) == 16)? (byte)((fromSquare + toSquare) / 2) : INVALIDSQUARE;
        }
        else {
            if (movingFigure == KING) {
                if (fromSquare == E1) {
                    switch (toSquare) {
                        case G1:
                            removePiece(H1);
                            putPiece(F1, WHITEROOK);
                            break;
                        case C1:
                            removePiece(A1);
                            putPiece(D1, WHITEROOK);
                            break;
                    }
                }
                else if (fromSquare == E8) {
                    switch (toSquare) {
                        case G8:
                            removePiece(H8);
                            putPiece(F8, BLACKROOK);
                            break;
                        case C8:
                            removePiece(A8);
                            putPiece(D8, BLACKROOK);
                            break;
                    }
                }
            }
            epSquare = INVALIDSQUARE;
        }
        removePiece(fromSquare);
        putPiece(toSquare, movingPiece);
        castleState &= CASTLEMASK[fromSquare] & CASTLEMASK[toSquare];
        sideToMove = getOppositeSide(sideToMove);
        return appliedMove;
    }

    public void unmakeMove (long move) {

        byte fromSquare = (byte)((move & MOVE_FROM_SQUARE_MASK) >>> MOVE_FROM_SQUARE_OFFSET);
        byte toSquare = (byte)((move & MOVE_TO_SQUARE_MASK) >>> MOVE_TO_SQUARE_OFFSET);
        byte capturedPiece = (byte)((move & MOVE_CAPTURED_PIECE_MASK) >>> MOVE_CAPTURED_PIECE_OFFSET);
        byte lastCastleState = (byte)((move & MOVE_CASTLE_STATE_MASK) >>> MOVE_CASTLE_STATE_OFFSET);
        byte lastEpSquare = (byte)((move & MOVE_EP_SQUARE_MASK) >>> MOVE_EP_SQUARE_OFFSET);
        byte movingPiece = getPiece(toSquare);
        byte movingFigure = squareFigure[toSquare];
        byte movingSide = squareSide[toSquare];

        if (movingFigure == PAWN) {
            if (toSquare == lastEpSquare) {
                if (movingSide == WHITE) {
                    putPiece((byte)(toSquare - 8), BLACKPAWN);
                }
                else {
                    putPiece((byte)(toSquare + 8), WHITEPAWN);
                }
            }
        }
        else if (movingFigure == KING) {
            if (fromSquare == E1) {
                switch (toSquare) {
                    case G1:
                        removePiece(F1);
                        putPiece(H1, WHITEROOK);
                        break;
                    case C1:
                        removePiece(D1);
                        putPiece(A1, WHITEROOK);
                        break;
                }
            }
            else if (fromSquare == E8) {
                switch (toSquare) {
                    case G8:
                        removePiece(F8);
                        putPiece(H8, BLACKROOK);
                        break;
                    case C8:
                        removePiece(D8);
                        putPiece(A8, BLACKROOK);
                        break;
                }
            }
        }
        if (capturedPiece != EMPTY) {
            putPiece(toSquare, capturedPiece);
        }
        else {
            removePiece(toSquare);
        }
        putPiece(fromSquare, movingPiece);
        epSquare = lastEpSquare;
        castleState = lastCastleState;
        sideToMove = getOppositeSide(sideToMove);
    }

    public byte getKingSquare (byte side) {

        return pieces[side][KING] != BitBoard.NULLBITBOARD? (byte)BitBoard.getLeastSignificantBit(pieces[side][KING]) : INVALIDSQUARE;
    }

    public boolean isSquareAttacked (byte square, byte side) {

        long[] sidePieces = pieces[side];
        byte oppositeSide = getOppositeSide(side);
        if ((sidePieces[KNIGHT] & moveArray[KNIGHT][square]) != 0) return true;
        if ((sidePieces[KING] & moveArray[KING][square]) != 0) return true;
        if ((sidePieces[PAWN] & moveArray[oppositeSide == WHITE? PAWN : BPAWN][square]) != 0) return true;

        long[] c = fromtoRay[square];
        long b = (sidePieces[BISHOP] | sidePieces[QUEEN]) & moveArray[BISHOP][square];
        long d = ~b & blocker;
        int t;
        while (b != 0)
        {
            t = BitBoard.getLeastSignificantBit(b);
            if ((c[t] & d) == 0)
                return (true);
            b &= BitBoard.squareBitX[t];
        }
        b = (sidePieces[ROOK] | sidePieces[QUEEN]) & moveArray[ROOK][square];
        d = ~b & blocker;
        while (b != 0)
        {
            t = BitBoard.getLeastSignificantBit(b);
            if ((c[t] & d) == 0)
                return (true);
            b &= BitBoard.squareBitX[t];
        }
        return (false);
    }

    public boolean inCheck () {
        return inCheck(sideToMove);
    }

    public boolean inCheck (byte side) {
        byte kingSquare = getKingSquare(side);
        return (kingSquare != INVALIDSQUARE)? isSquareAttacked(kingSquare, getOppositeSide(side)) : false;
    }

    public long getSquareAttackers (byte square, byte side) {

        byte xside = getOppositeSide(side);
        long[] sidePieces;
        long[] slideMoves;
        long moves, attackers;
        byte t;
        sidePieces = pieces[side];
        attackers = (sidePieces[KNIGHT] & moveArray[KNIGHT][square]);
        attackers |= (sidePieces[KING] & moveArray[KING][square]);
        attackers |= (sidePieces[PAWN] & moveArray[xside==WHITE?PAWN:BPAWN][square]);
        slideMoves = fromtoRay[square];
        moves = (sidePieces[BISHOP] | sidePieces[QUEEN]) & moveArray[BISHOP][square];
        while (moves != 0) {
            t = (byte)BitBoard.getLeastSignificantBit(moves);
            moves &= BitBoard.squareBitX[t];
            if ((slideMoves[t] & blocker & BitBoard.squareBitX[t]) == 0)
                attackers |= BitBoard.squareBit[t];
        }
        moves = (sidePieces[ROOK] | sidePieces[QUEEN]) & moveArray[ROOK][square];
        while (moves != 0) {
            t = (byte)BitBoard.getLeastSignificantBit(moves);
            moves &= BitBoard.squareBitX[t];
            if ((slideMoves[t] & blocker & BitBoard.squareBitX[t]) == 0)
                attackers |= BitBoard.squareBit[t];
        }
        return attackers;
    }

    public long getSquareXAttackers (byte square, byte side) {

        byte xside = getOppositeSide(side);
        long[] sidePieces, xsidePieces, slideMoves;
        long moves, attackers, blocker;
        byte t;
        sidePieces = pieces[side];
        xsidePieces = pieces[xside];
        attackers = (sidePieces[KNIGHT] & moveArray[KNIGHT][square]);
        attackers |= (sidePieces[KING] & moveArray[KING][square]);
        slideMoves = fromtoRay[square];
        moves = (sidePieces[PAWN] & moveArray[xside==WHITE?PAWN:BPAWN][square]);
        blocker = this.blocker;
        blocker &= ~(sidePieces[BISHOP] | sidePieces[QUEEN] | xsidePieces[BISHOP] | xsidePieces[QUEEN] | moves);
        moves |= (sidePieces[BISHOP] | sidePieces[QUEEN]) & moveArray[BISHOP][square];
        while (moves != 0) {
            t = (byte)BitBoard.getLeastSignificantBit(moves);
            moves &= BitBoard.squareBitX[t];
            if ((slideMoves[t] & blocker & BitBoard.squareBitX[t]) == 0)
                attackers |= BitBoard.squareBit[t];
        }
        moves = (sidePieces[ROOK] | sidePieces[QUEEN]) & moveArray[ROOK][square];
        blocker = this.blocker;
        blocker &= ~(sidePieces[ROOK] | sidePieces[QUEEN] | xsidePieces[ROOK] | xsidePieces[QUEEN]);
        while (moves != 0) {
            t = (byte)BitBoard.getLeastSignificantBit(moves);
            moves &= BitBoard.squareBitX[t];
            if ((slideMoves[t] & blocker & BitBoard.squareBitX[t]) == 0)
                attackers |= BitBoard.squareBit[t];
        }
        return attackers;
    }

    public long getSquareAttacks (byte square, byte figure, byte side) {

        switch (figure) {
            case PAWN:
                return moveArray[side==WHITE?PAWN:BPAWN][square];
            case KNIGHT:
                return moveArray[KNIGHT][square];
            case BISHOP:
                return getBishopAttacks(square);
            case ROOK:
                return getRookAttacks(square);
            case QUEEN:
                return getQueenAttacks(square);
            case KING:
                return moveArray[KING][square];
        }
        return 0;
    }

    public long getSquareXAttacks (byte square, byte side) {

        long[] sidePieces;
        long attacks, rays, blocker;
        int piece, dir, blocksq;
        sidePieces = pieces[side];
        piece = squareFigure[square];
        blocker = this.blocker;
        attacks = 0;
        switch (piece) {
            case PAWN:
                attacks = moveArray[side==WHITE?PAWN:BPAWN][square];
                break;
            case KNIGHT:
                attacks = moveArray[KNIGHT][square];
                break;
            case BISHOP:
            case QUEEN:
                blocker &= ~(sidePieces[BISHOP] | sidePieces[QUEEN]);
                for (dir = raybeg[BISHOP]; dir < rayend[BISHOP]; dir++) {
                    rays = ray[square][dir] & blocker;
                    if (rays == BitBoard.NULLBITBOARD) {
                        rays = ray[square][dir];
                    }
                    else {
                        blocksq = (dirpos[dir] == 1? BitBoard.getMostSignificantBit(rays) : BitBoard.getLeastSignificantBit(rays));
                        rays = fromtoRay[square][blocksq];
                    }
                    attacks |= rays;
                }
                if (piece == BISHOP)
                    break;
                blocker = this.blocker;
            case ROOK:
                blocker &= ~(sidePieces[ROOK] | sidePieces[QUEEN]);
                for (dir = raybeg[ROOK]; dir < rayend[ROOK]; dir++) {
                    rays = ray[square][dir] & blocker;
                    if (rays == BitBoard.NULLBITBOARD) {
                        rays = ray[square][dir];
                    }
                    else {
                        blocksq = (dirpos[dir] == 1? BitBoard.getMostSignificantBit(rays) : BitBoard.getLeastSignificantBit(rays));
                        rays = fromtoRay[square][blocksq];
                    }
                    attacks |= rays;
                }
                break;
            case KING:
                attacks = moveArray[KING][square];
                break;
        }
        return (attacks);
    }

    public long getBishopAttacks (byte square) {

        long bishopAttack45 = bishop45Atak[square][(int)((blockerr45 >>> shift45[square]) & mask45[square])];
        long bishopAttack315 = bishop315Atak[square][(int)((blockerr315 >>> shift315[square]) & mask315[square])];
        return bishopAttack45 | bishopAttack315;
    }

    public long getRookAttacks (byte square) {

        long rookAttack00 = rook00Atak[square][(int)((blocker >>> shift00[square]) & 0xFF)];
        long rookAttack90 = rook90Atak[square][(int)((blockerr90 >>> shift90[square]) & 0xFF)];
        return rookAttack00 | rookAttack90;
    }

    public long getQueenAttacks (byte square) {

        return getRookAttacks(square) | getBishopAttacks(square);
    }

    public long[][] getAttacks () {

        byte side;
        byte square;
        long movers;
        long[] sidePieces;
        long[][] atacks = new long[2][6];

        for (side = WHITE; side <= BLACK; side++) {

            sidePieces = pieces[side];
            atacks[side][KNIGHT] = 0;
            movers = sidePieces[KNIGHT];
            while (movers != 0) {

                square = (byte)BitBoard.getLeastSignificantBit(movers);
                movers &= BitBoard.squareBitX[square];
                atacks[side][KNIGHT] |= moveArray[KNIGHT][square];
            }

            atacks[side][BISHOP] = 0;
            movers = sidePieces[BISHOP];
            while (movers != 0) {

                square = (byte)BitBoard.getLeastSignificantBit(movers);
                movers &= BitBoard.squareBitX[square];
                atacks[side][BISHOP] |= getBishopAttacks(square);
            }

            atacks[side][ROOK] = 0;
            movers = sidePieces[ROOK];
            while (movers != 0) {

                square = (byte)BitBoard.getLeastSignificantBit(movers);
                movers &= BitBoard.squareBitX[square];
                atacks[side][ROOK] |= getRookAttacks(square);
            }

            atacks[side][QUEEN] = 0;
            movers = sidePieces[QUEEN];
            while (movers != 0) {

                square = (byte)BitBoard.getLeastSignificantBit(movers);
                movers &= BitBoard.squareBitX[square];
                atacks[side][ROOK] |= getQueenAttacks(square);
            }

            atacks[side][KING] = 0;
            movers = sidePieces[KING];
            while (movers != 0) {

                square = (byte)BitBoard.getLeastSignificantBit(movers);
                movers &= BitBoard.squareBitX[square];
                atacks[side][KING] |= moveArray[KING][square];
            }

            atacks[side][PAWN] = 0;
            if (side == WHITE) {

                movers = pieces[WHITE][PAWN] & ~fileBits[0];
                atacks[side][PAWN] |= (movers >> 7);
                movers = pieces[WHITE][PAWN] & ~fileBits[7];
                atacks[side][PAWN] |= (movers >> 9);
            }
            else {

                movers = pieces[BLACK][PAWN] & ~fileBits[0];
                atacks[side][PAWN] |= (movers << 9);
                movers = pieces[BLACK][PAWN] & ~fileBits[7];
                atacks[side][PAWN] |= (movers << 7);
            }
        }
        return atacks;
    }

    private boolean isPinningKing (byte square, byte side) {

        int xside;
        int KingSq, dir, sq1;
        long b;
        KingSq = getKingSquare(side);
        if ((dir = directions[KingSq][square]) == -1)
            return false;
        xside = 1 ^ side;
        if ((fromtoRay[KingSq][square] & BitBoard.squareBitX[square] & blocker) != 0)
            return false;
        b = (ray[KingSq][dir] ^ fromtoRay[KingSq][square]) & blocker;
        if (b == 0)
            return false;
        sq1 = (square > KingSq ? BitBoard.getLeastSignificantBit(b) : BitBoard.getMostSignificantBit(b));
        if (dir <= 3 && ((BitBoard.squareBit[sq1] & (pieces[xside][QUEEN] | pieces[xside][BISHOP])) != 0))
            return true;
        if (dir >= 4 && ((BitBoard.squareBit[sq1] & (pieces[xside][QUEEN] | pieces[xside][ROOK])) != 0))
            return true;
        return false;
    }

    public void generatePseudoLegalMoves(long[] movesArray) {

        int moveIndex = 0;
        byte side = sideToMove;
        byte xside = getOppositeSide(side);
        byte piece, fsq, tsq;
        long movers, moves, captures;
        long[] sidePieces = pieces[side];
        long sideFriends = friends[side];
        long notfriends = ~sideFriends;
        long notblocker = ~blocker;

        for (piece = KNIGHT; piece <= KING; piece += 4) {
            movers = sidePieces[piece];
            while (movers != 0) {
                fsq = (byte)BitBoard.getLeastSignificantBit(movers);
                movers &= BitBoard.squareBitX[fsq];
                moves = moveArray[piece][fsq] & notfriends;
                while (moves != 0) {
                    tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                    moves &= BitBoard.squareBitX[tsq];
                    movesArray[moveIndex++] = createMove(fsq, tsq);
                }
            }
        }

        movers = sidePieces[BISHOP];
        while (movers != 0) {
            fsq = (byte)BitBoard.getLeastSignificantBit(movers);
            movers &= BitBoard.squareBitX[fsq];
            moves = getBishopAttacks(fsq) & notfriends;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        movers = sidePieces[ROOK];
        while (movers != 0) {
            fsq = (byte)BitBoard.getLeastSignificantBit(movers);
            movers &= BitBoard.squareBitX[fsq];
            moves = getRookAttacks(fsq) & notfriends;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        movers = sidePieces[QUEEN];
        while (movers != 0) {
            fsq = (byte)BitBoard.getLeastSignificantBit(movers);
            movers &= BitBoard.squareBitX[fsq];
            moves = getQueenAttacks(fsq) & notfriends;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        captures = (friends[xside] | (epSquare != INVALIDSQUARE? BitBoard.squareBit[epSquare] : BitBoard.NULLBITBOARD));
        if (side == WHITE) {
            moves = (sidePieces[PAWN] >> 8) & notblocker;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-8), tsq);
            }

            movers = sidePieces[PAWN] & rankBits[1];
            moves = (movers >> 8) & notblocker;
            moves = (moves >> 8) & notblocker;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-16), tsq);
            }

            movers = sidePieces[PAWN] & ~fileBits[0];
            moves = (movers >> 7) & captures;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-7), tsq);
            }

            movers = sidePieces[PAWN] & ~fileBits[7];
            moves = (movers >> 9) & captures;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-9), tsq);
            }
        }
        else if (side == BLACK) {

            moves = (sidePieces[PAWN] << 8) & notblocker;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+8), tsq);
            }

            movers = sidePieces[PAWN] & rankBits[6];
            moves = (movers << 8) & notblocker;
            moves = (moves << 8) & notblocker;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+16), tsq);
            }

            movers = sidePieces[PAWN] & ~fileBits[7];
            moves = (movers << 7) & captures;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+7), tsq);
            }

            movers = sidePieces[PAWN] & ~fileBits[0];
            moves = (movers << 9) & captures;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+9), tsq);
            }
        }

        if (side == WHITE) {
            if ((castleState & WHITECASTLESHORT) > 0)
                if (squareSide[F1] == EMPTY && squareSide[G1] == EMPTY && !inCheck() && !isSquareAttacked(F1, BLACK) && !isSquareAttacked(G1, BLACK))
                    movesArray[moveIndex++] = createMove(E1, G1);
            if ((castleState & WHITECASTLELONG) > 0)
                if (squareSide[B1] == EMPTY && squareSide[C1] == EMPTY && squareSide[D1] == EMPTY && !inCheck() && !isSquareAttacked(C1, BLACK) && !isSquareAttacked(D1, BLACK))
                    movesArray[moveIndex++] = createMove(E1, C1);
        }
        else {
            if ((castleState & BLACKCASTLESHORT) > 0)
                if (squareSide[F8] == EMPTY && squareSide[G8] == EMPTY && !inCheck() && !isSquareAttacked(F8, WHITE) && !isSquareAttacked(G8, WHITE))
                    movesArray[moveIndex++] = createMove(E8, G8);
            if ((castleState & BLACKCASTLELONG) > 0)
                if (squareSide[B8] == EMPTY && squareSide[C8] == EMPTY && squareSide[D8] == EMPTY && !inCheck() && !isSquareAttacked(C8, WHITE) && !isSquareAttacked(D8, WHITE))
                    movesArray[moveIndex++] = createMove(E8, C8);
        }
        movesArray[moveIndex++] = 0;
    }

    public void generateCaptureMoves (long[] movesArray) {

        int moveIndex = 0;
        byte side = sideToMove;
        byte xside = getOppositeSide(side);
        byte piece, fsq, tsq;
        long movers, moves, captures;
        long[] sidePieces = pieces[side];
        long enemy = friends[xside];

        for (piece = KNIGHT; piece <= KING; piece += 4) {
            movers = sidePieces[piece];
            while (movers != 0) {
                fsq = (byte)BitBoard.getLeastSignificantBit(movers);
                movers &= BitBoard.squareBitX[fsq];
                moves = moveArray[piece][fsq] & enemy;
                while (moves != 0) {
                    tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                    moves &= BitBoard.squareBitX[tsq];
                    movesArray[moveIndex++] = createMove(fsq, tsq);
                }
            }
        }

        movers = sidePieces[BISHOP];
        while (movers != 0) {
            fsq = (byte)BitBoard.getLeastSignificantBit(movers);
            movers &= BitBoard.squareBitX[fsq];
            moves = getBishopAttacks(fsq) & enemy;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        movers = sidePieces[ROOK];
        while (movers != 0) {
            fsq = (byte)BitBoard.getLeastSignificantBit(movers);
            movers &= BitBoard.squareBitX[fsq];
            moves = getRookAttacks(fsq) & enemy;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        movers = sidePieces[QUEEN];
        while (movers != 0) {
            fsq = (byte)BitBoard.getLeastSignificantBit(movers);
            movers &= BitBoard.squareBitX[fsq];
            moves = getQueenAttacks(fsq) & enemy;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        captures = (friends[xside] | (epSquare != INVALIDSQUARE? BitBoard.squareBit[epSquare] : BitBoard.NULLBITBOARD));
        if (side == WHITE) {
            movers = sidePieces[PAWN] & rankBits[6];
            moves = (movers >> 8) & ~blocker;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-8), tsq);
            }

            movers = sidePieces[PAWN] & ~fileBits[0];
            moves = (movers >> 7) & captures;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-7), tsq);
            }

            movers = sidePieces[PAWN] & ~fileBits[7];
            moves = (movers >> 9) & captures;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-9), tsq);
            }
        }
        else if (side == BLACK) {
            movers = sidePieces[PAWN] & rankBits[1];
            moves = (movers << 8) & ~blocker;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+8), tsq);
            }

            movers = sidePieces[PAWN] & ~fileBits[7];
            moves = (movers << 7) & captures;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+7), tsq);
            }

            movers = sidePieces[PAWN] & ~fileBits[0];
            moves = (movers << 9) & captures;
            while (moves != 0) {
                tsq = (byte)BitBoard.getLeastSignificantBit(moves);
                moves &= BitBoard.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+9), tsq);
            }
        }
        movesArray[moveIndex++] = 0;
    }

    public void generateEscapeMoves (long[] movesArray) {

        int moveIndex = 0;
        byte side, xside;
        byte kingsq, chksq, sq, sq1, epsq;
        int dir;
        long checkers, b, c, p, escapes;
        escapes = 0;
        side = sideToMove;
        xside = getOppositeSide(side);

        kingsq = getKingSquare(side);
        checkers = getSquareAttackers(kingsq, xside);
        p = pieces[side][PAWN];

        if (BitBoard.getBitCount(checkers) == 1) {
            chksq = (byte)BitBoard.getLeastSignificantBit(checkers);
            b = getSquareAttackers(chksq, side);
            b &= ~pieces[side][KING];
            while (b != 0) {
                sq = (byte)BitBoard.getLeastSignificantBit(b);
                b &= BitBoard.squareBitX[sq];
                if (!isPinningKing(sq, side))
                    movesArray[moveIndex++] = createMove(sq, chksq);
            }

            if (epSquare != INVALIDSQUARE) {
                epsq = epSquare;
                if (epsq + (side == WHITE ? -8 : 8) == chksq) {
                    b = moveArray[xside == WHITE?PAWN:BPAWN][epsq] & p;
                    while (b != 0)
                    {
                        sq = (byte)BitBoard.getLeastSignificantBit(b);
                        b &= BitBoard.squareBitX[sq];
                        if (!isPinningKing(sq, side))
                            movesArray[moveIndex++] = createMove(sq, epsq);
                    }
                }
            }

            if (slider[squareFigure[chksq]] == 1) {
                c = fromtoRay[kingsq][chksq] & BitBoard.squareBitX[chksq];
                while (c != 0) {
                    sq = (byte)BitBoard.getLeastSignificantBit(c);
                    c &= BitBoard.squareBitX[sq];
                    b = getSquareAttackers(sq, side);
                    b &= ~(pieces[side][KING] | p);

                    if (side == WHITE && sq > H2) {
                        if ((BitBoard.squareBit[sq - 8] & p) != 0)
                            b |= BitBoard.squareBit[sq - 8];

                        if (getSquareRank(sq) == 3 && squareSide[sq - 8] == NOSIDE && ((BitBoard.squareBit[sq - 16] & p) != 0))
                            b |= BitBoard.squareBit[sq - 16];
                    }
                    if (side == BLACK && sq < H7) {
                        if ((BitBoard.squareBit[sq + 8] & p) != 0)
                            b |= BitBoard.squareBit[sq + 8];
                        if (getSquareRank(sq) == 4 && squareSide[sq + 8] == NOSIDE && ((BitBoard.squareBit[sq + 16] & p) != 0))
                            b |= BitBoard.squareBit[sq + 16];
                    }
                    while (b != 0) {
                        sq1 = (byte)BitBoard.getLeastSignificantBit(b);
                        b &= BitBoard.squareBitX[sq1];
                        if (!isPinningKing(sq1, side))
                            movesArray[moveIndex++] = createMove(sq1, sq);
                    }
                }
            }
        }

        if (checkers != 0)
            escapes = moveArray[KING][kingsq] & ~friends[side];

        while (checkers != 0) {
            chksq = (byte)BitBoard.getLeastSignificantBit(checkers);
            checkers &= BitBoard.squareBitX[chksq];
            dir = directions[chksq][kingsq];
            if (slider[squareFigure[chksq]] == 1)
                escapes &= ~ray[chksq][dir];
        }
        while (escapes != 0) {
            sq = (byte)BitBoard.getLeastSignificantBit(escapes);
            escapes &= BitBoard.squareBitX[sq];
            if (!isSquareAttacked(sq, xside))
                movesArray[moveIndex++] = createMove(kingsq, sq);
        }
        movesArray[moveIndex] = 0;
    }

    public void generateLegalMoves (long[] moves) {

        byte currentSideToMove = sideToMove;
        generatePseudoLegalMoves(moves);
        int moveIndex = 0;
        for (int currentMoveIndex = 0; currentMoveIndex < moves.length; currentMoveIndex++) {
            long move = moves[currentMoveIndex];
            if (move == 0) {
                break;
            }
            long appliedMove = makeMove(move);
            if (!inCheck(currentSideToMove)) {
                if (moveIndex != currentMoveIndex) {
                    moves[moveIndex] = move;
                }
                moveIndex++;
            }
            unmakeMove(appliedMove);
        }
        moves[moveIndex] = 0;
    }
}
