package sandbox;

import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.util.Actions;
import org.scijava.ui.behaviour.util.InputActionBindings;

import javax.swing.*;

public class DummyMain
{
	public static void main(String... args) {
		JFrame frame = new JFrame( "Window" );
		InputActionBindings bindings = new InputActionBindings();
		frame.getRootPane().setFocusable( true );
		frame.getRootPane().requestFocus();
		Actions actions = new Actions(new InputTriggerConfig());
		actions.runnableAction( () -> System.out.println( "Hello World" ), "hello world", "ctrl H" );
		actions.install( bindings, "helloworld" );
		SwingUtilities.replaceUIActionMap( frame.getRootPane(), bindings.getConcatenatedActionMap() );
		SwingUtilities.replaceUIInputMap( frame.getRootPane(), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, bindings.getConcatenatedInputMap() );
		frame.setSize( 300, 300);
		frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
		frame.setVisible( true );

	}
}
