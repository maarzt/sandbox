package sandbox.combine;

import bdv.util.BdvFunctions;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;
import sandbox.OctTree;
import sandbox.OctTrees;
import sandbox.Sphere;

import java.util.Random;

public class SingleSphereDemo {

	private static final Sphere sphere = new Sphere(new Point(900, 400, 2000), 1000);

	public static void main(String... args) {
		OctTree< Boolean > image = OctTrees.create(12, sphere, true, false);
		CombinedTree< String > combinedTree = new CombinedTree<>(12);
		combinedTree.addLabel("a", image);
		BdvFunctions.show(combinedTree.indexImage(), "sphere").setDisplayRange(0, 1);
	}

}
