package sandbox;

import ij.ImagePlus;
import io.scif.SCIFIO;
import io.scif.img.IO;
import io.scif.img.ImgOpener;
import io.scif.services.DatasetIOService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.labeling.ImgLabeling;
import org.scijava.Context;

import java.io.IOException;

public class ImageOpen
{
	public static void main(String... args) throws IOException
	{
//		ImagePlus image = new ImagePlus("/home/arzt/Documents/Datasets/beans.tif");
//		RandomAccessibleInterval<?> image2 = ImageJFunctions.wrap( image );

		Context context = new Context();
//		DatasetIOService datastIoService = context.service( DatasetIOService.class );
//		RandomAccessibleInterval<?> dataset = new SCIFIO(context).datasetIO().open( "/home/arzt/Documents/Datasets/beans.tif" ).getImgPlus().getImg();
//
//		RandomAccessibleInterval< ? > image3 = new ImgOpener(context).openImgs( "/home/arzt/Documents/Datasets/beans.tif").get(0).getImg();
//		RandomAccessibleInterval< ? > image4 = IO.open( "/home/arzt/Documents/Datasets/beans.tif" );

		LabelingIoService labelingIoService = context.service( LabelingIoService.class );
		ImgLabeling labeling = labelingIoService.open( "/path/to/labeling.bson" );
		context.dispose();
	}
}
