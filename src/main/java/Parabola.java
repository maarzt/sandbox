import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.RealTransform;

/**
 * RealTransform from a 2d parameter space, to a 3d coordinate space that represents a parabola.
 */
class Parabola implements RealTransform
{

	@Override
	public int numSourceDimensions()
	{
		return 2;
	}

	@Override
	public int numTargetDimensions()
	{
		return 3;
	}

	@Override
	public void apply( double[] source, double[] target )
	{
		double s = source[ 0 ];
		double t = source[ 1 ];
		target[0] = x(s, t);
		target[1] = y(s, t);
		target[2] = z(s, t);
	}

	@Override
	public void apply( RealLocalizable source, RealPositionable target )
	{
		double s = source.getDoublePosition( 0 );
		double t = source.getDoublePosition( 1 );
		target.setPosition( x( s, t ), 0 );
		target.setPosition( y( s, t ), 1 );
		target.setPosition( z( s, t ), 2 );
	}

	private double x( double s, double t )
	{
		return s * 100;
	}

	private double y( double s, double t )
	{
		return t * 100;
	}

	private double z( double s, double t )
	{
		return ( Math.pow( s - 0.5, 2 ) + Math.pow( t - 0.5, 2 ) ) * 100;
	}

	@Override
	public RealTransform copy()
	{
		return this;
	}
}
