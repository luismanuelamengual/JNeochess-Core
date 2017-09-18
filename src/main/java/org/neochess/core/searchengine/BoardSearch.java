
package org.neochess.core.searchengine;

public class BoardSearch {

    private final static int MATE = 32000;
    private final static int INFINITY = 32000;
    private final static int ASPIRATIONWINDOW_SIZE = 80;
    private final static int ASPIRATIONWINDOW_TOLERANCE = 255;
    private final static int MAX_DEPTH = 64;
    private final static int MAX_DEPTH_MOVES = 200;
    private final static int[] PIECEVALUE = {100, 350, 350, 550, 1100, 10000};

    private final Board board;
    private long searchMilliseconds;
    private BoardEvaluator evaluator;
    private boolean searching;
    private int searchIteration;
    private long startSearchTimestamp;
    private long stopSearchTimestamp;
    private int searchHistory[][];
    private long searchMove;
    private long pv[][];
    private int pv_length[];
    private long moves[][];

    public BoardSearch(Board board) {
        this(board, 10000);
    }

    public BoardSearch(Board board, long searchMilliseconds) {
        this.board = board.clone();
        this.searchMilliseconds = searchMilliseconds;
        evaluator = BoardEvaluator.getDefault();
        searchHistory = new int[64][64];
        pv_length = new int[MAX_DEPTH];
        pv = new long[MAX_DEPTH][MAX_DEPTH];
        moves = new long [MAX_DEPTH][MAX_DEPTH_MOVES];
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

    public long getSearchMove() {
        return searchMove;
    }

    public void start() {
        if (!searching) {
            this.startSearchTimestamp = System.currentTimeMillis();
            this.startSearchTimestamp = System.currentTimeMillis();
            this.stopSearchTimestamp = this.startSearchTimestamp + searchMilliseconds;
            startSearchIterations();
            searching = true;
        }
    }

    public void stop () {
        this.stopSearchTimestamp = System.currentTimeMillis();
    }

    private boolean isTimeUp () {
        return System.currentTimeMillis() > this.stopSearchTimestamp;
    }

    private int evaluateBoard (Board board) {
        int score = evaluator.evaluate(board);
        if (board.getSideToMove() == Board.BLACK) {
            score = -score;
        }
        return score;
    }

    private void preparePrincipalVariation (int ply) {
        pv_length[ply] = ply;
    }

    private void updatePrincipalVariation (int ply, long move) {
        pv[ply][ply] = move;
        for (int j = ply + 1; j < pv_length[ply + 1]; j++)
            pv[ply][j] = pv[ply + 1][j];
        pv_length[ply] = pv_length[ply + 1];
    }

    private void ponderMoves (long[] moves) {

        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0) {
                break;
            }
            int score = 0;
            byte fromSquare = (byte)((moves[i] & Board.MOVE_FROM_SQUARE_MASK) >>> Board.MOVE_FROM_SQUARE_OFFSET);
            byte toSquare = (byte)((moves[i] & Board.MOVE_TO_SQUARE_MASK) >>> Board.MOVE_TO_SQUARE_OFFSET);
            byte capturedFigure = toSquare == board.getEpSquare()? Board.PAWN : Board.getPieceFigure((byte)((moves[i] & Board.MOVE_CAPTURED_PIECE_MASK) >>> Board.MOVE_CAPTURED_PIECE_OFFSET));
            if (capturedFigure != Board.EMPTY) {
                byte sourceFigure = board.getSquareFigure(fromSquare);
                int toValue = PIECEVALUE[capturedFigure];
                int fromValue = PIECEVALUE[sourceFigure];
                score += PIECEVALUE[capturedFigure] - PIECEVALUE[sourceFigure];
                if (toValue >= fromValue) {
                    score += 100;
                }
            }
            score += searchHistory[fromSquare][toSquare];
            moves[i] |= ((long)score << Board.MOVE_SCORE_OFFSET);
        }
    }

    private void ponderCaptureMoves (long[] moves) {

        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0) {
                break;
            }
            byte fromSquare = (byte)((moves[i] & Board.MOVE_FROM_SQUARE_MASK) >>> Board.MOVE_FROM_SQUARE_OFFSET);
            byte toSquare = (byte)((moves[i] & Board.MOVE_TO_SQUARE_MASK) >>> Board.MOVE_TO_SQUARE_OFFSET);
            byte movingFigure = board.getSquareFigure(fromSquare);
            int sourceValue = PIECEVALUE[movingFigure];
            int destinationValue = 0;

            if (movingFigure == Board.PAWN && toSquare == board.getEpSquare()) {
                destinationValue = PIECEVALUE[Board.PAWN];
            }
            else {
                byte capturedFigure = board.getSquareFigure(toSquare);
                destinationValue = PIECEVALUE[(capturedFigure == Board.EMPTY)? Board.QUEEN : capturedFigure];
            }
            if (sourceValue <= destinationValue) {
                moves[i] |= ((long)(destinationValue - sourceValue) << Board.MOVE_SCORE_OFFSET);
            }
            else {
                int swapOffValue = SEE(moves[i]);
                moves[i] |= ((long)(swapOffValue < 0? -INFINITY : swapOffValue) << Board.MOVE_SCORE_OFFSET);
            }
        }
    }

    private int SEE (long move) {

        byte f, t, sq, figure, side, xside;
        int n, lastval;
        int[] swaplist = new int[20];
        long b, c, r;
        long[] d, e;
        long[][] pieces = board.getPieces();
        long[] friends = board.getFriends();
        byte nsq, nfigure;
        int dir;
        long a;
        f = (byte)((move & Board.MOVE_FROM_SQUARE_MASK) >>> Board.MOVE_FROM_SQUARE_OFFSET);
        t = (byte)((move & Board.MOVE_TO_SQUARE_MASK) >>> Board.MOVE_TO_SQUARE_OFFSET);
        side = board.getSquareSide(f);
        xside = board.getOppositeSide(side);
        b = board.getSquareAttackers(t, side);
        c = board.getSquareAttackers(t, xside);
        b &= BoardUtils.squareBitX[f];

        if (BoardUtils.sliderX[board.getSquareFigure(f)] == 1) {
            dir = BoardUtils.directions[t][f];
            a = BoardUtils.ray[f][dir] & board.getBlocker();
            if (a != 0) {
                nsq = (byte)(t < f ? BoardUtils.getLeastSignificantBit(a) : BoardUtils.getMostSignificantBit(a));
                nfigure = board.getSquareFigure(nsq);
                if ((nfigure == Board.QUEEN) || (nfigure == Board.ROOK && dir > 3) || (nfigure == Board.BISHOP && dir < 4)) {
                    if ((BoardUtils.squareBit[nsq] & friends[side]) != 0)
                        b |= BoardUtils.squareBit[nsq];
                    else
                        c |= BoardUtils.squareBit[nsq];
                }
            }
        }

        d = pieces[side];
        e = pieces[xside];
        swaplist[0] = ((board.getSquareFigure(f) == Board.PAWN && t == board.getEpSquare())? PIECEVALUE[Board.PAWN] : PIECEVALUE[board.getSquareFigure(t)]);
        lastval = -PIECEVALUE[board.getSquareFigure(f)];
        n = 1;
        while (true) {
            if (c == 0) {
                break;
            }
            for (figure = Board.PAWN; figure <= Board.KING; figure++) {
                r = c & e[figure];
                if (r != 0) {
                    sq = (byte)BoardUtils.getLeastSignificantBit(r);
                    c &= BoardUtils.squareBitX[sq];
                    if (BoardUtils.sliderX[figure] == 1) {
                        dir = BoardUtils.directions[t][sq];
                        a = BoardUtils.ray[sq][dir] & board.getBlocker();
                        if (a != 0) {
                            nsq = (byte)(t < sq ? BoardUtils.getLeastSignificantBit(a) : BoardUtils.getMostSignificantBit(a));
                            nfigure = board.getSquareFigure(nsq);
                            if ((nfigure == Board.QUEEN) || (nfigure == Board.ROOK && dir > 3) || (nfigure == Board.BISHOP && dir < 4)) {
                                if ((BoardUtils.squareBit[nsq] & friends[xside]) != 0)
                                    c |= BoardUtils.squareBit[nsq];
                                else
                                    b |= BoardUtils.squareBit[nsq];
                            }
                        }
                    }
                    swaplist[n] = swaplist[n - 1] + lastval;
                    n++;
                    lastval = PIECEVALUE[figure];
                    break;
                }
            }

            if (b == 0) {
                break;
            }
            for (figure = Board.PAWN; figure <= Board.KING; figure++) {
                r = b & d[figure];
                if (r != 0) {
                    sq = (byte)BoardUtils.getLeastSignificantBit(r);
                    b &= BoardUtils.squareBitX[sq];
                    if (BoardUtils.sliderX[figure] == 1) {
                        dir = BoardUtils.directions[t][sq];
                        a = BoardUtils.ray[sq][dir] & board.getBlocker();
                        if (a != 0) {
                            nsq = (byte)(t < sq ? BoardUtils.getLeastSignificantBit(a) : BoardUtils.getMostSignificantBit(a));
                            nfigure = board.getSquareFigure(nsq);
                            if ((nfigure == Board.QUEEN) || (nfigure == Board.ROOK && dir > 3) || (nfigure == Board.BISHOP && dir < 4)) {
                                if ((BoardUtils.squareBit[nsq] & friends[side]) != 0)
                                    b |= BoardUtils.squareBit[nsq];
                                else
                                    c |= BoardUtils.squareBit[nsq];
                            }
                        }
                    }
                    swaplist[n] = swaplist[n - 1] + lastval;
                    n++;
                    lastval = -PIECEVALUE[figure];
                    break;
                }
            }
        }

        --n;
        while (n > 0) {
            if ((n & 1) != 0) {
                if (swaplist[n] <= swaplist[n - 1])
                    swaplist[n - 1] = swaplist[n];
            }
            else {
                if (swaplist[n] >= swaplist[n - 1])
                    swaplist[n - 1] = swaplist[n];
            }
            --n;
        }
        return (swaplist[0]);
    }

    private void sortMoves (long[] moves) {

        int n = moves.length;
        long temp = 0;
        int score1, score2;
        for(int i=0; i < n; i++) {
            if (moves[i] == 0) {
                break;
            }
            for(int j=1; j < (n-i); j++) {
                if (moves[j] == 0) {
                    break;
                }
                score1 = (int)((moves[j-1] & Board.MOVE_SCORE_MASK) >>> Board.MOVE_SCORE_OFFSET);
                score2 = (int)((moves[j] & Board.MOVE_SCORE_MASK) >>> Board.MOVE_SCORE_OFFSET);
                if(score1 < score2){
                    temp = moves[j-1];
                    moves[j-1] = moves[j];
                    moves[j] = temp;
                }
            }
        }
    }

    private void startSearchIterations () {

        int rootAlpha = Integer.MIN_VALUE;
        int rootBeta = Integer.MAX_VALUE;
        int rootSearchResult = evaluateBoard(board);

        searchIteration = 0;
        searchMove = 0;
        for (byte source = 0; source < 64; source++)
            for (byte destination = 0; destination < 64; destination++)
                searchHistory[source][destination] = 0;
        for (int depth1 = 0; depth1 < MAX_DEPTH; depth1++) {
            pv_length[depth1] = 0;
            for (int depth2 = 0; depth2 < MAX_DEPTH; depth2++)
                pv[depth1][depth2] = 0;
        }
        board.generateLegalMoves(moves[0]);

        do {
            searchIteration++;
            if (rootSearchResult > (MATE-ASPIRATIONWINDOW_TOLERANCE)) {
                rootAlpha = rootSearchResult - 1;
                rootBeta = MATE;
            }
            else if (rootSearchResult < (-MATE+ASPIRATIONWINDOW_TOLERANCE)) {
                rootAlpha = -MATE;
                rootBeta = rootSearchResult + 1;
            }
            else {
                rootAlpha = Math.max (rootSearchResult - ASPIRATIONWINDOW_SIZE, -MATE);
                rootBeta = Math.min (rootSearchResult + ASPIRATIONWINDOW_SIZE, MATE);
            }

            rootSearchResult = alphaBetaSearch (rootAlpha, rootBeta, searchIteration, 0);
            if (!isTimeUp()) {
                if (rootSearchResult >= rootBeta && rootSearchResult < MATE) {
                    rootAlpha = rootBeta;
                    rootBeta = Integer.MAX_VALUE;
                    rootSearchResult = alphaBetaSearch (rootAlpha, rootBeta, searchIteration, 0);
                }
                else if (rootSearchResult <= rootAlpha) {
                    rootBeta = rootAlpha;
                    rootAlpha = Integer.MIN_VALUE;
                    rootSearchResult = alphaBetaSearch (rootAlpha, rootBeta, searchIteration, 0);
                }
            }
        } while (!isTimeUp());
    }

    private int alphaBetaSearch (int alpha, int beta, int depth, int ply) {

        int searchResult;
        boolean foundPV = false;

        //Chequear que el lado a jugar tenga el Rey
        if (board.getKingSquare(board.getSideToMove()) == Board.INVALIDSQUARE) {
            return (-MATE + ply - 2);
        }

        //Verificar si ya hemos llegado al limite de busqueda
        if (depth == 0) {
            return quiescentSearch(alpha, beta, ply);
        }

        //Preparar la Linea de Variacion Principal
        preparePrincipalVariation (ply);

        //Generar los movimientos
        if (ply > 0) {
            board.generatePseudoLegalMoves(moves[ply]);
        }
        if (moves[ply][0] == 0) {
            return -MATE + ply - 2;
        }

        //Ponderación y Ordenamiento de movimientos
        if (ply > 0) {
            ponderMoves(moves[ply]);
        }
        sortMoves(moves[ply]);

        //Iterar sobre los movimientos posibles
        for (int i = 0; i < moves[ply].length; i++) {
            if (moves[ply][i] == 0) {
                break;
            }
            moves[ply][i] = board.makeMove(moves[ply][i]);
            if (foundPV) {
                searchResult = -alphaBetaSearch (-alpha - 1, -alpha, depth - 1, ply + 1);
                if ((searchResult > alpha) && (searchResult < beta))
                    searchResult = -alphaBetaSearch (-beta, -alpha, depth - 1, ply + 1);
            }
            else {
                searchResult = -alphaBetaSearch (-beta, -alpha, depth - 1, ply + 1);
            }
            moves[ply][i] |= ((long)searchResult << Board.MOVE_SCORE_OFFSET);
            board.unmakeMove(moves[ply][i]);

            if (searchIteration > 1 && isTimeUp()) {
                return -MATE;
            }

            if (searchResult > alpha) {
                foundPV = true;
                alpha = searchResult;
                updatePrincipalVariation (ply, moves[ply][i]);
                byte fromSquare = (byte)((moves[ply][i] & Board.MOVE_FROM_SQUARE_MASK) >>> Board.MOVE_FROM_SQUARE_OFFSET);
                byte toSquare = (byte)((moves[ply][i] & Board.MOVE_TO_SQUARE_MASK) >>> Board.MOVE_TO_SQUARE_OFFSET);
                searchHistory[fromSquare][toSquare] += ply*ply;
                if (ply == 0) {
                    searchMove = moves[ply][i];
                }
                if (alpha >= beta) {
                    break;
                }
            }
        }

        return alpha;
    }

    private int quiescentSearch (int alpha, int beta, int ply) {

        int quiescResult;
        byte sideToMove = board.getSideToMove();
        boolean foundPV = false;

        //Chequear que el lado a jugar tenga el Rey
        if (board.getKingSquare(sideToMove) == Board.INVALIDSQUARE) {
            return (-MATE + ply - 2);
        }

        //Funcion de evaluacion
        boolean inCheck = board.inCheck();
        quiescResult = evaluateBoard(board);
        if (quiescResult >= beta && !inCheck) {
            return quiescResult;
        }

        //Generacion de movimientos
        if (inCheck) {
            board.generateEscapeMoves(moves[ply]);
            if (moves[ply][0] == 0) {
                return (-MATE + ply - 2);
            }
            if (quiescResult >= beta) {
                return quiescResult;
            }
            ponderMoves(moves[ply]);
        }
        else {
            board.generateCaptureMoves(moves[ply]);
            if (moves[ply][0] == 0) {
                return quiescResult;
            }
            ponderCaptureMoves(moves[ply]);
        }
        sortMoves(moves[ply]);

        if (quiescResult > alpha) {
            alpha = quiescResult;
        }

        //Prepare principalVariation
        preparePrincipalVariation (ply);

        //Iterar sobre los movimientos posibles
        for (int i = 0; i < moves[ply].length; i++) {
            if (moves[ply][i] == 0) {
                break;
            }
            moves[ply][i] = board.makeMove(moves[ply][i]);
            if (foundPV) {
                quiescResult = -quiescentSearch (-alpha - 1, -alpha, ply + 1);
                if ((quiescResult > alpha) && (quiescResult < beta)) {
                    quiescResult = -quiescentSearch(-beta, -alpha, ply + 1);
                }
            }
            else {
                quiescResult = -quiescentSearch (-beta, -alpha, ply + 1);
            }
            board.unmakeMove(moves[ply][i]);

            if (quiescResult > alpha) {
                foundPV = true;
                alpha = quiescResult;
                updatePrincipalVariation (ply, moves[ply][i]);
                if (quiescResult >= beta) {
                    return quiescResult;
                }
            }
        }
        return alpha;
    }
}
