import java.util.TreeSet;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Font;

public class PointSET {
    private final ArrayList<TreeSet<Point2D>> grid;
    private final TreeSet<Point2D> allPoints;
    private static final int DIVISION = 5;
    private static final int TOTAL_SQUARES = DIVISION * DIVISION;
    private static final double PARTITION = 0.2;

    // construct an empty set of points 
    public PointSET() {
	allPoints = new TreeSet<Point2D>();
	grid = new ArrayList<TreeSet<Point2D>>();
	for (int j = 0; j < TOTAL_SQUARES; j++) {
	    grid.add(j, new TreeSet<Point2D>());
	}
    }

    // is the set empty? 
    public boolean isEmpty() {
	return allPoints.isEmpty();
    }

    // number of points in the set 
    public int size() {
	return allPoints.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
	if (p == null) throw new IllegalArgumentException();
	grid.get(calculateSquare(p)).add(p);
	allPoints.add(p);
    }

    // does the set contain point p? 
    public boolean contains(Point2D p) {
	if (p == null) throw new IllegalArgumentException();
	return allPoints.contains(p);
    }

    // draw all points to standard draw 
    public void draw() {
	StdDraw.setPenColor(StdDraw.BLACK);
	StdDraw.setPenRadius(0.01);
	Font font = new Font("Arial", Font.BOLD, 10);
	StdDraw.setFont(font);
	for (Point2D point : allPoints) {
	   double x = point.x();
	   double y = point.y();
	   StdDraw.point(point.x(), point.y());
	   StdDraw.text(x, y - 0.02, String.format("(%.3f, %.3f)", x, y));
	}
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
	if (rect == null) throw new IllegalArgumentException();

	rect.draw();
	Point2D min = new Point2D(rect.xmin(), rect.ymin());
	Point2D max = new Point2D(rect.xmax(), rect.ymax());
	

	ArrayList<Integer> squaresInRec = new ArrayList<>();
	for (double j = rect.xmin(); j <= rect.xmax(); j+= PARTITION) {
	    for (double i = rect.ymin(); i <= rect.ymax(); i += PARTITION) {
		Point2D p = new Point2D(j, i);
		squaresInRec.add(calculateSquare(p));
	    }
	}

	TreeSet<Point2D> allPoints = new TreeSet<Point2D>();
	for (Integer idx : squaresInRec) {
	    allPoints.addAll(grid.get(idx));
	}

	Iterable<Point2D> pointsWithinYRange = allPoints.subSet(min, true, max, true);
	ArrayList<Point2D> res = new ArrayList<>();
	for (Point2D inRange : pointsWithinYRange) {
	    if (rect.contains(inRange)) res.add(inRange);
	}
	return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
	if (p == null) throw new IllegalArgumentException();

	StdDraw.setPenColor(StdDraw.BLUE);
	StdDraw.setPenRadius(0.01);

	Font font = new Font("Arial", Font.BOLD, 10);
	StdDraw.setFont(font);
	StdDraw.point(p.x(), p.y());
	StdDraw.text(p.x(), p.y() - 0.02, String.format("(%.3f, %.3f)", p.x(), p.y()));

	TreeSet<Point2D> nearPoints = grid.get(calculateSquare(p));
	for (Integer idx : neighbors(p)) {
	    nearPoints.addAll(grid.get(idx));
	}

	if (nearPoints.isEmpty()) {
	    //print("Empty");
	    nearPoints = allPoints;
	}

	Point2D res = null;
	Double distance = Double.POSITIVE_INFINITY;
	for (Point2D point : nearPoints) {
	    //print(point);
	    double newDistance = p.distanceSquaredTo(point);
	    if (newDistance < distance) {
		distance = newDistance;
		res = point;
	    }
	    if (distance == 0) return res;
	}

	return res;
    }

    private int calculateSquare(Point2D point) {
	int idxX = getIndex(point.x());
	int idxY = getIndex(point.y());

	return getSquareFromIdx(idxX, idxY);
    }

    private int getSquareFromIdx(int x, int y) {
	return y * DIVISION + x;
    }

    private Iterable<Integer> neighbors(Point2D point) {
	ArrayList<Integer> res = new ArrayList<>();

	int x = getIndex(point.x());
	int y = getIndex(point.y());

	ArrayList<Integer> neighbors = new ArrayList<>();
	for (int j = x - 1; j < x + 2; j++) {
	    for (int i = y - 1; i < y + 2; i++) {
		if (0 <= j && 0 <= i && j < DIVISION && i < DIVISION) {
		    if (j == x && i == y) continue;
		    //print(String.format("Adding neighbor (%d, %d); Pos is: %d", j, i, getSquareFromIdx(j, i)));
		    neighbors.add(getSquareFromIdx(j, i));
		}
	    }
	}
	
	return neighbors;
    }

    private int getIndex(double val) {
	int idx = (int) Math.round(val / PARTITION);
	return idx == 0 ? 0 : idx - 1;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
	PointSET points = new PointSET();
	print(points.isEmpty());
	print(points.size());

	Point2D p = new Point2D(1.0,0.75);
	Point2D q = new Point2D(0.5,1.0);

	print("Calculate Square");
	
	// points.insert(new Point2D(0.25,0.25));
        // points.insert(new Point2D(1.0,0.5));
        // points.insert(new Point2D(0.5,0.25));
        // points.insert(new Point2D(0.0,0.5));
        // points.insert(new Point2D(0.25,0.0));
        // points.insert(new Point2D(0.5,0.0));
        // points.insert(q);
        // points.insert(new Point2D(0.75,0.0));
        // points.insert(new Point2D(1.0,0.5));
        // points.insert(p);
	// points.insert(new Point2D(0.7, 0.2));
	// points.insert(new Point2D(0.5, 0.4));
	// points.insert(new Point2D(0.2, 0.3));
	// points.insert(new Point2D(0.4, 0.7));
	// points.insert(new Point2D(0.9, 0.6));
	points.insert(new Point2D(  1.0, 0.25));
	points.insert(new Point2D(  0.0, 0.0));
	points.insert(new Point2D(  0.25, 0.5));
	points.insert(new Point2D(  0.5, 0.25));
	points.insert(new Point2D(  0.0, 0.0));
	points.insert(new Point2D(  0.0, 0.75));
	points.insert(new Point2D(  0.0, 0.25));
	points.insert(new Point2D(  0.75, 0.5));
	points.insert(new Point2D(  0.75, 0.5));
	points.insert(new Point2D(  0.5, 0.0));

	points.draw();

	print(points.isEmpty());
	print(points.size());
	
	print("Contains");
	print(points.contains(new Point2D(0.5, 0.1))); // false
	print(points.contains(new Point2D(0.7, 0.2))); // true
	print(points.contains(new Point2D(0.1, 0.1))); // false

	print("Nearest");
	// print(points.nearest(new Point2D(0, 0.4)));
	// print(points.nearest(new Point2D(0.4, 0.4)));
	// print(points.nearest(new Point2D(0.5, 0.75)));
	print(points.nearest(new Point2D(1.0, 0.75)));


	print("Points in rectangle");
	Iterable<Point2D> pointsWithinRange = points.range(new RectHV(0, 0, 0.5, 0.5));
	for (Point2D pointInRectangle : pointsWithinRange) {
	    print(pointInRectangle);
	}
    }

    private static void print(Object obj) {
	System.out.println(obj);
    }
}
