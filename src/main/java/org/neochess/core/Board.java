
package org.neochess.core;

import java.util.*;

import static org.neochess.core.Square.*;
import static org.neochess.core.Side.*;
import static org.neochess.core.Figure.*;
import static org.neochess.core.Piece.*;
import static org.neochess.core.Rank.*;
import static org.neochess.core.File.*;

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
    private Square epSquare;
    private EnumMap<Side,CastleRights> castleRights;
    private int moveCounter;
    private int halfMoveCounter;
    private Stack<MoveHistorySlot> moveHistorySlots;

    public Board() {
        squares = new EnumMap<Square, Piece>(Square.class);
        castleRights = new EnumMap<Side, CastleRights>(Side.class);
        castleRights.put(WHITE, new CastleRights());
        castleRights.put(BLACK, new CastleRights());
        moveHistorySlots = new Stack<>();
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

    public Square getEpSquare() {
        return epSquare;
    }

    public void setEpSquare(Square epSquare) {
        this.epSquare = epSquare;
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
        setEpSquare(null);
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
                    switch (fenCharacter) {
                        case 'p': putPiece (square, BLACK_PAWN); break;
                        case 'n': putPiece (square, BLACK_KNIGHT); break;
                        case 'b': putPiece (square, BLACK_BISHOP); break;
                        case 'r': putPiece (square, BLACK_ROOK); break;
                        case 'q': putPiece (square, BLACK_QUEEN); break;
                        case 'k': putPiece (square, BLACK_KING); break;
                        case 'P': putPiece (square, WHITE_PAWN); break;
                        case 'N': putPiece (square, WHITE_KNIGHT); break;
                        case 'B': putPiece (square, WHITE_BISHOP); break;
                        case 'R': putPiece (square, WHITE_ROOK); break;
                        case 'Q': putPiece (square, WHITE_QUEEN); break;
                        case 'K': putPiece (square, WHITE_KING); break;
                    }
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
                    epSquare = ep;
                }
                else {
                    epSquare = null;
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
                    switch (piece) {
                        case WHITE_PAWN: fen.append("P"); break;
                        case WHITE_KNIGHT: fen.append("N"); break;
                        case WHITE_BISHOP: fen.append("B"); break;
                        case WHITE_ROOK: fen.append("R"); break;
                        case WHITE_QUEEN: fen.append("Q"); break;
                        case WHITE_KING: fen.append("K"); break;
                        case BLACK_PAWN: fen.append("p"); break;
                        case BLACK_KNIGHT: fen.append("n"); break;
                        case BLACK_BISHOP: fen.append("b"); break;
                        case BLACK_ROOK: fen.append("r"); break;
                        case BLACK_QUEEN: fen.append("q"); break;
                        case BLACK_KING: fen.append("k"); break;
                    }
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
        fen.append(epSquare != null? epSquare.toString().toLowerCase() : "-");
        fen.append(" ");
        fen.append(halfMoveCounter);
        fen.append(" ");
        fen.append((int)Math.ceil(moveCounter/2) + 1);
        return fen.toString();
    }

    public void makeMove (Move move) {

        Square fromSquare = move.getFromSquare();
        Square toSquare = move.getToSquare();
        Piece movingPiece = getPiece(fromSquare);
        Piece capturedPiece = getPiece(toSquare);
        EnumMap<Side,CastleRights> castleRights = new EnumMap<Side, CastleRights>(Side.class);
        castleRights.put(WHITE, this.castleRights.get(WHITE).clone());
        castleRights.put(BLACK, this.castleRights.get(BLACK).clone());

        MoveHistorySlot slot = new MoveHistorySlot();
        slot.setMove(move);
        slot.setMovingPiece(movingPiece);
        slot.setCapturedPiece(capturedPiece);
        slot.setEpSquare(epSquare);
        slot.setCastleRights(castleRights);
        slot.setHalfMoveCounter(halfMoveCounter);
        moveHistorySlots.push(slot);

        Figure movingFigure = movingPiece.getFigure();
        if (movingFigure == PAWN) {

            if (sideToMove == WHITE) {
                if (fromSquare.getRank() == TWO && toSquare.getRank() == FOUR) {
                    setEpSquare(Square.getSquare(fromSquare.getFile(), THREE));
                }
                else {
                    if (toSquare == epSquare) {
                        removePiece(toSquare.getOffsetSquare(0,-1));
                    }
                    else if (toSquare.getRank() == EIGHT) {
                        movingPiece = move.getPromotionPiece() != null? move.getPromotionPiece() : WHITE_QUEEN;
                    }
                    setEpSquare(null);
                }
            }
            else {
                if (fromSquare.getRank() == SEVEN && toSquare.getRank() == FIVE) {
                    setEpSquare(Square.getSquare(fromSquare.getFile(), SIX));
                }
                else {
                    if (toSquare == epSquare) {
                        removePiece(toSquare.getOffsetSquare(0, 1));
                    }
                    else if (toSquare.getRank() == ONE) {
                        movingPiece = move.getPromotionPiece() != null ? move.getPromotionPiece() : BLACK_QUEEN;
                    }
                    setEpSquare(null);
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
            setEpSquare(null);
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

    public void unmakeMove () {

        MoveHistorySlot slot = moveHistorySlots.pop();
        Square fromSquare = slot.getMove().getFromSquare();
        Square toSquare = slot.getMove().getToSquare();
        Piece capturedPiece = slot.getCapturedPiece();
        EnumMap<Side,CastleRights> castleRights = slot.getCastleRights();
        Square epSquare = slot.getEpSquare();
        Piece movingPiece = slot.getMovingPiece();
        Figure movingFigure = movingPiece.getFigure();
        Side movingSide = movingPiece.getSide();

        if (movingFigure == PAWN) {
            if (toSquare == epSquare) {
                if (movingSide == WHITE) {
                    putPiece(toSquare.getOffsetSquare(0,-1), BLACK_PAWN);
                }
                else {
                    putPiece(toSquare.getOffsetSquare(0,1), WHITE_PAWN);
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
        if (capturedPiece != null) {
            putPiece(toSquare, capturedPiece);
        }
        else {
            removePiece(toSquare);
        }
        putPiece(fromSquare, movingPiece);
        this.epSquare = epSquare;
        this.castleRights.put(WHITE, castleRights.get(WHITE));
        this.castleRights.put(BLACK, castleRights.get(BLACK));

        moveCounter--;
        halfMoveCounter = slot.getHalfMoveCounter();
        sideToMove = getOppositeSide(sideToMove);
    }

    public List<Move> getLegalMoves () {

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
                                moves.add(new Move(testSquare, leftCaptureSquare));
                            }
                            Square rightCaptureSquare = testSquare.getOffsetSquare(1, 1);
                            if (pieceFile != H && getPiece(rightCaptureSquare) != null && getPiece(rightCaptureSquare).getSide().equals(BLACK)) {
                                moves.add(new Move(testSquare, rightCaptureSquare));
                            }
                            Square nextSquare = testSquare.getOffsetSquare(0, 1);
                            if (getPiece(nextSquare) == null) {
                                moves.add(new Move(testSquare, nextSquare));
                                if (testSquare.getRank().equals(TWO)) {
                                    Square nextTwoSquare = testSquare.getOffsetSquare(0, 2);
                                    if (getPiece(nextTwoSquare) == null) {
                                        moves.add(new Move(testSquare, nextTwoSquare));
                                    }
                                }
                            }
                        } else {

                            Square leftCaptureSquare = testSquare.getOffsetSquare(-1, -1);
                            if (pieceFile != A && getPiece(leftCaptureSquare) != null && getPiece(leftCaptureSquare).getSide().equals(WHITE)) {
                                moves.add(new Move(testSquare, leftCaptureSquare));
                            }
                            Square rightCaptureSquare = testSquare.getOffsetSquare(1, -1);
                            if (pieceFile != H && getPiece(rightCaptureSquare) != null && getPiece(rightCaptureSquare).getSide().equals(WHITE)) {
                                moves.add(new Move(testSquare, rightCaptureSquare));
                            }
                            Square nextSquare = testSquare.getOffsetSquare(0, -1);
                            if (getPiece(nextSquare) == null) {
                                moves.add(new Move(testSquare, nextSquare));
                                if (testSquare.getRank().equals(SEVEN)) {
                                    Square nextTwoSquare = testSquare.getOffsetSquare(0, -2);
                                    if (getPiece(nextTwoSquare) == null) {
                                        moves.add(new Move(testSquare, nextTwoSquare));
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
                                        moves.add(new Move(testSquare, currentOffsetSquare));
                                    }
                                    break;
                                }
                                moves.add(new Move(testSquare, currentOffsetSquare));
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
                            moves.add(new Move(E1, G1));
                        }
                    }
                    if (sideCastleRights.canCastleQueenSide() && getPiece(D1) == null && getPiece(C1) == null && getPiece(B1) == null) {
                        if (!isSquareAttacked(D1, BLACK) && !isSquareAttacked(C1, BLACK) && !isSquareAttacked(B1, BLACK)) {
                            moves.add(new Move(E1, C1));
                        }
                    }
                }
            }
            if (epSquare != null) {
                File epSquareFile = epSquare.getFile();
                if (!epSquareFile.equals(A) && getPiece(epSquare.getOffsetSquare(-1,-1)) == WHITE_PAWN) {
                    moves.add(new Move(epSquare.getOffsetSquare(-1,-1), epSquare));
                }
                if (!epSquareFile.equals(H) && getPiece(epSquare.getOffsetSquare(1,-1)) == WHITE_PAWN) {
                    moves.add(new Move(epSquare.getOffsetSquare(1,-1), epSquare));
                }
            }
        }
        else {

            CastleRights sideCastleRights = this.castleRights.get(BLACK);
            if (sideCastleRights.canCastleKingSide() || sideCastleRights.canCastleQueenSide()) {
                if (!isSquareAttacked(E8, WHITE)) {
                    if (sideCastleRights.canCastleKingSide() && getPiece(F8) == null && getPiece(G8) == null) {
                        if (!isSquareAttacked(F8, WHITE) && !isSquareAttacked(G8, WHITE)) {
                            moves.add(new Move(E8, G8));
                        }
                    }
                    if (sideCastleRights.canCastleQueenSide() && getPiece(D8) == null && getPiece(C8) == null && getPiece(B8) == null) {
                        if (!isSquareAttacked(D8, WHITE) && !isSquareAttacked(C8, WHITE) && !isSquareAttacked(B8, WHITE)) {
                            moves.add(new Move(E8, C8));
                        }
                    }
                }
            }
            if (epSquare != null) {
                File epSquareFile = epSquare.getFile();
                if (!epSquareFile.equals(A) && getPiece(epSquare.getOffsetSquare(-1,1)) == BLACK_PAWN) {
                    moves.add(new Move(epSquare.getOffsetSquare(-1,1), epSquare));
                }
                if (!epSquareFile.equals(H) && getPiece(epSquare.getOffsetSquare(1,1)) == BLACK_PAWN) {
                    moves.add(new Move(epSquare.getOffsetSquare(1,1), epSquare));
                }
            }
        }

        Side currentSideToMove = sideToMove;
        Iterator<Move> moveIterator = moves.iterator();
        while (moveIterator.hasNext()) {
            Move move = moveIterator.next();
            makeMove(move);
            if (isKingSquareAttacked(currentSideToMove)) {
                moveIterator.remove();
            }
            unmakeMove();
        }

        return moves;
    }

    public boolean isLegalMove (Move move) {
        boolean isLegal = false;
        List<Move> legalMoves = getLegalMoves();
        for (Move legalMove : legalMoves) {
            if (move.getFromSquare().equals(legalMove.getFromSquare()) && move.getToSquare().equals(legalMove.getToSquare())) {
                isLegal = true;
                break;
            }
        }
        return isLegal;
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

    public boolean isKingSquareAttacked (Side side) {
        return isSquareAttacked(getKingSquare(side), side.getOppositeSide());
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

    public boolean inCheck () {
        return isKingSquareAttacked(sideToMove);
    }

    public boolean isCheckMate() {
        return inCheck() && getLegalMoves().isEmpty();
    }

    public boolean isStaleMate() {
        return !inCheck() && getLegalMoves().isEmpty();
    }

    public boolean isDraw() {
        return isStaleMate() || halfMoveCounter >= 100;
    }
}
