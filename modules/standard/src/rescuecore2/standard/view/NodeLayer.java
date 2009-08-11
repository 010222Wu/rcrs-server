package rescuecore2.standard.view;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.standard.entities.Node;

/**
   A view layer that renders nodes.
 */
public class NodeLayer extends StandardEntityViewLayer<Node> {
    private static final int SIZE = 5;

    /**
       Construct a node renderer.
     */
    public NodeLayer() {
        super(Node.class);
    }

    @Override
    public Shape render(Node n, Graphics2D g, ScreenTransform t, StandardWorldModel world) {
        int x = t.scaleX(n.getX()) - (SIZE / 2);
        int y = t.scaleY(n.getY()) - (SIZE / 2);
        g.setColor(Color.BLACK);
        Shape shape = new Ellipse2D.Double(x, y, SIZE, SIZE);
        g.fill(shape);
        return shape;
    }
}