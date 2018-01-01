package part1_week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private List<LineSegment> lineSegments = new ArrayList<>();
    private List<Point> maxPoints = new ArrayList<>();
    private List<Point> minPoints = new ArrayList<>();
    private LineSegment[] segments;


    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validateParam(points);
        Point[] myPoints = new Point[points.length];
        System.arraycopy(points, 0, myPoints, 0, points.length);

        int length = myPoints.length;
        for (int i=0 ; i < length ; i++) {
            Arrays.sort(myPoints, myPoints[i].slopeOrder());

            for (int j=1; j < length; j++) {
                double k1 = myPoints[0].slopeTo(myPoints[j]);
                Point maxPoint = myPoints[0];
                Point minPoint = myPoints[0];
                if (myPoints[j].compareTo(maxPoint) > 0) maxPoint = myPoints[j];
                if (myPoints[j].compareTo(minPoint) < 0) minPoint = myPoints[j];

                int pointCount = 2;
                while (j+1<length  && k1 == myPoints[0].slopeTo(myPoints[j+1])) {
                    j = j + 1;
                    if (myPoints[j].compareTo(maxPoint) > 0) maxPoint = myPoints[j];
                    if (myPoints[j].compareTo(minPoint) < 0) minPoint = myPoints[j];
                    k1 = myPoints[0].slopeTo(myPoints[j]);
                    pointCount++;
                }
                if (pointCount > 3) {
                    // deleted duplicate item
                    if (!hasDuplicateItem(maxPoint, minPoint)) {
                        maxPoints.add(maxPoint);
                        minPoints.add(minPoint);
                        lineSegments.add(new LineSegment(maxPoint, minPoint));
                    }
                }
            }
        }
    }

    private boolean hasDuplicateItem(Point maxPoint, Point minPoint) {
        for(int i = 0; i < maxPoints.size(); i++)
            if(maxPoint.compareTo(maxPoints.get(i)) == 0 && minPoint.compareTo(minPoints.get(i)) == 0)
                return true;
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        //if (segments == null) {
            LineSegment[] s = new LineSegment[lineSegments.size()];
            segments = lineSegments.toArray(s);
        //}
        return segments;
    }

    private void validateParam(Point[] points) {
        if(points == null) throw new IllegalArgumentException("Data must be not null!");
        int length = points.length;
        for(int i = 0; i < length; i++)
            if(points[i] == null) throw new IllegalArgumentException("Data must be not null!");
        for (int i = 0; i < length - 1; i++)
            for (int j = i + 1; j < length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Data invalid, please check your input!");
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println("numberOfSegments = "+collinear.numberOfSegments());
        StdDraw.show();
    }
}
