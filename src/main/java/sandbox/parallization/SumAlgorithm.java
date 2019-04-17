package sandbox.parallization;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.view.Views;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * An example algorithm that uses the class {@link Parallelization}
 * to configure the {@link ExecutorService} it uses.
 * <p>
 * The algorithm takes a {@link RandomAccessibleInterval} of {@link IntType}
 * and sums up the pixel values.
 * <p>
 * The algorithm can be executed single threaded:
 * <p>
 * {@code
 * Parallelization.singleThreaded( () -> SumAlgorithm.sum( image ) );
 * }
 * <p>
 * Or multi threaded:
 * <p>
 * {@code
 * Parallelization.multiThreaded( () -> SumAlgorithm.sum( image ) );
 * }
 * Or with a specific executor service, see: {@link Parallelization#useExecutor(ExecutorService, Callable)}
 */
public class SumAlgorithm
{
	public static long sum( RandomAccessibleInterval< IntType > image )
	{
		if ( image.numDimensions() <= 1 )
		{
			long sum = 0;
			for ( IntType pixel : Views.iterable( image ) )
				sum += pixel.getInteger();
			return sum;
		}
		else
		{
			int lastDim = image.numDimensions() - 1;
			final long min = image.min( lastDim );
			final long max = image.max( lastDim );
			List< RandomAccessibleInterval<IntType> > slices = LongStream.rangeClosed( min, max )
					.mapToObj( position -> Views.hyperSlice( image, lastDim, position ) )
					.collect( Collectors.toList() );
			List< Long > subSums = Parallelization.forEach( slices, SumAlgorithm::sum );
			return subSums.stream().mapToLong( Long::longValue ).sum();
		}
	}
}
