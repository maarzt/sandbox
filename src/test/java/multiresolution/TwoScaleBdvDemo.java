package multiresolution;

import bdv.util.BdvFunctions;
import bdv.util.BdvHandle;
import bdv.util.BdvOptions;
import bdv.util.BdvSource;
import bdv.viewer.Source;
import net.imagej.axis.Axes;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import java.awt.*;
import java.util.List;

public class TwoScaleBdvDemo {

	public static void main(String... args) {
		MultiResolutionImage< UnsignedShortType > image =
				ExampleImage.mitosis();
		show(image);
	}

	private static final Color[] COLORS =
			{ Color.red, Color.green, Color.blue, Color.white, Color.cyan,
					Color.magenta, Color.yellow };

	private static void show(MultiResolutionImage< UnsignedShortType > image) {
		List< MultiResolutionImage< UnsignedShortType > > channels =
				MultiResolutionViews.hyperSlice(image, Axes.CHANNEL);
		BdvHandle handle = null;
		for (int i = 0; i < channels.size(); i++) {
			MultiResolutionImage< UnsignedShortType > channel = channels.get(i);
			Source< UnsignedShortType > source =
					new MultiResolutionSource<>(channel);
			long timePoints = ImgPlusUtils
					.getDimension(image.resolutions().get(0), Axes.TIME);
			BdvSource bdvSource = BdvFunctions.show(source, (int) timePoints,
					BdvOptions.options().addTo(handle));
			bdvSource.setColor(getColor(i));
			handle = bdvSource.getBdvHandle();
		}
	}

	private static ARGBType getColor(int i) {
		Color color = i >= COLORS.length ? Color.white : COLORS[i];
		return new ARGBType(color.getRGB());
	}
}
