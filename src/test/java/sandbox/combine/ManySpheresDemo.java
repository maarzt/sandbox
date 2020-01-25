package sandbox.combine;

import bdv.util.BdvFunctions;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.StopWatch;
import net.imglib2.view.Views;
import sandbox.OctTree;
import sandbox.Sizes;
import sandbox.Sphere;
import sandbox.lazy.LazyTree;

import java.util.Random;

public class ManySpheresDemo {

	private static final int depth = 10;
	private static final int n = 1000000;
	private static final int size = 1 << depth;
	private static final Random random = new Random();
	private static final int radius = 5;

	public static void main(String... args) {
		StopWatch stopWatch = StopWatch.createAndStart();
		RandomAccessibleInterval< IntType > img = combinedTree();
		System.out.println(stopWatch);
		//Sizes.printSize(img);
		BdvFunctions.show(img, "sphere").setDisplayRange(0, n);
	}

	private static RandomAccessibleInterval< IntType > naive() {
		Img< IntType > image = ArrayImgs.ints(size, size, size);
		HyperSphereShape sphereShape = new HyperSphereShape(radius);
		RandomAccessible< Neighborhood< IntType > > neighborhood = sphereShape
				.neighborhoodsRandomAccessible(Views.extendZero(image));
		RandomAccess< Neighborhood< IntType > > randomAccess =
				neighborhood.randomAccess();
		for (int value = 0; value < n; value++) {
			System.out.println(value);
			randomAccess.setPosition(getCenter());
			Neighborhood< IntType > sphere = randomAccess.get();
			final int i1 = value;
			sphere.forEach(pixel -> pixel.setInteger(i1));
		}
		return image;
	}

	private static RandomAccessibleInterval< IntType > imgLabeling() {
		ArrayImg< IntType, IntArray > ints = ArrayImgs.ints(size, size, size);
		RandomAccessibleInterval< LabelingType<Integer> > image =
				new ImgLabeling<>(ints);
		HyperSphereShape sphereShape = new HyperSphereShape(radius);
		RandomAccessible< Neighborhood< LabelingType<Integer> > > neighborhood = sphereShape
				.neighborhoodsRandomAccessible(Views.extendBorder(image));
		RandomAccess< Neighborhood< LabelingType<Integer> > > randomAccess =
				neighborhood.randomAccess();
		for (int i = 0; i < n; i++) {
			System.out.println(i);
			randomAccess.setPosition(getCenter());
			Neighborhood< LabelingType< Integer > > sphere = randomAccess.get();
			Integer value = i;
			sphere.forEach(pixel -> pixel.add(value));
		}
		return ints;
	}

	private static RandomAccessibleInterval< IntType > combinedTree() {
		CombinedTree< Integer > combinedTree = new CombinedTree<>(depth);
		for (int i = 0; i < n; i++) {
			System.out.println(i);
			Sphere mask = getSphere();
			OctTree< Boolean > a = LazyTree.booleanOctTree(depth, mask);
			combinedTree.addLabel(i, a);
		}
		//combinedTree.memoryStatistic();
		return combinedTree.indexImage();
	}

	private static Sphere getSphere() {
		return new Sphere(getCenter(), radius);
	}

	private static Point getCenter() {
		int x = random.nextInt(size);
		int y = random.nextInt(size);
		int z = random.nextInt(size);
		return new Point(x, y, z);
	}
}
