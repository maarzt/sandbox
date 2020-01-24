package sandbox.lazy;

import bdv.util.BdvFunctions;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Util;
import sandbox.OctTree;
import sandbox.OctTrees;
import sandbox.Sphere;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;

public class TwoSpheresDemo {

	public static void main(String... args) {
		OctTree< IntType > a = OctTrees.create(12, new Sphere(new Point(900, 400, 2000), 1000));
		OctTree< IntType > b = OctTrees.create(12, new Sphere(new Point(1100, 1800, 2000), 400));
		OctTree< IntType > c = LazyMerge.merge(a, b, Util::max);
		BdvFunctions.show(c, "sphere").setDisplayRange(0, 1);
	}

}
