import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> seg;
    private int number;
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null || Arrays.asList(points).contains(null)) throw new IllegalArgumentException();
        checkDuplicate(points);
        seg = new ArrayList<>();
        Point[] copy = Arrays.copyOf(points, points.length);
        Arrays.sort(copy);
        for (int i = 0; i < copy.length; i++) {
            for (int j = i + 1; j < copy.length; j++) {
                for (int k = j + 1; k < copy.length; k++) {
                    if (copy[i].slopeOrder().compare(copy[j], copy[k]) == 0) {
                        for (int m = k + 1; m < copy.length; m++) {
                            if (copy[i].slopeOrder().compare(copy[k], copy[m]) == 0) {
                                seg.add(new LineSegment(copy[i], copy[m]));
                                number++;
                            }
                        }
                    }
                }
            }
        }
    }
    private void checkDuplicate(Point[] p) {
        for (int i = 0; i < p.length; i++) {
            for (int j = i + 1; j < p.length; j++) {
               if (p[i].compareTo(p[j]) == 0) throw new IllegalArgumentException();
            }
        }

    }
    // the number of line segments
    public int numberOfSegments() {
        return number;
    }
    // the line segments
    public LineSegment[] segments() {
        return seg.toArray(new LineSegment[number]);
    }
}
