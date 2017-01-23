package ru.agentlab.hgdb.editor.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Java Fx View
 */
public class JavaFxView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.bmstu.coursework.javafxview.views.JavaFxView";

	/**
	 * The constructor.
	 */
	public JavaFxView() {
		// Does nothing
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(3, true));

		org.eclipse.swt.widgets.Label inputLabel = new org.eclipse.swt.widgets.Label(parent, SWT.NONE);
		inputLabel.setText("String to revert:");
		GridDataFactory.fillDefaults().applyTo(inputLabel);

		final Text input = new Text(parent, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(input);

		Button button = new Button(parent, SWT.PUSH);
		button.setText("Revert");
		GridDataFactory.defaultsFor(button).applyTo(button);

		org.eclipse.swt.widgets.Label outputLabel = new org.eclipse.swt.widgets.Label(parent, SWT.NONE);
		outputLabel.setText("Inverted String:");
		GridDataFactory.fillDefaults().applyTo(outputLabel);

		FXCanvas fxCanvas = new FXCanvas(parent, SWT.NONE);
		BorderPane layout = new BorderPane();
		Scene scene = new Scene(layout);

		fxCanvas.setScene(scene);

		Label output = new Label();
		layout.setCenter(output);

		RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), output);
		rotateTransition.setByAngle(360);

		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), output);
		scaleTransition.setFromX(1.0);
		scaleTransition.setFromY(1.0);
		scaleTransition.setToX(4.0);
		scaleTransition.setToY(4.0);

		ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, scaleTransition);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				output.setText(new StringBuffer(input.getText()).reverse().toString());
				parallelTransition.play();
			}
		});

		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					output.setText(new StringBuffer(input.getText()).reverse().toString());
					parallelTransition.play();
				}
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
}
