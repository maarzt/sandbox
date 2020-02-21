package multiresolution;

import bdv.viewer.Source;
import net.imagej.axis.Axes;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.test.ImgLib2Assert;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.Test;

import java.util.Arrays;

public class MultiResolutionTest {

	@Test
	public void testAsSource() {
		Img<FloatType> a = ArrayImgs.floats(new float[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }, 4, 4);
		MultiResolution<FloatType> multiResolution = new MultiResolution<>(Arrays.asList(a), "a", array(Axes.X, Axes.Y), new double[]{1.0, 1.0});
		Source<FloatType> source = MultiResolution.asSources(multiResolution).get(0);
		ImgLib2Assert.assertImageEquals(a, source.getSource(0, 0));;
	}

	private double[] nativeArray(double... values) {
		return values;
	}

	private <T> T[] array(T... values) {
		return values;
	}
}
