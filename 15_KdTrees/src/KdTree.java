import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;
    private final RectHV rootRect = new RectHV(0, 0, 1, 1);

    private static class Node {
        private final Point2D p;      // the point
        // private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private final boolean isVertical;
        private Node(Point2D p, boolean isVertical) {
            this.p = p;
            this.isVertical = isVertical;
        }
    }
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }
    // number of points in the set
    public int size() {
        return size;
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, true);
    }
    private Node insert(Node n, Point2D p, boolean isVertical) {
        if (n == null) {
            size++;
            return new Node(p, isVertical);
        }
        if (p.x() == n.p.x() && p.y() == n.p.y()) return n;
        if (n.isVertical && p.x() < n.p.x() || !n.isVertical && p.y() < n.p.y())
            n.lb = insert(n.lb, p, !n.isVertical);
        else n.rt = insert(n.rt, p, !n.isVertical);
        return n;
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p);
    }
    private boolean get(Node n, Point2D p) {
        if (n == null) return false;
        if (p.x() == n.p.x() && p.y() == n.p.y()) return true;
        if (n.isVertical && p.x() < n.p.x() || !n.isVertical && p.y() < n.p.y()) return get(n.lb, p);
        else return get(n.rt, p);
    }
    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        RectHV rect = new RectHV(0, 0, 1, 1);
        rect.draw();

        draw(root, rect);
    }
    private void draw(Node n, RectHV rect) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();

        Point2D start, end;
        if (n.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            start = new Point2D(n.p.x(), rect.ymin());
            end = new Point2D(n.p.x(), rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            start = new Point2D(rect.xmin(), n.p.y());
            end = new Point2D(rect.xmax(), n.p.y());
        }
        StdDraw.setPenRadius();
        start.drawTo(end);

        draw(n.lb, leftRect(n, rect));
        draw(n.rt, rightRect(n, rect));
    }
    private RectHV leftRect(Node n, RectHV rect) {
        if (n.isVertical) return new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax());
        else return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y());
    }
    private RectHV rightRect(Node n, RectHV rect) {
        if (n.isVertical) return new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
        else return new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax());
    }
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> list = new ArrayList<>();
        range(root, rect, rootRect, list);
        return list;
    }
    private void range(Node n, RectHV rect, RectHV nrect, ArrayList<Point2D> list) {
        if (n == null) return;
        if (rect.intersects(nrect)) {
            if (rect.contains(n.p)) list.add(n.p);
            range(n.lb, rect, leftRect(n, nrect), list);
            range(n.rt, rect, rightRect(n, nrect), list);
        }
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return nearest(root, p, rootRect, null);
    }
    private Point2D nearest(Node n, Point2D p, RectHV rect, Point2D p2) {
        if (n == null) return p2;
        Point2D near = p2;
        double rectDis = 0, nearDis = 0;
        RectHV lbRect, rtRect;

        if (near != null) {
            rectDis = rect.distanceSquaredTo(p);
            nearDis = near.distanceSquaredTo(p);
        }
        if (near == null || rectDis < nearDis) {
            if (near == null || n.p.distanceSquaredTo(p) < nearDis) near = n.p;
            if (n.isVertical) {
                lbRect = new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax());
                rtRect = new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                if (p.x() < n.p.x()) {
                    near = nearest(n.lb, p, lbRect, near);
                    near = nearest(n.rt, p, rtRect, near);
                } else {
                    near = nearest(n.rt, p, rtRect, near);
                    near = nearest(n.lb, p, lbRect, near);
                }
            }
            else {
                lbRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y());
                rtRect = new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax());
                if (p.y() < n.p.y()) {
                    near = nearest(n.lb, p, lbRect, near);
                    near = nearest(n.rt, p, rtRect, near);
                } else {
                    near = nearest(n.rt, p, rtRect, near);
                    near = nearest(n.lb, p, lbRect, near);
                }
            }
        }
        return near;
    }
    // unit testing of the methods (optional)
//    public static void main(String[] args) {
//    }
}
