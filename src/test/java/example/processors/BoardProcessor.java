
package example.processors;

import org.neochess.core.Board;
import org.neochess.core.evaluators.DefaultEvaluator;
import org.neochess.core.evaluators.Evaluator;
import org.neogroup.sparks.console.Command;
import org.neogroup.sparks.console.Console;
import org.neogroup.sparks.console.processors.ConsoleProcessor;
import org.neogroup.sparks.console.processors.ProcessCommands;

@ProcessCommands({"print", "init", "flip", "move", "list", "evaluate"})
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
    private Evaluator evaluator;

    public BoardProcessor() {
        flipped = false;
        evaluator = new DefaultEvaluator();
        board = new Board();
        board.setInitialPosition();
    }

    @Override
    protected void processCommand(Console console, Command command) {

        switch (command.getName()) {
            case "init":
                board.setInitialPosition();
                break;
            case "print":
                printBoard (console);
                break;
            case "flip":
                flipped = !flipped;
                break;
            case "move":
                String moveString = command.getParameters().get(0);
                int fromSquare = getSquareFromString(moveString.substring(0,2));
                int toSquare = getSquareFromString(moveString.substring(2));
                board.makeMove(Board.createMove(fromSquare, toSquare));
                printBoard(console);
                break;
            case "list":
                printLegalMoves(console);
                break;
            case "evaluate":
                console.println ("Score: " + evaluator.evaluate(board));
                break;
        }
    }

    private String getSquareString (int square) {

        int file = Board.getSquareFile(square);
        int rank = Board.getSquareRank(square);
        StringBuilder squareString = new StringBuilder();
        switch (file) {
            case Board.FILE_A: squareString.append("a"); break;
            case Board.FILE_B: squareString.append("b"); break;
            case Board.FILE_C: squareString.append("c"); break;
            case Board.FILE_D: squareString.append("d"); break;
            case Board.FILE_E: squareString.append("e"); break;
            case Board.FILE_F: squareString.append("f"); break;
            case Board.FILE_G: squareString.append("g"); break;
            case Board.FILE_H: squareString.append("h"); break;
        }
        switch (rank) {
            case Board.RANK_1: squareString.append("1"); break;
            case Board.RANK_2: squareString.append("2"); break;
            case Board.RANK_3: squareString.append("3"); break;
            case Board.RANK_4: squareString.append("4"); break;
            case Board.RANK_5: squareString.append("5"); break;
            case Board.RANK_6: squareString.append("6"); break;
            case Board.RANK_7: squareString.append("7"); break;
            case Board.RANK_8: squareString.append("8"); break;
        }
        return squareString.toString();
    }

    private int getSquareFromString (String squareString) {

        char fileChar = squareString.charAt(0);
        char rankChar = squareString.charAt(1);
        int file = -1;
        switch (fileChar) {
            case 'a': file = Board.FILE_A; break;
            case 'b': file = Board.FILE_B; break;
            case 'c': file = Board.FILE_C; break;
            case 'd': file = Board.FILE_D; break;
            case 'e': file = Board.FILE_E; break;
            case 'f': file = Board.FILE_F; break;
            case 'g': file = Board.FILE_G; break;
            case 'h': file = Board.FILE_H; break;
        }
        int rank = -1;
        switch (rankChar) {
            case '1': rank = Board.RANK_1; break;
            case '2': rank = Board.RANK_2; break;
            case '3': rank = Board.RANK_3; break;
            case '4': rank = Board.RANK_4; break;
            case '5': rank = Board.RANK_5; break;
            case '6': rank = Board.RANK_6; break;
            case '7': rank = Board.RANK_7; break;
            case '8': rank = Board.RANK_8; break;
        }
        return Board.getSquare(file, rank);
    }

    private void printLegalMoves (Console console) {
        int[] moves = new int[200];
        board.generateLegalMoves(moves);
        for (int move : moves) {
            if (move == 0) {
                break;
            }
            console.print(getSquareString(Board.getMoveFromSquare(move)));
            console.print(getSquareString(Board.getMoveToSquare(move)));
            console.print(" ");
        }
        console.println();
    }

    private void printBoard (Console console) {
        console.print("   ");
        console.println("╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗");
        for (int rank = Board.RANK_8; rank >= Board.RANK_1; rank--) {
            int currentRank = rank;
            if (flipped) {
                currentRank = 7 - currentRank;
            }
            console.print(" ");
            console.print(getRankString(currentRank));
            console.print(" ");
            console.print("║");
            for (int file = Board.FILE_A; file <= Board.FILE_H; file++) {
                int currentFile = file;
                if (flipped) {
                    currentFile = 7 - currentFile;
                }
                int square = Board.getSquare(currentFile,currentRank);
                int piece = board.getPiece(square);
                int pieceSide = Board.getPieceSide(piece);
                console.print(" ");
                if (pieceSide == Board.WHITE) {
                    console.print(ANSI_YELLOW);
                    console.print(getPieceString(piece));
                    console.print(ANSI_RESET);
                }
                else if (pieceSide == Board.BLACK) {
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

            if (rank == Board.RANK_1) {
                console.print("   ");
                console.println("╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝");
            }
            else {
                console.print("   ");
                console.println("╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣");
            }
        }

        console.print("   ");
        for (int file = Board.FILE_A; file <= Board.FILE_H; file++) {
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
            case Board.WHITE_PAWN: return "P";
            case Board.WHITE_KNIGHT: return "N";
            case Board.WHITE_BISHOP: return "B";
            case Board.WHITE_ROOK: return "R";
            case Board.WHITE_QUEEN: return "Q";
            case Board.WHITE_KING: return "K";
            case Board.BLACK_PAWN: return "p";
            case Board.BLACK_KNIGHT: return "n";
            case Board.BLACK_BISHOP: return "b";
            case Board.BLACK_ROOK: return "r";
            case Board.BLACK_QUEEN: return "q";
            case Board.BLACK_KING: return "k";
            default: return " ";
        }
    }

    private String getFileString (int file) {
        switch (file) {
            case Board.FILE_A: return "A";
            case Board.FILE_B: return "B";
            case Board.FILE_C: return "C";
            case Board.FILE_D: return "D";
            case Board.FILE_E: return "E";
            case Board.FILE_F: return "F";
            case Board.FILE_G: return "G";
            case Board.FILE_H: return "H";
            default: return "";
        }
    }

    private String getRankString (int rank) {
        return String.valueOf(rank+1);
    }
}
