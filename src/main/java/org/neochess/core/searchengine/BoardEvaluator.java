
package org.neochess.core.searchengine;

import java.util.Map;

public abstract class BoardEvaluator {

    private static BoardEvaluator defaultEvaluator = new DefaultBoardEvaluator();

    public abstract int evaluate (Board board);

    public static BoardEvaluator getDefault() {
        return defaultEvaluator;
    }

    private static class DefaultBoardEvaluator extends BoardEvaluator {

        private final static int PHASENUMBER = 8;
        private final static int[] pawncover = {-60, -30, 0, 5, 30, 30, 30, 30, 30};
        private final static int[] phaseFactor = {7, 8, 8, 7, 6, 5, 4, 2, 0,};
        private final static int[][] passedPawnFactor = {{0, 48, 48, 120, 144, 192, 240, 0}, {0, 240, 192, 144, 120, 48, 48, 0}};
        private final static int[] isolaniNormalFactor = {12, 10, 8, 6, 6, 8, 10, 12};
        private final static int[] isolaniWeakerFactor = {-2, -4, -6, -8, -8, -6, -4, -2};
        private final static long[] rootKnights = {0x4200000000000000L, 0x0000000000000042L};
        private final static long[] rootBithops = {0x2400000000000000L, 0x0000000000000024L};
        private final static long[] rootRooks = {BoardUtils.squareBit[Board.A1] | BoardUtils.squareBit[Board.H1], BoardUtils.squareBit[Board.A8] | BoardUtils.squareBit[Board.H8]};
        private final static long[] rootQueens = {BoardUtils.squareBit[Board.D1], BoardUtils.squareBit[Board.D8]};
        private final static long[] d2e2 = {0x0018000000000000L, 0x0000000000001800L};
        private final static long[] rank7 = {0x000000000000FF00L, 0x00FF000000000000L};
        private final static long[] rank8 = {0x00000000000000FFL, 0xFF00000000000000L};
        private final static long[] rank58 = {0x00000000FFFFFFFFL, 0xFFFFFFFF00000000L};
        private final static int[] sideRank7 = {6, 1};
        private final static int[] sideRank8 = {7, 0};
        private final static long[][] squarePawnMask = new long[2][64];
        private final static long[][] passedPawnMask = new long[2][64];
        private final static long[] isolaniPawnMask = new long[8];
        private final static long centerFiles = 0x000000FFFF000000L;
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

        public int scorePawn = 100;
        public int scoreKnight = 350;
        public int scoreBishop = 350;
        public int scoreRook = 550;
        public int scoreQueen = 1100;
        public int scoreKing = 10000;
        public int scoreMinorNotDeveloped = -15;
        public int scoreNotCastled = -30;
        public int scoreKingMoved = -20;
        public int scoreEarlyQueenMove = -50;
        public int scoreEarlyRookMove = -50;
        public int scoreEarlyMinorRepeat = -15;
        public int scoreEarlyCenterRepeat = -12;
        public int scoreEarlyWingPawnMove = -9;
        public int scoreDoubledPawns = -20;
        public int scoreIsolatedPawns = -15;
        public int scoreBlockedPawns = -40;
        public int scorePawnNearKing = 40;
        public int scoreAllPawns = -10;
        public int scoreCenterPawns = 17;
        public int scoreBackwardPawns = -9;
        public int scorePassedPawns = 15;
        public int scorePawnBaseAttack = -18;
        public int scoreLockedPawns = -10;
        public int scoreAttackWeakPawn = 8;
        public int scoreKnightOnRim = -13;
        public int scoreOutpostKnight = 10;
        public int scorePinnedKnight = -30;
        public int scoreKnightTrapped = -250;
        public int scorePinnedBishop = -30;
        public int scoreOutpostBishop = 8;
        public int scoreFianchetto = 8;
        public int scoreGoodEndingBishop = 16;
        public int scoreBishopTrapped = -250;
        public int scoreDoubledBishops = 15;
        public int scoreRook7Rank = 30;
        public int scoreRooks7Rank = 30;
        public int scoreRookHalfFile = 5;
        public int scoreRookOpenFile = 8;
        public int scoreRookBehindPassedPawn = 6;
        public int scoreRookInFrontPassedPawn = -10;
        public int scorePinnedRook = -50;
        public int scoreRookTrapped = -10;
        public int scoreRookLiberated = 40;
        public int scorePinnedQueen = -90;
        public int scoreQueenNearKing = 12;
        public int scoreQueenNotPresent = -25;
        public int scoreGOpen = -30;
        public int scoreHOpen = -600;
        public int scoreKingOpenFile = -10;
        public int scoreKingOpenFile1 = -6;
        public int scoreKingEnemyOpenFile = -6;
        public int scoreKingDefenceDeficit = -50;
        public int scoreKingBackRankWeak = -40;
        public int scoreFianchettoTarget = -13;
        public int scoreRupture = -20;
        public int scoreHungedPiece = -20;

        public int[][] scorePawnPosition = {
        {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -30, -30, 0, 0, 0,
            1, 2, 3, -10, -10, 3, 2, 1,
            2, 4, 6, 8, 8, 6, 4, 2,
            3, 6, 9, 12, 12, 9, 6, 3,
            4, 8, 12, 16, 16, 12, 8, 4,
            5, 10, 15, 20, 20, 15, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0
        }, {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 10, 15, 20, 20, 15, 10, 5,
            4, 8, 12, 16, 16, 12, 8, 4,
            3, 6, 9, 12, 12, 9, 6, 3,
            2, 4, 6, 8, 8, 6, 4, 2,
            1, 2, 3, -10, -10, 3, 2, 1,
            0, 0, 0, -30, -30, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        }};

        public int[] scoreKnightPosition = {
            -10, -10, -10, -10, -10, -10, -10, -10,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 5, 5, 5, 0, -10,
            -10, 0, 5, 10, 10, 5, 0, -10,
            -10, 0, 5, 10, 10, 5, 0, -10,
            -10, 0, 5, 5, 5, 5, 0, -10,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, -10, -10, -10, -10, -10, -10, -10
        };

        public int[] scoreBishopPosition = {
              0, -5, -10, -10, -10, -10, -5,   0,
             -5,  2,   0,   0,   0,   0,  2,  -5,
            -10,  0,   6,   5,   5,   6,  0, -10,
            -10,  0,   5,  10,  10,   5,  0, -10,
            -10,  0,   5,  10,  10,   5,  0, -10,
            -10,  0,   6,   5,   5,   6,  0, -10,
             -5,  2,   0,   0,   0,   0,  2,  -5,
              0, -5, -10, -10, -10, -10, -5,   0
        };

        public int scoreKingPosition[] = {
            24, 24, 24, 16, 16, 0, 32, 32,
            24, 20, 16, 12, 12, 16, 20, 24,
            16, 12, 8, 4, 4, 8, 12, 16,
            12, 8, 4, 0, 0, 4, 8, 12,
            12, 8, 4, 0, 0, 4, 8, 12,
            16, 12, 8, 4, 4, 8, 12, 16,
            24, 20, 16, 12, 12, 16, 20, 24,
            24, 24, 24, 16, 16, 0, 32, 32
        };

        public int scoreKingEndingPosition[] = {
            0, 6, 12, 18, 18, 12, 6, 0,
            6, 12, 18, 24, 24, 18, 12, 6,
            12, 18, 24, 32, 32, 24, 18, 12,
            18, 24, 32, 48, 48, 32, 24, 18,
            18, 24, 32, 48, 48, 32, 24, 18,
            12, 18, 24, 32, 32, 24, 18, 12,
            6, 12, 18, 24, 24, 18, 12, 6,
            0, 6, 12, 18, 18, 12, 6, 0
        };

        private static void initPassedPawnMasks() {

            byte square;
            for (square = 0; square < 64; square++) {
                passedPawnMask[Board.WHITE][square] = 0;
                passedPawnMask[Board.BLACK][square] = 0;
            }
            for (square = 0; square < 64; square++) {
                passedPawnMask[Board.WHITE][square] = BoardUtils.ray[square][7];
                if (Board.getSquareFile(square) != 0)
                    passedPawnMask[Board.WHITE][square] |= BoardUtils.ray[square - 1][7];
                if (Board.getSquareFile(square) != 7)
                    passedPawnMask[Board.WHITE][square] |= BoardUtils.ray[square + 1][7];
            }
            for (square = 0; square < 64; square++) {
                passedPawnMask[Board.BLACK][square] = BoardUtils.ray[square][4];
                if (Board.getSquareFile(square) != 0)
                    passedPawnMask[Board.BLACK][square] |= BoardUtils.ray[square - 1][4];
                if (Board.getSquareFile(square) != 7)
                    passedPawnMask[Board.BLACK][square] |= BoardUtils.ray[square + 1][4];
            }
        }

        private static void initIsolaniPawnMask() {

            isolaniPawnMask[0] = BoardUtils.fileBits[1];
            isolaniPawnMask[7] = BoardUtils.fileBits[6];
            for (int i = 1; i <= 6; i++) {
                isolaniPawnMask[i] = BoardUtils.fileBits[i - 1] | BoardUtils.fileBits[i + 1];
            }
        }

        private static void initSquarePawnMask() {
            byte sq;
            int len, i, j;
            for (sq = 0; sq < 64; sq++) {
                len = 7 - Board.getSquareRank(sq);
                i = Math.max(sq & 56, sq - len);
                j = Math.min(sq | 7, sq + len);
                while (i <= j) {
                    squarePawnMask[Board.WHITE][sq] |= (BoardUtils.squareBit[i] | BoardUtils.fromtoRay[i][i | 56]);
                    i++;
                }

                len = Board.getSquareRank(sq);
                i = Math.max(sq & 56, sq - len);
                j = Math.min(sq | 7, sq + len);
                while (i <= j) {
                    squarePawnMask[Board.BLACK][sq] |= (BoardUtils.squareBit[i] | BoardUtils.fromtoRay[i][i & 7]);
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
            initPassedPawnMasks();
            initIsolaniPawnMask();
            initSquarePawnMask();
        }

        private Map<String, Integer> scores;
        private byte kingSquare[] = new byte[2];
        private long passedPawns[] = new long[2];
        private long weakPawns[] = new long[2];
        private long pinned;
        private int phase;
        private long[][] squaresAttacked;
        private long[] squaresAttackedBySide;

        public int evaluate(Board board) {

            int materialWhite = evaluateMaterial(board, Board.WHITE);
            int materialBlack = evaluateMaterial(board, Board.BLACK);
            int originalMaterial = (scorePawn * 16) + (scoreKnight * 4) + (scoreBishop * 4) + (scoreRook * 4) + (scoreQueen * 2);
            int actualMaterial = materialWhite + materialBlack - (2 * scoreKing);
            phase = PHASENUMBER - (int) (((double) actualMaterial * (double) PHASENUMBER) / (double) originalMaterial);
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
            score += (board.getSideToMove() == Board.WHITE) ? 15 : -15;
            score += scoreHungedPiece * (getHungedPiecesCount(board, Board.WHITE) - getHungedPiecesCount(board, Board.BLACK));
            return score;
        }

        private int evaluateMaterial(Board board, byte side) {

            int score = 0;
            for (byte square = Board.A1; square <= Board.H8; square++) {
                if (board.getSquareSide(square) == side) {
                    switch (board.getSquareFigure(square)) {
                        case Board.PAWN:
                            score += scorePawn;
                            break;
                        case Board.KNIGHT:
                            score += scoreKnight;
                            break;
                        case Board.BISHOP:
                            score += scoreBishop;
                            break;
                        case Board.ROOK:
                            score += scoreRook;
                            break;
                        case Board.QUEEN:
                            score += scoreQueen;
                            break;
                        case Board.KING:
                            score += scoreKing;
                            break;
                        default:
                            continue;
                    }
                }
            }
            return score;
        }

        private int evaluateDevelopment(Board board, byte side) {

            int score = 0;
            if (phase <= 2) {
                long[][] pieces = board.getPieces();
                long movers = (pieces[side][Board.KNIGHT] & rootKnights[side]) | (pieces[side][Board.BISHOP] & rootBithops[side]);
                int piecesNotDeveloped = BoardUtils.getBitCount(movers);
                if (piecesNotDeveloped > 0) {
                    score += (piecesNotDeveloped * scoreMinorNotDeveloped);
                    if ((pieces[side][Board.QUEEN] & rootQueens[side]) == 0)
                        score += piecesNotDeveloped * scoreEarlyQueenMove;
                    if ((pieces[side][Board.ROOK] & rootRooks[side]) == 0)
                        score += piecesNotDeveloped * scoreEarlyRookMove;
                }

                byte friendlyKing = kingSquare[side];
                if (friendlyKing != Board.INVALIDSQUARE) {
                    byte originalKingRank = side == Board.WHITE ? Board.RANK_1 : Board.RANK_8;
                    if (Board.getSquareRank(friendlyKing) != originalKingRank) {
                        score += scoreKingMoved;
                    } else {
                        long sideRooks = pieces[side][Board.ROOK];
                        if (((BoardUtils.ray[friendlyKing][5] & sideRooks) != 0) && ((BoardUtils.ray[friendlyKing][6] & sideRooks) != 0))
                            score += scoreNotCastled;
                    }
                }
            }
            return score;
        }

        private int evaluatePawns(Board board, byte side) {

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
                square = (byte) BoardUtils.getLeastSignificantBit(movers);
                movers &= BoardUtils.squareBitX[square];
                score += scorePawnPosition[side][square];

                //Verificar si es un peon pasado
                if ((xsidePawns & passedPawnMask[side][square]) == 0) {
                    if ((side == Board.WHITE && (BoardUtils.fromtoRay[square][square | 56] & sidePawns) == 0) || (side == Board.BLACK && (BoardUtils.fromtoRay[square][square & 7] & sidePawns) == 0)) {
                        passedPawns[side] |= BoardUtils.squareBit[square];
                        score += (scorePassedPawns * ((side == Board.WHITE) ? board.getSquareRank(square) : (7 - board.getSquareRank(square))) * phase) / 12;
                    }
                }

                //Verificar si es un peon debil
                testsquare = (byte) (square + (side == Board.WHITE ? 8 : -8));
                if (((passedPawnMask[xside][testsquare] & ~BoardUtils.fileBits[Board.getSquareFile(square)] & sidePawns) == 0) && board.getSquareFigure(testsquare) != Board.PAWN) {
                    int nbits1 = BoardUtils.getBitCount(sidePawns & BoardUtils.moveArray[xside == Board.WHITE ? Board.PAWN : Board.BPAWN][testsquare]);
                    int nbits2 = BoardUtils.getBitCount(xsidePawns & BoardUtils.moveArray[side == Board.WHITE ? Board.PAWN : Board.BPAWN][testsquare]);
                    if (nbits1 < nbits2) {
                        weakPawns[side] |= BoardUtils.squareBit[square];
                        score += scoreBackwardPawns;
                    }
                }

                //Ataque al peon base
                pawnMoves = BoardUtils.moveArray[side == Board.WHITE ? Board.PAWN : Board.BPAWN][square];
                if (((pawnMoves & sidePawns) != 0) && ((pawnMoves & xsidePawns) != 0))
                    score += scorePawnBaseAttack;

                //Incrementar la cantidad de peones de la columna
                pawnCounter[Board.getSquareFile(square)]++;
            }

            for (int fileindex = 0; fileindex <= 7; fileindex++) {
                //Peones doblados
                if (pawnCounter[fileindex] > 1) score += scoreDoubledPawns;

                //Peones isolados
                if ((pawnCounter[fileindex] > 0) && ((sidePawns & isolaniPawnMask[fileindex]) == 0)) {
                    if ((BoardUtils.fileBits[fileindex] & xsidePawns) == 0)
                        score += (scoreIsolatedPawns + isolaniWeakerFactor[fileindex]) * pawnCounter[fileindex];
                    else
                        score += (scoreIsolatedPawns + isolaniNormalFactor[fileindex]) * pawnCounter[fileindex];
                    weakPawns[side] |= (sidePawns & BoardUtils.fileBits[fileindex]);
                }
            }

            //Favorecer el tener peones en el centro
            score += BoardUtils.getBitCount(sidePawns & centerFiles) * scoreCenterPawns;

            //Calcular ataques al Rey
            byte xsideKingSquare = kingSquare[xside];
            if (xsideKingSquare != Board.INVALIDSQUARE) {
                long sideQueens = pieces[side][Board.QUEEN];
                if (side == Board.WHITE && (sideQueens != 0) && ((BoardUtils.squareBit[board.C6] | BoardUtils.squareBit[board.F6]) & sidePawns) != 0) {
                    if (sidePawns != 0 && BoardUtils.squareBit[board.F6] != 0 && xsideKingSquare > board.H6 && BoardUtils.distance[xsideKingSquare][board.G7] == 1)
                        score += scorePawnNearKing;
                    if (sidePawns != 0 && BoardUtils.squareBit[board.C6] != 0 && xsideKingSquare > board.H6 && BoardUtils.distance[xsideKingSquare][board.B7] == 1)
                        score += scorePawnNearKing;
                } else if (side == Board.BLACK && (sideQueens != 0) && ((BoardUtils.squareBit[board.C3] | BoardUtils.squareBit[board.F3]) & sidePawns) != 0) {
                    if (sidePawns != 0 && BoardUtils.squareBit[board.F3] != 0 && xsideKingSquare < board.A3 && BoardUtils.distance[xsideKingSquare][board.G2] == 1)
                        score += scorePawnNearKing;
                    if (sidePawns != 0 && BoardUtils.squareBit[board.C3] != 0 && xsideKingSquare < board.A3 && BoardUtils.distance[xsideKingSquare][board.B2] == 1)
                        score += scorePawnNearKing;
                }
            }

            //Calcular peones bloqueados en e2, d2, e7, d7
            movers = board.getBlocker();
            if (side == Board.WHITE && (((sidePawns & d2e2[Board.WHITE]) >>> 8) & movers) != 0)
                score += scoreBlockedPawns;
            if (side == Board.BLACK && (((sidePawns & d2e2[Board.BLACK]) << 8) & movers) != 0)
                score += scoreBlockedPawns;

            //Peones pasados fuera del alcance del ray
            if (passedPawns[side] != 0 && pieces[xside][Board.PAWN] == 0) {
                movers = passedPawns[side];
                while (movers != 0) {
                    square = (byte) BoardUtils.getLeastSignificantBit(movers);
                    movers &= BoardUtils.squareBitX[square];
                    if (board.getSideToMove() == side) {
                        if ((squarePawnMask[side][square] & pieces[xside][Board.KING]) == 0)
                            score += scoreQueen * passedPawnFactor[side][Board.getSquareRank(square)] / 550;
                        else if (kingSquare[xside] != Board.INVALIDSQUARE && (BoardUtils.moveArray[Board.KING][kingSquare[xside]] & squarePawnMask[side][square]) == 0)
                            score += scoreQueen * passedPawnFactor[side][Board.getSquareRank(square)] / 550;
                    }
                }
            }

            //Favorecer tormeta de peones si los reyes han enrocado en direcciones opuestas
            if (kingSquare[side] != Board.INVALIDSQUARE && kingSquare[xside] != Board.INVALIDSQUARE) {
                movers = pieces[side][Board.PAWN];
                if (Math.abs(Board.getSquareFile(kingSquare[side]) - Board.getSquareFile(kingSquare[xside])) >= 4 && phase < 6) {
                    byte xsideKingFile = Board.getSquareFile(kingSquare[xside]);
                    long pawnsInKingsColumns = (isolaniPawnMask[xsideKingFile] | BoardUtils.fileBits[xsideKingFile]) & movers;
                    while (pawnsInKingsColumns != 0) {
                        square = (byte) BoardUtils.getLeastSignificantBit(pawnsInKingsColumns);
                        pawnsInKingsColumns &= BoardUtils.squareBitX[square];
                        score += 10 * (5 - BoardUtils.distance[square][kingSquare[xside]]);
                    }
                }
            }

            return score;
        }

        private int evaluateKnights(Board board, byte side) {

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
                score += scorePinnedKnight * BoardUtils.getBitCount(knights & pinned);
            while (knights != 0) {
                square = (byte) BoardUtils.getLeastSignificantBit(knights);
                knights &= BoardUtils.squareBitX[square];
                tempScore = evaluateControl(board, square, side);
                tempScore += scoreKnightPosition[square];
                if (outpost[side][square] == 1 && ((enemyPawns & isolaniPawnMask[Board.getSquareFile(square)] & passedPawnMask[side][square]) == 0)) {
                    tempScore += scoreOutpostKnight;
                    if ((BoardUtils.moveArray[xside == Board.WHITE ? Board.PAWN : Board.BPAWN][square] & pieces[side][Board.PAWN]) != 0)
                        tempScore += scoreOutpostKnight;
                }
                if ((BoardUtils.moveArray[Board.KNIGHT][square] & weakPawns[xside]) != 0)
                    tempScore += scoreAttackWeakPawn;
                score += tempScore;
            }
            return score;
        }

        private int evaluateBishops(Board board, byte side) {

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
                score += scorePinnedBishop * BoardUtils.getBitCount(bishops & pinned);
            while (bishops != 0) {
                square = (byte) BoardUtils.getLeastSignificantBit(bishops);
                bishops &= BoardUtils.squareBitX[square];
                bishopCount++;
                tempScore = evaluateControl(board, square, side);
                tempScore += scoreBishopPosition[square];
                if (outpost[side][square] == 1 && (enemyPawns & isolaniPawnMask[Board.getSquareFile(square)] & passedPawnMask[side][square]) == 0) {
                    tempScore += scoreOutpostBishop;
                    if ((BoardUtils.moveArray[xside == Board.WHITE ? Board.PAWN : Board.BPAWN][square] & pieces[side][Board.PAWN]) != 0)
                        tempScore += scoreOutpostBishop;
                }
                if (kingSquare[side] != Board.INVALIDSQUARE) {
                    if (side == Board.WHITE) {
                        if (kingSquare[side] >= Board.F1 && kingSquare[side] <= Board.H1 && square == Board.G2)
                            tempScore += scoreFianchetto;
                        if (kingSquare[side] >= Board.A1 && kingSquare[side] <= Board.C1 && square == Board.B2)
                            tempScore += scoreFianchetto;
                    } else {
                        if (kingSquare[side] >= Board.F8 && kingSquare[side] <= Board.H8 && square == Board.G7)
                            tempScore += scoreFianchetto;
                        if (kingSquare[side] >= Board.A8 && kingSquare[side] <= Board.C8 && square == Board.B7)
                            tempScore += scoreFianchetto;
                    }
                }
                if ((board.getBishopAttacks(square) & weakPawns[xside]) != 0)
                    tempScore += scoreAttackWeakPawn;
                score += tempScore;
            }
            if (bishopCount > 1)
                score += scoreDoubledBishops;
            return score;
        }

        private int evaluateRooks(Board board, byte side) {

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
                score += scorePinnedRook * BoardUtils.getBitCount(rooks & pinned);
            while (rooks != 0) {
                square = (byte) BoardUtils.getLeastSignificantBit(rooks);
                rooks &= BoardUtils.squareBitX[square];
                tempScore = evaluateControl(board, square, side);
                fyle = Board.getSquareFile(square);
                if (phase < 7) {
                    if ((pieces[side][Board.PAWN] & BoardUtils.fileBits[fyle]) == 0) {
                        if (enemyKingSquare != Board.INVALIDSQUARE && (fyle == 5 && Board.getSquareFile(enemyKingSquare) >= Board.FILE_E))
                            tempScore += scoreRookLiberated;
                        tempScore += scoreRookHalfFile;
                        if ((pieces[xside][Board.PAWN] & BoardUtils.fileBits[fyle]) == 0)
                            tempScore += scoreRookOpenFile;
                    }
                }
                if (phase > 6) {
                    if ((BoardUtils.fileBits[fyle] & passedPawns[Board.WHITE] & rank58[Board.WHITE]) != 0) {
                        if (BoardUtils.getBitCount(BoardUtils.ray[square][7] & passedPawns[Board.WHITE]) == 1)
                            tempScore += scoreRookBehindPassedPawn;
                        else if ((BoardUtils.ray[square][4] & passedPawns[Board.WHITE]) != 0)
                            tempScore += scoreRookInFrontPassedPawn;
                    }
                    if ((BoardUtils.fileBits[fyle] & passedPawns[Board.BLACK] & rank58[Board.BLACK]) != 0) {
                        if (BoardUtils.getBitCount(BoardUtils.ray[square][4] & passedPawns[Board.BLACK]) == 1)
                            tempScore += scoreRookBehindPassedPawn;
                        else if ((BoardUtils.ray[square][7] & passedPawns[Board.BLACK]) != 0)
                            tempScore += scoreRookInFrontPassedPawn;
                    }
                }
                if ((board.getRookAttacks(square) & weakPawns[xside]) != 0)
                    tempScore += scoreAttackWeakPawn;
                if (Board.getSquareRank(square) == sideRank7[side] && ((enemyKingSquare != Board.INVALIDSQUARE && Board.getSquareRank(enemyKingSquare) == sideRank8[side]) || ((pieces[xside][Board.PAWN] & BoardUtils.rankBits[Board.getSquareRank(square)]) != 0)))
                    tempScore += scoreRook7Rank;
                score += tempScore;
            }

            if (BoardUtils.getBitCount((pieces[side][Board.QUEEN] | pieces[side][Board.ROOK]) & rank7[side]) > 1 && (((pieces[xside][Board.KING] & rank8[side]) > 0) || ((pieces[xside][Board.PAWN] & rank7[side]) > 0)))
                score += scoreRooks7Rank;
            return score;
        }

        private int evaluateQueens(Board board, byte side) {

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
                score += scorePinnedQueen * BoardUtils.getBitCount(queens & pinned);
            while (queens != 0) {
                square = (byte) BoardUtils.getLeastSignificantBit(queens);
                queens &= BoardUtils.squareBitX[square];
                tempScore = evaluateControl(board, square, side);
                if (enemyKing != Board.INVALIDSQUARE && BoardUtils.distance[square][enemyKing] <= 2)
                    tempScore += scoreQueenNearKing;
                if ((board.getQueenAttacks(square) & weakPawns[xside]) != 0)
                    tempScore += scoreAttackWeakPawn;
                score += tempScore;
            }
            return score;
        }

        private int evaluateKing(Board board, byte side) {

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
                score += ((6 - phase) * scoreKingPosition[square] + phase * scoreKingEndingPosition[square]) / 6;

                n = 0;
                if (side == Board.WHITE) {
                    if (rank < Board.RANK_8)
                        n = BoardUtils.getBitCount(BoardUtils.moveArray[Board.KING][square] & sidePawns & BoardUtils.rankBits[rank + 1]);
                } else {
                    if (rank > Board.RANK_1)
                        n = BoardUtils.getBitCount(BoardUtils.moveArray[Board.KING][square] & sidePawns & BoardUtils.rankBits[rank - 1]);
                }
                score += pawncover[n];

                if ((BoardUtils.fileBits[file] & sidePawns) == 0)
                    score += scoreKingOpenFile;

                if ((BoardUtils.fileBits[file] & sidePawns) == 0)
                    score += scoreKingOpenFile1;

                switch (file) {
                    case Board.FILE_A:
                    case Board.FILE_E:
                    case Board.FILE_F:
                    case Board.FILE_G:
                        if ((BoardUtils.fileBits[file + 1] & sidePawns) == 0)
                            score += scoreKingOpenFile;
                        if ((BoardUtils.fileBits[file + 1] & sidePawns) == 0)
                            score += scoreKingOpenFile1;
                        break;
                    case Board.FILE_H:
                    case Board.FILE_D:
                    case Board.FILE_C:
                    case Board.FILE_B:
                        if ((BoardUtils.fileBits[file - 1] & sidePawns) == 0)
                            score += scoreKingOpenFile;
                        if ((BoardUtils.fileBits[file - 1] & sidePawns) == 0)
                            score += scoreKingOpenFile1;
                        break;
                    default:
                        break;
                }

                if (file > Board.FILE_E && Board.getSquareFile(kingSquare[xside]) < Board.FILE_D) {
                    square2 = (side == Board.WHITE) ? Board.G3 : Board.G6;
                    if ((BoardUtils.squareBit[square2] & sidePawns) != 0)
                        if (((BoardUtils.squareBit[Board.F4] | BoardUtils.squareBit[Board.H4] | BoardUtils.squareBit[Board.F5] | BoardUtils.squareBit[Board.H5]) & pieces[xside][Board.PAWN]) != 0)
                            score += scoreFianchettoTarget;
                } else if (file < Board.FILE_E && Board.getSquareFile(kingSquare[xside]) > Board.FILE_E) {
                    square2 = (side == Board.WHITE) ? Board.B3 : Board.B6;
                    if ((BoardUtils.squareBit[square2] & sidePawns) != 0)
                        if (((BoardUtils.squareBit[Board.A4] | BoardUtils.squareBit[Board.C4] | BoardUtils.squareBit[Board.A5] | BoardUtils.squareBit[Board.C5]) & pieces[xside][Board.PAWN]) != 0)
                            score += scoreFianchettoTarget;
                }

                x = BoardUtils.boardHalf[side] & BoardUtils.boardSide[file <= Board.FILE_D ? 1 : 0];
                n1 = BoardUtils.getBitCount(x & (friends[xside]));
                if (n1 > 0) {
                    n2 = BoardUtils.getBitCount(x & (friends[side] & ~sidePawns & ~pieces[side][Board.KING]));
                    if (n1 > n2)
                        score += (n1 - n2) * scoreKingDefenceDeficit;
                }
                score = (score * phaseFactor[phase]) / 8;
            } else {
                score += scoreKingEndingPosition[square];
                score += evaluateControl(board, square, side);
                b = (pieces[Board.WHITE][Board.PAWN] | pieces[Board.BLACK][Board.PAWN]);
                while (b != 0) {
                    square1 = (byte) BoardUtils.getLeastSignificantBit(b);
                    b &= BoardUtils.squareBitX[square1];
                    if ((BoardUtils.squareBit[square1] & pieces[Board.WHITE][Board.PAWN]) != 0)
                        score -= BoardUtils.distance[square][square1 + 8] * 10 - 5;
                    else if ((BoardUtils.squareBit[square1] & pieces[Board.BLACK][Board.PAWN]) != 0)
                        score -= BoardUtils.distance[square][square1 - 8] * 10 - 5;
                    else
                        score -= BoardUtils.distance[square][square1] - 5;
                }

                if ((BoardUtils.moveArray[Board.KING][square] & weakPawns[xside]) != 0)
                    score += scoreAttackWeakPawn * 2;
            }

            if (phase >= 4) {
                if (side == Board.WHITE) {
                    if (square < Board.A2)
                        if ((BoardUtils.moveArray[Board.KING][square] & (~pieces[side][Board.PAWN] & BoardUtils.rankBits[1])) == 0)
                            score += scoreKingBackRankWeak;
                } else {
                    if (square > Board.H7)
                        if ((BoardUtils.moveArray[Board.KING][square] & (~pieces[side][Board.PAWN] & BoardUtils.rankBits[6])) == 0)
                            score += scoreKingBackRankWeak;
                }
            }
            return score;
        }

        private int evaluateControl(Board board, byte square, byte side) {

            int score = 0;
            long controlled = board.getSquareXAttacks(square, side);
            score += (4 * BoardUtils.getBitCount(controlled & BoardUtils.boxes[0]));
            score += (4 * BoardUtils.getBitCount(controlled));
            byte enemyKing = kingSquare[1 ^ side];
            if (enemyKing != Board.INVALIDSQUARE)
                score += BoardUtils.getBitCount(controlled & BoardUtils.distMap[enemyKing][2]);
            byte friendlyKing = kingSquare[side];
            if (friendlyKing != Board.INVALIDSQUARE)
                score += BoardUtils.getBitCount(controlled & BoardUtils.distMap[friendlyKing][2]);
            return score;
        }

        private int getHungedPiecesCount(Board board, byte side) {

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
                hunged += BoardUtils.getBitCount(c);
            if ((squaresAttackedBySide[xside] & pieces[side][Board.KING]) != 0)
                hunged++;
            return hunged;
        }

        private long getPinnedPieces(Board board) {

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
                    square = (byte) BoardUtils.getLeastSignificantBit(b);
                    b &= BoardUtils.squareBitX[square];
                    c = BoardUtils.moveArray[Board.BISHOP][square] & e;
                    while (c != 0) {
                        square2 = (byte) BoardUtils.getLeastSignificantBit(c);
                        c &= BoardUtils.squareBitX[square2];
                        f = t & BoardUtils.squareBitX[square] & BoardUtils.fromtoRay[square2][square];
                        if (((friends[xside] & f) != 0) && BoardUtils.getBitCount(f) == 1)
                            pin |= f;
                    }
                }

                e = p[Board.QUEEN] | p[Board.KING];
                e |= (p[Board.ROOK] | p[Board.BISHOP] | p[Board.KNIGHT]) & ~squaresAttackedBySide[xside];
                b = pieces[side][Board.ROOK];
                while (b != 0) {
                    square = (byte) BoardUtils.getLeastSignificantBit(b);
                    b &= BoardUtils.squareBitX[square];
                    c = BoardUtils.moveArray[Board.ROOK][square] & e;
                    while (c != 0) {
                        square2 = (byte) BoardUtils.getLeastSignificantBit(c);
                        c &= BoardUtils.squareBitX[square2];
                        f = t & BoardUtils.squareBitX[square] & BoardUtils.fromtoRay[square2][square];
                        if (((friends[xside] & f) != 0) && BoardUtils.getBitCount(f) == 1)
                            pin |= f;
                    }
                }

                e = pieces[xside][Board.KING];
                e |= (p[Board.QUEEN] | p[Board.ROOK] | p[Board.BISHOP] | p[Board.KNIGHT]) & ~squaresAttackedBySide[xside];
                b = pieces[side][Board.QUEEN];
                while (b != 0) {
                    square = (byte) BoardUtils.getLeastSignificantBit(b);
                    b &= BoardUtils.squareBitX[square];
                    c = BoardUtils.moveArray[Board.QUEEN][square] & e;
                    while (c != 0) {
                        square2 = (byte) BoardUtils.getLeastSignificantBit(c);
                        c &= BoardUtils.squareBitX[square2];
                        f = t & BoardUtils.squareBitX[square] & BoardUtils.fromtoRay[square2][square];
                        if (((friends[xside] & f) != 0) && BoardUtils.getBitCount(f) == 1)
                            pin |= f;
                    }
                }
            }

            return pin;
        }
    }
}
