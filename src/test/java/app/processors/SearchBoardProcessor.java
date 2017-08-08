
package app.processors;

import org.neochess.core.searchengine.BkpBoard;
import org.neogroup.sparks.console.Command;
import org.neogroup.sparks.console.Console;
import org.neogroup.sparks.console.ConsoleCommand;
import org.neogroup.sparks.console.processors.ConsoleProcessor;

public class SearchBoardProcessor extends ConsoleProcessor {

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
    private BkpBoard board;

    public SearchBoardProcessor() {
        flipped = false;
        board = new BkpBoard();
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
        int fromSquare = getSquareFromString(moveString.substring(0,2));
        int toSquare = getSquareFromString(moveString.substring(2));
        board.makeMove(BkpBoard.createMove(fromSquare, toSquare));
        printBoard(console);
    }

    @ConsoleCommand("list")
    public void listMoves (Console console, Command command) {
        printLegalMoves(console);
    }

    private String getSquareString (int square) {

        int file = BkpBoard.getSquareFile(square);
        int rank = BkpBoard.getSquareRank(square);
        StringBuilder squareString = new StringBuilder();
        switch (file) {
            case BkpBoard.FILE_A: squareString.append("a"); break;
            case BkpBoard.FILE_B: squareString.append("b"); break;
            case BkpBoard.FILE_C: squareString.append("c"); break;
            case BkpBoard.FILE_D: squareString.append("d"); break;
            case BkpBoard.FILE_E: squareString.append("e"); break;
            case BkpBoard.FILE_F: squareString.append("f"); break;
            case BkpBoard.FILE_G: squareString.append("g"); break;
            case BkpBoard.FILE_H: squareString.append("h"); break;
        }
        switch (rank) {
            case BkpBoard.RANK_1: squareString.append("1"); break;
            case BkpBoard.RANK_2: squareString.append("2"); break;
            case BkpBoard.RANK_3: squareString.append("3"); break;
            case BkpBoard.RANK_4: squareString.append("4"); break;
            case BkpBoard.RANK_5: squareString.append("5"); break;
            case BkpBoard.RANK_6: squareString.append("6"); break;
            case BkpBoard.RANK_7: squareString.append("7"); break;
            case BkpBoard.RANK_8: squareString.append("8"); break;
        }
        return squareString.toString();
    }

    private int getSquareFromString (String squareString) {

        char fileChar = squareString.charAt(0);
        char rankChar = squareString.charAt(1);
        int file = -1;
        switch (fileChar) {
            case 'a': file = BkpBoard.FILE_A; break;
            case 'b': file = BkpBoard.FILE_B; break;
            case 'c': file = BkpBoard.FILE_C; break;
            case 'd': file = BkpBoard.FILE_D; break;
            case 'e': file = BkpBoard.FILE_E; break;
            case 'f': file = BkpBoard.FILE_F; break;
            case 'g': file = BkpBoard.FILE_G; break;
            case 'h': file = BkpBoard.FILE_H; break;
        }
        int rank = -1;
        switch (rankChar) {
            case '1': rank = BkpBoard.RANK_1; break;
            case '2': rank = BkpBoard.RANK_2; break;
            case '3': rank = BkpBoard.RANK_3; break;
            case '4': rank = BkpBoard.RANK_4; break;
            case '5': rank = BkpBoard.RANK_5; break;
            case '6': rank = BkpBoard.RANK_6; break;
            case '7': rank = BkpBoard.RANK_7; break;
            case '8': rank = BkpBoard.RANK_8; break;
        }
        return BkpBoard.getSquare(file, rank);
    }

    private void printLegalMoves (Console console) {
        long[] moves = new long[200];
        board.generateLegalMoves(moves);
        for (long move : moves) {
            if (move == 0) {
                break;
            }
            console.print(getSquareString(BkpBoard.getMoveFromSquare(move)));
            console.print(getSquareString(BkpBoard.getMoveToSquare(move)));
            console.print(" ");
        }
        console.println();
    }

    private void printBoard (Console console) {
        console.print("   ");
        console.println("╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗");
        for (int rank = BkpBoard.RANK_8; rank >= BkpBoard.RANK_1; rank--) {
            int currentRank = rank;
            if (flipped) {
                currentRank = 7 - currentRank;
            }
            console.print(" ");
            console.print(getRankString(currentRank));
            console.print(" ");
            console.print("║");
            for (int file = BkpBoard.FILE_A; file <= BkpBoard.FILE_H; file++) {
                int currentFile = file;
                if (flipped) {
                    currentFile = 7 - currentFile;
                }
                int square = BkpBoard.getSquare(currentFile,currentRank);
                int piece = board.getPiece(square);
                int pieceSide = BkpBoard.getPieceSide(piece);
                console.print(" ");
                if (pieceSide == BkpBoard.WHITE) {
                    console.print(ANSI_YELLOW);
                    console.print(getPieceString(piece));
                    console.print(ANSI_RESET);
                }
                else if (pieceSide == BkpBoard.BLACK) {
                    console.print(ANSI_PURPLE);
                    console.print(getPieceString(piece));
                    console.print(ANSI_RESET);
                }
                else {
                    console.print(" ");
                }
                console.print(" ║");
            }
            console.println();

            if (rank == BkpBoard.RANK_1) {
                console.print("   ");
                console.println("╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝");
            }
            else {
                console.print("   ");
                console.println("╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣");
            }
        }

        console.print("   ");
        for (int file = BkpBoard.FILE_A; file <= BkpBoard.FILE_H; file++) {
            int currentFile = file;
            if (flipped) {
                currentFile = 7 - currentFile;
            }
            console.print("  ");
            console.print(getFileString(currentFile));
            console.print(" ");
        }
        console.println();
    }

    private String getPieceString (int piece) {
        switch (piece) {
            case BkpBoard.WHITE_PAWN: return "P";
            case BkpBoard.WHITE_KNIGHT: return "N";
            case BkpBoard.WHITE_BISHOP: return "B";
            case BkpBoard.WHITE_ROOK: return "R";
            case BkpBoard.WHITE_QUEEN: return "Q";
            case BkpBoard.WHITE_KING: return "K";
            case BkpBoard.BLACK_PAWN: return "p";
            case BkpBoard.BLACK_KNIGHT: return "n";
            case BkpBoard.BLACK_BISHOP: return "b";
            case BkpBoard.BLACK_ROOK: return "r";
            case BkpBoard.BLACK_QUEEN: return "q";
            case BkpBoard.BLACK_KING: return "k";
            default: return " ";
        }
    }

    private String getFileString (int file) {
        switch (file) {
            case BkpBoard.FILE_A: return "A";
            case BkpBoard.FILE_B: return "B";
            case BkpBoard.FILE_C: return "C";
            case BkpBoard.FILE_D: return "D";
            case BkpBoard.FILE_E: return "E";
            case BkpBoard.FILE_F: return "F";
            case BkpBoard.FILE_G: return "G";
            case BkpBoard.FILE_H: return "H";
            default: return "";
        }
    }

    private String getRankString (int rank) {
        return String.valueOf(rank+1);
    }
}
