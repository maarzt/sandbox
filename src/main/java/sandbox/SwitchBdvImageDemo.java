package sandbox;

import bdv.util.BdvFunctions;
import bdv.util.BdvHandle;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.ARGBType;

import java.util.Random;

public class SwitchBdvImageDemo
{

	private static final Random random = new Random();

	private static Img< ARGBType > red = createImage(0xffff0000, 20, 10, 10);
	private static Img< ARGBType > green = createImage(0xff00ff00, 10, 20, 10);
	private static Img< ARGBType > blue = createImage(0xff0000ff, 10, 10, 20);

	private static Img<ARGBType> createImage( int color, long... dims )
	{
		Img< ARGBType > result = ArrayImgs.argbs( dims );
		result.forEach( pixel -> pixel.set( random.nextInt() & color ) );
		return result;
	}

	public static void main(String... args) throws InterruptedException
	{
		ForwardingRandomAccessibleInterval< ARGBType > image = new ForwardingRandomAccessibleInterval<>( red );
		BdvHandle bdv = BdvFunctions.show( image, "test" ).getBdvHandle();
		while ( true ) {
			setSource( bdv, image, red );
			setSource( bdv, image, green );
			setSource( bdv, image, blue );
		}
	}

	private static void setSource( BdvHandle bdv, ForwardingRandomAccessibleInterval< ARGBType > image, Img< ARGBType > red ) throws InterruptedException
	{
		image.setSource( red );
		bdv.getViewerPanel().requestRepaint();
		Thread.sleep( 1000 );
	}
}
