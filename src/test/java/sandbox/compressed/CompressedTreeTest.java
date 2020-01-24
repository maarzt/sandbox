package sandbox.compressed;

import bdv.util.BdvFunctions;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.test.ImgLib2Assert;
import net.imglib2.type.numeric.integer.IntType;
import org.junit.Test;
import sandbox.OctTree;
import sandbox.Sphere;
import sandbox.compressed.CompressedOctTrees;
import sandbox.compressed.ProxyNode;

public class CompressedTreeTest {

	@Test
	public void test() {
		ProxyNode< IntType > root = CompressedOctTrees.createRoot();
		root.makeNode();
		root.childProxy(0).setValue(CompressedOctTrees.ONE);
		root.childProxy(1).setValue(CompressedOctTrees.ZERO);
		root.childProxy(2).setValue(CompressedOctTrees.ZERO);
		root.childProxy(3).setValue(CompressedOctTrees.ZERO);
		root.childProxy(4).setValue(CompressedOctTrees.ZERO);
		root.childProxy(5).setValue(CompressedOctTrees.ZERO);
		root.childProxy(6).setValue(CompressedOctTrees.ZERO);
		root.childProxy(7).setValue(CompressedOctTrees.ZERO);
		OctTree< IntType > image = new OctTree<>(1, root.value());
		ImgLib2Assert.assertImageEquals(
				ArrayImgs.ints(new int[] { 1, 0, 0, 0, 0, 0, 0, 0 }, 2, 2, 2),
				image);
	}

	public static void main(String... args) {
		final Sphere sphere = new Sphere(new Point(900, 400, 2000), 1000);
		int depth = 10;
		OctTree< IntType > octTree = CompressedOctTrees.create(depth, sphere);
		BdvFunctions.show(octTree, "experiment").setDisplayRange(0, 1);
	}

}
