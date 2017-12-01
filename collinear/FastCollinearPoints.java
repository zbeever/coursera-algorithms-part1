import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

 public class FastCollinearPoints {
    final private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) { throw new IllegalArgumentException(); }
        
        int n = points.length;
        
        Point[] aux = points.clone();
        
        for (int p = 0; p < n; p++) {
            // Sort the array. First, by order. Then, by slope order
            Arrays.sort(aux);
            Arrays.sort(aux, points[p].slopeOrder());

            // Setup two 'pointers', one each for our starting and ending positions
            // These are independent of p to allow for the checking of duplicates
            int min = 0;
            while (min < aux.length && points[p].slopeTo(aux[min]) == Double.NEGATIVE_INFINITY) { min++; } // Check against duplicates
            if (min != 1) { throw new IllegalArgumentException(); }
            int max = min; // Start our two pointers in the same place
            
            // While we still have indices to check...
            while (min < aux.length) {
                // If our p -> min slope is the same as our p -> max slope, increase our max pointer
                while (max < aux.length && points[p].slopeTo(aux[max]) == points[p].slopeTo(aux[min])) { max++; }
                // If we have four or more points between our pointers...
                if (max - min >= 3) {
                    // If our minimum is less than p, we know that we've already added this segment
                    Point pMin = aux[min].compareTo(points[p]) < 0 ? aux[min] : points[p];
                    Point pMax = aux[max - 1];
                    // If we have indeed found an original line segment, add it to the list
                    if (points[p] == pMin)
                        segments.add(new LineSegment(pMin, pMax));
                }
                // Increase our min to our previous max
                min = max;
            }
        }
    }
    
    public int numberOfSegments() {
        return this.segments.size();
    }
    
    public LineSegment[] segments() {
        LineSegment[] output = new LineSegment[numberOfSegments()];
        return this.segments.toArray(output);
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