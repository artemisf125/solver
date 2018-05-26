package uk.co.test.david.myapplication2;

import android.support.annotation.NonNull;

/**
 * Created by David on 12/05/2018.
 */

public class HoughPoint implements Comparable {
    public int r;
    public int t;
    public int value;
    public  HoughPoint(int r, int t, int value){
        this.r = r;
        this.t = t;
        this.value = value;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.value - ((HoughPoint)o).value;
    }
}
