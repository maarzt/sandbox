package sandbox.executors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SequentialExecutorServiceDemo
{
	public static void main( String... args )
	{
		ExecutorService executorService = new ForkJoinExecutorService();
		List< Callable< Object > > tasks = IntStream.range( 0, 10 )
				.mapToObj( index -> Executors.callable( () -> System.out.println( "Task: " + index ) ) )
				.collect( Collectors.toList() );
		try
		{
			List< Future< Object > > futures = executorService.invokeAll( tasks );
			for ( Future< ? > future : futures )
				future.get();
		}
		catch ( ExecutionException | InterruptedException e )
		{
			throw new RuntimeException( e );
		}
	}
}
