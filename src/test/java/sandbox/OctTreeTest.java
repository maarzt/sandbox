package sandbox;

import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.test.ImgLib2Assert;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Intervals;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OctTreeTest {

	private static final IntType zero = new IntType(0);

	private static final IntType one = new IntType(1);

	private static final Sphere sphere = new Sphere(new Point(50, 50, 50), 40);

	@Test
	public void testOnePixel() {
		OctTree< IntType > tree = new OctTree<>(0, one);
		Img< IntType > expected = ArrayImgs.ints(new int[] { 1 }, 1, 1, 1);
		ImgLib2Assert.assertImageEquals( expected, tree );
	}

	@Test
	public void testSmallSquarePixel() {
		OctTree< IntType > tree = new OctTree<>(1,
				new SimpleNode(one, zero, zero, zero, zero, zero, zero, zero));
		Img< IntType > expected = ArrayImgs.ints(new int[] { 1, 0, 0, 0, 0, 0, 0, 0 }, 2, 2, 2);
		ImgLib2Assert.assertImageEquals( expected, tree );
	}

	@Test
	public void testSphereContains() {
		assertTrue( sphere.contains( new Point(50, 50, 50) ) );
		assertFalse( sphere.contains( new Point(0, 50, 50) ) );
	}

	@Test
	public void testSphereContainsInterval() {
		Interval interval = Intervals.createMinSize( 50, 50, 50, 10, 10, 10 );
		assertTrue( sphere.contains(interval) );
	}

	@Test
	public void testSphereExcludes() {
		assertFalse( sphere.intersects(new FinalInterval(10, 10, 10)));
		assertTrue( sphere.intersects(new FinalInterval(40, 40, 40)));
	}
}
