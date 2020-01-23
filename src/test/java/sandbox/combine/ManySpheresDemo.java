package sandbox.combine;

import bdv.util.BdvFunctions;
import net.imglib2.RealPoint;
import sandbox.OctTree;
import sandbox.Sphere;
import sandbox.lazy.LazyTree;

import java.util.Random;

public class ManySpheresDemo {

	public static void main(String... args) {
		Random random = new Random();
		int depth = 11;
		int size = 1 << depth;
		CombinedTree< Integer > c = new CombinedTree<>(depth);
		for (int i = 0; i < 10000; i++) {
			System.out.println(i);
			OctTree< Boolean > a = LazyTree.octTree(depth, new Sphere(
					new RealPoint(random.nextInt(size), random.nextInt(size), random.nextInt(size)),
					random.nextInt(20)), true, false);
			c.addLabel(i, a);
		}
		BdvFunctions.show(c.indexImage(), "sphere").setDisplayRange(0, 1);
	}
}
