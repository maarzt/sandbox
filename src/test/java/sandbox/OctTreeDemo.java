package sandbox;

import bdv.util.BdvFunctions;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;

import java.util.Random;

public class OctTreeDemo {

	private static final Sphere sphere = new Sphere(new RealPoint(900, 400, 2000), 1000);

	private static Random random = new Random();

	public static void main(String... args) {
		OctTree< IntType > image = OctTrees.create(12, sphere);
		BdvFunctions.show(image, "sphere").setDisplayRange(0, 1);
	}

}
