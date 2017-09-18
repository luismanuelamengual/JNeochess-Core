package org.neochess.core.searchengine;

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
    public static final byte WHITE_PAWN = 0;
    public static final byte WHITE_KNIGHT = 1;
    public static final byte WHITE_BISHOP = 2;
    public static final byte WHITE_ROOK = 3;
    public static final byte WHITE_QUEEN = 4;
    public static final byte WHITE_KING = 5;
    public static final byte BLACK_PAWN = 6;
    public static final byte BLACK_KNIGHT = 7;
    public static final byte BLACK_BISHOP = 8;
    public static final byte BLACK_ROOK = 9;
    public static final byte BLACK_QUEEN = 10;
    public static final byte BLACK_KING = 11;

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

    public static final long MOVE_FROM_SQUARE_MASK = 0xFFL;
    public static final long MOVE_TO_SQUARE_MASK = 0xFF00L;
    public static final long MOVE_PROMOTION_PIECE_MASK = 0xFF0000L;
    public static final long MOVE_CAPTURED_PIECE_MASK = 0xFF000000L;
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
        for (byte piece = WHITE_PAWN; piece <= BLACK_KING; piece++) {
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
    }

    public Board () {}

    public Board (Board copyBoard) {
        copy(copyBoard);
    }

    public Board clone () {
        return new Board(this);
    }

    public void copy (Board board) {
        for (byte square = A1; square <= H8; square++) {
            squareSide[square] = board.squareSide[square];
            squareFigure[square] = board.squareFigure[square];
        }
        for (byte figure = PAWN; figure <= KING; figure++) {
            pieces[WHITE][figure] = board.pieces[WHITE][figure];
            pieces[BLACK][figure] = board.pieces[BLACK][figure];
        }
        friends[WHITE] = board.friends[WHITE];
        friends[BLACK] = board.friends[BLACK];
        blocker = board.blocker;
        blockerr90 = board.blockerr90;
        blockerr45 = board.blockerr45;
        blockerr315 = board.blockerr315;
        epSquare = board.epSquare;
        castleState = board.castleState;
        sideToMove = board.sideToMove;
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

    public static byte getPiece (byte side, byte figure) {
        return side == NOSIDE? EMPTY : (byte)((side * 6) + figure);
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

    public byte getEpSquare() {
        return epSquare;
    }

    public long[] getFriends () {
        return friends;
    }

    public long getBlocker() {
        return blocker;
    }

    public byte getSideToMove() {
        return sideToMove;
    }

    public long[][] getPieces() {
        return pieces;
    }

    public byte getSquareSide (byte square) {
        return squareSide[square];
    }

    public byte getSquareFigure (byte square) {
        return squareFigure[square];
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
            blocker |= BoardUtils.squareBit[square];
            blockerr90 |= BoardUtils.squareBit90[square];
            blockerr45 |= BoardUtils.squareBit45[square];
            blockerr315 |= BoardUtils.squareBit315[square];
            friends[pieceSide] |= BoardUtils.squareBit[square];
        }
        else {
            if (pieceSide != squareSide[square]) {
                friends[squareSide[square]] &= BoardUtils.squareBitX[square];
                friends[pieceSide] |= BoardUtils.squareBit[square];
            }
            pieces[squareSide[square]][squareFigure[square]] &= BoardUtils.squareBitX[square];
        }

        pieces[pieceSide][pieceFigure] |= BoardUtils.squareBit[square];
        squareSide[square] = pieceSide;
        squareFigure[square] = pieceFigure;
    }

    public byte getPiece (byte square) {
        return squareSide[square] == NOSIDE? EMPTY : (byte)((squareSide[square]*6) + squareFigure[square]);
    }

    public void removePiece (byte square) {
        blocker &= BoardUtils.squareBitX[square];
        blockerr90 &= BoardUtils.squareBitX90[square];
        blockerr45 &= BoardUtils.squareBitX45[square];
        blockerr315 &= BoardUtils.squareBitX315[square];
        friends[squareSide[square]] &= BoardUtils.squareBitX[square];
        pieces[squareSide[square]][squareFigure[square]] &= BoardUtils.squareBitX[square];
        squareSide[square] = NOSIDE;
        squareFigure[square] = EMPTY;
    }

    public void clear () {

        for (byte square = A1; square <= H8; square++) {
            squareSide[square] = NOSIDE;
            squareFigure[square] = EMPTY;
        }
        for (byte side = WHITE; side <= BLACK; side++) {
            friends[side] = BoardUtils.NULLBITBOARD;
            for (byte figure = PAWN; figure <= KING; figure++) {
                pieces[side][figure] = BoardUtils.NULLBITBOARD;
            }
        }
        blocker = BoardUtils.NULLBITBOARD;
        blockerr90 = BoardUtils.NULLBITBOARD;
        blockerr45 = BoardUtils.NULLBITBOARD;
        blockerr315 = BoardUtils.NULLBITBOARD;
        epSquare = INVALIDSQUARE;
        castleState = NOCASTLE;
        sideToMove = NOSIDE;
    }

    public void setStartupPosition () {
        clear();

        putPiece (A1, WHITE_ROOK);
        putPiece (B1, WHITE_KNIGHT);
        putPiece (C1, WHITE_BISHOP);
        putPiece (D1, WHITE_QUEEN);
        putPiece (E1, WHITE_KING);
        putPiece (F1, WHITE_BISHOP);
        putPiece (G1, WHITE_KNIGHT);
        putPiece (H1, WHITE_ROOK);
        putPiece (A2, WHITE_PAWN);
        putPiece (B2, WHITE_PAWN);
        putPiece (C2, WHITE_PAWN);
        putPiece (D2, WHITE_PAWN);
        putPiece (E2, WHITE_PAWN);
        putPiece (F2, WHITE_PAWN);
        putPiece (G2, WHITE_PAWN);
        putPiece (H2, WHITE_PAWN);

        putPiece (A8, BLACK_ROOK);
        putPiece (B8, BLACK_KNIGHT);
        putPiece (C8, BLACK_BISHOP);
        putPiece (D8, BLACK_QUEEN);
        putPiece (E8, BLACK_KING);
        putPiece (F8, BLACK_BISHOP);
        putPiece (G8, BLACK_KNIGHT);
        putPiece (H8, BLACK_ROOK);
        putPiece (A7, BLACK_PAWN);
        putPiece (B7, BLACK_PAWN);
        putPiece (C7, BLACK_PAWN);
        putPiece (D7, BLACK_PAWN);
        putPiece (E7, BLACK_PAWN);
        putPiece (F7, BLACK_PAWN);
        putPiece (G7, BLACK_PAWN);
        putPiece (H7, BLACK_PAWN);

        sideToMove = WHITE;
        epSquare = INVALIDSQUARE;
        castleState = WHITECASTLESHORT | WHITECASTLELONG | BLACKCASTLESHORT | BLACKCASTLELONG;
    }

    private long createMove (byte fromSquare, byte toSquare) {
        return createMove(fromSquare, toSquare, EMPTY);
    }

    private long createMove (byte fromSquare, byte toSquare, byte promotionPiece) {
        return (fromSquare << MOVE_FROM_SQUARE_OFFSET) | (toSquare << MOVE_TO_SQUARE_OFFSET) | ((long)promotionPiece << MOVE_PROMOTION_PIECE_OFFSET);
    }

    public long getMove(byte fromSquare, byte toSquare) {

        long foundMove = 0;
        long[] moves = new long[100];
        generateLegalMoves(moves);
        for (int i = 0; i < moves.length; i++) {
            long move = moves[i];
            if (move == 0) {
                break;
            }
            byte moveFromSquare = (byte)((move & MOVE_FROM_SQUARE_MASK) >>> MOVE_FROM_SQUARE_OFFSET);
            byte moveToSquare = (byte)((move & MOVE_TO_SQUARE_MASK) >>> MOVE_TO_SQUARE_OFFSET);
            if (fromSquare == moveFromSquare && toSquare == moveToSquare) {
                foundMove = move;
                break;
            }
        }
        return foundMove;
    }

    public long makeMove (long move) {

        byte fromSquare = (byte)((move & MOVE_FROM_SQUARE_MASK) >>> MOVE_FROM_SQUARE_OFFSET);
        byte toSquare = (byte)((move & MOVE_TO_SQUARE_MASK) >>> MOVE_TO_SQUARE_OFFSET);
        byte movingPiece = getPiece(fromSquare);
        byte movingFigure = squareFigure[fromSquare];
        byte capturedPiece = getPiece(toSquare);

        long appliedMove = move;
        appliedMove &= ~(MOVE_EP_SQUARE_MASK | MOVE_CAPTURED_PIECE_MASK | MOVE_CASTLE_STATE_MASK);
        appliedMove |= ((long)epSquare << MOVE_EP_SQUARE_OFFSET) | ((long)capturedPiece << MOVE_CAPTURED_PIECE_OFFSET) | ((long)castleState << MOVE_CASTLE_STATE_OFFSET);

        if (movingFigure == PAWN) {
            if (sideToMove == WHITE) {
                if (getSquareRank(toSquare) == RANK_8) {
                    movingPiece = (byte)((move & MOVE_PROMOTION_PIECE_MASK) >>> MOVE_PROMOTION_PIECE_OFFSET);
                }
                else if (toSquare == epSquare) {
                    removePiece((byte) (toSquare - 8));
                }
            }
            else {
                if (getSquareRank(toSquare) == RANK_1) {
                    movingPiece = (byte)((move & MOVE_PROMOTION_PIECE_MASK) >>> MOVE_PROMOTION_PIECE_OFFSET);
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
                            putPiece(F1, WHITE_ROOK);
                            break;
                        case C1:
                            removePiece(A1);
                            putPiece(D1, WHITE_ROOK);
                            break;
                    }
                }
                else if (fromSquare == E8) {
                    switch (toSquare) {
                        case G8:
                            removePiece(H8);
                            putPiece(F8, BLACK_ROOK);
                            break;
                        case C8:
                            removePiece(A8);
                            putPiece(D8, BLACK_ROOK);
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
        byte promotionPiece = (byte)((move & MOVE_PROMOTION_PIECE_MASK) >>> MOVE_PROMOTION_PIECE_OFFSET);
        byte lastCastleState = (byte)((move & MOVE_CASTLE_STATE_MASK) >>> MOVE_CASTLE_STATE_OFFSET);
        byte lastEpSquare = (byte)((move & MOVE_EP_SQUARE_MASK) >>> MOVE_EP_SQUARE_OFFSET);
        byte movingPiece = promotionPiece != EMPTY? getPiece(getPieceSide(promotionPiece),PAWN) : getPiece(toSquare);
        byte movingFigure = getPieceFigure(movingPiece);
        byte movingSide = getPieceSide(movingPiece);

        if (movingFigure == PAWN) {
            if (toSquare == lastEpSquare) {
                if (movingSide == WHITE) {
                    putPiece((byte)(toSquare - 8), BLACK_PAWN);
                }
                else {
                    putPiece((byte)(toSquare + 8), WHITE_PAWN);
                }
            }
        }
        else if (movingFigure == KING) {
            if (fromSquare == E1) {
                switch (toSquare) {
                    case G1:
                        removePiece(F1);
                        putPiece(H1, WHITE_ROOK);
                        break;
                    case C1:
                        removePiece(D1);
                        putPiece(A1, WHITE_ROOK);
                        break;
                }
            }
            else if (fromSquare == E8) {
                switch (toSquare) {
                    case G8:
                        removePiece(F8);
                        putPiece(H8, BLACK_ROOK);
                        break;
                    case C8:
                        removePiece(D8);
                        putPiece(A8, BLACK_ROOK);
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

        return pieces[side][KING] != BoardUtils.NULLBITBOARD? (byte) BoardUtils.getLeastSignificantBit(pieces[side][KING]) : INVALIDSQUARE;
    }

    public boolean isSquareAttacked (byte square, byte side) {

        long[] sidePieces = pieces[side];
        byte oppositeSide = getOppositeSide(side);
        if ((sidePieces[KNIGHT] & BoardUtils.moveArray[KNIGHT][square]) != 0) return true;
        if ((sidePieces[KING] & BoardUtils.moveArray[KING][square]) != 0) return true;
        if ((sidePieces[PAWN] & BoardUtils.moveArray[oppositeSide == WHITE? PAWN : BPAWN][square]) != 0) return true;

        long[] c = BoardUtils.fromtoRay[square];
        long b = (sidePieces[BISHOP] | sidePieces[QUEEN]) & BoardUtils.moveArray[BISHOP][square];
        long d = ~b & blocker;
        int t;
        while (b != 0)
        {
            t = BoardUtils.getLeastSignificantBit(b);
            if ((c[t] & d) == 0)
                return (true);
            b &= BoardUtils.squareBitX[t];
        }
        b = (sidePieces[ROOK] | sidePieces[QUEEN]) & BoardUtils.moveArray[ROOK][square];
        d = ~b & blocker;
        while (b != 0)
        {
            t = BoardUtils.getLeastSignificantBit(b);
            if ((c[t] & d) == 0)
                return (true);
            b &= BoardUtils.squareBitX[t];
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
        attackers = (sidePieces[KNIGHT] & BoardUtils.moveArray[KNIGHT][square]);
        attackers |= (sidePieces[KING] & BoardUtils.moveArray[KING][square]);
        attackers |= (sidePieces[PAWN] & BoardUtils.moveArray[xside==WHITE?PAWN:BPAWN][square]);
        slideMoves = BoardUtils.fromtoRay[square];
        moves = (sidePieces[BISHOP] | sidePieces[QUEEN]) & BoardUtils.moveArray[BISHOP][square];
        while (moves != 0) {
            t = (byte) BoardUtils.getLeastSignificantBit(moves);
            moves &= BoardUtils.squareBitX[t];
            if ((slideMoves[t] & blocker & BoardUtils.squareBitX[t]) == 0)
                attackers |= BoardUtils.squareBit[t];
        }
        moves = (sidePieces[ROOK] | sidePieces[QUEEN]) & BoardUtils.moveArray[ROOK][square];
        while (moves != 0) {
            t = (byte) BoardUtils.getLeastSignificantBit(moves);
            moves &= BoardUtils.squareBitX[t];
            if ((slideMoves[t] & blocker & BoardUtils.squareBitX[t]) == 0)
                attackers |= BoardUtils.squareBit[t];
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
        attackers = (sidePieces[KNIGHT] & BoardUtils.moveArray[KNIGHT][square]);
        attackers |= (sidePieces[KING] & BoardUtils.moveArray[KING][square]);
        slideMoves = BoardUtils.fromtoRay[square];
        moves = (sidePieces[PAWN] & BoardUtils.moveArray[xside==WHITE?PAWN:BPAWN][square]);
        blocker = this.blocker;
        blocker &= ~(sidePieces[BISHOP] | sidePieces[QUEEN] | xsidePieces[BISHOP] | xsidePieces[QUEEN] | moves);
        moves |= (sidePieces[BISHOP] | sidePieces[QUEEN]) & BoardUtils.moveArray[BISHOP][square];
        while (moves != 0) {
            t = (byte) BoardUtils.getLeastSignificantBit(moves);
            moves &= BoardUtils.squareBitX[t];
            if ((slideMoves[t] & blocker & BoardUtils.squareBitX[t]) == 0)
                attackers |= BoardUtils.squareBit[t];
        }
        moves = (sidePieces[ROOK] | sidePieces[QUEEN]) & BoardUtils.moveArray[ROOK][square];
        blocker = this.blocker;
        blocker &= ~(sidePieces[ROOK] | sidePieces[QUEEN] | xsidePieces[ROOK] | xsidePieces[QUEEN]);
        while (moves != 0) {
            t = (byte) BoardUtils.getLeastSignificantBit(moves);
            moves &= BoardUtils.squareBitX[t];
            if ((slideMoves[t] & blocker & BoardUtils.squareBitX[t]) == 0)
                attackers |= BoardUtils.squareBit[t];
        }
        return attackers;
    }

    public long getSquareAttacks (byte square, byte figure, byte side) {

        switch (figure) {
            case PAWN:
                return BoardUtils.moveArray[side==WHITE?PAWN:BPAWN][square];
            case KNIGHT:
                return BoardUtils.moveArray[KNIGHT][square];
            case BISHOP:
                return getBishopAttacks(square);
            case ROOK:
                return getRookAttacks(square);
            case QUEEN:
                return getQueenAttacks(square);
            case KING:
                return BoardUtils.moveArray[KING][square];
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
                attacks = BoardUtils.moveArray[side==WHITE?PAWN:BPAWN][square];
                break;
            case KNIGHT:
                attacks = BoardUtils.moveArray[KNIGHT][square];
                break;
            case BISHOP:
            case QUEEN:
                blocker &= ~(sidePieces[BISHOP] | sidePieces[QUEEN]);
                for (dir = BoardUtils.raybeg[BISHOP]; dir < BoardUtils.rayend[BISHOP]; dir++) {
                    rays = BoardUtils.ray[square][dir] & blocker;
                    if (rays == BoardUtils.NULLBITBOARD) {
                        rays = BoardUtils.ray[square][dir];
                    }
                    else {
                        blocksq = (BoardUtils.dirpos[dir] == 1? BoardUtils.getMostSignificantBit(rays) : BoardUtils.getLeastSignificantBit(rays));
                        rays = BoardUtils.fromtoRay[square][blocksq];
                    }
                    attacks |= rays;
                }
                if (piece == BISHOP)
                    break;
                blocker = this.blocker;
            case ROOK:
                blocker &= ~(sidePieces[ROOK] | sidePieces[QUEEN]);
                for (dir = BoardUtils.raybeg[ROOK]; dir < BoardUtils.rayend[ROOK]; dir++) {
                    rays = BoardUtils.ray[square][dir] & blocker;
                    if (rays == BoardUtils.NULLBITBOARD) {
                        rays = BoardUtils.ray[square][dir];
                    }
                    else {
                        blocksq = (BoardUtils.dirpos[dir] == 1? BoardUtils.getMostSignificantBit(rays) : BoardUtils.getLeastSignificantBit(rays));
                        rays = BoardUtils.fromtoRay[square][blocksq];
                    }
                    attacks |= rays;
                }
                break;
            case KING:
                attacks = BoardUtils.moveArray[KING][square];
                break;
        }
        return (attacks);
    }

    public long getBishopAttacks (byte square) {

        long bishopAttack45 = BoardUtils.bishop45Atak[square][(int)((blockerr45 >>> BoardUtils.shift45[square]) & BoardUtils.mask45[square])];
        long bishopAttack315 = BoardUtils.bishop315Atak[square][(int)((blockerr315 >>> BoardUtils.shift315[square]) & BoardUtils.mask315[square])];
        return bishopAttack45 | bishopAttack315;
    }

    public long getRookAttacks (byte square) {

        long rookAttack00 = BoardUtils.rook00Atak[square][(int)((blocker >>> BoardUtils.shift00[square]) & 0xFF)];
        long rookAttack90 = BoardUtils.rook90Atak[square][(int)((blockerr90 >>> BoardUtils.shift90[square]) & 0xFF)];
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

                square = (byte) BoardUtils.getLeastSignificantBit(movers);
                movers &= BoardUtils.squareBitX[square];
                atacks[side][KNIGHT] |= BoardUtils.moveArray[KNIGHT][square];
            }

            atacks[side][BISHOP] = 0;
            movers = sidePieces[BISHOP];
            while (movers != 0) {

                square = (byte) BoardUtils.getLeastSignificantBit(movers);
                movers &= BoardUtils.squareBitX[square];
                atacks[side][BISHOP] |= getBishopAttacks(square);
            }

            atacks[side][ROOK] = 0;
            movers = sidePieces[ROOK];
            while (movers != 0) {

                square = (byte) BoardUtils.getLeastSignificantBit(movers);
                movers &= BoardUtils.squareBitX[square];
                atacks[side][ROOK] |= getRookAttacks(square);
            }

            atacks[side][QUEEN] = 0;
            movers = sidePieces[QUEEN];
            while (movers != 0) {

                square = (byte) BoardUtils.getLeastSignificantBit(movers);
                movers &= BoardUtils.squareBitX[square];
                atacks[side][ROOK] |= getQueenAttacks(square);
            }

            atacks[side][KING] = 0;
            movers = sidePieces[KING];
            while (movers != 0) {

                square = (byte) BoardUtils.getLeastSignificantBit(movers);
                movers &= BoardUtils.squareBitX[square];
                atacks[side][KING] |= BoardUtils.moveArray[KING][square];
            }

            atacks[side][PAWN] = 0;
            if (side == WHITE) {

                movers = pieces[WHITE][PAWN] & ~BoardUtils.fileBits[0];
                atacks[side][PAWN] |= (movers >> 7);
                movers = pieces[WHITE][PAWN] & ~BoardUtils.fileBits[7];
                atacks[side][PAWN] |= (movers >> 9);
            }
            else {

                movers = pieces[BLACK][PAWN] & ~BoardUtils.fileBits[0];
                atacks[side][PAWN] |= (movers << 9);
                movers = pieces[BLACK][PAWN] & ~BoardUtils.fileBits[7];
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
        if ((dir = BoardUtils.directions[KingSq][square]) == -1)
            return false;
        xside = 1 ^ side;
        if ((BoardUtils.fromtoRay[KingSq][square] & BoardUtils.squareBitX[square] & blocker) != 0)
            return false;
        b = (BoardUtils.ray[KingSq][dir] ^ BoardUtils.fromtoRay[KingSq][square]) & blocker;
        if (b == 0)
            return false;
        sq1 = (square > KingSq ? BoardUtils.getLeastSignificantBit(b) : BoardUtils.getMostSignificantBit(b));
        if (dir <= 3 && ((BoardUtils.squareBit[sq1] & (pieces[xside][QUEEN] | pieces[xside][BISHOP])) != 0))
            return true;
        if (dir >= 4 && ((BoardUtils.squareBit[sq1] & (pieces[xside][QUEEN] | pieces[xside][ROOK])) != 0))
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
                fsq = (byte) BoardUtils.getLeastSignificantBit(movers);
                movers &= BoardUtils.squareBitX[fsq];
                moves = BoardUtils.moveArray[piece][fsq] & notfriends;
                while (moves != 0) {
                    tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                    moves &= BoardUtils.squareBitX[tsq];
                    movesArray[moveIndex++] = createMove(fsq, tsq);
                }
            }
        }

        movers = sidePieces[BISHOP];
        while (movers != 0) {
            fsq = (byte) BoardUtils.getLeastSignificantBit(movers);
            movers &= BoardUtils.squareBitX[fsq];
            moves = getBishopAttacks(fsq) & notfriends;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        movers = sidePieces[ROOK];
        while (movers != 0) {
            fsq = (byte) BoardUtils.getLeastSignificantBit(movers);
            movers &= BoardUtils.squareBitX[fsq];
            moves = getRookAttacks(fsq) & notfriends;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        movers = sidePieces[QUEEN];
        while (movers != 0) {
            fsq = (byte) BoardUtils.getLeastSignificantBit(movers);
            movers &= BoardUtils.squareBitX[fsq];
            moves = getQueenAttacks(fsq) & notfriends;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        captures = (friends[xside] | (epSquare != INVALIDSQUARE? BoardUtils.squareBit[epSquare] : BoardUtils.NULLBITBOARD));
        if (side == WHITE) {
            moves = (sidePieces[PAWN] >> 8) & notblocker;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-8), tsq, getSquareRank(tsq) == RANK_8? WHITE_QUEEN : EMPTY);
            }

            movers = sidePieces[PAWN] & BoardUtils.rankBits[1];
            moves = (movers >> 8) & notblocker;
            moves = (moves >> 8) & notblocker;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-16), tsq);
            }

            movers = sidePieces[PAWN] & ~BoardUtils.fileBits[0];
            moves = (movers >> 7) & captures;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-7), tsq, getSquareRank(tsq) == RANK_8? WHITE_QUEEN : EMPTY);
            }

            movers = sidePieces[PAWN] & ~BoardUtils.fileBits[7];
            moves = (movers >> 9) & captures;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-9), tsq, getSquareRank(tsq) == RANK_8? WHITE_QUEEN : EMPTY);
            }
        }
        else if (side == BLACK) {

            moves = (sidePieces[PAWN] << 8) & notblocker;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+8), tsq, getSquareRank(tsq) == RANK_1? BLACK_QUEEN : EMPTY);
            }

            movers = sidePieces[PAWN] & BoardUtils.rankBits[6];
            moves = (movers << 8) & notblocker;
            moves = (moves << 8) & notblocker;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+16), tsq);
            }

            movers = sidePieces[PAWN] & ~BoardUtils.fileBits[7];
            moves = (movers << 7) & captures;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+7), tsq, getSquareRank(tsq) == RANK_1? BLACK_QUEEN : EMPTY);
            }

            movers = sidePieces[PAWN] & ~BoardUtils.fileBits[0];
            moves = (movers << 9) & captures;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+9), tsq, getSquareRank(tsq) == RANK_1? BLACK_QUEEN : EMPTY);
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
                fsq = (byte) BoardUtils.getLeastSignificantBit(movers);
                movers &= BoardUtils.squareBitX[fsq];
                moves = BoardUtils.moveArray[piece][fsq] & enemy;
                while (moves != 0) {
                    tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                    moves &= BoardUtils.squareBitX[tsq];
                    movesArray[moveIndex++] = createMove(fsq, tsq);
                }
            }
        }

        movers = sidePieces[BISHOP];
        while (movers != 0) {
            fsq = (byte) BoardUtils.getLeastSignificantBit(movers);
            movers &= BoardUtils.squareBitX[fsq];
            moves = getBishopAttacks(fsq) & enemy;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        movers = sidePieces[ROOK];
        while (movers != 0) {
            fsq = (byte) BoardUtils.getLeastSignificantBit(movers);
            movers &= BoardUtils.squareBitX[fsq];
            moves = getRookAttacks(fsq) & enemy;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        movers = sidePieces[QUEEN];
        while (movers != 0) {
            fsq = (byte) BoardUtils.getLeastSignificantBit(movers);
            movers &= BoardUtils.squareBitX[fsq];
            moves = getQueenAttacks(fsq) & enemy;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove(fsq, tsq);
            }
        }

        captures = (friends[xside] | (epSquare != INVALIDSQUARE? BoardUtils.squareBit[epSquare] : BoardUtils.NULLBITBOARD));
        if (side == WHITE) {
            movers = sidePieces[PAWN] & BoardUtils.rankBits[6];
            moves = (movers >> 8) & ~blocker;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-8), tsq, WHITE_QUEEN);
            }

            movers = sidePieces[PAWN] & ~BoardUtils.fileBits[0];
            moves = (movers >> 7) & captures;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-7), tsq, getSquareRank(tsq) == RANK_8? WHITE_QUEEN : EMPTY);
            }

            movers = sidePieces[PAWN] & ~BoardUtils.fileBits[7];
            moves = (movers >> 9) & captures;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq-9), tsq, getSquareRank(tsq) == RANK_8? WHITE_QUEEN : EMPTY);
            }
        }
        else if (side == BLACK) {
            movers = sidePieces[PAWN] & BoardUtils.rankBits[1];
            moves = (movers << 8) & ~blocker;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+8), tsq, BLACK_QUEEN);
            }

            movers = sidePieces[PAWN] & ~BoardUtils.fileBits[7];
            moves = (movers << 7) & captures;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+7), tsq, getSquareRank(tsq) == RANK_1? BLACK_QUEEN : EMPTY);
            }

            movers = sidePieces[PAWN] & ~BoardUtils.fileBits[0];
            moves = (movers << 9) & captures;
            while (moves != 0) {
                tsq = (byte) BoardUtils.getLeastSignificantBit(moves);
                moves &= BoardUtils.squareBitX[tsq];
                movesArray[moveIndex++] = createMove((byte)(tsq+9), tsq, getSquareRank(tsq) == RANK_1? BLACK_QUEEN : EMPTY);
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

        if (BoardUtils.getBitCount(checkers) == 1) {
            chksq = (byte) BoardUtils.getLeastSignificantBit(checkers);
            b = getSquareAttackers(chksq, side);
            b &= ~pieces[side][KING];
            while (b != 0) {
                sq = (byte) BoardUtils.getLeastSignificantBit(b);
                b &= BoardUtils.squareBitX[sq];
                if (!isPinningKing(sq, side))
                    movesArray[moveIndex++] = createMove(sq, chksq);
            }

            if (epSquare != INVALIDSQUARE) {
                epsq = epSquare;
                if (epsq + (side == WHITE ? -8 : 8) == chksq) {
                    b = BoardUtils.moveArray[xside == WHITE?PAWN:BPAWN][epsq] & p;
                    while (b != 0)
                    {
                        sq = (byte) BoardUtils.getLeastSignificantBit(b);
                        b &= BoardUtils.squareBitX[sq];
                        if (!isPinningKing(sq, side))
                            movesArray[moveIndex++] = createMove(sq, epsq);
                    }
                }
            }

            if (BoardUtils.slider[squareFigure[chksq]] == 1) {
                c = BoardUtils.fromtoRay[kingsq][chksq] & BoardUtils.squareBitX[chksq];
                while (c != 0) {
                    sq = (byte) BoardUtils.getLeastSignificantBit(c);
                    c &= BoardUtils.squareBitX[sq];
                    b = getSquareAttackers(sq, side);
                    b &= ~(pieces[side][KING] | p);

                    if (side == WHITE && sq > H2) {
                        if ((BoardUtils.squareBit[sq - 8] & p) != 0)
                            b |= BoardUtils.squareBit[sq - 8];

                        if (getSquareRank(sq) == 3 && squareSide[sq - 8] == NOSIDE && ((BoardUtils.squareBit[sq - 16] & p) != 0))
                            b |= BoardUtils.squareBit[sq - 16];
                    }
                    if (side == BLACK && sq < H7) {
                        if ((BoardUtils.squareBit[sq + 8] & p) != 0)
                            b |= BoardUtils.squareBit[sq + 8];
                        if (getSquareRank(sq) == 4 && squareSide[sq + 8] == NOSIDE && ((BoardUtils.squareBit[sq + 16] & p) != 0))
                            b |= BoardUtils.squareBit[sq + 16];
                    }
                    while (b != 0) {
                        sq1 = (byte) BoardUtils.getLeastSignificantBit(b);
                        b &= BoardUtils.squareBitX[sq1];
                        if (!isPinningKing(sq1, side))
                            movesArray[moveIndex++] = createMove(sq1, sq);
                    }
                }
            }
        }

        if (checkers != 0)
            escapes = BoardUtils.moveArray[KING][kingsq] & ~friends[side];

        while (checkers != 0) {
            chksq = (byte) BoardUtils.getLeastSignificantBit(checkers);
            checkers &= BoardUtils.squareBitX[chksq];
            dir = BoardUtils.directions[chksq][kingsq];
            if (BoardUtils.slider[squareFigure[chksq]] == 1)
                escapes &= ~BoardUtils.ray[chksq][dir];
        }
        while (escapes != 0) {
            sq = (byte) BoardUtils.getLeastSignificantBit(escapes);
            escapes &= BoardUtils.squareBitX[sq];
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

    public boolean checkIntegrity() {

        if (epSquare < 0 || epSquare > 64) {
            return false;
        }

        for (byte square = A1; square <= H8; square++) {
            byte squareSide = this.squareSide[square];
            byte squareFigure = this.squareFigure[square];
            switch (squareSide) {
                case NOSIDE:
                    if (squareFigure != EMPTY) {
                        return false;
                    }
                    break;
                case WHITE:
                    if (squareFigure < PAWN || squareFigure > KING) {
                        return false;
                    }
                    break;
                case BLACK:
                    if (squareFigure < PAWN || squareFigure > KING) {
                        return false;
                    }
                    break;
                default:
                    return false;
            }

            long squareBitBoard = BoardUtils.squareBit[square];
            for (byte side = WHITE; side <= BLACK; side++) {
                boolean friendsBitSet = (friends[side] & squareBitBoard) != 0;
                if (side == squareSide) {
                    if (!friendsBitSet) {
                        return false;
                    }
                }
                else {
                    if (friendsBitSet) {
                        return false;
                    }
                }

                for (byte figure = PAWN; figure <= KING; figure++) {
                    boolean pieceBitSet = (pieces[side][figure] & squareBitBoard) != 0;
                    if (side == squareSide && figure == squareFigure) {
                        if (!pieceBitSet) {
                            return false;
                        }
                    }
                    else {
                        if (pieceBitSet) {
                            return false;
                        }
                    }
                }
            }

            if (squareSide != NOSIDE) {
                if ((blocker & squareBitBoard) == 0)
                    return false;
                if ((blockerr45 & BoardUtils.squareBit45[square]) == 0)
                    return false;
                if ((blockerr90 & BoardUtils.squareBit90[square]) == 0)
                    return false;
                if ((blockerr315 & BoardUtils.squareBit315[square]) == 0)
                    return false;
            }
            else {
                if ((blocker & squareBitBoard) != 0)
                    return false;
                if ((blockerr45 & BoardUtils.squareBit45[square]) != 0)
                    return false;
                if ((blockerr90 & BoardUtils.squareBit90[square]) != 0)
                    return false;
                if ((blockerr315 & BoardUtils.squareBit315[square]) != 0)
                    return false;
            }
        }
        return true;
    }
}
