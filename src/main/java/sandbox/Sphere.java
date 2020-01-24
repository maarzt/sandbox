package sandbox;

import net.imglib2.Interval;
import net.imglib2.Localizable;

public class Sphere implements IntervalMask {

	private final Localizable center;

	private final long squaredRadius;

	public Sphere(Localizable center, double radius) {
		this.center = center;
		this.squaredRadius = (long) (radius * radius);
	}

	@Override
	public boolean contains(Localizable localizable) {
		return Points.squaredDistance(localizable, center) <= squaredRadius;
	}

	@Override
	public boolean contains(Interval interval) {
		long squaredDistance = 0;
		for (int d = 0; d < center.numDimensions(); d++) {
			long c = center.getLongPosition(d);
			long max = interval.max(d);
			long min = interval.min(d);
			squaredDistance += square( Math.max(max - c, c - min) );
		}
		return squaredDistance <= squaredRadius;
	}

	@Override
	public boolean intersects(Interval interval) {
		long squaredDistance = 0;
		for (int d = 0; d < center.numDimensions(); d++) {
			long c = center.getLongPosition(d);
			long max = interval.max(d);
			long min = interval.min(d);
			squaredDistance += square( ensureRange(0, min - c, max - c) );
		}
		return squaredDistance <= squaredRadius;
	}

	private static long ensureRange(long value, long min, long max) {
		return Math.min(Math.max(value, min), max);
	}

	private static long square(long value) {
		return value * value;
	}
}
