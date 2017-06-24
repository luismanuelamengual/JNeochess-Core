
package org.neochess.core;

public class CastleRights {

    private boolean castleKingSide;
    private boolean castleQueenSide;

    public CastleRights() {
        this.castleKingSide = false;
        this.castleQueenSide = false;
    }

    public void clear () {
        castleKingSide = false;
        castleQueenSide = false;
    }

    public boolean canCastleKingSide() {
        return castleKingSide;
    }

    public void setCastleKingSide(boolean castleKingSide) {
        this.castleKingSide = castleKingSide;
    }

    public boolean canCastleQueenSide() {
        return castleQueenSide;
    }

    public void setCastleQueenSide(boolean castleQueenSide) {
        this.castleQueenSide = castleQueenSide;
    }
}
