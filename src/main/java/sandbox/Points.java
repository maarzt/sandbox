package sandbox;

import net.imglib2.Dimensions;
import net.imglib2.EuclideanSpace;
import net.imglib2.Interval;
import net.imglib2.RealInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;

import java.util.function.IntToDoubleFunction;

public class Points {

	public static double squaredDistance(RealLocalizable a, RealLocalizable b) {
		int n = checkDimensionsEqual(a, b);
		double result = 0;
		for (int d = 0; d < n; d++)
			result += sqr(a.getDoublePosition(d) - b.getDoublePosition(d));
		return result;
	}

	public static RealLocalizable min(RealLocalizable a, RealLocalizable b)
	{
		return newRealPoint(checkDimensionsEqual(a, b), d -> Math.min(a.getDoublePosition(d), b.getDoublePosition(d)));
	}

	public static RealLocalizable projectToInterval(RealLocalizable point, RealInterval interval)
	{
		return newRealPoint(checkDimensionsEqual(point, interval),
				d -> ensureRange( point.getDoublePosition(d), interval.realMin(d), interval.realMax(d)));
	}

	private static double ensureRange(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	private static int checkDimensionsEqual(EuclideanSpace a, EuclideanSpace b)
	{
		int n = a.numDimensions();
		assert n == b.numDimensions();
		return n;
	}

	private static RealLocalizable newRealPoint(int n,
			IntToDoubleFunction coordinates)
	{
		RealPoint result = new RealPoint(n);
		for (int d = 0; d < n; d++)
			result.setPosition(coordinates.applyAsDouble(d), d);
		return result;
	}

	private static double sqr(double v) {
		return v * v;
	}
}
