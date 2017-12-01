import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.In;
import java.util.Stack;

public class KdTree {
    private Node root;
    private int size;
    
    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }
    
    private static class Node {
        final private Point2D p;
        final private RectHV rect;
        private Node lb;
        private Node rt;
            
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.lb = null;
            this.rt = null;
        }
    }
    
    // is the set empty?
    public boolean isEmpty() {
        return (size() == 0);
    }
    
    // number of points in the set
    public int size() {
        return this.size;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        RectHV rect = new RectHV(0, 0, 1, 1);
        this.root = put(this.root, p, true, rect);
        this.size++;
    }
    
    
    private boolean smaller(Point2D p1, Point2D p2, boolean compareX) {
        if (compareX) { return (p1.x() < p2.x()); }
        else { return (p1.y() < p2.y()); }
    }
    
    private Node put(Node x, Point2D p, boolean compareX, RectHV rect) {
        if (x == null) { return new Node(p, rect); }
        
        boolean smaller = smaller(p, x.p, compareX);
        
        if (smaller) {
            x.lb = put(x.lb, p, !compareX, rect(x, compareX, smaller));
        } else {
            if (x.p.equals(p)) { return x; }
            x.rt = put(x.rt, p, !compareX, rect(x, compareX, smaller));
        }
        
        return x;
    }
    
    private RectHV rect(Node x, boolean compareX, boolean smaller) {
        if (smaller) {
            if (compareX) {
                return new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            } else {
                return new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
            }
        } else {
            if (compareX) {
                return new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
            } else {
                return new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
            }
        }
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) { throw new IllegalArgumentException("contains(): null argument"); }
        return (get(this.root, p, true) != null);
    }
    
    private Point2D get(Node x, Point2D p, boolean compareX) {
        if (x == null) { return null; }
        if (x.p.equals(p)) { return x.p; }
        
        boolean smaller = smaller(p, x.p, compareX);
        
        if (smaller) { return get(x.lb, p, !compareX); }
        else { return get(x.rt, p, !compareX); }
    }
    
    // draw all points to standard draw
    public void draw() {
        draw(this.root, true);
    }
    
    private void draw(Node x, boolean compareX) {
        if (x == null) { return; }
        
        StdDraw.setPenColor(StdDraw.BLACK);
        x.p.draw();
        
        StdDraw.setPenRadius();
        if (compareX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        
        draw(x.lb, !compareX);
        draw(x.rt, !compareX);
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) { throw new IllegalArgumentException("range(): null argument"); }
        
        Stack<Point2D> points = new Stack<Point2D>();
        if (!isEmpty()) { range(this.root, rect, points); }
        return points;
    }
    
    private void range(Node x, RectHV rect, Stack<Point2D> stack) {
        if (!rect.intersects(x.rect)) { return; }
        
        if (rect.contains(x.p)) { stack.push(x.p); }
            
        if (x.lb != null) { range(x.lb, rect, stack); }
        if (x.rt != null) { range(x.rt, rect, stack); }
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) { throw new IllegalArgumentException("nearest(): null argument"); }
        
        Point2D nearest = nearest(this.root, p, this.root.p);
        return nearest;
    }
    
    private Point2D nearest(Node x, Point2D p, Point2D nearest) {
        if (x == null) { return nearest; }
        if (nearest.distanceSquaredTo(p) < x.rect.distanceSquaredTo(p)) { return nearest; }
        
        if (x.p.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) { nearest = x.p; }
        
        if (x.lb != null && x.rt != null) {
            if (x.lb.p.distanceSquaredTo(nearest) < x.rt.p.distanceSquaredTo(nearest)) {
                nearest = nearest(x.lb, p, nearest);
                nearest = nearest(x.rt, p, nearest);
            } else {
                nearest = nearest(x.rt, p, nearest);
                nearest = nearest(x.lb, p, nearest);
            }
        } else if (x.lb != null && x.rt == null) {
            nearest = nearest(x.lb, p, nearest);
        } else {
            nearest = nearest(x.rt, p, nearest);
        }
        
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In in = new In(args[0]);
        KdTree test = new KdTree();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            test.insert(new Point2D(x, y));
        }
        
        test.draw();
    }
}