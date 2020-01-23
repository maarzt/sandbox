package sandbox;

import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.Point;
import net.imglib2.util.Intervals;
import net.imglib2.util.Localizables;

import java.util.Arrays;

public class Cube implements Interval {

	private final int depth;

	private final Point position;

	private final Cube child;

	public Cube(int depth) {
		this.depth = depth;
		this.position = new Point(3);
		this.child = (depth > 0) ? new Cube(depth - 1) : null;
	}

	/** Copy constructor */
	private Cube(Cube original) {
		this.depth = original.depth;
		this.position = new Point(original.position);
		this.child = original.child != null ? new Cube(original.child) : null;
	}

	@Override
	public long min(int d) {
		return position.getIntPosition(d);
	}

	@Override
	public long max(int d) {
		return min(d) + (1 << depth) - 1;
	}

	@Override
	public int numDimensions() {
		return 3;
	}

	public Cube child(long index) {
		child.setPosition(position, index);
		return child;
	}

	public boolean hasChildren() {
		return child != null;
	}

	private void setPosition(Localizable parentPosition, long index) {
		for (int d = 0; d < 3; d++) {
			position.setPosition(parentPosition.getLongPosition(d) + (((index >> d) & 1) << depth), d);
		}
	}

	public Cube threadSafeCopy() {
		return new Cube(this);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Cube))
			return false;
		Cube other = (Cube) obj;
		return position.equals(other.position) && depth == other.depth;
	}

	@Override
	public String toString() {
		return "Cube " + Arrays.toString(Localizables.asLongArray(position)) + " " + (1 << depth);
	}
}
