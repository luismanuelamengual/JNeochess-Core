
package org.neochess.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.neochess.core.Square.*;
import static org.neochess.core.Side.*;
import static org.neochess.core.Figure.*;
import static org.neochess.core.Piece.*;
import static org.neochess.core.Rank.*;
import static org.neochess.core.File.*;

public class Board {

    private EnumMap<Square,Piece> squares;
    private Side sideToMove;
    private Square epSquare;
    private EnumMap<Side,CastleRights> castleRights;

    public Board() {
        squares = new EnumMap<Square, Piece>(Square.class);
        castleRights = new EnumMap<Side, CastleRights>(Side.class);
        castleRights.put(WHITE, new CastleRights());
        castleRights.put(BLACK, new CastleRights());
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

    public void clear () {
        for (Square square : Square.values()) {
            removePiece(square);
        }
        setEpSquare(null);
        getCastleRights(WHITE).clear();
        getCastleRights(BLACK).clear();
        setSideToMove(WHITE);
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

    public void setFenPosition (String fen) {
        clear();
        byte i,s;
        char c;
        epSquare = null;
        i=0;
        s=56;
        c = fen.charAt(0);
        Square[] squares = Square.values();
        while (c != ' ')
        {
            switch (c)
            {
                case '/': s-=16; break;
                case '1': s+=1;  break;
                case '2': s+=2;  break;
                case '3': s+=3;  break;
                case '4': s+=4;  break;
                case '5': s+=5;  break;
                case '6': s+=6;  break;
                case '7': s+=7;  break;
                case '8': s+=8;  break;
                case 'p': putPiece (squares[s], BLACK_PAWN); s++; break;
                case 'n': putPiece (squares[s], BLACK_KNIGHT); s++; break;
                case 'b': putPiece (squares[s], BLACK_BISHOP); s++; break;
                case 'r': putPiece (squares[s], BLACK_ROOK); s++; break;
                case 'q': putPiece (squares[s], BLACK_QUEEN); s++; break;
                case 'k': putPiece (squares[s], BLACK_KING); s++; break;
                case 'P': putPiece (squares[s], WHITE_PAWN); s++; break;
                case 'N': putPiece (squares[s], WHITE_KNIGHT); s++; break;
                case 'B': putPiece (squares[s], WHITE_BISHOP); s++; break;
                case 'R': putPiece (squares[s], WHITE_ROOK); s++; break;
                case 'Q': putPiece (squares[s], WHITE_QUEEN); s++; break;
                case 'K': putPiece (squares[s], WHITE_KING); s++; break;
            }
            c = fen.charAt(++i);
        }
        c = fen.charAt(++i);
        if (c == 'w') sideToMove = Side.WHITE;
        else if (c == 'b') sideToMove = Side.BLACK;
        i+=2;
        getCastleRights(WHITE).clear();
        getCastleRights(BLACK).clear();
        if (i < fen.length()) {
            c = fen.charAt(i);
            while(c!=' ') {
                if ( c == 'K') getCastleRights(WHITE).setCastleKingSide(true);
                else if ( c == 'Q') getCastleRights(WHITE).setCastleQueenSide(true);
                else if ( c == 'k') getCastleRights(BLACK).setCastleKingSide(true);
                else if ( c == 'q') getCastleRights(BLACK).setCastleQueenSide(true);
                c = fen.charAt(++i);
            }
        }

        String remainingText = fen.substring(++i);
        epSquare = (!remainingText.equals("-"))? Square.valueOf(remainingText) : null;
    }

    public void makeMove (Move move) {

        Square fromSquare = move.getFromSquare();
        Square toSquare = move.getToSquare();
        Piece movingPiece = getPiece(fromSquare);
        Piece capturedPiece = getPiece(toSquare);
        move.setCapturedPiece(capturedPiece);
        move.setEpSquare(epSquare);
        move.setCastleRights(castleRights.clone());

        Figure movingFigure = movingPiece.getFigure();
        if (movingFigure == PAWN) {

            if (sideToMove == WHITE) {
                if (toSquare == epSquare) {
                    removePiece(toSquare.getOffsetSquare(0,-1));
                }
                else if (toSquare.getRank() == EIGHT) {
                    movingPiece = move.getPromotionPiece() != null? move.getPromotionPiece() : WHITE_QUEEN;
                }
                else if (fromSquare.getRank() == TWO && toSquare.getRank() == FOUR) {
                    setEpSquare(Square.getSquare(fromSquare.getFile(), THREE));
                }
            }
            else {
                if (toSquare == epSquare) {
                    removePiece(toSquare.getOffsetSquare(0,1));
                }
                else if (toSquare.getRank() == ONE) {
                    movingPiece = move.getPromotionPiece() != null? move.getPromotionPiece() : BLACK_QUEEN;
                }
                else if (fromSquare.getRank() == SEVEN && toSquare.getRank() == FIVE) {
                    setEpSquare(Square.getSquare(fromSquare.getFile(), SIX));
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

        sideToMove = sideToMove.getOppositeSide();
    }

    public void unmakeMove (Move move) {

        Square fromSquare = move.getFromSquare();
        Square toSquare = move.getToSquare();
        Piece capturedPiece = move.getCapturedPiece();
        EnumMap<Side,CastleRights> castleRights = move.getCastleRights();
        Square epSquare = move.getEpSquare();
        Piece movingPiece = getPiece(toSquare);
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
        sideToMove = getOppositeSide(sideToMove);
    }

    public List<Move> getLegalMoves () {

        EnumMap<Figure, int[][]> figureOffsets = new EnumMap<>(Figure.class);
        figureOffsets.put(KNIGHT, new int[][]{{1,2},{1,-2},{2,1},{2,-1},{-1,2},{-1,-2},{-2,1},{-2,-1}});
        figureOffsets.put(BISHOP, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
        figureOffsets.put(ROOK, new int[][]{{1,0},{-1,0},{0,1},{0,-1}});
        figureOffsets.put(QUEEN, new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}});
        figureOffsets.put(KING, new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}});

        List<Move> moves = new ArrayList<>();
        Side oppositeSide = sideToMove.getOppositeSide();
        for (Square testSquare : Square.values()) {
            Piece piece = getPiece(testSquare);
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
                        Square rightCaptureSquare = testSquare.getOffsetSquare(1,1);
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
                    }
                    else {

                        Square leftCaptureSquare = testSquare.getOffsetSquare(-1, -1);
                        if (pieceFile != A && getPiece(leftCaptureSquare) != null && getPiece(leftCaptureSquare).getSide().equals(WHITE)) {
                            moves.add(new Move(testSquare, leftCaptureSquare));
                        }
                        Square rightCaptureSquare = testSquare.getOffsetSquare(1,-1);
                        if (pieceFile != H && getPiece(rightCaptureSquare) != null && getPiece(rightCaptureSquare).getSide().equals(WHITE)) {
                            moves.add(new Move(testSquare, rightCaptureSquare));
                        }
                        Square nextSquare = testSquare.getOffsetSquare(0, -1);
                        if (getPiece(nextSquare) == null) {
                            moves.add(new Move(testSquare, nextSquare));
                            if (testSquare.getRank().equals(TWO)) {
                                Square nextTwoSquare = testSquare.getOffsetSquare(0, -2);
                                if (getPiece(nextTwoSquare) == null) {
                                    moves.add(new Move(testSquare, nextTwoSquare));
                                }
                            }
                        }
                    }
                }
                else
                {
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
                            if (pieceFigure.equals(KNIGHT)) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        //TODO: checks attacked squares in castles
        if (sideToMove == WHITE) {

            CastleRights sideCastleRights = this.castleRights.get(WHITE);
            if (sideCastleRights.canCastleKingSide() && getPiece(F1) == null && getPiece(G1) == null) {
                moves.add(new Move(E1, G1));
            }
            if (sideCastleRights.canCastleQueenSide() && getPiece(D1) == null && getPiece(C1) == null && getPiece(B1) == null) {
                moves.add(new Move(E1, C1));
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
            if (sideCastleRights.canCastleKingSide() && getPiece(F8) == null && getPiece(G8) == null) {
                moves.add(new Move(E8, G8));
            }
            if (sideCastleRights.canCastleQueenSide() && getPiece(D8) == null && getPiece(C8) == null && getPiece(B8) == null) {
                moves.add(new Move(E8, C8));
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
        return moves;
    }
}
