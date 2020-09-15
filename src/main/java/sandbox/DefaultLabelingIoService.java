package sandbox;

import io.scif.services.DatasetIOService;
import net.imagej.ImageJService;
import net.imglib2.roi.labeling.ImgLabeling;
import org.scijava.Context;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

@Plugin( type = ImageJService.class )
public class DefaultLabelingIoService extends AbstractService implements LabelingIoService
{

	@Parameter
	private Context context;

	@Parameter
	private DatasetIOService datasetIOService;

	@Override
	public ImgLabeling< ?, ? > open( String file )
	{
		// TODO write the actual code
		System.out.println( datasetIOService );
		return null;
	}
}
