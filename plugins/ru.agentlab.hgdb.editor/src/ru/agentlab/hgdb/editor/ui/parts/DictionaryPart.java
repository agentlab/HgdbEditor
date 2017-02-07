package ru.agentlab.hgdb.editor.ui.parts;

import java.math.BigInteger;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.eclipse.gef4.fx.anchors.DynamicAnchor;
import org.eclipse.gef4.fx.nodes.Connection;
import org.eclipse.gef4.fx.nodes.GeometryNode;
import org.eclipse.gef4.geometry.planar.RoundedRectangle;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;

public class DictionaryPart {

    private static final int GRAPH_WIDTH = 2000;
    private static final int GRAPH_HEIGHT = 1000;
    private static final int DR = 20;
    private static final int SMALL_RADIUS = 75;
    private static final int BIG_RADIUS = 250;
    private static final int GRAPH_CENTER_X = 300;
    private static final int GRAPH_CENTER_Y = 300;
    private static final int RECTANGLE_SIZE = 40;
    private static final int ARC_SIZE = 10;

    public DictionaryPart() {
        System.out.println("Hhhh"); //$NON-NLS-1$
    }

    @PostConstruct
    void initUI(BorderPane pane) {
        Group group = new Group();
        fillGroup(group);
        pane.setCenter(group);
    }

    private void fillGroup(Group group) {


        //org.eclipse.gef4.geometry.planar.Rectangle

        Rectangle background = new Rectangle(0, 0, GRAPH_WIDTH, GRAPH_HEIGHT);
        background.setFill(Color.BLUE);
        group.getChildren().add(background);
        int dAlpha = -1;
        for (int cycleIndex = 0; cycleIndex < 3; cycleIndex++)
        {
            int radius = dAlpha == 0 ? BIG_RADIUS : BIG_RADIUS - DR;
            for (int rectIndex = 0; rectIndex < 20; rectIndex++)
            {
                double alpha = 2 * Math.PI / 20 * rectIndex + dAlpha;

                StackPane stackPane =
                    createStackPane(GRAPH_CENTER_X + Double.valueOf(radius * Math.sin(alpha)).intValue(),
                        GRAPH_CENTER_Y + Double.valueOf(radius * Math.cos(alpha)).intValue());
                StackPane otherStackPane =
                    createStackPane(GRAPH_CENTER_X + Double.valueOf(SMALL_RADIUS * Math.sin(alpha)).intValue(),
                        GRAPH_CENTER_Y + Double.valueOf(SMALL_RADIUS * Math.cos(alpha)).intValue());

                Connection connection = new Connection();
                connection.setStartAnchor(new DynamicAnchor(getRectangle(stackPane)));
                connection.setEndAnchor(new DynamicAnchor(getRectangle(otherStackPane)));
                connection.setStartDecoration(new ArrowHead());
                connection.setEndDecoration(new ArrowHead());

                group.getChildren().add(stackPane);
                group.getChildren().add(otherStackPane);
                group.getChildren().add(connection);
            }

            dAlpha++;
        }
    }

    private StackPane createStackPane(int x, int y) {
        StackPane stackPane = new StackPane();
        stackPane.setLayoutX(x);
        stackPane.setLayoutY(y);

        stackPane.getChildren().addAll(createRoundRectangle(), createRandomLabel());

        return stackPane;
    }

    private Label createRandomLabel() {
        Label label = new Label(createRandomString());

        return label;
    }

    private String createRandomString() {
        return new BigInteger(80, new Random()).toString();
    }

    private GeometryNode<RoundedRectangle> createRoundRectangle() {
        //Rectangle rectangle = new Rectangle(0, 0, RECTANGLE_SIZE, RECTANGLE_SIZE);
        GeometryNode<RoundedRectangle> rectangle =
            new GeometryNode<>(new RoundedRectangle(0, 0, RECTANGLE_SIZE, RECTANGLE_SIZE, ARC_SIZE, ARC_SIZE));

        //rectangle.setArcHeight(ARC_SIZE);
        //rectangle.setArcWidth(ARC_SIZE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        rectangle.setStrokeWidth(2);
        return rectangle;
    }

    private Node getRectangle(StackPane stackPane) {
        for (Node node : stackPane.getChildren())
        {
            if (node instanceof Rectangle)
            {
                return node;
            }
        }

        return stackPane;
    }

    private static class ArrowHead
        extends Polygon {
        public ArrowHead() {
            super(0, 0, 10, 3, 10, -3);
            setFill(Color.BLACK);
            setStroke(Color.BLACK);
            setStrokeLineJoin(StrokeLineJoin.ROUND);
        }
    }
}