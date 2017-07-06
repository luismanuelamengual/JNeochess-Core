
package org.neochess.core;

import java.util.*;

import static org.neochess.core.Square.*;
import static org.neochess.core.Side.*;
import static org.neochess.core.Figure.*;
import static org.neochess.core.Piece.*;
import static org.neochess.core.Rank.*;
import static org.neochess.core.File.*;

/**
 * TODO: Cosas a hacer
 * 1. Crear getHash() y método getDrawByRepetition() en clase match
 * 2. Agreagar métodos getPgn y setPgn al Match
 */
public class Board {

    private static EnumMap<Figure, int[][]> figureOffsets;

    static {
        figureOffsets = new EnumMap<>(Figure.class);
        figureOffsets.put(KNIGHT, new int[][]{{1, 2}, {1, -2}, {2, 1}, {2, -1}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}});
        figureOffsets.put(BISHOP, new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
        figureOffsets.put(ROOK, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
        figureOffsets.put(QUEEN, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
        figureOffsets.put(KING, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
    }

    private EnumMap<Square,Piece> squares;
    private Side sideToMove;
    private Square enPassantSquare;
    private EnumMap<Side,CastleRights> castleRights;
    private int moveCounter;
    private int halfMoveCounter;

    public Board() {
        squares = new EnumMap<Square, Piece>(Square.class);
        castleRights = new EnumMap<Side, CastleRights>(Side.class);
        castleRights.put(WHITE, new CastleRights());
        castleRights.put(BLACK, new CastleRights());
    }

    public Board (Board board) {
        squares = new EnumMap<Square, Piece>(Square.class);
        castleRights = new EnumMap<Side, CastleRights>(Side.class);
        setFrom(board);
    }

    public Board clone () {
        return new Board(this);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Board) {

            Board board = (Board)object;
            for (Square square : Square.values()) {
                if (getPiece(square) != board.getPiece(square)) {
                    return false;
                }
            }
            if (getSideToMove() != board.getSideToMove()) {
                return false;
            }
            if (getEnPassantSquare() != board.getEnPassantSquare()) {
                return false;
            }
            if (getCastleRights(WHITE) != board.getCastleRights(WHITE)) {
                return false;
            }
            if (getCastleRights(BLACK) != board.getCastleRights(BLACK)) {
                return false;
            }
            if (getMoveCounter() != board.getMoveCounter()) {
                return false;
            }
            if (getHalfMoveCounter() != board.getHalfMoveCounter()) {
                return false;
            }
        }
        return super.equals(object);
    }

    public void setFrom (Board board) {
        for (Square square : Square.values()) {
            putPiece(square, board.getPiece(square));
        }
        setCastleRights(WHITE, board.getCastleRights(WHITE).clone());
        setCastleRights(BLACK, board.getCastleRights(BLACK).clone());
        setSideToMove(board.getSideToMove());
        setEnPassantSquare(board.getEnPassantSquare());
        setMoveCounter(board.getMoveCounter());
        setHalfMoveCounter(board.getHalfMoveCounter());
    }

    public Piece getPiece (Square square) {
        return squares.get(square);
    }

    public void putPiece (Square square, Piece piece) {
        squares.put(square, piece);
    }

    public void removePiece (Square square) {
        squares.remove(square);
    }

    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    public void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public Side getSideToMove() {
        return sideToMove;
    }

    public void setSideToMove(Side sideToMove) {
        this.sideToMove = sideToMove;
    }

    public CastleRights getCastleRights(Side side) {
        return castleRights.get(side);
    }

    public void setCastleRights(Side side, CastleRights castleRights) {
        this.castleRights.put(side, castleRights);
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public void setMoveCounter(int moveCounter) {
        this.moveCounter = moveCounter;
    }

    public int getHalfMoveCounter() {
        return halfMoveCounter;
    }

    public void setHalfMoveCounter(int halfMoveCounter) {
        this.halfMoveCounter = halfMoveCounter;
    }

    public void clear () {
        for (Square square : Square.values()) {
            removePiece(square);
        }
        setEnPassantSquare(null);
        getCastleRights(WHITE).clear();
        getCastleRights(BLACK).clear();
        setSideToMove(WHITE);
        moveCounter = 0;
        halfMoveCounter = 0;
    }

    public void setInitialPosition () {
        clear();
        putPiece(A1, WHITE_ROOK);
        putPiece(H1, WHITE_ROOK);
        putPiece(B1, WHITE_KNIGHT);
        putPiece(G1, WHITE_KNIGHT);
        putPiece(C1, WHITE_BISHOP);
        putPiece(F1, WHITE_BISHOP);
        putPiece(D1, WHITE_QUEEN);
        putPiece(E1, WHITE_KING);
        putPiece(A2, WHITE_PAWN);
        putPiece(B2, WHITE_PAWN);
        putPiece(C2, WHITE_PAWN);
        putPiece(D2, WHITE_PAWN);
        putPiece(E2, WHITE_PAWN);
        putPiece(F2, WHITE_PAWN);
        putPiece(G2, WHITE_PAWN);
        putPiece(H2, WHITE_PAWN);
        putPiece(A8, BLACK_ROOK);
        putPiece(H8, BLACK_ROOK);
        putPiece(B8, BLACK_KNIGHT);
        putPiece(G8, BLACK_KNIGHT);
        putPiece(C8, BLACK_BISHOP);
        putPiece(F8, BLACK_BISHOP);
        putPiece(D8, BLACK_QUEEN);
        putPiece(E8, BLACK_KING);
        putPiece(A7, BLACK_PAWN);
        putPiece(B7, BLACK_PAWN);
        putPiece(C7, BLACK_PAWN);
        putPiece(D7, BLACK_PAWN);
        putPiece(E7, BLACK_PAWN);
        putPiece(F7, BLACK_PAWN);
        putPiece(G7, BLACK_PAWN);
        putPiece(H7, BLACK_PAWN);
        getCastleRights(WHITE).setCastleKingSide(true);
        getCastleRights(WHITE).setCastleQueenSide(true);
        getCastleRights(BLACK).setCastleKingSide(true);
        getCastleRights(BLACK).setCastleQueenSide(true);
        setSideToMove(WHITE);
    }

    public void setFen(String fen) {

        clear();
        String squares = fen.substring(0, fen.indexOf(' '));
        String state = fen.substring(fen.indexOf(' ') + 1);
        Rank[] ranks = { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT };
        File[] files = { A, B, C, D, E, F, G, H };
        String rankLines[] = squares.split("/");
        int fileIndex = 0;
        int rankIndex = 7;
        for (String rankLine : rankLines) {
            fileIndex = 0;
            for (int fenIndex = 0; fenIndex < rankLine.length(); fenIndex++) {
                char fenCharacter = rankLine.charAt(fenIndex);
                if (Character.isDigit(fenCharacter)) {
                    fileIndex += Integer.parseInt(fenCharacter + "");
                } else {
                    Square square = Square.getSquare(files[fileIndex], ranks[rankIndex]);
                    putPiece(square, Piece.fromSan("" + fenCharacter));
                    fileIndex++;
                }
            }
            rankIndex--;
        }

        sideToMove = state.toLowerCase().charAt(0) == 'w' ? Side.WHITE : Side.BLACK;

        CastleRights whiteCastleRights = getCastleRights(WHITE);
        whiteCastleRights.clear();
        if (state.contains("K")) {
            whiteCastleRights.setCastleKingSide(true);
        }
        if (state.contains("Q")) {
            whiteCastleRights.setCastleQueenSide(true);
        }
        CastleRights blackCastleRights = getCastleRights(BLACK);
        blackCastleRights.clear();
        if (state.contains("k")) {
            blackCastleRights.setCastleKingSide(true);
        }
        if (state.contains("q")) {
            blackCastleRights.setCastleQueenSide(true);
        }

        String flags[] = state.split(" ");
        if (flags != null) {
            if (flags.length >= 3) {
                String s = flags[2].toUpperCase().trim();
                if (!s.equals("-")) {
                    Square ep = Square.valueOf(s.toUpperCase());
                    enPassantSquare = ep;
                }
                else {
                    enPassantSquare = null;
                }
                if (flags.length >= 4) {
                    halfMoveCounter = Integer.parseInt(flags[3]);
                    if (flags.length >= 5) {
                        moveCounter = (Integer.parseInt(flags[4]) - 1) * 2;
                        if (sideToMove == BLACK) {
                            moveCounter++;
                        }
                    }
                }
            }
        }
    }

    public String getFen () {

        Rank[] ranks = { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT };
        File[] files = { A, B, C, D, E, F, G, H };
        StringBuilder fen = new StringBuilder();
        for (int rankIndex = 7; rankIndex >= 0; rankIndex--) {
            Rank rank = ranks[rankIndex];
            for (int fileIndex = 0; fileIndex <= 7; fileIndex++) {
                File file = files[fileIndex];
                Square square = Square.getSquare(file, rank);
                Piece piece = getPiece(square);
                if (piece == null) {
                    int spaceCounter = 1;
                    while ((++fileIndex) <= 7) {
                        file = files[fileIndex];
                        square = Square.getSquare(file, rank);
                        if (getPiece(square) == null)
                            spaceCounter++;
                        else
                            break;
                    }
                    fen.append(String.valueOf(spaceCounter));
                    fileIndex--;
                }
                else {
                    fen.append(piece.getSan());
                }
            }
            if (rankIndex > 0) {
                fen.append("/");
            }
        }

        fen.append(" ");
        fen.append(getSideToMove() == WHITE? "w" : "b");

        fen.append(" ");
        CastleRights whiteCastleRights = castleRights.get(WHITE);
        CastleRights blackCastleRights = castleRights.get(BLACK);
        if (whiteCastleRights.canCastle() || blackCastleRights.canCastle())
        {
            if (whiteCastleRights.canCastleKingSide()) fen.append("K");
            if (whiteCastleRights.canCastleQueenSide()) fen.append("Q");
            if (blackCastleRights.canCastleKingSide()) fen.append("k");
            if (blackCastleRights.canCastleQueenSide()) fen.append("q");
        }
        else
        {
            fen.append("-");
        }

        fen.append(" ");
        fen.append(enPassantSquare != null? enPassantSquare.toString().toLowerCase() : "-");
        fen.append(" ");
        fen.append(halfMoveCounter);
        fen.append(" ");
        fen.append((int)Math.ceil(moveCounter/2) + 1);
        return fen.toString();
    }

    public List<Move> getLegalMoves () {

        Board board = clone();
        List<Move> moves = new ArrayList<>();
        Side oppositeSide = sideToMove.getOppositeSide();
        for (Square testSquare : Square.values()) {
            Piece piece = getPiece(testSquare);
            if (piece != null) {
                Side pieceSide = piece.getSide();
                if (sideToMove == pieceSide) {
                    Figure pieceFigure = piece.getFigure();
                    if (pieceFigure == PAWN) {
                        File pieceFile = testSquare.getFile();
                        if (sideToMove == WHITE) {

                            Square leftCaptureSquare = testSquare.getOffsetSquare(-1, 1);
                            if (pieceFile != A && getPiece(leftCaptureSquare) != null && getPiece(leftCaptureSquare).getSide().equals(BLACK)) {
                                if (leftCaptureSquare.getRank().equals(EIGHT)) {
                                    moves.addAll(createPromotionMoves(board, testSquare, leftCaptureSquare));
                                }
                                else {
                                    moves.add(createMove(board, testSquare, leftCaptureSquare));
                                }
                            }
                            Square rightCaptureSquare = testSquare.getOffsetSquare(1, 1);
                            if (pieceFile != H && getPiece(rightCaptureSquare) != null && getPiece(rightCaptureSquare).getSide().equals(BLACK)) {
                                if (rightCaptureSquare.getRank().equals(EIGHT)) {
                                    moves.addAll(createPromotionMoves(board, testSquare, rightCaptureSquare));
                                }
                                else {
                                    moves.add(createMove(board, testSquare, rightCaptureSquare));
                                }
                            }
                            Square nextSquare = testSquare.getOffsetSquare(0, 1);
                            if (getPiece(nextSquare) == null) {
                                if (nextSquare.getRank().equals(EIGHT)) {
                                    moves.addAll(createPromotionMoves(board, testSquare, nextSquare));
                                }
                                else {
                                    moves.add(createMove (board, testSquare, nextSquare));
                                    if (testSquare.getRank().equals(TWO)) {
                                        Square nextTwoSquare = testSquare.getOffsetSquare(0, 2);
                                        if (getPiece(nextTwoSquare) == null) {
                                            moves.add(createMove(board, testSquare, nextTwoSquare));
                                        }
                                    }
                                }
                            }
                        } else {

                            Square leftCaptureSquare = testSquare.getOffsetSquare(-1, -1);
                            if (pieceFile != A && getPiece(leftCaptureSquare) != null && getPiece(leftCaptureSquare).getSide().equals(WHITE)) {
                                if (leftCaptureSquare.getRank().equals(ONE)) {
                                    moves.addAll(createPromotionMoves(board, testSquare, leftCaptureSquare));
                                }
                                else {
                                    moves.add(createMove(board, testSquare, leftCaptureSquare));
                                }
                            }
                            Square rightCaptureSquare = testSquare.getOffsetSquare(1, -1);
                            if (pieceFile != H && getPiece(rightCaptureSquare) != null && getPiece(rightCaptureSquare).getSide().equals(WHITE)) {
                                if (rightCaptureSquare.getRank().equals(ONE)) {
                                    moves.addAll(createPromotionMoves(board, testSquare, rightCaptureSquare));
                                }
                                else {
                                    moves.add(createMove(board, testSquare, rightCaptureSquare));
                                }
                            }
                            Square nextSquare = testSquare.getOffsetSquare(0, -1);
                            if (getPiece(nextSquare) == null) {
                                if (nextSquare.getRank().equals(ONE)) {
                                    moves.addAll(createPromotionMoves(board, testSquare, nextSquare));
                                }
                                else {
                                    moves.add(createMove(board, testSquare, nextSquare));
                                    if (testSquare.getRank().equals(SEVEN)) {
                                        Square nextTwoSquare = testSquare.getOffsetSquare(0, -2);
                                        if (getPiece(nextTwoSquare) == null) {
                                            moves.add(createMove(board, testSquare, nextTwoSquare));
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        for (int[] offset : figureOffsets.get(pieceFigure)) {

                            Square currentOffsetSquare = testSquare;
                            while (true) {

                                currentOffsetSquare = currentOffsetSquare.getOffsetSquare(offset[0], offset[1]);
                                if (currentOffsetSquare == null) {
                                    break;
                                }
                                Piece pieceAtCurrentSquare = getPiece(currentOffsetSquare);
                                if (pieceAtCurrentSquare != null) {
                                    if (pieceAtCurrentSquare.getSide().equals(oppositeSide)) {
                                        moves.add(createMove(board, testSquare, currentOffsetSquare));
                                    }
                                    break;
                                }
                                moves.add(createMove(board, testSquare, currentOffsetSquare));
                                if (pieceFigure.equals(KNIGHT) || pieceFigure.equals(KING)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (sideToMove == WHITE) {

            CastleRights sideCastleRights = this.castleRights.get(WHITE);
            if (sideCastleRights.canCastleKingSide() || sideCastleRights.canCastleQueenSide()) {
                if (!isSquareAttacked(E1, BLACK)) {
                    if (sideCastleRights.canCastleKingSide() && getPiece(F1) == null && getPiece(G1) == null) {
                        if (!isSquareAttacked(F1, BLACK) && !isSquareAttacked(G1, BLACK)) {
                            moves.add(createMove(board, E1, G1));
                        }
                    }
                    if (sideCastleRights.canCastleQueenSide() && getPiece(D1) == null && getPiece(C1) == null && getPiece(B1) == null) {
                        if (!isSquareAttacked(D1, BLACK) && !isSquareAttacked(C1, BLACK) && !isSquareAttacked(B1, BLACK)) {
                            moves.add(createMove(board, E1, C1));
                        }
                    }
                }
            }
            if (enPassantSquare != null) {
                File epSquareFile = enPassantSquare.getFile();
                if (!epSquareFile.equals(A) && getPiece(enPassantSquare.getOffsetSquare(-1,-1)) == WHITE_PAWN) {
                    moves.add(createMove(board, enPassantSquare.getOffsetSquare(-1,-1), enPassantSquare));
                }
                if (!epSquareFile.equals(H) && getPiece(enPassantSquare.getOffsetSquare(1,-1)) == WHITE_PAWN) {
                    moves.add(createMove(board, enPassantSquare.getOffsetSquare(1,-1), enPassantSquare));
                }
            }
        }
        else {

            CastleRights sideCastleRights = this.castleRights.get(BLACK);
            if (sideCastleRights.canCastleKingSide() || sideCastleRights.canCastleQueenSide()) {
                if (!isSquareAttacked(E8, WHITE)) {
                    if (sideCastleRights.canCastleKingSide() && getPiece(F8) == null && getPiece(G8) == null) {
                        if (!isSquareAttacked(F8, WHITE) && !isSquareAttacked(G8, WHITE)) {
                            moves.add(createMove(board, E8, G8));
                        }
                    }
                    if (sideCastleRights.canCastleQueenSide() && getPiece(D8) == null && getPiece(C8) == null && getPiece(B8) == null) {
                        if (!isSquareAttacked(D8, WHITE) && !isSquareAttacked(C8, WHITE) && !isSquareAttacked(B8, WHITE)) {
                            moves.add(createMove(board, E8, C8));
                        }
                    }
                }
            }
            if (enPassantSquare != null) {
                File epSquareFile = enPassantSquare.getFile();
                if (!epSquareFile.equals(A) && getPiece(enPassantSquare.getOffsetSquare(-1,1)) == BLACK_PAWN) {
                    moves.add(createMove(board, enPassantSquare.getOffsetSquare(-1,1), enPassantSquare));
                }
                if (!epSquareFile.equals(H) && getPiece(enPassantSquare.getOffsetSquare(1,1)) == BLACK_PAWN) {
                    moves.add(createMove(board, enPassantSquare.getOffsetSquare(1,1), enPassantSquare));
                }
            }
        }

        Iterator<Move> moveIterator = moves.iterator();
        while (moveIterator.hasNext()) {
            Move move = moveIterator.next();
            Side currentSideToMove = getSideToMove();
            makeMove(move);
            if (isKingSquareAttacked(currentSideToMove)) {
                moveIterator.remove();
            }
            unmakeMove(move);
        }
        return moves;
    }

    protected List<Move> createPromotionMoves (Board board, Square fromSquare, Square toSquare) {
        List<Move> promotionMoves = new ArrayList<>();
        promotionMoves.add(createMove(board, fromSquare, toSquare, QUEEN));
        promotionMoves.add(createMove(board, fromSquare, toSquare, ROOK));
        promotionMoves.add(createMove(board, fromSquare, toSquare, BISHOP));
        promotionMoves.add(createMove(board, fromSquare, toSquare, KNIGHT));
        return promotionMoves;
    }

    protected Move createMove (Board board, Square fromSquare, Square toSquare) {
        return createMove(board, fromSquare, toSquare, null);
    }

    protected Move createMove (Board board, Square fromSquare, Square toSquare, Figure promotionFigure) {
        return new Move(board, fromSquare, toSquare, promotionFigure);
    }

    protected void makeMove(Move move) {

        Square fromSquare = move.getFromSquare();
        Square toSquare = move.getToSquare();
        Piece movingPiece = getPiece(fromSquare);
        Piece capturedPiece = getPiece(toSquare);
        Figure movingFigure = movingPiece.getFigure();

        if (movingFigure == PAWN) {

            if (sideToMove == WHITE) {
                if (fromSquare.getRank() == TWO && toSquare.getRank() == FOUR) {
                    setEnPassantSquare(Square.getSquare(fromSquare.getFile(), THREE));
                }
                else {
                    if (toSquare == enPassantSquare) {
                        removePiece(toSquare.getOffsetSquare(0,-1));
                    }
                    else if (toSquare.getRank() == EIGHT) {
                        movingPiece = move.getPromotionFigure() != null? Piece.getPiece(sideToMove,move.getPromotionFigure()) : WHITE_QUEEN;
                    }
                    setEnPassantSquare(null);
                }
            }
            else {
                if (fromSquare.getRank() == SEVEN && toSquare.getRank() == FIVE) {
                    setEnPassantSquare(Square.getSquare(fromSquare.getFile(), SIX));
                }
                else {
                    if (toSquare == enPassantSquare) {
                        removePiece(toSquare.getOffsetSquare(0, 1));
                    }
                    else if (toSquare.getRank() == ONE) {
                        movingPiece = move.getPromotionFigure() != null ? Piece.getPiece(sideToMove,move.getPromotionFigure()) : BLACK_QUEEN;
                    }
                    setEnPassantSquare(null);
                }
            }
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
            setEnPassantSquare(null);
        }

        removePiece(fromSquare);
        putPiece(toSquare, movingPiece);

        CastleRights whiteCastleRights = getCastleRights(WHITE);
        if (whiteCastleRights.canCastleKingSide()) {
            if (fromSquare == H1 || toSquare == H1 || fromSquare == E1) {
                whiteCastleRights.setCastleKingSide(false);
            }
        }
        if (whiteCastleRights.canCastleQueenSide()) {
            if (fromSquare == A1 || toSquare == A1 || fromSquare == E1) {
                whiteCastleRights.setCastleQueenSide(false);
            }
        }

        CastleRights blackCastleRights = getCastleRights(BLACK);
        if (blackCastleRights.canCastleKingSide()) {
            if (fromSquare == H8 || toSquare == H8 || fromSquare == E8) {
                blackCastleRights.setCastleKingSide(false);
            }
        }
        if (blackCastleRights.canCastleQueenSide()) {
            if (fromSquare == A8 || toSquare == A8 || fromSquare == E8) {
                blackCastleRights.setCastleQueenSide(false);
            }
        }

        moveCounter++;
        if (movingFigure.equals(PAWN) || capturedPiece != null) {
            halfMoveCounter = 0;
        }
        else {
            halfMoveCounter++;
        }
        sideToMove = sideToMove.getOppositeSide();
    }

    protected void unmakeMove(Move move) {
        setFrom(move.getBoard());
    }

    public Square getKingSquare (Side side) {

        Square kingSquare = null;
        Piece sideKingPiece = side == WHITE? WHITE_KING : BLACK_KING;
        for (Square testSquare : Square.values()) {
            if (getPiece(testSquare) == sideKingPiece) {
                kingSquare = testSquare;
                break;
            }
        }
        return kingSquare;
    }

    public List<Square> getAttackingSquares (Square square, Side side) {

        List<Square> squares = new ArrayList<>();
        for (Square testSquare : Square.values()) {
            Piece piece = getPiece(testSquare);
            if (piece != null) {
                Side pieceSide = piece.getSide();
                if (side == pieceSide) {

                    Figure pieceFigure = piece.getFigure();
                    if (pieceFigure == PAWN) {

                        File pieceFile = testSquare.getFile();
                        if (side == WHITE) {
                            if (!pieceFile.equals(A) && testSquare.getOffsetSquare(-1, 1) == square) {
                                squares.add(testSquare);
                            }
                            if (!pieceFile.equals(H) && testSquare.getOffsetSquare(1, 1) == square) {
                                squares.add(testSquare);
                            }
                        } else {
                            if (!pieceFile.equals(A) && testSquare.getOffsetSquare(-1, -1) == square) {
                                squares.add(testSquare);
                            }
                            if (!pieceFile.equals(H) && testSquare.getOffsetSquare(1, -1) == square) {
                                squares.add(testSquare);
                            }
                        }
                    } else {

                        boolean squareFound = false;
                        for (int[] offset : figureOffsets.get(pieceFigure)) {
                            Square currentOffsetSquare = testSquare;
                            while (true) {
                                currentOffsetSquare = currentOffsetSquare.getOffsetSquare(offset[0], offset[1]);
                                if (currentOffsetSquare == null) {
                                    break;
                                }
                                if (currentOffsetSquare == square) {
                                    squares.add(testSquare);
                                    squareFound = true;
                                    break;
                                }
                                if (getPiece(currentOffsetSquare) != null) {
                                    break;
                                }
                                if (pieceFigure.equals(KNIGHT) || pieceFigure.equals(KING)) {
                                    break;
                                }
                            }
                            if (squareFound) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return squares;
    }

    public boolean isSquareAttacked (Square square, Side side) {

        for (Square testSquare : Square.values()) {
            Piece piece = getPiece(testSquare);
            if (piece != null) {
                Side pieceSide = piece.getSide();
                if (side == pieceSide) {

                    Figure pieceFigure = piece.getFigure();
                    if (pieceFigure == PAWN) {

                        File pieceFile = testSquare.getFile();
                        if (side == WHITE) {
                            if (!pieceFile.equals(A) && testSquare.getOffsetSquare(-1, 1) == square) return true;
                            if (!pieceFile.equals(H) && testSquare.getOffsetSquare(1, 1) == square) return true;
                        } else {
                            if (!pieceFile.equals(A) && testSquare.getOffsetSquare(-1, -1) == square) return true;
                            if (!pieceFile.equals(H) && testSquare.getOffsetSquare(1, -1) == square) return true;
                        }
                    } else {
                        for (int[] offset : figureOffsets.get(pieceFigure)) {

                            Square currentOffsetSquare = testSquare;
                            while (true) {
                                currentOffsetSquare = currentOffsetSquare.getOffsetSquare(offset[0], offset[1]);
                                if (currentOffsetSquare == null) {
                                    break;
                                }
                                if (currentOffsetSquare == square) {
                                    return true;
                                }
                                if (getPiece(currentOffsetSquare) != null) {
                                    break;
                                }
                                if (pieceFigure.equals(KNIGHT) || pieceFigure.equals(KING)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isKingSquareAttacked (Side side) {
        return isSquareAttacked(getKingSquare(side), side.getOppositeSide());
    }

    public boolean inCheck () {
        return isKingSquareAttacked(sideToMove);
    }

    public boolean isCheckMate() {
        return inCheck() && getLegalMoves().isEmpty();
    }

    public boolean isStaleMate() {
        return !inCheck() && getLegalMoves().isEmpty();
    }

    public boolean isDrawByFiftyMoveRule() {
        return halfMoveCounter >= 50;
    }

    public boolean isDraw() {
        return isStaleMate() || isDrawByFiftyMoveRule();
    }
}
