package sandbox;

import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Intervals;
import org.ehcache.sizeof.SizeOf;
import sandbox.compressed.CompressedOctTrees;
import sandbox.leaf.LeafImgs;

public class Sizes {

	private static final Sphere point =
			new Sphere(new Point(100, 100, 100), 1);

	private static final Sphere sphere =
			new Sphere(new Point(100, 100, 100), 100);

	private static final IntervalMask dirty = new IntervalMask() {

		@Override
		public boolean contains(Localizable point) {
			long l = point.getLongPosition(0) + point.getLongPosition(1) +
					point.getLongPosition(2);
			return l % 2 == 0;
		}

		@Override
		public boolean contains(Interval interval) {
			long l = interval.min(0) + interval.min(1) +
					interval.min(2);
			return (Intervals.numElements(interval) == 1) && (l % 2 == 0);
		}

		@Override
		public boolean intersects(Interval interval) {
			long l = interval.min(0) + interval.min(1) +
					interval.min(2);
			return (Intervals.numElements(interval) > 1) || (l % 2 == 0);
		}
	};

	private static final IntervalMask less_dense = new IntervalMask() {

		long k = 2;

		@Override
		public boolean contains(Localizable point) {
			long l = point.getLongPosition(0) + point.getLongPosition(1) +
					point.getLongPosition(2);
			l /= k;
			return l % 2 == 0;
		}

		@Override
		public boolean contains(Interval interval) {
			long l = interval.min(0) + interval.min(1) +
					interval.min(2);
			l /= k;
			return (Intervals.numElements(interval) / k / k / k == 1) && (l % 2 == 0);
		}

		@Override
		public boolean intersects(Interval interval) {
			long l = interval.min(0) + interval.min(1) +
					interval.min(2);
			l /= k;
			return (Intervals.numElements(interval) / k / k / k > 1) || (l % 2 == 0);
		}
	};

	private static final SizeOf sizeOf = SizeOf.newInstance();

	public static void main(String... args) {
		print("Point", point);
		print("Sphere", sphere);
		print("Dense", less_dense);
	}

	private static void print(String title, IntervalMask content) {
		System.out.println(title + ":");
		int depth = 8;
		printSize(ArrayImgs.ints(1<<depth, 1<<depth, 1<<depth));
		printSize(OctTrees.create(depth, content));
		printSize(CompressedOctTrees.create(depth, content));
		BitType type = new BitType();
		printSize(LeafImgs.create(type, 1, depth - 1, content));
		printSize(LeafImgs.create(type, 2, depth - 2, content));
		printSize(LeafImgs.create(type, 3, depth - 3, content));
		printSize(LeafImgs.create(type, 4, depth - 4, content));
		printSize(LeafImgs.create(type, 5, depth - 5, content));
	}

	public static void printSize(Object o) {
		long l = sizeOf.deepSizeOf(o);
		double megaBytes = l / 1024.;
		System.out.println(String.format("%s: %.2f kB", o.getClass().getSimpleName(), megaBytes));
	}
}
