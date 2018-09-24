import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;


public class FastCollinearPoints {
    private final ArrayList<LineSegment> seg;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null || Arrays.asList(points).contains(null)) throw new IllegalArgumentException();

        seg = new ArrayList<>();

        Point[] copy = Arrays.copyOf(points, points.length);
        Arrays.sort(copy);

        for (int m = 1; m < copy.length; m++) {
            if (copy[m] == copy[m - 1]) throw new IllegalArgumentException();
        }

        for (int i = 0; i < copy.length - 1; i++) {
            Point origin = copy[i];
            Point[] pointsAfter = new Point[copy.length - 1 - i];

            double[] slopeBefore = new double[i];

            for (int j = 0; j < copy.length - 1 - i; j++) pointsAfter[j] = copy[j + i + 1];
            for (int k = 0; k < i; k++) slopeBefore[k] = origin.slopeTo(copy[k]);

            Arrays.sort(pointsAfter, origin.slopeOrder());

            double slope = Double.NEGATIVE_INFINITY;
            double prevSlope = Double.NEGATIVE_INFINITY;
            int count = 0;
            for (int n = 0; n < pointsAfter.length; n++) {
                slope = origin.slopeTo(pointsAfter[n]);

                if (slope != prevSlope) {
                    if (count >= 3 && !Arrays.asList(slopeBefore).contains(slope)) {
                        seg.add(new LineSegment(origin, pointsAfter[n - 1]));
                    }
                    count = 1;
                } else count++;
                prevSlope = slope;
            }
            if (count >= 3 && !Arrays.asList(slopeBefore).contains(slope))
                seg.add(new LineSegment(origin, pointsAfter[pointsAfter.length - 1]));
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return seg.size();
    }
    // the line segments
    public LineSegment[] segments() {
        return seg.toArray(new LineSegment[seg.size()]);
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
        StdDraw.show();
    }
}
