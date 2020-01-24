package sandbox.lazy;

import bdv.util.BdvFunctions;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Util;
import sandbox.OctTree;
import sandbox.OctTrees;
import sandbox.Sphere;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ManySpheresDemo {

	public static void main(String... args) {
		Random random = new Random();
		int depth = 10;
		int size = 1 << depth;
		int numberOfSegments = 100000;
		List<OctTree<IntType>> trees = new ArrayList<>();
		for (int i = 0; i < numberOfSegments; i++) {
			System.out.println(i);
			Sphere intervalMask = new Sphere(
					new Point(random.nextInt(size), random.nextInt(size),
							random.nextInt(size)), 20);
			OctTree< IntType >
					tree = LazyTree.octTree(depth, intervalMask, new IntType(i+1), new IntType(0));
			trees.add(tree);
		}
		OctTree<IntType> c = LazyMergeMany.merge(new IntType(0), Util::max, trees);
		BdvFunctions.show(c, "sphere").setDisplayRange(0, numberOfSegments);
	}
}
