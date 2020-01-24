package sandbox.compressed;

import bdv.util.BdvFunctions;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;
import sandbox.OctTree;
import sandbox.Sphere;
import sandbox.compressed.CompressedOctTrees;

public class CompressedTwoSpheresDemo {

	public static void main(String... args) {
		OctTree< IntType > a = CompressedOctTrees
				.create(12, new Sphere(new Point(900, 400, 2000), 1000));
		OctTree< IntType > b = CompressedOctTrees.create(12, new Sphere(new Point(1100, 1800, 2000), 400));
		OctTree< IntType > c = CompressedOctTrees.max(a, b);
		BdvFunctions.show(c, "sphere").setDisplayRange(0, 1);
	}

}
