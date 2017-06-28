package app.processors;

import org.neochess.core.*;
import org.neogroup.sparks.console.Command;
import org.neogroup.sparks.console.Console;
import org.neogroup.sparks.console.ConsoleCommand;
import org.neogroup.sparks.console.processors.ConsoleProcessor;

import java.util.List;

import static org.neochess.core.File.*;
import static org.neochess.core.Rank.*;
import static org.neochess.core.Side.BLACK;
import static org.neochess.core.Side.WHITE;

public class BoardProcessor extends ConsoleProcessor {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private boolean flipped;
    private Board board;

    public BoardProcessor() {
        flipped = false;
        board = new Board();
        board.setInitialPosition();
    }

    @ConsoleCommand("init")
    public void initBoard (Console console, Command command) {
        board.setInitialPosition();
        console.println("Board initialized !!");
    }

    @ConsoleCommand("print")
    public void printBoard (Console console, Command command) {
        printBoard(console);
    }

    @ConsoleCommand("flip")
    public void flipBoard (Console console, Command command) {
        flipped = !flipped;
        console.println("Board flipped !!");
    }

    @ConsoleCommand("move")
    public void makeMove (Console console, Command command) {
        String moveString = command.getParameters().get(0);
        Square fromSquare = getSquareFromString(moveString.substring(0,2));
        Square toSquare = getSquareFromString(moveString.substring(2));
        Move moveMade = board.makeMove(fromSquare, toSquare);
        if (moveMade != null) {
            printBoard(console);
        }
        else {
            console.println ("Illegal move !!");
        }
    }

    @ConsoleCommand("takeback")
    public void unmakeMove (Console console, Command command) {
        board.unmakeMove();
        printBoard(console);
    }

    @ConsoleCommand("setFen")
    public void setFenPosition (Console console, Command command) {
        String fen = command.getParameters().get(0);
        board.setFen(fen);
        printBoard(console);
    }

    @ConsoleCommand("getFen")
    public void getFenPosition (Console console, Command command) {
        String fen = board.getFen();
        console.println(fen);
    }

    @ConsoleCommand("list")
    public void listMoves (Console console, Command command) {
        List<Move> moves = board.getLegalMoves();
        for (Move move : moves) {
            console.print(move.getSan());
            console.print(" ");
        }
        console.println();
    }

    @ConsoleCommand("history")
    public void listHistoryMoves (Console console, Command command) {
        List<Move> moves = board.getHistory();
        for (Move move : moves) {
            console.print(move.getSan());
            console.print(" ");
        }
        console.println();
    }

    private String getSquareString (Square square) {

        File file = square.getFile();
        Rank rank = square.getRank();
        StringBuilder squareString = new StringBuilder();
        switch (file) {
            case A: squareString.append("a"); break;
            case B: squareString.append("b"); break;
            case C: squareString.append("c"); break;
            case D: squareString.append("d"); break;
            case E: squareString.append("e"); break;
            case F: squareString.append("f"); break;
            case G: squareString.append("g"); break;
            case H: squareString.append("h"); break;
        }
        switch (rank) {
            case ONE: squareString.append("1"); break;
            case TWO: squareString.append("2"); break;
            case THREE: squareString.append("3"); break;
            case FOUR: squareString.append("4"); break;
            case FIVE: squareString.append("5"); break;
            case SIX: squareString.append("6"); break;
            case SEVEN: squareString.append("7"); break;
            case EIGHT: squareString.append("8"); break;
        }
        return squareString.toString();
    }

    private Square getSquareFromString (String squareString) {

        char fileChar = squareString.charAt(0);
        char rankChar = squareString.charAt(1);
        File file = null;
        switch (fileChar) {
            case 'a': file = A; break;
            case 'b': file = B; break;
            case 'c': file = C; break;
            case 'd': file = D; break;
            case 'e': file = E; break;
            case 'f': file = F; break;
            case 'g': file = G; break;
            case 'h': file = H; break;
        }
        Rank rank = null;
        switch (rankChar) {
            case '1': rank = ONE; break;
            case '2': rank = TWO; break;
            case '3': rank = THREE; break;
            case '4': rank = FOUR; break;
            case '5': rank = FIVE; break;
            case '6': rank = SIX; break;
            case '7': rank = SEVEN; break;
            case '8': rank = EIGHT; break;
        }
        return Square.getSquare(file, rank);
    }

    private void printBoard (Console console) {

        Rank[] ranks = { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT };
        File[] files = { A, B, C, D, E, F, G, H };
        console.print("   ");
        console.println("╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗");
        for (int rankIndex = 7; rankIndex >= 0; rankIndex--) {
            int currentRankIndex = rankIndex;
            if (flipped) {
                currentRankIndex = 7 - currentRankIndex;
            }
            Rank rank = ranks[currentRankIndex];
            console.print(" ");
            console.print(getRankString(rank));
            console.print(" ");
            console.print("║");
            for (int fileIndex = 0; fileIndex <= 7; fileIndex++) {
                int currentFileIndex = fileIndex;
                if (flipped) {
                    currentFileIndex = 7 - currentFileIndex;
                }
                File file = files[currentFileIndex];
                Square square = Square.getSquare(file,rank);
                Piece piece = board.getPiece(square);
                if (piece != null) {
                    Side pieceSide = piece.getSide();
                    console.print(" ");
                    if (pieceSide == WHITE) {
                        console.print(ANSI_YELLOW);
                        console.print(getPieceString(piece));
                        console.print(ANSI_RESET);
                    } else if (pieceSide == BLACK) {
                        console.print(ANSI_PURPLE);
                        console.print(getPieceString(piece));
                        console.print(ANSI_RESET);
                    }
                }
                else {
                    console.print(" ");
                    console.print(" ");
                }
                console.print(" ║");
            }
            console.println();

            if (!flipped && rank == ONE || flipped && rank == EIGHT) {
                console.print("   ");
                console.println("╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝");
            }
            else {
                console.print("   ");
                console.println("╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣");
            }
        }

        console.print("   ");
        for (int fileIndex = 0; fileIndex <= 7; fileIndex++) {
            int currentFileIndex = fileIndex;
            if (flipped) {
                currentFileIndex = 7 - currentFileIndex;
            }
            File file = files[currentFileIndex];
            console.print("  ");
            console.print(getFileString(file));
            console.print(" ");
        }
        console.println();
    }

    private String getPieceString (Piece piece) {
        switch (piece) {
            case WHITE_PAWN: return "P";
            case WHITE_KNIGHT: return "N";
            case WHITE_BISHOP: return "B";
            case WHITE_ROOK: return "R";
            case WHITE_QUEEN: return "Q";
            case WHITE_KING: return "K";
            case BLACK_PAWN: return "p";
            case BLACK_KNIGHT: return "n";
            case BLACK_BISHOP: return "b";
            case BLACK_ROOK: return "r";
            case BLACK_QUEEN: return "q";
            case BLACK_KING: return "k";
            default: return " ";
        }
    }

    private String getFileString (File file) {
        switch (file) {
            case A: return "A";
            case B: return "B";
            case C: return "C";
            case D: return "D";
            case E: return "E";
            case F: return "F";
            case G: return "G";
            case H: return "H";
            default: return "";
        }
    }

    private String getRankString (Rank rank) {
        return String.valueOf(rank.ordinal()+1);
    }
}
