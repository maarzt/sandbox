package sandbox;

import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.Sampler;

public class OctTree<T> implements RandomAccessibleInterval<T> {

	final int depth;

	final Node<T> root;

	public OctTree(int depth, Node< T > root) {
		this.depth = depth;
		this.root = root;
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
			Node<T> node = root;
			long x = getLongPosition(0);
			long y = getLongPosition(1);
			long z = getLongPosition(2);
			for(int d = depth - 1; d >= 0; d--) {
				if (node instanceof Leaf)
					return ((Leaf<T>) node).value();
				long bitX = (x >> d) & 1;
				long bitY = (y >> d) & 1;
				long bitZ = (z >> d) & 1;
				long index = bitX | (bitY << 1) | (bitZ << 2);
				node = ((Branch<T>) node).child((int) index);
			}
			if (node instanceof Leaf)
				return ((Leaf<T>) node).value();
			throw new AssertionError("Tree is to deep.");
		}

		@Override
		public Sampler< T > copy() {
			return randomAccess();
		}
	}
}
