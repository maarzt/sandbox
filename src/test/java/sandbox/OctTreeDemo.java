package sandbox;

import bdv.util.BdvFunctions;
import net.imglib2.Interval;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.IntType;

import java.util.Random;
import java.util.function.Function;

public class OctTreeDemo {

	private static final Sphere sphere = new Sphere(new RealPoint(900, 400, 2000), 1000);
	private static final IntType one = new IntType(1);
	private static final IntType zero = new IntType(0);

	private static Random random = new Random();

	public static void main(String... args) {
		OctTree< IntType > image = newOctTree(12, sphere);
		BdvFunctions.show(image, "sphere").setDisplayRange(0, 1);
	}

	private static OctTree< IntType > newOctTree(int depth, IntervalMask intervalMask) {
		return new OctTree<>(depth, interval -> {
			if(intervalMask.contains(interval))
				return one;
			if(!intervalMask.intersects(interval))
				return zero;
			return null;
		});
	}

}
