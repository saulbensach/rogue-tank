package com.bensach.saul.map.generator;

/**
 * Created by saul- on 18/04/2016.
 */
public class Edge implements Comparable<Edge> {

    private Point p1, p2;
    int src, dest, weight;

    public Edge(Point p1, Point p2) {
        this.p2 = p2;
        this.p1 = p1;
        weight = (int) Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()) , 2));
    }

    public Edge(){

    }

    public int getWeight() {
        return weight;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    @Override
    public int compareTo(Edge o) {
        return weight - o.weight;
    }
}
