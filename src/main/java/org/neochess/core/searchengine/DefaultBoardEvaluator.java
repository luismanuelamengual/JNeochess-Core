
package org.neochess.core.searchengine;

import java.util.HashMap;
import java.util.Map;

public class DefaultBoardEvaluator implements BoardEvaluator {

    private final static int PHASENUMBER = 8;
    private final static int[] pawncover = { -60, -30, 0, 5, 30, 30, 30, 30, 30 };
    private final static int[] phaseFactor = { 7, 8, 8, 7, 6, 5, 4, 2, 0, };
    private final static int[][] passedPawnFactor = { { 0, 48, 48, 120, 144, 192, 240, 0}, {0, 240, 192, 144, 120, 48, 48, 0} };
    private final static int[] isolaniNormalFactor = { 12, 10, 8, 6, 6, 8, 10, 12 };
    private final static int[] isolaniWeakerFactor = { -2, -4, -6, -8, -8, -6, -4, -2 };
    private final static long[] rootKnights = { 0x4200000000000000L, 0x0000000000000042L };
    private final static long[] rootBithops = { 0x2400000000000000L, 0x0000000000000024L };
    private final static long[] rootRooks = { BitBoard.squareBit[Board.A1] | BitBoard.squareBit[Board.H1], BitBoard.squareBit[Board.A8] | BitBoard.squareBit[Board.H8] };
    private final static long[] rootQueens = { BitBoard.squareBit[Board.D1], BitBoard.squareBit[Board.D8] };
    private final static long[] d2e2 = { 0x0018000000000000L, 0x0000000000001800L };
    private final static long[] rank7 = { 0x000000000000FF00L, 0x00FF000000000000L };
    private final static long[] rank8 = { 0x00000000000000FFL, 0xFF00000000000000L };
    private final static long[] rank58 = { 0x00000000FFFFFFFFL, 0xFFFFFFFF00000000L };
    private final static int[] sideRank7 = { 6, 1 };
    private final static int[] sideRank8 = { 7, 0 };
    private final static long[][] squarePawnMask = new long[2][64];
    private final static long[][] passedPawnMask = new long[2][64];
    private final static long[] isolaniPawnMask = new long[8];
    private final static long centerFiles = 0x000000FFFF000000L;

    private final static int[][] scorePawn = {
    {
        0,   0,   0,   0,   0,   0,   0,   0,
        0,   0,   0, -30, -30,   0,   0,   0,
        1,   2,   3, -10, -10,   3,   2,   1,
        2,   4,   6,   8,   8,   6,   4,   2,
        3,   6,   9,  12,  12,   9,   6,   3,
        4,   8,  12,  16,  16,  12,   8,   4,
        5,  10,  15,  20,  20,  15,  10,   5,
        0,   0,   0,   0,   0,   0,   0,   0
    }, {
        0,   0,   0,   0,   0,   0,   0,   0,
        5,  10,  15,  20,  20,  15,  10,   5,
        4,   8,  12,  16,  16,  12,   8,   4,
        3,   6,   9,  12,  12,   9,   6,   3,
        2,   4,   6,   8,   8,   6,   4,   2,
        1,   2,   3, -10, -10,   3,   2,   1,
        0,   0,   0, -30, -30,   0,   0,   0,
        0,   0,   0,   0,   0,   0,   0,   0
    }};

    private final static int[] scoreKnight = {
        -10, -10, -10, -10, -10, -10, -10, -10,
        -10,   0,   0,   0,   0,   0,   0, -10,
        -10,   0,   5,   5,   5,   5,   0, -10,
        -10,   0,   5,  10,  10,   5,   0, -10,
        -10,   0,   5,  10,  10,   5,   0, -10,
        -10,   0,   5,   5,   5,   5,   0, -10,
        -10,   0,   0,   0,   0,   0,   0, -10,
        -10, -10, -10, -10, -10, -10, -10, -10
    };

    private final static int[] scoreBishop = {
        0,  -5, -10, -10, -10, -10,  -5,   0,
        -5,   2,   0,   0,   0,   0,   2,  -5,
        -10,   0,   6,   5,   5,   6,   0, -10,
        -10,   0,   5,  10,  10,   5,   0, -10,
        -10,   0,   5,  10,  10,   5,   0, -10,
        -10,   0,   6,   5,   5,   6,   0, -10,
        -5,   2,   0,   0,   0,   0,   2,  -5,
        0,  -5, -10, -10, -10, -10,  -5,   0
    };

    private final static int scoreKing[] = {
        24, 24, 24, 16, 16,  0, 32, 32,
        24, 20, 16, 12, 12, 16, 20, 24,
        16, 12,  8,  4,  4,  8, 12, 16,
        12,  8,  4,  0,  0,  4,  8, 12,
        12,  8,  4,  0,  0,  4,  8, 12,
        16, 12,  8,  4,  4,  8, 12, 16,
        24, 20, 16, 12, 12, 16, 20, 24,
        24, 24, 24, 16, 16,  0, 32, 32
    };

    private final static int scoreKingEnding[] = {
        0,  6, 12, 18, 18, 12,  6,  0,
        6, 12, 18, 24, 24, 18, 12,  6,
        12, 18, 24, 32, 32, 24, 18, 12,
        18, 24, 32, 48, 48, 32, 24, 18,
        18, 24, 32, 48, 48, 32, 24, 18,
        12, 18, 24, 32, 32, 24, 18, 12,
        6, 12, 18, 24, 24, 18, 12,  6,
        0,  6, 12, 18, 18, 12,  6,  0
    };

    private final static int outpost[][] = {{
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 1, 1, 1, 1, 0, 0,
        0, 1, 1, 1, 1, 1, 1, 0,
        0, 0, 1, 1, 1, 1, 0, 0,
        0, 0, 0, 1, 1, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    }, {
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 1, 1, 0, 0, 0,
        0, 0, 1, 1, 1, 1, 0, 0,
        0, 1, 1, 1, 1, 1, 1, 0,
        0, 0, 1, 1, 1, 1, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    }};

    private static void initPassedPawnMasks () {

        byte square;
        for (square = 0; square < 64; square++) {
            passedPawnMask[Board.WHITE][square] = 0;
            passedPawnMask[Board.BLACK][square] = 0;
        }
        for (square = 0; square < 64; square++) {
            passedPawnMask[Board.WHITE][square] = Board.ray[square][7];
            if (Board.getSquareFile(square) != 0) passedPawnMask[Board.WHITE][square] |= Board.ray[square-1][7];
            if (Board.getSquareFile(square) != 7) passedPawnMask[Board.WHITE][square] |= Board.ray[square+1][7];
        }
        for (square = 0; square < 64; square++) {
            passedPawnMask[Board.BLACK][square] = Board.ray[square][4];
            if (Board.getSquareFile(square) != 0) passedPawnMask[Board.BLACK][square] |= Board.ray[square-1][4];
            if (Board.getSquareFile(square) != 7) passedPawnMask[Board.BLACK][square] |= Board.ray[square+1][4];
        }
    }

    private static void initIsolaniPawnMask () {

        isolaniPawnMask[0] = Board.fileBits[1];
        isolaniPawnMask[7] = Board.fileBits[6];
        for (int i = 1; i <= 6; i++) {
            isolaniPawnMask[i] = Board.fileBits[i - 1] | Board.fileBits[i + 1];
        }
    }

    private static void initSquarePawnMask () {
        byte sq;
        int len, i, j;
        for (sq = 0; sq < 64; sq++) {
            len = 7 - Board.getSquareRank(sq);
            i = Math.max(sq & 56, sq - len);
            j = Math.min(sq | 7, sq + len);
            while (i <= j) {
                squarePawnMask[Board.WHITE][sq] |= (BitBoard.squareBit[i] | Board.fromtoRay[i][i|56]);
                i++;
            }

            len = Board.getSquareRank(sq);
            i = Math.max(sq & 56, sq - len);
            j = Math.min(sq | 7, sq + len);
            while (i <= j) {
                squarePawnMask[Board.BLACK][sq] |= (BitBoard.squareBit[i] | Board.fromtoRay[i][i&7]);
                i++;
            }
        }
        for (sq = Board.A2; sq <= Board.H2; sq++) {
            squarePawnMask[Board.WHITE][sq] = squarePawnMask[Board.WHITE][sq + 8];
        }
        for (sq = Board.A7; sq <= Board.H7; sq++) {
            squarePawnMask[Board.BLACK][sq] = squarePawnMask[Board.BLACK][sq - 8];
        }
    }

    static {
        initPassedPawnMasks ();
        initIsolaniPawnMask ();
        initSquarePawnMask ();
    }

    private Map<String, Integer> scores;
    private byte kingSquare[] = new byte[2];
    private long passedPawns[] = new long[2];
    private long weakPawns[] = new long[2];
    private long pinned;
    private int phase;
    private long[][] squaresAttacked;
    private long[] squaresAttackedBySide;

    public DefaultBoardEvaluator ()
    {
        scores = new HashMap<String, Integer>();
        scores.put("SCORE_PAWN", 100);
        scores.put("SCORE_KNIGHT", 350);
        scores.put("SCORE_BISHOP", 350);
        scores.put("SCORE_ROOK", 550);
        scores.put("SCORE_QUEEN", 1100);
        scores.put("SCORE_KING", 10000);
        scores.put("SCORE_MINORNOTDEVELOPED", -15);
        scores.put("SCORE_NOTCASTLED", -30);
        scores.put("SCORE_KINGMOVED", -20);
        scores.put("SCORE_EARLYQUEENMOVE", -50);
        scores.put("SCORE_EARLYROOKMOVE", -50);
        scores.put("SCORE_EARLYMINORREPEAT", -15);
        scores.put("SCORE_EARLYCENTERPREPEAT", -12);
        scores.put("SCORE_EARLYWINGPAWNMOVE", -9);
        scores.put("SCORE_DOUBLEDPAWNS", -20);
        scores.put("SCORE_ISOLATEDPAWNS", -15);
        scores.put("SCORE_BLOCKDEPAWNS", -40);
        scores.put("SCORE_PAWNNEARKING", 40);
        scores.put("SCORE_ALLPAWNS", -10);
        scores.put("SCORE_CENTERPAWNS", 17);
        scores.put("SCORE_BACKWARDPAWNS", -9);
        scores.put("SCORE_PASSEDPAWNS", 15);
        scores.put("SCORE_PAWNBASEATAK", -18);
        scores.put("SCORE_LOCKEDPAWNS", -10);
        scores.put("SCORE_ATAKWEAKPAWN", 8);
        scores.put("SCORE_KNIGHTONRIM", -13);
        scores.put("SCORE_OUTPOSTKNIGHT", 10);
        scores.put("SCORE_PINNEDKNIGHT", -30);
        scores.put("SCORE_KNIGHTTRAPPED", -250);
        scores.put("SCORE_PINNEDBISHOP", -30);
        scores.put("SCORE_OUTPOSTBISHOP", 8);
        scores.put("SCORE_FIANCHETTO", 8);
        scores.put("SCORE_GOODENDINGBISHOP", 16);
        scores.put("SCORE_BISHOPTRAPPED", -250);
        scores.put("SCORE_DOUBLEDBISHOPS", 15);
        scores.put("SCORE_ROOK7RANK", 30);
        scores.put("SCORE_ROOKS7RANK", 30);
        scores.put("SCORE_ROOKHALFFILE", 5);
        scores.put("SCORE_ROOKOPENFILE", 8);
        scores.put("SCORE_ROOKBEHINDPP", 6);
        scores.put("SCORE_ROOKINFRONTPP", -10);
        scores.put("SCORE_PINNEDROOK", -50);
        scores.put("SCORE_ROOKTRAPPED", -10);
        scores.put("SCORE_ROOKLIBERATED", 40);
        scores.put("SCORE_PINNEDQUEEN", -90);
        scores.put("SCORE_QUEENNEARKING", 12);
        scores.put("SCORE_QUEENNOTPRESENT", -25);
        scores.put("SCORE_GOPEN", -30);
        scores.put("SCORE_HOPEN", -600);
        scores.put("SCORE_KINGOPENFILE", -10);
        scores.put("SCORE_KINGOPENFILE1", -6);
        scores.put("SCORE_KINGENEMYOPENFILE", -6);
        scores.put("SCORE_KINGDEFENCEDEFICIT", -50);
        scores.put("SCORE_KINGBACKRANKWEAK", -40);
        scores.put("SCORE_FIANCHETTOTARGET", -13);
        scores.put("SCORE_RUPTURE", -20);
        scores.put("SCORE_HUNGEDPIECE", -20);
    }

    private void setScore (String key, int value) {
        scores.put(key, value);
    }

    private int getScore (String key) {
        return scores.get(key);
    }

    @Override
    public int evaluate (Board board) {

        int materialWhite = evaluateMaterial(board, Board.WHITE);
        int materialBlack = evaluateMaterial(board, Board.BLACK);
        int originalMaterial = (getScore("SCORE_PAWN")*16)+(getScore("SCORE_KNIGHT")*4)+(getScore("SCORE_BISHOP")*4)+(getScore("SCORE_ROOK")*4)+(getScore("SCORE_QUEEN")*2);
        int actualMaterial = materialWhite + materialBlack - (2*getScore("SCORE_KING"));
        phase = PHASENUMBER - (int)(((double)actualMaterial * (double)PHASENUMBER) / (double)originalMaterial);
        phase = Math.max(phase, 0);
        phase = Math.min(phase, PHASENUMBER);
        kingSquare[Board.WHITE] = board.getKingSquare(Board.WHITE);
        kingSquare[Board.BLACK] = board.getKingSquare(Board.BLACK);
        squaresAttacked = board.getAttacks();
        squaresAttackedBySide = new long[2];
        squaresAttackedBySide[Board.WHITE] = squaresAttacked[Board.WHITE][Board.PAWN] | squaresAttacked[Board.WHITE][Board.KNIGHT] | squaresAttacked[Board.WHITE][Board.BISHOP] | squaresAttacked[Board.WHITE][Board.ROOK] | squaresAttacked[Board.WHITE][Board.QUEEN] | squaresAttacked[Board.WHITE][Board.KING];
        squaresAttackedBySide[Board.BLACK] = squaresAttacked[Board.BLACK][Board.PAWN] | squaresAttacked[Board.BLACK][Board.KNIGHT] | squaresAttacked[Board.BLACK][Board.BISHOP] | squaresAttacked[Board.BLACK][Board.ROOK] | squaresAttacked[Board.BLACK][Board.QUEEN] | squaresAttacked[Board.BLACK][Board.KING];
        pinned = getPinnedPieces(board);

        int score = 0;
        score += (materialWhite - materialBlack);
        score += (evaluateDevelopment(board, Board.WHITE) - evaluateDevelopment(board, Board.BLACK));
        score += (evaluatePawns(board, Board.WHITE) - evaluatePawns(board, Board.BLACK));
        score += (evaluateKnights(board, Board.WHITE) - evaluateKnights(board, Board.BLACK));
        score += (evaluateBishops(board, Board.WHITE) - evaluateBishops(board, Board.BLACK));
        score += (evaluateRooks(board, Board.WHITE) - evaluateRooks(board, Board.BLACK));
        score += (evaluateQueens(board, Board.WHITE) - evaluateQueens(board, Board.BLACK));
        score += (evaluateKing(board, Board.WHITE) - evaluateKing(board, Board.BLACK));
        score += (board.getSideToMove() == Board.WHITE)? 15 : -15;
        score += getScore("SCORE_HUNGEDPIECE") * (getHungedPiecesCount(board, Board.WHITE) - getHungedPiecesCount(board, Board.BLACK));
        return score;
    }

    private int evaluateMaterial (Board board, byte side) {

        int score = 0;
        for (byte square = Board.A1; square <= Board.H8; square++) {
            if (board.getSquareSide(square) == side) {
                switch (board.getSquareFigure(square)) {
                    case Board.PAWN: score += getScore("SCORE_PAWN"); break;
                    case Board.KNIGHT: score += getScore("SCORE_KNIGHT"); break;
                    case Board.BISHOP: score += getScore("SCORE_BISHOP"); break;
                    case Board.ROOK: score += getScore("SCORE_ROOK"); break;
                    case Board.QUEEN: score += getScore("SCORE_QUEEN"); break;
                    case Board.KING: score += getScore("SCORE_KING"); break;
                    default: continue;
                }
            }
        }
        return score;
    }

    private int evaluateDevelopment (Board board, byte side) {

        int score = 0;
        if (phase <= 2) {
            long[][] pieces = board.getPieces();
            long movers = (pieces[side][Board.KNIGHT] & rootKnights[side]) | (pieces[side][Board.BISHOP] & rootBithops[side]);
            int piecesNotDeveloped = BitBoard.getBitCount(movers);
            if (piecesNotDeveloped > 0) {
                score += (piecesNotDeveloped * getScore("SCORE_MINORNOTDEVELOPED"));
                if ((pieces[side][Board.QUEEN] & rootQueens[side]) == 0)
                    score += piecesNotDeveloped * getScore("SCORE_EARLYQUEENMOVE");
                if ((pieces[side][Board.ROOK] & rootRooks[side]) == 0)
                    score += piecesNotDeveloped * getScore("SCORE_EARLYROOKMOVE");
            }

            byte friendlyKing = kingSquare[side];
            if (friendlyKing != Board.INVALIDSQUARE) {
                byte originalKingRank = side == Board.WHITE? Board.RANK_1 :  Board.RANK_8;
                if (Board.getSquareRank(friendlyKing) != originalKingRank) {
                    score += getScore("SCORE_KINGMOVED");
                }
                else {
                    long sideRooks = pieces[side][Board.ROOK];
                    if (((Board.ray[friendlyKing][5] & sideRooks) != 0) && ((Board.ray[friendlyKing][6] & sideRooks) != 0))
                        score += getScore("SCORE_NOTCASTLED");
                }
            }
        }
        return score;
    }

    private int evaluatePawns (Board board, byte side) {

        byte square, testsquare, score = 0;
        byte xside = Board.getOppositeSide(side);
        int pawnCounter[] = new int[8];
        long[][] pieces = board.getPieces();
        long sidePawns = pieces[side][Board.PAWN];
        long xsidePawns = pieces[xside][Board.PAWN];
        long pawnMoves;
        long movers = sidePawns;
        passedPawns[side] = 0;
        weakPawns[side] = 0;

        while (movers != 0) {
            square = (byte)BitBoard.getLeastSignificantBit(movers);
            movers &= BitBoard.squareBitX[square];
            score += scorePawn[side][square];

            //Verificar si es un peon pasado
            if ((xsidePawns & passedPawnMask[side][square]) == 0) {
                if ( (side == Board.WHITE && (Board.fromtoRay[square][square|56] & sidePawns) == 0) || (side == Board.BLACK && (Board.fromtoRay[square][square&7] & sidePawns) == 0)) {
                    passedPawns[side] |= BitBoard.squareBit[square];
                    score += (getScore("SCORE_PASSEDPAWNS") * ((side == Board.WHITE)? board.getSquareRank(square) : (7-board.getSquareRank(square)) ) * phase) / 12;
                }
            }

            //Verificar si es un peon debil
            testsquare = (byte)(square + (side == Board.WHITE ? 8 : -8));
            if (((passedPawnMask[xside][testsquare] & ~Board.fileBits[Board.getSquareFile(square)] & sidePawns) == 0) && board.getSquareFigure(testsquare) != Board.PAWN) {
                int nbits1 = BitBoard.getBitCount(sidePawns & Board.moveArray[xside == Board.WHITE? Board.PAWN:Board.BPAWN][testsquare]);
                int nbits2 = BitBoard.getBitCount(xsidePawns & Board.moveArray[side == Board.WHITE? Board.PAWN:Board.BPAWN][testsquare]);
                if (nbits1 < nbits2) {
                    weakPawns[side] |= BitBoard.squareBit[square];
                    score += getScore("SCORE_BACKWARDPAWNS");
                }
            }

            //Ataque al peon base
            pawnMoves = Board.moveArray[side == Board.WHITE? Board.PAWN:Board.BPAWN][square];
            if (((pawnMoves & sidePawns) != 0) && ((pawnMoves & xsidePawns) != 0))
                score += getScore("SCORE_PAWNBASEATAK");

            //Incrementar la cantidad de peones de la columna
            pawnCounter[Board.getSquareFile(square)]++;
        }

        for ( int fileindex = 0; fileindex <= 7; fileindex++ ) {
            //Peones doblados
            if (pawnCounter[fileindex] > 1) score += getScore("SCORE_DOUBLEDPAWNS");

            //Peones isolados
            if ((pawnCounter[fileindex] > 0) && ((sidePawns & isolaniPawnMask[fileindex]) == 0)) {
                if ((Board.fileBits[fileindex] & xsidePawns) == 0)
                    score += (getScore("SCORE_ISOLATEDPAWNS") + isolaniWeakerFactor[fileindex]) * pawnCounter[fileindex];
                else
                    score += (getScore("SCORE_ISOLATEDPAWNS") + isolaniNormalFactor[fileindex]) * pawnCounter[fileindex];
                weakPawns[side] |= (sidePawns & Board.fileBits[fileindex]);
            }
        }

        //Favorecer el tener peones en el centro
        score += BitBoard.getBitCount(sidePawns & centerFiles) * getScore("SCORE_CENTERPAWNS");

        //Calcular ataques al Rey
        byte xsideKingSquare = kingSquare[xside];
        if (xsideKingSquare != Board.INVALIDSQUARE) {
            long sideQueens = pieces[side][Board.QUEEN];
            if (side == Board.WHITE && (sideQueens != 0) && ((BitBoard.squareBit[board.C6] | BitBoard.squareBit[board.F6]) & sidePawns) != 0) {
                if (sidePawns != 0 && BitBoard.squareBit[board.F6] != 0 && xsideKingSquare > board.H6 && Board.distance[xsideKingSquare][board.G7]==1) score += getScore("SCORE_PAWNNEARKING");
                if (sidePawns != 0 && BitBoard.squareBit[board.C6] != 0 && xsideKingSquare > board.H6 && Board.distance[xsideKingSquare][board.B7]==1) score += getScore("SCORE_PAWNNEARKING");
            }
            else if (side == Board.BLACK && (sideQueens != 0) && ((BitBoard.squareBit[board.C3] | BitBoard.squareBit[board.F3]) & sidePawns) != 0) {
                if (sidePawns != 0 && BitBoard.squareBit[board.F3] != 0 && xsideKingSquare < board.A3 && Board.distance[xsideKingSquare][board.G2]==1) score += getScore("SCORE_PAWNNEARKING");
                if (sidePawns != 0 && BitBoard.squareBit[board.C3] != 0 && xsideKingSquare < board.A3 && Board.distance[xsideKingSquare][board.B2]==1) score += getScore("SCORE_PAWNNEARKING");
            }
        }

        //Calcular peones bloqueados en e2, d2, e7, d7
        movers = board.getBlocker();
        if (side == Board.WHITE && (((sidePawns & d2e2[Board.WHITE]) >>> 8) & movers) != 0) score += getScore("SCORE_BLOCKDEPAWNS");
        if (side == Board.BLACK && (((sidePawns & d2e2[Board.BLACK]) << 8) & movers) != 0) score += getScore("SCORE_BLOCKDEPAWNS");

        //Peones pasados fuera del alcance del ray
        if (passedPawns[side] != 0 && pieces[xside][Board.PAWN] == 0) {
            movers = passedPawns[side];
            while (movers != 0) {
                square = (byte)BitBoard.getLeastSignificantBit(movers);
                movers &= BitBoard.squareBitX[square];
                if (board.getSideToMove() == side) {
                    if ((squarePawnMask[side][square] & pieces[xside][Board.KING]) == 0)
                        score += getScore("SCORE_QUEEN") * passedPawnFactor[side][Board.getSquareRank(square)] / 550;
                    else if (kingSquare[xside] != Board.INVALIDSQUARE && (Board.moveArray[Board.KING][kingSquare[xside]] & squarePawnMask[side][square]) == 0)
                        score += getScore("SCORE_QUEEN") * passedPawnFactor[side][Board.getSquareRank(square)] / 550;
                }
            }
        }

        //Favorecer tormeta de peones si los reyes han enrocado en direcciones opuestas
        if (kingSquare[side] != Board.INVALIDSQUARE && kingSquare[xside] != Board.INVALIDSQUARE) {
            movers = pieces[side][Board.PAWN];
            if (Math.abs(Board.getSquareFile(kingSquare[side]) - Board.getSquareFile(kingSquare[xside])) >= 4 && phase < 6) {
                byte xsideKingFile = Board.getSquareFile(kingSquare[xside]);
                long pawnsInKingsColumns = (isolaniPawnMask[xsideKingFile] | Board.fileBits[xsideKingFile]) & movers;
                while (pawnsInKingsColumns != 0) {
                    square = (byte)BitBoard.getLeastSignificantBit(pawnsInKingsColumns);
                    pawnsInKingsColumns &= BitBoard.squareBitX[square];
                    score += 10 * (5 - Board.distance[square][kingSquare[xside]]);
                }
            }
        }

        return score;
    }

    private int evaluateKnights (Board board, byte side) {

        byte xside, square;
        int score, tempScore;
        long[][] pieces = board.getPieces();
        long knights, enemyPawns;
        if (pieces[side][Board.KNIGHT] == 0)
            return 0;
        xside = Board.getOppositeSide(side);
        score = tempScore = 0;
        knights = pieces[side][Board.KNIGHT];
        enemyPawns = pieces[xside][Board.PAWN];
        if ((knights & pinned) != 0)
            score += getScore("SCORE_PINNEDKNIGHT") * BitBoard.getBitCount(knights & pinned);
        while (knights != 0) {
            square = (byte)BitBoard.getLeastSignificantBit(knights);
            knights &= BitBoard.squareBitX[square];
            tempScore = evaluateControl(board,square,side);
            tempScore += scoreKnight[square];
            if (outpost[side][square] == 1 && ((enemyPawns & isolaniPawnMask[Board.getSquareFile(square)] & passedPawnMask[side][square]) == 0)) {
                tempScore += getScore("SCORE_OUTPOSTKNIGHT");
                if ((Board.moveArray[xside == Board.WHITE? Board.PAWN : Board.BPAWN][square] & pieces[side][Board.PAWN]) != 0)
                    tempScore += getScore("SCORE_OUTPOSTKNIGHT");
            }
            if ((Board.moveArray[Board.KNIGHT][square] & weakPawns[xside]) != 0)
                tempScore += getScore("SCORE_ATAKWEAKPAWN");
            score += tempScore;
        }
        return score;
    }

    private int evaluateBishops (Board board, byte side) {

        int score, tempScore, bishopCount;
        byte xside, square;
        long[][] pieces = board.getPieces();
        long bishops, enemyPawns;
        if (pieces[side][Board.BISHOP] == 0)
            return 0;
        score = tempScore = 0;
        bishops = pieces[side][Board.BISHOP];
        xside = Board.getOppositeSide(side);
        bishopCount = 0;
        enemyPawns = pieces[xside][Board.PAWN];
        if ((bishops & pinned) != 0)
            score += getScore("SCORE_PINNEDBISHOP") * BitBoard.getBitCount(bishops & pinned);
        while (bishops != 0) {
            square = (byte)BitBoard.getLeastSignificantBit(bishops);
            bishops &= BitBoard.squareBitX[square];
            bishopCount++;
            tempScore = evaluateControl(board,square,side);
            tempScore += scoreBishop[square];
            if (outpost[side][square] == 1 && (enemyPawns & isolaniPawnMask[Board.getSquareFile(square)] & passedPawnMask[side][square]) == 0) {
                tempScore += getScore("SCORE_OUTPOSTBISHOP");
                if ((Board.moveArray[xside == Board.WHITE? Board.PAWN : Board.BPAWN][square] & pieces[side][Board.PAWN]) != 0)
                    tempScore += getScore("SCORE_OUTPOSTBISHOP");
            }
            if (kingSquare[side] != Board.INVALIDSQUARE) {
                if (side == Board.WHITE) {
                    if (kingSquare[side] >= Board.F1 && kingSquare[side] <= Board.H1 && square == Board.G2)
                        tempScore += getScore("SCORE_FIANCHETTO");
                    if (kingSquare[side] >= Board.A1 && kingSquare[side] <= Board.C1 && square == Board.B2)
                        tempScore += getScore("SCORE_FIANCHETTO");
                }
                else {
                    if (kingSquare[side] >= Board.F8 && kingSquare[side] <= Board.H8 && square == Board.G7)
                        tempScore += getScore("SCORE_FIANCHETTO");
                    if (kingSquare[side] >= Board.A8 && kingSquare[side] <= Board.C8 && square == Board.B7)
                        tempScore += getScore("SCORE_FIANCHETTO");
                }
            }
            if ((board.getBishopAttacks(square) & weakPawns[xside]) != 0)
                tempScore += getScore("SCORE_ATAKWEAKPAWN");
            score += tempScore;
        }
        if (bishopCount > 1)
            score += getScore("SCORE_DOUBLEDBISHOPS");
        return score;
    }

    private int evaluateRooks (Board board, byte side) {

        int score, tempScore;
        byte square, xside, fyle, enemyKingSquare;
        long[][] pieces = board.getPieces();
        long rooks;
        if (pieces[side][Board.ROOK] == 0)
            return 0;
        score = tempScore = 0;
        rooks = pieces[side][Board.ROOK];
        xside = Board.getOppositeSide(side);
        enemyKingSquare = kingSquare[xside];
        if ((rooks & pinned) != 0)
            score += getScore("SCORE_PINNEDROOK") * BitBoard.getBitCount(rooks & pinned);
        while (rooks != 0) {
            square = (byte)BitBoard.getLeastSignificantBit(rooks);
            rooks &= BitBoard.squareBitX[square];
            tempScore = evaluateControl(board,square,side);
            fyle = Board.getSquareFile(square);
            if (phase < 7) {
                if ((pieces[side][Board.PAWN] & Board.fileBits[fyle]) == 0) {
                    if (enemyKingSquare != Board.INVALIDSQUARE && (fyle == 5 && Board.getSquareFile(enemyKingSquare) >= Board.FILE_E))
                        tempScore += getScore("SCORE_ROOKLIBERATED");
                    tempScore += getScore("SCORE_ROOKHALFFILE");
                    if ((pieces[xside][Board.PAWN] & Board.fileBits[fyle]) == 0)
                        tempScore += getScore("SCORE_ROOKOPENFILE");
                }
            }
            if (phase > 6) {
                if ((Board.fileBits[fyle] & passedPawns[Board.WHITE] & rank58[Board.WHITE]) != 0) {
                    if (BitBoard.getBitCount(Board.ray[square][7] & passedPawns[Board.WHITE]) == 1)
                        tempScore += getScore("SCORE_ROOKBEHINDPP");
                    else if ((Board.ray[square][4] & passedPawns[Board.WHITE]) != 0)
                        tempScore += getScore("SCORE_ROOKINFRONTPP");
                }
                if ((Board.fileBits[fyle] & passedPawns[Board.BLACK] & rank58[Board.BLACK]) != 0) {
                    if (BitBoard.getBitCount(Board.ray[square][4] & passedPawns[Board.BLACK]) == 1)
                        tempScore += getScore("SCORE_ROOKBEHINDPP");
                    else if ((Board.ray[square][7] & passedPawns[Board.BLACK]) != 0)
                        tempScore += getScore("SCORE_ROOKINFRONTPP");
                }
            }
            if ((board.getRookAttacks(square) & weakPawns[xside]) != 0)
                tempScore += getScore("SCORE_ATAKWEAKPAWN");
            if (Board.getSquareRank(square) == sideRank7[side] && ((enemyKingSquare != Board.INVALIDSQUARE && Board.getSquareRank(enemyKingSquare) == sideRank8[side]) || ((pieces[xside][Board.PAWN] & Board.rankBits[Board.getSquareRank(square)]) != 0)))
                tempScore += getScore("SCORE_ROOK7RANK");
            score += tempScore;
        }

        if (BitBoard.getBitCount((pieces[side][Board.QUEEN]|pieces[side][Board.ROOK]) & rank7[side]) > 1 && (((pieces[xside][Board.KING] & rank8[side]) > 0) || ((pieces[xside][Board.PAWN] & rank7[side]) > 0)))
            score += getScore("SCORE_ROOKS7RANK");
        return score;
    }

    private int evaluateQueens (Board board, byte side) {

        int score, tempScore;
        byte xside, square, enemyKing;
        long[][] pieces = board.getPieces();
        long queens;
        score = tempScore = 0;
        if (pieces[side][Board.QUEEN] == 0)
            return 0;
        xside = Board.getOppositeSide(side);
        queens = pieces[side][Board.QUEEN];
        enemyKing = kingSquare[xside];

        if ((queens & pinned) != 0)
            score += getScore("SCORE_PINNEDQUEEN") * BitBoard.getBitCount(queens & pinned);
        while (queens != 0) {
            square = (byte)BitBoard.getLeastSignificantBit(queens);
            queens &= BitBoard.squareBitX[square];
            tempScore = evaluateControl(board,square,side);
            if (enemyKing != Board.INVALIDSQUARE && Board.distance[square][enemyKing] <= 2)
                tempScore += getScore("SCORE_QUEENNEARKING");
            if ((board.getQueenAttacks(square) & weakPawns[xside]) != 0)
                tempScore += getScore("SCORE_ATAKWEAKPAWN");
            score += tempScore;
        }
        return score;
    }

    private int evaluateKing (Board board, byte side) {

        byte xside, square, square1, square2, file, rank;
        int score, n, n1, n2;
        long[][] pieces = board.getPieces();
        long[] friends = board.getFriends();
        long sidePawns = pieces[side][Board.PAWN];
        long b, x;
        score = 0;
        xside = board.getOppositeSide(side);
        square = kingSquare[side];
        if (square == Board.INVALIDSQUARE)
            return 0;
        file = Board.getSquareFile(square);
        rank = Board.getSquareRank(square);
        if (phase < 6) {
            score += ((6 - phase) * scoreKing[square] + phase * scoreKingEnding[square]) / 6;

            n = 0;
            if (side == Board.WHITE) {
                if (rank < Board.RANK_8)
                    n = BitBoard.getBitCount(Board.moveArray[Board.KING][square] & sidePawns & Board.rankBits[rank + 1]);
            }
            else {
                if (rank > Board.RANK_1)
                    n = BitBoard.getBitCount(Board.moveArray[Board.KING][square] & sidePawns & Board.rankBits[rank - 1]);
            }
            score += pawncover[n];

            if ((Board.fileBits[file] & sidePawns) == 0)
                score += getScore("SCORE_KINGOPENFILE");

            if ((Board.fileBits[file] & sidePawns) == 0)
                score += getScore("SCORE_KINGOPENFILE1");

            switch (file)
            {
                case Board.FILE_A:
                case Board.FILE_E:
                case Board.FILE_F:
                case Board.FILE_G:
                    if ((Board.fileBits[file + 1] & sidePawns) == 0)
                        score += getScore("SCORE_KINGOPENFILE");
                    if ((Board.fileBits[file + 1] & sidePawns) == 0)
                        score += getScore("SCORE_KINGOPENFILE1");
                    break;
                case Board.FILE_H:
                case Board.FILE_D:
                case Board.FILE_C:
                case Board.FILE_B:
                    if ((Board.fileBits[file - 1] & sidePawns) == 0)
                        score += getScore("SCORE_KINGOPENFILE");
                    if ((Board.fileBits[file - 1] & sidePawns) == 0)
                        score += getScore("SCORE_KINGOPENFILE1");
                    break;
                default:
                    break;
            }

            if (file > Board.FILE_E && Board.getSquareFile(kingSquare[xside]) < Board.FILE_D) {
                square2 = (side == Board.WHITE)? Board.G3 : Board.G6;
                if ((BitBoard.squareBit[square2] & sidePawns) != 0)
                    if (((BitBoard.squareBit[Board.F4] | BitBoard.squareBit[Board.H4] | BitBoard.squareBit[Board.F5] | BitBoard.squareBit[Board.H5]) & pieces[xside][Board.PAWN]) != 0)
                        score += getScore("SCORE_FIANCHETTOTARGET");
            }
            else if (file < Board.FILE_E && Board.getSquareFile(kingSquare[xside]) > Board.FILE_E) {
                square2 = (side == Board.WHITE)? Board.B3 : Board.B6;
                if ((BitBoard.squareBit[square2] & sidePawns) != 0)
                    if (((BitBoard.squareBit[Board.A4] | BitBoard.squareBit[Board.C4] | BitBoard.squareBit[Board.A5] | BitBoard.squareBit[Board.C5]) & pieces[xside][Board.PAWN]) != 0)
                        score += getScore("SCORE_FIANCHETTOTARGET");
            }

            x = Board.boardHalf[side] & Board.boardSide[file <= Board.FILE_D?1:0];
            n1 = BitBoard.getBitCount(x & (friends[xside]));
            if (n1 > 0) {
                n2 = BitBoard.getBitCount(x & (friends[side] & ~sidePawns & ~pieces[side][Board.KING]));
                if (n1 > n2)
                    score += (n1 - n2) * getScore("SCORE_KINGDEFENCEDEFICIT");
            }
            score = (score * phaseFactor[phase]) / 8;
        }
        else {
            score += scoreKingEnding[square];
            score += evaluateControl(board,square,side);
            b = (pieces[Board.WHITE][Board.PAWN] | pieces[Board.BLACK][Board.PAWN]);
            while (b != 0) {
                square1 = (byte)BitBoard.getLeastSignificantBit(b);
                b &= BitBoard.squareBitX[square1];
                if ((BitBoard.squareBit[square1] & pieces[Board.WHITE][Board.PAWN]) != 0)
                    score -= Board.distance[square][square1 + 8] * 10 - 5;
                else if ((BitBoard.squareBit[square1] & pieces[Board.BLACK][Board.PAWN]) != 0)
                    score -= Board.distance[square][square1 - 8] * 10 - 5;
                else
                    score -= Board.distance[square][square1] - 5;
            }

            if ((Board.moveArray[Board.KING][square] & weakPawns[xside]) != 0)
                score += getScore("SCORE_ATAKWEAKPAWN") * 2;
        }

        if (phase >= 4) {
            if (side == Board.WHITE)
                if (square < Board.A2)
                    if ((Board.moveArray[Board.KING][square] & (~pieces[side][Board.PAWN] & Board.rankBits[1])) == 0)
                        score += getScore("SCORE_KINGBACKRANKWEAK");
                    else
                    if (square > Board.H7)
                        if ((Board.moveArray[Board.KING][square] & (~pieces[side][Board.PAWN] & Board.rankBits[6])) == 0)
                            score += getScore("SCORE_KINGBACKRANKWEAK");
        }
        return score;
    }

    private int evaluateControl (Board board, byte square, byte side) {

        int score = 0;
        long controlled = board.getSquareXAttacks(square, side);
        score += (4 * BitBoard.getBitCount(controlled & Board.boxes[0]));
        score += (4 * BitBoard.getBitCount(controlled));
        byte enemyKing = kingSquare[1^side];
        if (enemyKing != Board.INVALIDSQUARE)
            score += BitBoard.getBitCount(controlled & Board.distMap[enemyKing][2]);
        byte friendlyKing = kingSquare[side];
        if (friendlyKing != Board.INVALIDSQUARE)
            score += BitBoard.getBitCount(controlled & Board.distMap[friendlyKing][2]);
        return score;
    }

    private int getHungedPiecesCount (Board board, byte side) {

        long c, n, b, r, q;
        long[][] pieces = board.getPieces();
        byte xside = Board.getOppositeSide(side);
        int hunged = 0;
        n = (squaresAttacked[xside][Board.PAWN] & pieces[side][Board.KNIGHT]);
        n |= (squaresAttackedBySide[xside] & pieces[side][Board.KNIGHT] & ~squaresAttackedBySide[side]);
        b = (squaresAttacked[xside][Board.PAWN] & pieces[side][Board.BISHOP]);
        b |= (squaresAttackedBySide[xside] & pieces[side][Board.BISHOP] & ~squaresAttackedBySide[side]);
        r = squaresAttacked[xside][Board.PAWN] | squaresAttacked[xside][Board.KNIGHT] | squaresAttacked[xside][Board.BISHOP];
        r = (r & pieces[side][Board.ROOK]);
        r |= (squaresAttackedBySide[xside] & pieces[side][Board.ROOK] & ~squaresAttackedBySide[side]);
        q = squaresAttacked[xside][Board.PAWN] | squaresAttacked[xside][Board.KNIGHT] | squaresAttacked[xside][Board.BISHOP] | squaresAttacked[xside][Board.ROOK];
        q = (q & pieces[side][Board.QUEEN]);
        q |= (squaresAttackedBySide[xside] & pieces[side][Board.QUEEN] & ~squaresAttackedBySide[side]);
        c = n | b | r | q;
        if (c != 0)
            hunged += BitBoard.getBitCount(c);
        if ((squaresAttackedBySide[xside] & pieces[side][Board.KING]) != 0)
            hunged++;
        return hunged;
    }

    private long getPinnedPieces (Board board) {

        long pin = 0;
        byte side, xside;
        byte square, square2;
        long b, c, e, f, t;
        long[] p;
        long[] friends = board.getFriends();
        long[][] pieces = board.getPieces();

        t = friends[Board.WHITE] | friends[Board.BLACK];
        for (side = Board.WHITE; side <= Board.BLACK; side++) {
            xside = Board.getOppositeSide(side);
            p = pieces[xside];
            e = p[Board.ROOK] | p[Board.QUEEN] | p[Board.KING];
            e |= (p[Board.BISHOP] | p[Board.KNIGHT]) & ~squaresAttackedBySide[xside];
            b = pieces[side][Board.BISHOP];
            while (b != 0) {
                square = (byte)BitBoard.getLeastSignificantBit(b);
                b &= BitBoard.squareBitX[square];
                c = Board.moveArray[Board.BISHOP][square] & e;
                while (c != 0) {
                    square2 = (byte)BitBoard.getLeastSignificantBit(c);
                    c &= BitBoard.squareBitX[square2];
                    f = t & BitBoard.squareBitX[square] & Board.fromtoRay[square2][square];
                    if (((friends[xside] & f) != 0) && BitBoard.getBitCount(f) == 1)
                        pin |= f;
                }
            }

            e = p[Board.QUEEN] | p[Board.KING];
            e |= (p[Board.ROOK] | p[Board.BISHOP] | p[Board.KNIGHT]) & ~squaresAttackedBySide[xside];
            b = pieces[side][Board.ROOK];
            while (b != 0) {
                square = (byte)BitBoard.getLeastSignificantBit(b);
                b &= BitBoard.squareBitX[square];
                c = Board.moveArray[Board.ROOK][square] & e;
                while (c != 0) {
                    square2 = (byte)BitBoard.getLeastSignificantBit(c);
                    c &= BitBoard.squareBitX[square2];
                    f = t & BitBoard.squareBitX[square] & Board.fromtoRay[square2][square];
                    if (((friends[xside] & f) != 0) && BitBoard.getBitCount(f) == 1)
                        pin |= f;
                }
            }

            e = pieces[xside][Board.KING];
            e |= (p[Board.QUEEN] | p[Board.ROOK] | p[Board.BISHOP] | p[Board.KNIGHT]) & ~squaresAttackedBySide[xside];
            b = pieces[side][Board.QUEEN];
            while (b != 0) {
                square = (byte)BitBoard.getLeastSignificantBit(b);
                b &= BitBoard.squareBitX[square];
                c = Board.moveArray[Board.QUEEN][square] & e;
                while (c != 0) {
                    square2 = (byte)BitBoard.getLeastSignificantBit(c);
                    c &= BitBoard.squareBitX[square2];
                    f = t & BitBoard.squareBitX[square] & Board.fromtoRay[square2][square];
                    if (((friends[xside] & f) != 0) && BitBoard.getBitCount(f) == 1)
                        pin |= f;
                }
            }
        }

        return pin;
    }
}