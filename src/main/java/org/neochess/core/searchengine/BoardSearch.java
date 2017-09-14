
package org.neochess.core.searchengine;

public class BoardSearch {

    private final Board board;
    private long searchMilliseconds;
    private BoardEvaluator evaluator;
    private boolean searching;
    private long startSearchTimestamp;
    private long stopSearchTimestamp;

    public BoardSearch(Board board) {
        this(board, 10000);
    }

    public BoardSearch(Board board, long searchMilliseconds) {
        this.board = board;
        this.searchMilliseconds = searchMilliseconds;
        searching = false;
    }

    public long getSearchMilliseconds() {
        return searchMilliseconds;
    }

    public void setSearchMilliseconds(long searchMilliseconds) {
        this.searchMilliseconds = searchMilliseconds;
    }

    public BoardEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(BoardEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void start() {
        if (!searching) {
            this.startSearchTimestamp = System.currentTimeMillis();
            this.startSearchTimestamp = System.currentTimeMillis();
            this.stopSearchTimestamp = this.startSearchTimestamp + searchMilliseconds;


            searching = true;
        }
    }

    public void stop () {
        this.stopSearchTimestamp = System.currentTimeMillis();
    }

    private boolean isTimeUp () {
        return System.currentTimeMillis() > this.stopSearchTimestamp;
    }
}
