package sandbox;

import net.imagej.ImageJService;
import net.imglib2.roi.labeling.ImgLabeling;

public interface LabelingIoService extends ImageJService
{
	ImgLabeling<?, ?> open(String file);
}
