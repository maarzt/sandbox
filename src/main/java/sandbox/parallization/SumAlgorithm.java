package sandbox.parallization;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.view.Views;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Supplier;
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
 * Or with a specific executor service, see: {@link Parallelization#useExecutor(ExecutorService, Supplier)}
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
			ExecutorService executor = Parallelization.getExecutorService();
			int lastDim = image.numDimensions() - 1;
			List< Callable< Long > > callables = LongStream.rangeClosed( image.min( lastDim ), image.max( lastDim ) )
					.mapToObj( index -> ( Callable< Long > ) () -> sum( Views.hyperSlice( image, lastDim, index ) ) )
					.collect( Collectors.toList() );
			try
			{
				long sum = 0;
				for( Future<Long> future : executor.invokeAll( callables ) )
					sum = future.get();
				return sum;
			}
			catch ( InterruptedException | ExecutionException e )
			{
				throw new RuntimeException( e );
			}
		}
	}
}
