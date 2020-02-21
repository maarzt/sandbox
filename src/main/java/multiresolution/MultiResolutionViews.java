package multiresolution;

import net.imagej.ImgPlus;
import net.imagej.axis.AxisType;
import net.imglib2.view.Views;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiResolutionViews {

	/**
	 * @param image Input image.
	 * @param axis Axis, along which to slice the input image.
	 * @param position Position, at which to slice the image.
	 * @returns a sliced view of the given image.
	 */
	public static < T > MultiResolutionImage< T > hyperSlice(
			MultiResolutionImage< T > image, AxisType axis, long position)
	{
		if(axis.isSpatial())
			throw new UnsupportedOperationException("This method can't be used to slice in X, Y or Z.");
		List< ImgPlus< T > > resolutions = image.resolutions().stream()
				.map(level -> ImgPlusViews2.hyperSlice(level, axis, position))
				.collect(Collectors.toList());
		return new DefaultMultiResolutionImage<>(resolutions);
	}

	public static < T > List<MultiResolutionImage<T>> hyperSlice(
			MultiResolutionImage<T> image, AxisType axis)
	{
		ImgPlus< T > highestResolution = image.resolutions().get(0);
		int dimensionIndex = highestResolution.dimensionIndex(axis);
		long min = highestResolution.min(dimensionIndex);
		long max = highestResolution.max(dimensionIndex);
		List< MultiResolutionImage<T> > result = new ArrayList<>((int) (max - min));
		for (long d = min; d <= max; d++) {
			result.add(hyperSlice(image, axis, d));
		}
		return result;
	}
}
