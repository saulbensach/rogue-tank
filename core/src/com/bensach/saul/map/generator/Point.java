package com.bensach.saul.map.generator;

/**
 * Created by saul- on 18/04/2016.
 */
public class Point {

    private float x, y;
    private int value;
    private Point parent;

    public Point(float x, float y, int value){
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean inside(Point a, Point b, Point c){
        if (ccw(a, b, c) > 0) return (in(a, b, c) > 0);
        else if (ccw(a, b, c) < 0) return (in(a, b, c) < 0);
        return true;
    }

    private int ccw(Point a, Point b, Point c){
        float area = area(a,b,c);
        if(area > 0)return + 1;
        else if(area < 0) return - 1;
        else return 0;
    }

    private float in(Point a, Point b, Point c){
        Point d = this;
        double adx = a.x - d.x;
        double ady = a.y - d.y;
        double bdx = b.x - d.x;
        double bdy = b.y - d.y;
        double cdx = c.x - d.x;
        double cdy = c.y - d.y;

        double abdet = adx * bdy - bdx * ady;
        double bcdet = bdx * cdy - cdx * bdy;
        double cadet = cdx * ady - adx * cdy;
        double alift = adx * adx + ady * ady;
        double blift = bdx * bdx + bdy * bdy;
        double clift = cdx * cdx + cdy * cdy;

        return (float) (alift * bcdet + blift * cadet + clift * abdet);
    }

    private float area(Point a, Point b, Point c){
        return 0.5f * (a.x * b.y - a.y * b.x + a.y * c.x - a.x * c.y + b.x * c.y - c.x * b.y);
    }
}
