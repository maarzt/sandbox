package sandbox;

import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.Sampler;

import java.util.function.Function;

public class OctTree<T> implements RandomAccessibleInterval<T> {

	final int depth;

	final Object root;

	public OctTree(int depth, Object root) {
		this.depth = depth;
		this.root = root;
	}

	public OctTree(int depth, Function< Interval, T > initializer)
	{
		this(depth, createTree(new Cube(depth), initializer));
	}

	private static <T> Object createTree(Cube cube,
			Function<Interval, T> initializer) {
		T value = initializer.apply(cube);
		if(value != null)
			return value;

		Object[] childs = new Object[8];
		for (int i = 0; i < 8; i++) childs[i] = createTree(cube.child(i),
				initializer);
		return new SimpleTree(childs);
	}

	@Override
	public long min(int d) {
		return 0;
	}

	@Override
	public long max(int d) {
		return (1 << depth) - 1;
	}

	@Override
	public RandomAccess< T > randomAccess() {
		return new RA();
	}

	@Override
	public RandomAccess< T > randomAccess(Interval interval) {
		return randomAccess();
	}

	@Override
	public int numDimensions() {
		return 3;
	}

	public class RA extends Point implements RandomAccess<T> {

		private RA(RA other) {
			super(other);
		}

		private RA() {
			super(3);
		}

		@Override
		public RandomAccess< T > copyRandomAccess() {
			return new RA(this);
		}

		@Override
		public T get() {
			Object node = root;
			long x = getLongPosition(0);
			long y = getLongPosition(1);
			long z = getLongPosition(2);
			for(int d = depth - 1; d >= 0; d--) {
				if (!(node instanceof SimpleTree))
					return (T) node;
				long bitX = (x >> d) & 1;
				long bitY = (y >> d) & 1;
				long bitZ = (z >> d) & 1;
				long index = bitX | (bitY << 1) | (bitZ << 2);
				node = ((SimpleTree) node).child((int) index);
			}
			if (!(node instanceof SimpleTree))
				return (T) node;
			throw new AssertionError("Tree is to deep.");
		}

		@Override
		public Sampler< T > copy() {
			return randomAccess();
		}
	}
}
