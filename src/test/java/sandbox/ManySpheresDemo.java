package sandbox;

import bdv.util.BdvFunctions;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;

import java.util.Random;

public class ManySpheresDemo {

	public static void main(String... args) {
		Random random = new Random();
		int depth = 11;
		int size = 1 << depth;
		OctTree< IntType > c = new OctTree< IntType >(depth, new IntType(0));
		for (int i = 0; i < 100000; i++) {
			System.out.println(i);
			OctTree< IntType > a = OctTrees.create(depth,
					new Sphere(new RealPoint(random.nextInt(size), random.nextInt(size), random.nextInt(size)), random.nextInt(20)));
			c = OctTrees.max(c, a);
		}
		BdvFunctions.show(c, "sphere").setDisplayRange(0, 1);
	}
}
