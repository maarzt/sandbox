package sandbox;

import net.imglib2.Interval;
import net.imglib2.Point;

public class Cube implements Interval {

	private final int depth;

	private final Point position = new Point(3);

	private final Cube child;

	public Cube(int depth) {
		this.depth = depth;
		this.child = (depth > 0) ? new Cube(depth - 1) : null;
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
		child.position.setPosition(position);
		for (int d = 0; d < 3; d++) {
			if ( (index & (1 << d)) != 0 )
				child.position.move(1 << (depth - 1), d);
		}
		return child;
	}
}
