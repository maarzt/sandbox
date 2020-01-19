package sandbox.lazy;

import bdv.util.BdvFunctions;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;
import sandbox.IntervalMask;
import sandbox.OctTree;
import sandbox.Sphere;

public class SphereDemo {

	private static final IntervalMask
			sphere = new Sphere(new RealPoint(900, 400, 2000), 1000);

	public static void main(String... args) {
		OctTree< IntType > image = LazyTree.octTree(12, sphere, new IntType(1),
				new IntType(0));
		BdvFunctions.show(image, "sphere").setDisplayRange(0, 1);
	}

}
