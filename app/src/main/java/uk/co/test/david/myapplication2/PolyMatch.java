package uk.co.test.david.myapplication2;

import android.support.annotation.NonNull;

/**
 * Created by David on 13/05/2018.
 */

public class PolyMatch implements Comparable {
    public Polygon p1;
    public Polygon p2;
    public int start1;
    public int start2;
    public int length;

    public PolyMatch(Polygon p1, Polygon p2, int start1, int start2, int length){
        this.p1 = p1;
        this.p2 = p2;
        this. start1 = start1;
        this.start2 = start2;
        this.length = length;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return ((PolyMatch)o).length - this.length;
    }

    public Polygon getResult(){
        return Polygon.mergeMatch(this);
    }
}
