import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    // As we're unsure of the exact number of line segments we'll have, a resizable data structure works best here
    final private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
    
    public BruteCollinearPoints(Point[] points) {
        if (points == null) { throw new IllegalArgumentException(); }
        
        // We declare a variable to hold our array's length for convenience, then sort the array in ascending order
        // The sort is ABSOLUTELY NECESSARY to make sure we can correctly use the starting / ending indices in the for loops below
        int n = points.length;
        
        Arrays.sort(points);
        
        Point prevStart = points[0];
        Point prevEnd = points[n - 1];
        double prevSlope = prevStart.slopeTo(prevEnd);
        boolean first = true;
        
        for (int p = 0; p < n - 3; p++) {
            if (points[p] == null) { throw new IllegalArgumentException(); }
            
            for (int q = p + 1; q < n - 2; q++) {
                double slopePQ = points[p].slopeTo(points[q]);
                if (slopePQ == Double.NEGATIVE_INFINITY) { throw new IllegalArgumentException(); }
                
                for (int r = q + 1; r < n - 1; r++) {
                    double slopeQR = points[q].slopeTo(points[r]);
                    // If we can already tell our line isn't a line, we can skip this iteration
                    if (slopeQR != slopePQ) { continue; }
                    
                    for (int s = r + 1; s < n; s++) {
                        double slopeRS = points[r].slopeTo(points[s]);
                        if (slopeRS != slopeQR) { continue; }
                        
                        if (s < n - 1) {
                            if (slopeRS == points[r].slopeTo(points[s + 1])) {
                                q++;
                                r++;
                                continue;
                            }
                        }
                        
                        if (!first && points[p].compareTo(prevStart) == 0 && points[s].compareTo(prevEnd) == 0) { continue; }
                        if (points[p].compareTo(prevStart) == 0 && points[p].slopeTo(points[s]) == prevSlope) { continue; }
                        if (points[s].compareTo(prevEnd) == 0 && points[p].slopeTo(points[s]) == prevSlope) { continue; }
                        
                        this.segments.add(new LineSegment(points[p], points[s]));
                        prevStart = points[p];
                        prevEnd = points[s];
                        prevSlope = prevStart.slopeTo(prevEnd);
                     
                        if (first) { first = false; }
                    }
                }
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
    
    public static void main(String args[]) {
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
        StdDraw.show();
    }
}