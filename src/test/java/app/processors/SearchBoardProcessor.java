
package app.processors;

import org.neochess.core.searchengine.SearchBoard;
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
    private SearchBoard board;

    public SearchBoardProcessor() {
        flipped = false;
        board = new SearchBoard();
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
        board.makeMove(SearchBoard.createMove(fromSquare, toSquare));
        printBoard(console);
    }

    @ConsoleCommand("list")
    public void listMoves (Console console, Command command) {
        printLegalMoves(console);
    }

    private String getSquareString (int square) {

        int file = SearchBoard.getSquareFile(square);
        int rank = SearchBoard.getSquareRank(square);
        StringBuilder squareString = new StringBuilder();
        switch (file) {
            case SearchBoard.FILE_A: squareString.append("a"); break;
            case SearchBoard.FILE_B: squareString.append("b"); break;
            case SearchBoard.FILE_C: squareString.append("c"); break;
            case SearchBoard.FILE_D: squareString.append("d"); break;
            case SearchBoard.FILE_E: squareString.append("e"); break;
            case SearchBoard.FILE_F: squareString.append("f"); break;
            case SearchBoard.FILE_G: squareString.append("g"); break;
            case SearchBoard.FILE_H: squareString.append("h"); break;
        }
        switch (rank) {
            case SearchBoard.RANK_1: squareString.append("1"); break;
            case SearchBoard.RANK_2: squareString.append("2"); break;
            case SearchBoard.RANK_3: squareString.append("3"); break;
            case SearchBoard.RANK_4: squareString.append("4"); break;
            case SearchBoard.RANK_5: squareString.append("5"); break;
            case SearchBoard.RANK_6: squareString.append("6"); break;
            case SearchBoard.RANK_7: squareString.append("7"); break;
            case SearchBoard.RANK_8: squareString.append("8"); break;
        }
        return squareString.toString();
    }

    private int getSquareFromString (String squareString) {

        char fileChar = squareString.charAt(0);
        char rankChar = squareString.charAt(1);
        int file = -1;
        switch (fileChar) {
            case 'a': file = SearchBoard.FILE_A; break;
            case 'b': file = SearchBoard.FILE_B; break;
            case 'c': file = SearchBoard.FILE_C; break;
            case 'd': file = SearchBoard.FILE_D; break;
            case 'e': file = SearchBoard.FILE_E; break;
            case 'f': file = SearchBoard.FILE_F; break;
            case 'g': file = SearchBoard.FILE_G; break;
            case 'h': file = SearchBoard.FILE_H; break;
        }
        int rank = -1;
        switch (rankChar) {
            case '1': rank = SearchBoard.RANK_1; break;
            case '2': rank = SearchBoard.RANK_2; break;
            case '3': rank = SearchBoard.RANK_3; break;
            case '4': rank = SearchBoard.RANK_4; break;
            case '5': rank = SearchBoard.RANK_5; break;
            case '6': rank = SearchBoard.RANK_6; break;
            case '7': rank = SearchBoard.RANK_7; break;
            case '8': rank = SearchBoard.RANK_8; break;
        }
        return SearchBoard.getSquare(file, rank);
    }

    private void printLegalMoves (Console console) {
        long[] moves = new long[200];
        board.generateLegalMoves(moves);
        for (long move : moves) {
            if (move == 0) {
                break;
            }
            console.print(getSquareString(SearchBoard.getMoveFromSquare(move)));
            console.print(getSquareString(SearchBoard.getMoveToSquare(move)));
            console.print(" ");
        }
        console.println();
    }

    private void printBoard (Console console) {
        console.print("   ");
        console.println("╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗");
        for (int rank = SearchBoard.RANK_8; rank >= SearchBoard.RANK_1; rank--) {
            int currentRank = rank;
            if (flipped) {
                currentRank = 7 - currentRank;
            }
            console.print(" ");
            console.print(getRankString(currentRank));
            console.print(" ");
            console.print("║");
            for (int file = SearchBoard.FILE_A; file <= SearchBoard.FILE_H; file++) {
                int currentFile = file;
                if (flipped) {
                    currentFile = 7 - currentFile;
                }
                int square = SearchBoard.getSquare(currentFile,currentRank);
                int piece = board.getPiece(square);
                int pieceSide = SearchBoard.getPieceSide(piece);
                console.print(" ");
                if (pieceSide == SearchBoard.WHITE) {
                    console.print(ANSI_YELLOW);
                    console.print(getPieceString(piece));
                    console.print(ANSI_RESET);
                }
                else if (pieceSide == SearchBoard.BLACK) {
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

            if (rank == SearchBoard.RANK_1) {
                console.print("   ");
                console.println("╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝");
            }
            else {
                console.print("   ");
                console.println("╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣");
            }
        }

        console.print("   ");
        for (int file = SearchBoard.FILE_A; file <= SearchBoard.FILE_H; file++) {
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
            case SearchBoard.WHITE_PAWN: return "P";
            case SearchBoard.WHITE_KNIGHT: return "N";
            case SearchBoard.WHITE_BISHOP: return "B";
            case SearchBoard.WHITE_ROOK: return "R";
            case SearchBoard.WHITE_QUEEN: return "Q";
            case SearchBoard.WHITE_KING: return "K";
            case SearchBoard.BLACK_PAWN: return "p";
            case SearchBoard.BLACK_KNIGHT: return "n";
            case SearchBoard.BLACK_BISHOP: return "b";
            case SearchBoard.BLACK_ROOK: return "r";
            case SearchBoard.BLACK_QUEEN: return "q";
            case SearchBoard.BLACK_KING: return "k";
            default: return " ";
        }
    }

    private String getFileString (int file) {
        switch (file) {
            case SearchBoard.FILE_A: return "A";
            case SearchBoard.FILE_B: return "B";
            case SearchBoard.FILE_C: return "C";
            case SearchBoard.FILE_D: return "D";
            case SearchBoard.FILE_E: return "E";
            case SearchBoard.FILE_F: return "F";
            case SearchBoard.FILE_G: return "G";
            case SearchBoard.FILE_H: return "H";
            default: return "";
        }
    }

    private String getRankString (int rank) {
        return String.valueOf(rank+1);
    }
}
