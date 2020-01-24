package sandbox;

import bdv.util.BdvFunctions;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;

import java.util.Random;

public class TwoSpheresDemo {

	private static Random random = new Random();

	public static void main(String... args) {
		OctTree< IntType > a = OctTrees.create(12, new Sphere(new Point(900, 400, 2000), 1000));
		OctTree< IntType > b = OctTrees.create(12, new Sphere(new Point(1100, 1800, 2000), 400));
		OctTree< IntType > c = OctTrees.max(a, b);
		BdvFunctions.show(c, "sphere").setDisplayRange(0, 1);
	}

}
