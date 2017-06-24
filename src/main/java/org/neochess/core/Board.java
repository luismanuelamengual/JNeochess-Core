
package org.neochess.core;

import java.util.EnumMap;

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
        //castleRights &= self::$castleMask[$fromSquare] & self::$castleMask[$toSquare];
        sideToMove = sideToMove.getOppositeSide();
    }
}
