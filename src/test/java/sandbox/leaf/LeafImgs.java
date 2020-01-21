package sandbox.leaf;

import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Localizables;
import sandbox.IntervalMask;
import sandbox.OctTree;

public class LeafImgs {

	public static <T extends IntegerType<T> & NativeType<T> > LeafImg<T> create(T type, int leafDepth, int treeDepth, IntervalMask sphere) {
		long leafSize = 1L << leafDepth;
		Dimensions leafDimensions = new FinalDimensions(leafSize, leafSize, leafSize)	;
		long numElements = Intervals.numElements(leafDimensions);
		T zero = type.createVariable();
		zero.setZero();
		T one = type.createVariable();
		one.setZero();
		OctTree< Object > tree =
				new OctTree<>(treeDepth + leafDepth, interval -> {
					if (!sphere.intersects(interval)) return zero;
					if (sphere.contains(interval)) return one;
					if (isLeaf(interval, leafSize))
						return createAccess(type, interval, sphere);
					return null;
				});
		return new LeafImg<>(type.createVariable(), leafDepth, new OctTree<>(treeDepth, tree.getRoot()));
	}

	private static <T extends IntegerType<T> & NativeType<T> > Object createAccess(T type, Interval interval,
			IntervalMask sphere) {
		ArrayImg< T, ? > result = new ArrayImgFactory<>(type).create(interval);
		RandomAccessibleInterval< Localizable > positions = new Localizables().randomAccessibleInterval(interval);
		LoopBuilder.setImages(positions, result).forEachPixel(
				(position, pixel) -> pixel.setInteger(toInt(sphere.contains(position)))
		);
		return result.update(null);
	}

	private static int toInt(boolean value) {
		return value ? 1 : 0;
	}

	private static boolean isLeaf(Interval interval, long leafSize) {
		int n = interval.numDimensions();
		for (int d = 0; d < n; d++) {
			if(leafSize != interval.dimension(d))
				return false;
		}
		return true;
	}
}
