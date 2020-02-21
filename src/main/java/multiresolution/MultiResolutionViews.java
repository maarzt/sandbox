package multiresolution;

import net.imagej.ImgPlus;
import net.imagej.axis.AxisType;

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
}
