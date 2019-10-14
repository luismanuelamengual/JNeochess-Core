package app.processors;

import org.neochess.core.*;
import org.neogroup.sparks.console.Command;
import org.neogroup.sparks.console.Console;
import org.neogroup.sparks.console.ConsoleCommand;
import org.neogroup.sparks.console.processors.ConsoleProcessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.neochess.core.File.*;
import static org.neochess.core.Rank.*;
import static org.neochess.core.Side.BLACK;
import static org.neochess.core.Side.WHITE;

public class MatchConsoleProcessor extends ConsoleProcessor {

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
    private Match match;

    public MatchConsoleProcessor() {
        flipped = false;
        match = new Match();
        match.getBoard().setInitialPosition();
    }

    @ConsoleCommand("init")
    public void initBoard (Console console, Command command) {
        match.getBoard().setInitialPosition();
        console.println("Board initialized !!");
    }

    @ConsoleCommand("print")
    public void printBoard (Console console, Command command) {

        if (command.getParameters().size() > 0) {
            int ply = Integer.parseInt(command.getParameters().get(0));
            printBoard(console, match.getBoard(ply));
        }
        else {
            printBoard(console, match.getBoard());
        }
    }

    @ConsoleCommand("flip")
    public void flipBoard (Console console, Command command) {
        flipped = !flipped;
        console.println("Board flipped !!");
    }

    @ConsoleCommand("move")
    public void makeMove (Console console, Command command) {

        try {
            String moveString = command.getParameters().get(0);
            Pattern pattern = Pattern.compile("[a-h][1-8][a-h][1-8]");
            Matcher matcher = pattern.matcher(moveString);
            boolean moveMade;
            if (matcher.find()) {
                Square fromSquare = Square.fromSAN(moveString.substring(0, 2));
                Square toSquare = Square.fromSAN(moveString.substring(2));
                moveMade = match.makeMove(fromSquare, toSquare);
            } else {
                moveMade = match.makeMove(moveString);
            }
            if (moveMade) {
                printBoard(console, match.getBoard());
            } else {
                console.println("Illegal move !!");
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @ConsoleCommand("takeback")
    public void unmakeMove (Console console, Command command) {
        match.unmakeMove();
        printBoard(console, match.getBoard());
    }

    @ConsoleCommand("setFen")
    public void setFenPosition (Console console, Command command) {
        String fen = command.getParameters().get(0);
        match.getBoard().setFromFEN(fen);
        printBoard(console, match.getBoard());
    }

    @ConsoleCommand("getFen")
    public void getFenPosition (Console console, Command command) {
        String fen = match.getBoard().toFEN();
        console.println(fen);
    }

    @ConsoleCommand("list")
    public void listMoves (Console console, Command command) {
        List<Move> moves = match.getBoard().getLegalMoves();
        for (Move move : moves) {
            console.print(move.toSAN(match.getBoard()));
            console.print(" ");
        }
        console.println();
    }

    @ConsoleCommand("history")
    public void listHistoryMoves (Console console, Command command) {
        List<Move> moves = match.getMoves();
        for (Move move : moves) {
            console.print(move.toSAN(match.getBoard()));
            console.print(" ");
        }
        console.println();
    }

    private void printBoard (Console console, Board board) {

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
            console.print(rank.ordinal() + 1);
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
                        switch (piece.getFigure()) {
                            case KING: console.print("K"); break;
                            case QUEEN: console.print("Q"); break;
                            case ROOK: console.print("R"); break;
                            case BISHOP: console.print("B"); break;
                            case KNIGHT: console.print("N"); break;
                            case PAWN: console.print("P"); break;
                        }
                        console.print(ANSI_RESET);
                    } else if (pieceSide == BLACK) {
                        console.print(ANSI_PURPLE);
                        switch (piece.getFigure()) {
                            case KING: console.print("k"); break;
                            case QUEEN: console.print("q"); break;
                            case ROOK: console.print("r"); break;
                            case BISHOP: console.print("b"); break;
                            case KNIGHT: console.print("n"); break;
                            case PAWN: console.print("p"); break;
                        }
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
            console.print(file.toString().toLowerCase());
            console.print(" ");
        }
        console.println();
    }
}
