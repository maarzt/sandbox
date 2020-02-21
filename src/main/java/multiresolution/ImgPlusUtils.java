package multiresolution;

import mpicbg.spim.data.sequence.FinalVoxelDimensions;
import mpicbg.spim.data.sequence.VoxelDimensions;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import java.util.stream.Stream;

public class ImgPlusUtils {

	public static VoxelDimensions getVoxelDimensions(ImgPlus< ? > imgPlus) {
		double[] size = getVoxelDimensionsAsArrayOfDouble(imgPlus);
		String unit = Stream.of(Axes.X, Axes.Y, Axes.Z).map(axisType -> {
			int index = imgPlus.dimensionIndex(axisType);
			return imgPlus.axis(index).unit();
		}).findFirst().orElse("pixel");
		return new FinalVoxelDimensions(unit, size);
	}

	public static double[] getVoxelDimensionsAsArrayOfDouble(ImgPlus< ? > imgPlus)
	{
		return Stream.of(Axes.X, Axes.Y, Axes.Z).mapToDouble(axis -> {
				int index = imgPlus.dimensionIndex(axis);
				return index < 0 ? 1 : imgPlus.averageScale(index);
			}).toArray();
	}

	public static long getDimension(ImgPlus<UnsignedShortType> image, AxisType axis) {
		int index = image.dimensionIndex(axis);
		return (index < 0) ? 1 : image.dimension(index);
	}
}
