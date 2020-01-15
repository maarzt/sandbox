package sandbox;

import bdv.util.BdvFunctions;
import net.imglib2.Interval;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.ARGBType;

import java.util.Random;

public class OctTreeDemo {

	private static final Sphere sphere = new Sphere(new RealPoint(50, 50, 50), 300);

	private static Random random = new Random();

	public static void main(String... args) {
		OctTree< ARGBType > image = newOctTree(12, sphere);
		BdvFunctions.show(image, "sphere").setDisplayRange(0, 255);
	}

	private static OctTree< ARGBType > newOctTree(int depth, IntervalMask initializer) {
		Node< ARGBType > branch = getBranch(new Cube(depth), initializer);
		return new OctTree<>(depth, branch);
	}

	private static Node< ARGBType > getBranch(Cube cube,
			IntervalMask initializer) {
		OctTreeInitializer<ARGBType> oti = interval -> {
			if(initializer.contains(interval))
				return new ARGBType(random.nextInt() & 0x00ff00);
			if(!initializer.intersects(interval))
				return new ARGBType(random.nextInt() & 0xff0000);
			return null;
		};
		ARGBType value = oti.valueForInterval(cube);
		if(value != null)
			return new Leaf<ARGBType>(value);

		Node[] childs = new Node[8];
		for (int i = 0; i < 8; i++) childs[i] = getBranch(cube.child(i),
				initializer);
		return new Branch<>(childs);
	}

	public interface OctTreeInitializer<T> {

		T valueForInterval(Interval interval);
	}
}
