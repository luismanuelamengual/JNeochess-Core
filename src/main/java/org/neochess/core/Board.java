
package org.neochess.core;

import java.util.EnumMap;

public class Board {

    private EnumMap<Square,Piece> squares;
    private Side sideToMove;
    private Square epSquare;
    private EnumMap<Side,CastleRight> castleRight;

    public Board() {
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

    public CastleRight getCastleRight(Side side) {
        return castleRight.get(side);
    }

    public void setCastleRight(Side side, CastleRight castleRight) {
        this.castleRight.put(side, castleRight);
    }

    public void clear () {
        for (Square square : Square.values()) {
            removePiece(square);
        }
        setEpSquare(null);
        setCastleRight(Side.WHITE, null);
        setCastleRight(Side.BLACK, null);
        setSideToMove(Side.WHITE);
    }

    public void setInitialPosition () {
        clear();
        putPiece(Square.A1, Piece.WHITE_ROOK);
        putPiece(Square.H1, Piece.WHITE_ROOK);
        putPiece(Square.B1, Piece.WHITE_KNIGHT);
        putPiece(Square.G1, Piece.WHITE_KNIGHT);
        putPiece(Square.C1, Piece.WHITE_BISHOP);
        putPiece(Square.F1, Piece.WHITE_BISHOP);
        putPiece(Square.D1, Piece.WHITE_QUEEN);
        putPiece(Square.E1, Piece.WHITE_KING);
        putPiece(Square.A2, Piece.WHITE_PAWN);
        putPiece(Square.B2, Piece.WHITE_PAWN);
        putPiece(Square.C2, Piece.WHITE_PAWN);
        putPiece(Square.D2, Piece.WHITE_PAWN);
        putPiece(Square.E2, Piece.WHITE_PAWN);
        putPiece(Square.F2, Piece.WHITE_PAWN);
        putPiece(Square.G2, Piece.WHITE_PAWN);
        putPiece(Square.H2, Piece.WHITE_PAWN);
        putPiece(Square.A8, Piece.BLACK_ROOK);
        putPiece(Square.H8, Piece.BLACK_ROOK);
        putPiece(Square.B8, Piece.BLACK_KNIGHT);
        putPiece(Square.G8, Piece.BLACK_KNIGHT);
        putPiece(Square.C8, Piece.BLACK_BISHOP);
        putPiece(Square.F8, Piece.BLACK_BISHOP);
        putPiece(Square.D8, Piece.BLACK_QUEEN);
        putPiece(Square.E8, Piece.BLACK_KING);
        putPiece(Square.A7, Piece.BLACK_PAWN);
        putPiece(Square.B7, Piece.BLACK_PAWN);
        putPiece(Square.C7, Piece.BLACK_PAWN);
        putPiece(Square.D7, Piece.BLACK_PAWN);
        putPiece(Square.E7, Piece.BLACK_PAWN);
        putPiece(Square.F7, Piece.BLACK_PAWN);
        putPiece(Square.G7, Piece.BLACK_PAWN);
        putPiece(Square.H7, Piece.BLACK_PAWN);
        setCastleRight(Side.WHITE, CastleRight.KING_AND_QUEEN_SIDE);
        setCastleRight(Side.BLACK, CastleRight.KING_AND_QUEEN_SIDE);
        setSideToMove(Side.WHITE);
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
                case 'p': putPiece (squares[s], Piece.BLACK_PAWN); s++; break;
                case 'n': putPiece (squares[s], Piece.BLACK_KNIGHT); s++; break;
                case 'b': putPiece (squares[s], Piece.BLACK_BISHOP); s++; break;
                case 'r': putPiece (squares[s], Piece.BLACK_ROOK); s++; break;
                case 'q': putPiece (squares[s], Piece.BLACK_QUEEN); s++; break;
                case 'k': putPiece (squares[s], Piece.BLACK_KING); s++; break;
                case 'P': putPiece (squares[s], Piece.WHITE_PAWN); s++; break;
                case 'N': putPiece (squares[s], Piece.WHITE_KNIGHT); s++; break;
                case 'B': putPiece (squares[s], Piece.WHITE_BISHOP); s++; break;
                case 'R': putPiece (squares[s], Piece.WHITE_ROOK); s++; break;
                case 'Q': putPiece (squares[s], Piece.WHITE_QUEEN); s++; break;
                case 'K': putPiece (squares[s], Piece.WHITE_KING); s++; break;
            }
            c = fen.charAt(++i);
        }
        c = fen.charAt(++i);
        if (c == 'w') sideToMove = Side.WHITE;
        else if (c == 'b') sideToMove = Side.BLACK;
        i+=2;
        setCastleRight(Side.WHITE, null);
        setCastleRight(Side.BLACK, null);
        if (i < fen.length()) {
            c = fen.charAt(i);
            boolean whiteKingCastle = false;
            boolean whiteQueenCastle = false;
            boolean blackKingCastle = false;
            boolean blackQueenCastle = false;
            while(c!=' ') {
                if ( c == 'K') whiteKingCastle = true;
                else if ( c == 'Q') whiteQueenCastle = true;
                else if ( c == 'k') blackKingCastle = true;
                else if ( c == 'q') blackQueenCastle = true;
                c = fen.charAt(++i);
            }

            if (whiteKingCastle && whiteQueenCastle) {
                setCastleRight(Side.WHITE, CastleRight.KING_AND_QUEEN_SIDE);
            }
            else if (whiteKingCastle) {
                setCastleRight(Side.WHITE, CastleRight.KING_SIDE);
            }
            else if (whiteQueenCastle) {
                setCastleRight(Side.WHITE, CastleRight.QUEEN_SIDE);
            }

            if (blackKingCastle && blackQueenCastle) {
                setCastleRight(Side.BLACK, CastleRight.KING_AND_QUEEN_SIDE);
            }
            else if (blackKingCastle) {
                setCastleRight(Side.BLACK, CastleRight.KING_SIDE);
            }
            else if (blackQueenCastle) {
                setCastleRight(Side.BLACK, CastleRight.QUEEN_SIDE);
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
        if (movingFigure == Figure.PAWN) {

            if (sideToMove == Side.WHITE) {
                if (toSquare == epSquare) {
                    removePiece(toSquare.getOffsetSquare(0,-1));
                }
                else if (toSquare.getRank() == Rank.EIGHT) {
                    movingPiece = move.getPromotionPiece() != null? move.getPromotionPiece() : Piece.WHITE_QUEEN;
                }
                else if (fromSquare.getRank() == Rank.TWO && toSquare.getRank() == Rank.FOUR) {
                    setEpSquare(Square.getSquare(fromSquare.getFile(), Rank.THREE));
                }
            }
            else {
                if (toSquare == epSquare) {
                    removePiece(toSquare.getOffsetSquare(0,1));
                }
                else if (toSquare.getRank() == Rank.ONE) {
                    movingPiece = move.getPromotionPiece() != null? move.getPromotionPiece() : Piece.BLACK_QUEEN;
                }
                else if (fromSquare.getRank() == Rank.SEVEN && toSquare.getRank() == Rank.FIVE) {
                    setEpSquare(Square.getSquare(fromSquare.getFile(), Rank.SIX));
                }
            }
        }
        else {

            if (movingFigure == Figure.KING) {
                if (fromSquare == Square.E1) {
                    switch (toSquare) {
                        case G1:
                            removePiece(Square.H1);
                            putPiece(Square.F1, Piece.WHITE_ROOK);
                            break;
                        case C1:
                            removePiece(Square.A1);
                            putPiece(Square.D1, Piece.WHITE_ROOK);
                            break;
                    }
                }
                else if (fromSquare == Square.E8) {
                    switch (toSquare) {
                        case G8:
                            removePiece(Square.H8);
                            putPiece(Square.F8, Piece.BLACK_ROOK);
                            break;
                        case C8:
                            removePiece(Square.A8);
                            putPiece(Square.D8, Piece.BLACK_ROOK);
                            break;
                    }
                }
            }
            setEpSquare(null);
        }

        removePiece(fromSquare);
        putPiece(toSquare, movingPiece);
        //castleRight &= self::$castleMask[$fromSquare] & self::$castleMask[$toSquare];
        sideToMove = sideToMove.getOppositeSide();
    }
}
