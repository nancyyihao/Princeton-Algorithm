package part1_week5;


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class PointSET {

    private SET<Point2D> points = new SET<>();

    public PointSET() {

    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        validateParam(p);
        points.add(p);
    }

    public boolean contains(Point2D p) {
        validateParam(p);
        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        validateParam(rect);
        List<Point2D> result = new ArrayList<>();
        if (!points.isEmpty()) {
            for (Point2D p : points) {
                if (rect.contains(p)) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        validateParam(p);
        Point2D result = null;
        if (!points.isEmpty()) {
            double minDistance = Double.MAX_VALUE;
            for (Point2D that : points) {
                double d = p.distanceTo(that);
                if (d > 0.0f && d < minDistance) {
                    minDistance = d;
                    result = that;
                }
            }
        }
        return result;
    }

    private void validateParam(Object o) {
        if (o == null)
            throw new IllegalArgumentException("input data can't be null!");
    }

    public static void main(String[] args) {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        PointSET pointSET = new PointSET();
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    pointSET.insert(p);
                    StdDraw.clear();
                    pointSET.draw();
                    StdDraw.show();
                }
            }
            StdDraw.pause(20);
        }
    }
}
