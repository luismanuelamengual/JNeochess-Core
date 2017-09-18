
package app.processors;

import org.neochess.core.searchengine.Board;
import org.neochess.core.searchengine.BoardEvaluator;
import org.neochess.core.searchengine.BoardSearch;
import org.neochess.core.searchengine.BoardUtils;
import org.neogroup.sparks.console.Command;
import org.neogroup.sparks.console.Console;
import org.neogroup.sparks.console.ConsoleCommand;
import org.neogroup.sparks.console.processors.ConsoleProcessor;

import java.util.ArrayList;
import java.util.List;

public class SearchBoardProcessor extends ConsoleProcessor {

    private boolean flipped;
    private List<Long> movesMade;
    private Board board;

    public SearchBoardProcessor() {
        flipped = false;
        movesMade = new ArrayList<>();
        board = new Board();
        board.setStartupPosition();
    }

    @ConsoleCommand("init")
    public void initBoard (Console console, Command command) {
        board.setStartupPosition();
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
        long move = BoardUtils.getMoveFromString(board, moveString);
        if (move != 0) {
            long moveMade = board.makeMove(move);
            movesMade.add(moveMade);
        }
        printBoard(console);
    }

    @ConsoleCommand("takeback")
    public void unmakeMove (Console console, Command command) {
        if (!movesMade.isEmpty()) {
            long lastMove = movesMade.remove(movesMade.size() - 1);
            board.unmakeMove(lastMove);
        }
        printBoard(console);
    }

    @ConsoleCommand("list")
    public void listMoves (Console console, Command command) {
        printLegalMoves(console);
    }

    @ConsoleCommand("evaluate")
    public void evaluateBoard (Console console, Command command) {
        int evaluation = BoardEvaluator.getDefault().evaluate(board);
        console.println("Evaluation: " + evaluation);
    }

    @ConsoleCommand("search")
    public void searchBoard (Console console, Command command) {

        try {
            BoardSearch search = new BoardSearch(board, 10000);
            search.start();
            long move = search.getSearchMove();
            console.print(BoardUtils.getMoveString(move));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printLegalMoves (Console console) {
        long[] moves = new long[200];
        board.generateLegalMoves(moves);
        for (long move : moves) {
            if (move == 0) {
                break;
            }
            console.print(BoardUtils.getMoveString(move));
            console.print(" ");
        }
        console.println();
    }

    private void printBoard(Console console) {
        BoardUtils.printBoard(board, System.out, flipped);
        System.out.println("Integrity: " + board.checkIntegrity());
        System.out.println();
    }
}
