
package org.neochess.core;

public class CastleRights {

    private boolean castleKingSide;
    private boolean castleQueenSide;

    public CastleRights() {
        this.castleKingSide = false;
        this.castleQueenSide = false;
    }

    public CastleRights clone() {
        CastleRights clone = new CastleRights();
        clone.setCastleKingSide(castleKingSide);
        clone.setCastleQueenSide(castleQueenSide);
        return clone;
    }

    public void clear () {
        castleKingSide = false;
        castleQueenSide = false;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof CastleRights) {
            CastleRights castleRights = (CastleRights)object;
            if (castleKingSide != castleRights.castleKingSide) {
                return false;
            }
            if (castleQueenSide != castleRights.castleQueenSide) {
                return false;
            }
        }
        return super.equals(object);
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

    public boolean canCastle () {
        return castleKingSide || castleQueenSide;
    }
}
