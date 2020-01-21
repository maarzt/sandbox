package sandbox;

import net.imglib2.Interval;
import net.imglib2.Localizable;

public interface IntervalMask {

	boolean contains(Localizable point);

	boolean contains(Interval interval);

	boolean intersects(Interval interval);
}
