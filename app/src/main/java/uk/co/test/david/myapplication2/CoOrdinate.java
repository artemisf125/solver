package uk.co.test.david.myapplication2;

/**
 * Created by David on 16/04/2018.
 */

public class CoOrdinate {
    public int x;
    public int y;
    public  CoOrdinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public CoOrdinate(){
        this.x = 0;
        this.y = 0;
    }

    public boolean closeTo(CoOrdinate p) {
        return (Math.pow(x-p.x,2)+Math.pow(y-p.y,2) <= 10);
    }
}
