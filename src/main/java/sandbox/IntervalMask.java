package sandbox;

import net.imglib2.Interval;

public interface IntervalMask {

	boolean contains(Interval interval);

	boolean intersects(Interval interval);
}
