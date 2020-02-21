package multiresolution;

import bdv.util.RandomAccessibleIntervalSource;
import bdv.viewer.Source;
import net.imagej.ImgPlus;
import net.imagej.axis.AxisType;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.util.Intervals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultiResolution<T> {

	private final List<ImgPlus<T>> resolutions;

	public MultiResolution(List<Img<T>> resolutions, String name, AxisType[] axisTypes, double[] calibration) {
		Img<T> highesResolution = resolutions.get(0);
		long[] fullSize = Intervals.dimensionsAsLongArray(highesResolution);
		this.resolutions = resolutions.stream()
				.map(img -> {
					long[] scaledSize = Intervals.dimensionsAsLongArray(img);
					double[] scaleFactors = elementWiseDiv(fullSize, scaledSize);
					double[] scaledCalibration = elementWiseMultiplication(calibration, scaleFactors);
					return new ImgPlus<>(img, name, axisTypes, scaledCalibration);
				}).collect(Collectors.toList());
	}

	private double[] elementWiseMultiplication(double[] a, double[] b) {
		double[] d = new double[a.length];
		Arrays.setAll(d, i -> a[i] / b[i]);
		return d;
	}


	private double[] elementWiseDiv(long[] a, long[] b) {
		double[] d = new double[a.length];
		Arrays.setAll(d, i -> (double) a[i] / (double) b[i]);
		return d;
	}

	public MultiResolution(ImgPlus<T>... resolution) {
		this.resolutions = Arrays.asList(resolution);
	}

	public List<ImgPlus<T>> resolutions() {
		return resolutions;
	}

	public static <T extends NumericType<T>> List<Source<T>> asSources(MultiResolution<T> multiResolution) {
		ImgPlus<T> imgPlus = multiResolution.resolutions().get(0);
		T type = imgPlus.firstElement();
		Source<T> source = new RandomAccessibleIntervalSource<>(imgPlus.getImg(), type, imgPlus.getName());
		return Collections.singletonList(source);
	}
}
