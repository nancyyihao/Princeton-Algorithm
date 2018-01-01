package part1_week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private List<LineSegment> lineSegments = new ArrayList<>();
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        validateParam(points);

        Point[] internalPoints = new Point[points.length];
        System.arraycopy(points, 0, internalPoints, 0, points.length);

        Arrays.sort(internalPoints);
        int length = internalPoints.length;
        for (int i=0 ; i < length - 3; i++)
            for (int j= i+1; j<length - 2; j++)
                for (int k=j+1; k<length - 1; k++)
                    for (int p=length-1; p >= k+1; p--) {
                        double k1 = internalPoints[i].slopeTo(internalPoints[j]);
                        double k2 = internalPoints[j].slopeTo(internalPoints[k]);
                        double k3 = internalPoints[k].slopeTo(internalPoints[p]);
                        if (Double.compare(k1, k2) == 0 && Double.compare(k2, k3) == 0) {
                            lineSegments.add(new LineSegment(internalPoints[i], internalPoints[p]));
                            break;
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println("numberOfSegments = "+collinear.numberOfSegments());
        StdDraw.show();
    }
}
