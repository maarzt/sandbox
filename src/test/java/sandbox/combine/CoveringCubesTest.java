package sandbox.combine;

import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.util.Intervals;
import org.junit.Test;
import sandbox.Cube;
import sandbox.OctTree;
import sandbox.OctTrees;
import sandbox.Sphere;

import java.util.Arrays;
import java.util.List;

import static net.imglib2.test.ImgLib2Assert.assertIntervalEquals;
import static org.junit.Assert.assertEquals;

public class CoveringCubesTest {

	@Test
	public void testEntryCubes() {
		Sphere sphere = new Sphere(new Point(64, 64, 64), 3);
		OctTree< Boolean > octTree = OctTrees.create(7, sphere, true, false);
		List< Cube > entryCubes = CoveringCubes.calculate(octTree);
		List< Interval > expected =
				Arrays.asList(Intervals.createMinSize(62, 62, 62, 2, 2, 2),
						Intervals.createMinSize(64, 60, 60, 4, 4, 4),
						Intervals.createMinSize(60, 64, 60, 4, 4, 4),
						Intervals.createMinSize(64, 64, 60, 4, 4, 4),
						Intervals.createMinSize(60, 60, 64, 4, 4, 4),
						Intervals.createMinSize(64, 60, 64, 4, 4, 4),
						Intervals.createMinSize(60, 64, 64, 4, 4, 4),
						Intervals.createMinSize(64, 64, 64, 4, 4, 4));
		assertIntervalsEquals(expected, entryCubes);
	}

	@Test
	public void testEntryCubes2() {
		Sphere sphere = new Sphere(new Point(63, 20, 30), 3);
		OctTree< Boolean > octTree = OctTrees.create(7, sphere, true, false);
		List< Cube > entryCubes = CoveringCubes.calculate(octTree);
		List< Interval > expected =
				Arrays.asList(Intervals.createMinSize(56, 16, 24, 8, 8, 8),
						Intervals.createMinSize(60, 16, 32, 4, 4, 4),
						Intervals.createMinSize(60, 20, 32, 4, 4, 4),
						Intervals.createMinSize(64, 16, 28, 4, 4, 4),
						Intervals.createMinSize(64, 20, 28, 4, 4, 4),
						Intervals.createMinSize(64, 18, 32, 2, 2, 2),
						Intervals.createMinSize(64, 20, 32, 4, 4, 4));
		assertIntervalsEquals(expected, entryCubes);
	}

	private void assertIntervalsEquals(List< ? extends Interval > expected,
			List< ? extends Interval > actual)
	{
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++)
			assertIntervalEquals(expected.get(i), actual.get(i));
	}

}
