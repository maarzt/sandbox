package sandbox.parallization;

import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.ConstantUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Fork( 0 )
@Warmup( iterations = 0 )
@Measurement( iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS )
@State( Scope.Benchmark )
@BenchmarkMode( { Mode.AverageTime } )
public class SumAlgorithmBenchmark
{

	private final Interval interval = new FinalInterval( 1_00_000_000L, 10, 10 );

	private final RandomAccessibleInterval< IntType > image = ConstantUtils.constantRandomAccessibleInterval( new IntType( 1 ), interval.numDimensions(), interval );

	@Benchmark
	public Long fixedThreadPool()
	{
		final ExecutorService executor = Executors.newFixedThreadPool( 10 );
		Long sum = Parallelization.useExecutor( executor, () -> SumAlgorithm.sum( image ) );
		executor.shutdown();
		return sum;
	}

	@Benchmark
	public Long twoThreadsForkJoinPool()
	{
		ForkJoinPool executor = new ForkJoinPool( 2 );
		Long sum = Parallelization.useExecutor( executor, () -> SumAlgorithm.sum( image ) );
		executor.shutdown();
		return sum;
	}

	@Benchmark
	public Long multiThreadedSum()
	{
		return Parallelization.multiThreaded( () -> SumAlgorithm.sum( image ) );
	}

	@Benchmark
	public Long singleThreadedSum()
	{
		return Parallelization.singleThreaded( () -> SumAlgorithm.sum( image ) );
	}

	public static void main( String... args ) throws RunnerException
	{
		Options options = new OptionsBuilder()
				.include( SumAlgorithmBenchmark.class.getName() )
				.build();
		new Runner( options ).run();
	}
}
