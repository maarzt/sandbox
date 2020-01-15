package sandbox;

import net.imglib2.Interval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;

public class Sphere implements IntervalMask {

	private final RealPoint center;

	private final double squaredRadius;

	public Sphere(RealPoint center, double radius) {
		this.center = center;
		this.squaredRadius = square(radius);
	}

	public boolean contains(RealLocalizable localizable) {
		return Points.squaredDistance(localizable, center) <= squaredRadius;
	}

	@Override
	public boolean contains(Interval interval) {
		double squaredDistance = 0;
		for (int d = 0; d < center.numDimensions(); d++) {
			double c = center.getDoublePosition(d);
			double max = interval.realMax(d);
			double min = interval.realMin(d);
			squaredDistance += square( Math.max(max - c, c - min) );
		}
		return squaredDistance <= squaredRadius;
	}

	@Override
	public boolean intersects(Interval interval) {
		return contains(Points.projectToInterval(center, interval));
	}

	private double square(double value) {
		return value * value;
	}
}
