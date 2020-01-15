package sandbox;

import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.util.Intervals;
import net.imglib2.util.Localizables;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointsTest {

	@Test
	public void testProjectToInterval() {
		Interval interval = Intervals.createMinMax(1,2,3,4);
		RealLocalizable point = new RealPoint(7, 3.6);
		RealLocalizable result = Points.projectToInterval(point, interval);
		RealLocalizable expected = new RealPoint(2,3.6);
		assertEquals(expected, result);
	}

}
