package multiresolution;

import bdv.util.DefaultInterpolators;
import bdv.viewer.Interpolation;
import bdv.viewer.Source;
import mpicbg.spim.data.sequence.VoxelDimensions;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccessible;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import java.util.List;

public class MultiResolutionSource< T extends NumericType< T > >
		implements Source< T >
{

	private static final DefaultInterpolators DEFAULT_INTERPOLATORS =
			new DefaultInterpolators();

	private final MultiResolutionImage< T > image;

	private final VoxelDimensions voxelDimensions;

	public MultiResolutionSource(MultiResolutionImage< T > image) {
		this.image = image;
		this.voxelDimensions = ImgPlusUtils
				.getVoxelDimensions(image.resolutions().get(0));
	}

	@Override
	public boolean isPresent(int t) {
		ImgPlus< T > image = this.image.resolutions().get(0);
		int timeDimension = image.dimensionIndex(Axes.TIME);
		if (timeDimension == -1) return true;
		return t >= image.min(timeDimension) && t <= image.max(timeDimension);
	}

	@Override
	public RandomAccessibleInterval< T > getSource(int t, int level) {
		ImgPlus< T > imgPlus = image.resolutions().get(level);
		return ImgPlusViews2.hyperSlice(imgPlus, Axes.TIME, t);
	}

	@Override
	public RealRandomAccessible< T > getInterpolatedSource(int t, int level,
			Interpolation method)
	{
		RandomAccessibleInterval< T > rai = getSource(t, level);
		InterpolatorFactory interpolatorFactory =
				DEFAULT_INTERPOLATORS.get(method);
		return Views.interpolate(Views.extendZero(rai), interpolatorFactory);
	}

	@Override
	public void getSourceTransform(int t, int level,
			AffineTransform3D transform)
	{
		List< ImgPlus< T > > resolutions = image.resolutions();
		double[] factors = ImgPlusUtils.getVoxelDimensionsAsArrayOfDouble(resolutions.get(level));
		transform.set(
				factors[0], 0, 0, factors[0] * 0.5,
				0, factors[1], 0, factors[1] * 0.5,
				0, 0, factors[2], factors[2] * 0.5);
	}

	@Override
	public T getType() {
		return Util.getTypeFromInterval(image.resolutions().get(0));
	}

	@Override
	public String getName() {
		return image.resolutions().get(0).getName();
	}

	@Override
	public VoxelDimensions getVoxelDimensions() {
		return voxelDimensions;
	}

	@Override
	public int getNumMipmapLevels() {
		return image.resolutions().size();
	}
}
