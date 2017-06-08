
package org.neochess.core.evaluators;

import org.neochess.core.Board;

public class DefaultEvaluator extends Evaluator {

    private static final int PHASES_COUNT = 8;

    private static final int SCORE_PAWN = 100;
    private static final int SCORE_KNIGHT = 340;
    private static final int SCORE_BISHOP = 330;
    private static final int SCORE_ROOK = 520;
    private static final int SCORE_QUEEN = 1020;
    private static final int SCORE_KING = 10000;

    private static final int SCORE_ORIGINAL_MATERIAL = (16*SCORE_PAWN) + (4*SCORE_KNIGHT) + (4*SCORE_BISHOP) + (4*SCORE_ROOK) + (2*SCORE_QUEEN);

    private static final int SCORE_SIDE_TO_MOVE = 15;
    private static final int SCORE_MINOR_NOT_DEVELOPED = -15;

    private static final int[][] SCORE_PAWN_POSITION = {{
        0,   0,   0,   0,   0,   0,   0,   0,
        0,   0,   0, -30, -30,   0,   0,   0,
        1,   2,   3, -10, -10,   3,   2,   1,
        2,   4,   6,   8,   8,   6,   4,   2,
        3,   6,   9,  12,  12,   9,   6,   3,
        4,   8,  12,  16,  16,  12,   8,   4,
        5,  10,  15,  20,  20,  15,  10,   5,
        0,   0,   0,   0,   0,   0,   0,   0
    }, {
        0,   0,   0,   0,   0,   0,   0,   0,
        5,  10,  15,  20,  20,  15,  10,   5,
        4,   8,  12,  16,  16,  12,   8,   4,
        3,   6,   9,  12,  12,   9,   6,   3,
        2,   4,   6,   8,   8,   6,   4,   2,
        1,   2,   3, -10, -10,   3,   2,   1,
        0,   0,   0, -30, -30,   0,   0,   0,
        0,   0,   0,   0,   0,   0,   0,   0
    }};

    private static final int[] SCORE_KNIGHT_POSITION = {
        -10, -10, -10, -10, -10, -10, -10, -10,
        -10,   0,   0,   0,   0,   0,   0, -10,
        -10,   0,   5,   5,   5,   5,   0, -10,
        -10,   0,   5,  10,  10,   5,   0, -10,
        -10,   0,   5,  10,  10,   5,   0, -10,
        -10,   0,   5,   5,   5,   5,   0, -10,
        -10,   0,   0,   0,   0,   0,   0, -10,
        -10, -10, -10, -10, -10, -10, -10, -10
    };

    private static final int[] SCORE_BISHOP_POSITION = {
          0,  -5, -10, -10, -10, -10,  -5,   0,
         -5,   2,   0,   0,   0,   0,   2,  -5,
        -10,   0,   6,   5,   5,   6,   0, -10,
        -10,   0,   5,  10,  10,   5,   0, -10,
        -10,   0,   5,  10,  10,   5,   0, -10,
        -10,   0,   6,   5,   5,   6,   0, -10,
         -5,   2,   0,   0,   0,   0,   2,  -5,
          0,  -5, -10, -10, -10, -10,  -5,   0
    };

    private static final int[] BASE_RANK = { Board.RANK_1, Board.RANK_8 };

    @Override
    public int evaluate(Board board) {

        int materialScore = 0;
        int phase = 0;
        int kingSquare[] = new int[2];
        int pawnsCountByFile[][] = new int[2][8];
        for (int square = Board.A1; square <= Board.H8; square++) {
            int squareFile = Board.getSquareFile(square);
            int squareRank = Board.getSquareRank(square);
            int piece = board.getPiece(square);
            int pieceSide = Board.getPieceSide(piece);
            int pieceFigure = Board.getPieceFigure(piece);
            switch (pieceFigure) {
                case Board.PAWN:
                    materialScore += SCORE_PAWN;
                    pawnsCountByFile[pieceSide][squareFile]++;
                    break;
                case Board.KNIGHT:
                    materialScore += SCORE_KNIGHT;
                    break;
                case Board.BISHOP:
                    materialScore += SCORE_BISHOP;
                    break;
                case Board.ROOK:
                    materialScore += SCORE_ROOK;
                    break;
                case Board.QUEEN:
                    materialScore += SCORE_QUEEN;
                    break;
                case Board.KING:
                    kingSquare[pieceSide] = square;
                    break;
            }
        }
        phase = PHASES_COUNT - (materialScore * PHASES_COUNT / SCORE_ORIGINAL_MATERIAL);

        int score[] = new int[2];
        for (int square = Board.A1; square <= Board.H8; square++) {
            int squareRank = Board.getSquareRank(square);
            int piece = board.getPiece(square);
            int pieceSide = Board.getPieceSide(piece);
            int pieceFigure = Board.getPieceFigure(piece);
            switch (pieceFigure) {
                case Board.PAWN:
                    score[pieceSide] += SCORE_PAWN;
                    score[pieceSide] += SCORE_PAWN_POSITION[pieceSide][square];
                    break;
                case Board.KNIGHT:
                    score[pieceSide] += SCORE_KNIGHT;
                    score[pieceSide] += SCORE_KNIGHT_POSITION[square];
                    if (phase <= 2) {
                        if (squareRank == BASE_RANK[pieceSide]) {
                            score[pieceSide] += SCORE_MINOR_NOT_DEVELOPED;
                        }
                    }
                    break;
                case Board.BISHOP:
                    score[pieceSide] += SCORE_BISHOP;
                    score[pieceSide] += SCORE_BISHOP_POSITION[square];
                    if (phase <= 2) {
                        if (squareRank == BASE_RANK[pieceSide]) {
                            score[pieceSide] += SCORE_MINOR_NOT_DEVELOPED;
                        }
                    }
                    break;
                case Board.ROOK:
                    score[pieceSide] += SCORE_ROOK;
                    break;
                case Board.QUEEN:
                    score[pieceSide] += SCORE_QUEEN;
                    break;
                case Board.KING:
                    score[pieceSide] += SCORE_KING;
                    break;
            }
        }
        score[board.getSideToMove()] += SCORE_SIDE_TO_MOVE;
        return score[Board.WHITE] - score[Board.BLACK];
    }
}
