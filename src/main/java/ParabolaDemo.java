import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOverlay;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.realtransform.RealTransform;
import net.imglib2.type.numeric.ARGBType;

import java.util.Random;

/**
 * Demo that shows a Overlay for BigDataViewer.
 * The Overlay shows a Parabola.
 */
public class ParabolaDemo
{

	public static void main( final String[] args )
	{
		System.out.println( "Please rotate!" );
		RealTransform parabola = new Parabola();
		final BdvOverlay overlay = new TransformedGridOverlay( parabola );
		final Bdv bdv3D = BdvFunctions.show( greenExampleImage(), "greens" );
		BdvFunctions.showOverlay( overlay, "overlay", Bdv.options().addTo( bdv3D ) );
	}

	private static Img< ARGBType > greenExampleImage()
	{
		final Random random = new Random();
		final Img< ARGBType > img = ArrayImgs.argbs( 100, 100, 100 );
		img.forEach( t -> t.set( random.nextInt() & 0xFF003F00 ) );
		return img;
	}

}
