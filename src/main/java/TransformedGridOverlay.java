import bdv.util.BdvOverlay;
import net.imglib2.RealPoint;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.realtransform.RealTransform;
import net.imglib2.realtransform.RealTransformSequence;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TransformedGridOverlay extends BdvOverlay
{
	private final RealTransform parabola;

	private List< List< RealPoint > > grid = createGrid();

	public TransformedGridOverlay( RealTransform parabola )
	{
		this.parabola = parabola;
	}

	@Override
	protected void draw( final Graphics2D g )
	{
		g.setColor( Color.RED );
		RealTransformSequence concatenate = new RealTransformSequence();
		concatenate.add( parabola );
		concatenate.add( toScreenTransform() );
		drawLines( g, concatenate, grid );
	}

	private static List< List< RealPoint > > createGrid()
	{
		List< List< RealPoint > > lines = new ArrayList<>();
		for ( int i = 0; i <= 10; i++ )
		{
			double x = i / 10.;
			lines.add( createLine( RealPoint.wrap( new double[] { x, 0 } ), RealPoint.wrap( new double[] { x, 1 } ) ) );
		}
		for ( int i = 0; i <= 10; i++ )
		{
			double y = i / 10.;
			lines.add( createLine( RealPoint.wrap( new double[] { 0, y } ), RealPoint.wrap( new double[] { 1, y } ) ) );
		}
		return lines;
	}

	private static List< RealPoint > createLine( RealPoint from, RealPoint to )
	{
		List< RealPoint > points = new ArrayList<>( 100 );
		for ( int i = 0; i <= 100; i++ )
			points.add( interpolate( from, to, i / 100. ) );
		return points;
	}

	private static RealPoint interpolate( RealPoint from, RealPoint to, double v )
	{
		int n = from.numDimensions();
		RealPoint result = new RealPoint( n );
		for ( int i = 0; i < n; i++ )
		{
			result.setPosition( from.getDoublePosition( i ) * v + to.getDoublePosition( i ) * ( 1 - v ), i );
		}
		return result;
	}

	private static void drawLines( Graphics2D g, RealTransform t, List< List< RealPoint > > grid )
	{
		for ( List< RealPoint > line : grid )
			drawLine( g, t, line );
	}

	private static void drawLine( Graphics2D g, RealTransform t, List< RealPoint > points )
	{
		final RealPoint screenCoordinates = new RealPoint( 3 );
		int[] x = new int[ points.size() ];
		int[] y = new int[ points.size() ];
		for ( int i = 0; i < points.size(); i++ )
		{
			t.apply( points.get( i ), screenCoordinates );
			x[ i ] = ( int ) screenCoordinates.getDoublePosition( 0 );
			y[ i ] = ( int ) screenCoordinates.getDoublePosition( 1 );
		}
		g.drawPolyline( x, y, points.size() );
	}

	private AffineTransform3D toScreenTransform()
	{
		final AffineTransform3D t = new AffineTransform3D();
		getCurrentTransform3D( t );
		return t;
	}
}
