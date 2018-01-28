package part1_week5;


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    /**
     *  reference : http://blog.csdn.net/liuweiran900217/article/details/20917495
     *  http://www.360doc.com/content/16/0513/10/110467_558718899.shtml
     *  https://www.cnblogs.com/notTao/p/6020918.html
     *
     *  TODO nearest 方法仍有些case没有通过
     */
    private static final RectHV CONTAINER = new RectHV(0, 0, 1, 1);
    private Node root;
    private int N;

    public KdTree() {
        root = null;
        N = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void insert(Point2D p) {
        validateParam(p);
        root = insert(p, root, false);
    }

    public boolean contains(Point2D p) {
        validateParam(p);
        return contains(p, root, false);
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(final RectHV rect) {
        validateParam(rect);
        final List<Point2D> result = new ArrayList<>();
        range(root, CONTAINER, rect, result);

        return result;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(final Point2D p) {
        validateParam(p);
        return nearest(root, CONTAINER, p.x(), p.y(), null);
    }

    // draw all of the points to standard draw
    public void draw() {
        StdDraw.setScale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        CONTAINER.draw();
        draw(root, CONTAINER);
    }

    private Node insert(Point2D p, Node node, boolean horizontal) {
        if (node == null) {
            Node n = new Node();
            n.horizontal = horizontal;
            n.right = null;
            n.left = null;
            n.x = p.x();
            n.y = p.y();
            N++;
            return n;
        }
        if (!horizontal && p.x() < node.x
                || horizontal && p.y() < node.y) {
            node.left = insert(p, node.left, !horizontal);
        } else {
            node.right = insert(p, node.right, !horizontal);
        }
        return node;
    }

    private boolean contains(Point2D p, Node node, boolean horizontal) {
        if (node == null) {
            return false;
        }
        if (node.x == p.x() && node.y == p.y()) {
            return true;
        }
        if (!horizontal && p.x() < node.x
                || horizontal && p.y() < node.y) {
            return contains(p, node.left, !horizontal);
        } else {
            return contains(p, node.right, !horizontal);
        }
    }

    private void draw(final Node node, final RectHV rect) {
        if (node == null) {
            return;
        }

        // draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        new Point2D(node.x, node.y).draw();

        // get the min and max points of division line
        Point2D min, max;
        if (node.horizontal) {
            StdDraw.setPenColor(StdDraw.BLUE);
            min = new Point2D(rect.xmin(), node.y);
            max = new Point2D(rect.xmax(), node.y);
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            min = new Point2D(node.x, rect.ymin());
            max = new Point2D(node.x, rect.ymax());
        }

        // draw that division line
        StdDraw.setPenRadius();
        min.drawTo(max);

        // recursively draw children
        draw(node.left, leftRect(rect, node));
        draw(node.right, rightRect(rect, node));
    }

    private RectHV leftRect(final RectHV rect, final Node node) {
        return new RectHV(
                rect.xmin(),
                rect.ymin(),
                node.horizontal ? rect.xmax() : node.x,
                node.horizontal ? node.y : rect.ymax()
        );
    }

    private RectHV rightRect(final RectHV rect, final Node node) {
        return new RectHV(
                node.horizontal ? rect.xmin() : node.x,
                node.horizontal ? node.y : rect.ymin(),
                rect.xmax(),
                rect.ymax()
        );
    }

    private void range(final Node node, final RectHV nrect, final RectHV rect,
                       final List<Point2D> rangeSet) {
        if (node == null)
            return;

        if (rect.intersects(nrect)) {
            final Point2D p = new Point2D(node.x, node.y);
            if (rect.contains(p)) rangeSet.add(p);
            range(node.left, leftRect(nrect, node), rect, rangeSet);
            range(node.right, rightRect(nrect, node), rect, rangeSet);
        }
    }

    private Point2D nearest(final Node node, final RectHV rect,
                            final double x, final double y,
                            final Point2D candidate) {
        if (node == null){
            return candidate;
        }

        double dqn = 0.0;
        double drq = 0.0;
        RectHV left = null;
        RectHV right = null;
        final Point2D query = new Point2D(x, y);
        Point2D nearest = candidate;

        if (nearest != null) {
            dqn = query.distanceSquaredTo(nearest);
            drq = rect.distanceSquaredTo(query);
        }

        if (nearest == null || dqn > drq) {
            final Point2D point = new Point2D(node.x, node.y);
            if (nearest == null || dqn > query.distanceSquaredTo(point))
                nearest = point;

            if (!node.horizontal) {
                left = new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());
                right = new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());

                if (x < node.x) {
                    nearest = nearest(node.left, left, x, y, nearest);
                    nearest = nearest(node.right, right, x, y, nearest);
                } else {
                    nearest = nearest(node.right, right, x, y, nearest);
                    nearest = nearest(node.left, left, x, y, nearest);
                }
            } else {
                left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);
                right = new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());

                if (y < node.y) {
                    nearest = nearest(node.left, left, x, y, nearest);
                    nearest = nearest(node.right, right, x, y, nearest);
                } else {
                    nearest = nearest(node.right, right, x, y, nearest);
                    nearest = nearest(node.left, left, x, y, nearest);
                }
            }
        }

        return nearest;
    }

    private static class Node {
        Node left;
        Node right;
        double x;
        double y;
        boolean horizontal; // true if horizontal
    }

    private void validateParam(Object o) {
        if (o == null)
            throw new IllegalArgumentException("input data can't be null!");
    }

    public static void main(String[] args) {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.show();
                }
            }
            StdDraw.pause(20);
        }
    }
}
