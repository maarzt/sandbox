package sandbox.leaf;

import net.imglib2.Cursor;
import net.imglib2.FlatIterationOrder;
import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.Sampler;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.view.IterableRandomAccessibleInterval;
import sandbox.OctTree;

import java.util.Iterator;

public class LeafImg< T extends NativeType< T > > implements Img< T > {

	private final T type;

	private final Class<?> typeClass;

	private final int leafDepth;

	private final int treeDepth;

	private final OctTree<?> tree;

	public LeafImg(T type, int leafDepth, OctTree< ? > tree) {
		this.type = type;
		this.typeClass = type.getClass();
		this.leafDepth = leafDepth;
		this.treeDepth = tree.getDepth();
		this.tree = tree;
	}

	@Override
	public ImgFactory< T > factory() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Img< T > copy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cursor< T > cursor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cursor< T > localizingCursor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T firstElement() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object iterationOrder() {
		return new FlatIterationOrder(this);
	}

	@Override
	public Iterator< T > iterator() {
		return new IterableRandomAccessibleInterval<>(this).iterator();
	}

	@Override
	public long min(int d) {
		return 0;
	}

	@Override
	public long max(int d) {
		return (1 << leafDepth) * (1 << treeDepth) - 1;
	}

	@Override
	public RandomAccess< T > randomAccess() {
		return new RA();
	}

	@Override
	public RandomAccess< T > randomAccess(Interval interval) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int numDimensions() {
		return 3; // TODO: implement other versions too.
	}

	private class RA extends Point implements RandomAccess< T > {

		private final T value =
				(T) type.getNativeTypeFactory().createLinkedType(new FakeNativeImg());

		private final RandomAccess<?> randomAccess = tree.randomAccess();

		private final long positionMask = (1 << leafDepth) - 1;

		private RA() {
			super(3);
		}

		public RA(RA ra) {
			super(ra);
		}

		@Override
		public RandomAccess< T > copyRandomAccess() {
			return new RA(this);
		}

		@Override
		public T get() {
			long index = 0;
			for (int d = 3 - 1; d >= 0; d--)
				index = (index << leafDepth) | (position[d] & positionMask);
			for (int d = 3 - 1; d >= 0; d--)
				randomAccess.setPosition(position[d] >> leafDepth, d);
			Object container = randomAccess.get();
			if(typeClass.isInstance(type))
				return (T) container;
			value.updateContainer(container);
			value.updateIndex((int) index);
			return value;
		}

		@Override
		public Sampler< T > copy() {
			return copyRandomAccess();
		}
	}
}
