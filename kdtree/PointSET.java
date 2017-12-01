import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.In;
import java.util.Stack;

public class PointSET {
    final private SET<Point2D> points;
    
    // construct an empty set of points
    public PointSET() {
        this.points = new SET<Point2D>();
    }
    
    // is the set empty?
    public boolean isEmpty() {
        return this.points.isEmpty();
    }
    
    // number of points in the set
    public int size() {
        return this.points.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) { throw new IllegalArgumentException("insert(): null argument"); }
        this.points.add(p);
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) { throw new IllegalArgumentException("contains(): null argument"); }
        return this.points.contains(p);
    }
    
    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (Point2D point : this.points) {
            point.draw();
        }
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) { throw new IllegalArgumentException("range(): null argument"); }
        Stack<Point2D> intersections = new Stack<Point2D>();
        for (Point2D point : this.points) {
            if (rect.contains(point)) {
                intersections.push(point);
            }
        }
        return intersections;
    }
        
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) { throw new IllegalArgumentException("nearest(): null argument"); }
        if (isEmpty()) { return null; }
        
        Point2D nearest = this.points.min();
        boolean firstPass = true;
        
        for (Point2D point : this.points) {
            if (p.distanceSquaredTo(point) < p.distanceSquaredTo(nearest)) {
                nearest = point;
            }
        }
        
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In in = new In(args[0]);
        PointSET test = new PointSET();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            test.insert(new Point2D(x, y));
        }
        
        test.draw();
    }
}