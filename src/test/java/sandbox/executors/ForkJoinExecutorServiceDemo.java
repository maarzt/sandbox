package sandbox.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ForkJoinExecutorServiceDemo
{
	public static void main(String... args) throws InterruptedException, ExecutionException
	{
		List< Future > futures = new ArrayList<>();
		ExecutorService executorService = new ForkJoinExecutorService();
		for ( int i = 0; i < 10; i++ )
		{
			final int t = i;
			Future< ? > future = executorService.submit( () -> {
				try
				{
					Thread.sleep( 6 );
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
				System.out.println( "Task: " + t );
			} );
			futures.add( future );
		}
		System.out.println("Submit ended:");
		for( Future<?> future : futures )
			future.get();
		System.out.println("Terminated");
		Thread.sleep( 1000 );
	}
}
