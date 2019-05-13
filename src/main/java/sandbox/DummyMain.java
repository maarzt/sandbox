package sandbox;

import hr.irb.fastRandomForest.FastRandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

public class DummyMain
{
	public static void main(String... args) throws Exception
	{
		FastRandomForest forest = train();
		// test
		Instances testData = newTable();
		DenseInstance good = new DenseInstance( 1.0, new double[] { 1, 2, 0 } );
		good.setDataset( testData );
		System.out.println( "the good one: " + forest.classifyInstance( good ) );
		DenseInstance bad = new DenseInstance( 1.0, new double[] { 50, 2, 0 } );
		bad.setDataset( testData );
		System.out.println( "the bad one: " + forest.classifyInstance( bad ) );
		System.out.println( "distribution for the bad one: " + Arrays.toString( forest.distributionForInstance( bad ) ) );
	}

	private static FastRandomForest train() throws Exception
	{
		FastRandomForest forest = new FastRandomForest();
		forest.setNumTrees( 100 );
		Instances trainingData = initializeTrainingData();
		forest.buildClassifier( trainingData );
		return forest;
	}

	private static Instances initializeTrainingData()
	{
		Instances trainingData = newTable();
		trainingData.add( new DenseInstance( 1.0, new double[] {1, 2, 1} ) );
		trainingData.add( new DenseInstance( 1.0, new double[] {50, 2, 0} ) );
		return trainingData;
	}

	private static Instances newTable()
	{
		ArrayList< Attribute > attInfo = new ArrayList<>();
		attInfo.add( new Attribute( "area" ) );
		attInfo.add( new Attribute( "perimeter" ) );
		attInfo.add( new Attribute( "class", Arrays.asList("bad", "good") ) );
		Instances table = new Instances( "foo bar", attInfo, 1 );
		table.setClassIndex( 2 );
		return table;
	}
}
