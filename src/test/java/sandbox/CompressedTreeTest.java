package sandbox;

import bdv.util.BdvFunctions;
import net.imglib2.RealPoint;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.test.ImgLib2Assert;
import net.imglib2.type.numeric.integer.IntType;
import org.junit.Test;

public class CompressedTreeTest {

	private static final IntType ZERO = new IntType(0);
	private static final IntType ONE = new IntType(1);

	private static final BiMap< IntType > BINARY_VALUES = new BiMap< IntType >() {

		private final IntType[] values = { ZERO, ONE };

		@Override
		public int getIndex(IntType value) {
			return value.getInteger();
		}

		@Override
		public IntType getValue(int index) {
			return values[index];
		}
	};

	@Test
	public void test() {
		ProxyNode< IntType > root = createRoot();
		root.makeNode();
		root.childProxy(0).setValue(ONE);
		root.childProxy(1).setValue(ZERO);
		root.childProxy(2).setValue(ZERO);
		root.childProxy(3).setValue(ZERO);
		root.childProxy(4).setValue(ZERO);
		root.childProxy(5).setValue(ZERO);
		root.childProxy(6).setValue(ZERO);
		root.childProxy(7).setValue(ZERO);
		OctTree< IntType > image = new OctTree<>(1, root.value());
		ImgLib2Assert.assertImageEquals(
				ArrayImgs.ints(new int[] { 1, 0, 0, 0, 0, 0, 0, 0 }, 2, 2, 2),
				image);
	}

	private static ProxyNode< IntType > createRoot() {
		CompressedTree< IntType > compressedTree =
				new CompressedTree<>(BINARY_VALUES);
		return new ProxyNode<>(compressedTree);
	}

	public static void main(String... args) {
		final Sphere sphere = new Sphere(new RealPoint(900, 400, 2000), 1000);
		final Cube cube = new Cube(10);
		ProxyNode< IntType > root = createRoot();
		fill(sphere, cube, root);
		BdvFunctions.show(new OctTree< IntType >(10, root.value()), "experiment").setDisplayRange(0, 1);
	}

	private static void fill(Sphere sphere, Cube cube,
			ProxyNode< IntType > node)
	{
		if( sphere.contains(cube) ) {
			node.setValue(ONE);
			return;
		}
		if( ! sphere.intersects(cube) ) {
			node.setValue(ZERO);
			return;
		}
		node.makeNode();
		for (int i = 0; i < 8; i++) {
			fill(sphere, cube.child(i), node.childProxy(i));
		}
	}
}
