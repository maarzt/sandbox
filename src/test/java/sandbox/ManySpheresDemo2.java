package sandbox;

import bdv.util.BdvFunctions;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingMapping;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.integer.IntType;
import sandbox.compressed.CompressedOctTrees;
import ucar.units.SI;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ManySpheresDemo2 {

	public static void main(String... args) {
		Random random = new Random();
		int depth = 9;
		int size = 1 << depth;
		OctTree< IntType > c = new OctTree<>(depth, new IntType());
		int numberOfSegments = 10000;
		for (int i = 0; i < numberOfSegments; i++) {
			System.out.println(i);
			Sphere intervalMask = new Sphere(
					new Point(random.nextInt(size), random.nextInt(size),
							random.nextInt(size)), 10	);
			OctTree< IntType > a = OctTrees.create(depth, intervalMask, new IntType(i+1), new IntType(0));
			c = OctTrees.max(c, a);
		}
		BdvFunctions.show(c, "sphere").setDisplayRange(0, numberOfSegments);
		Sizes.printSize(c);
		Sizes.printSize(CompressedOctTrees.copy(c));
	}
}
