package rescuecore2.view;

import java.util.List;
import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/**
   A JComponent that shows a view of a world model.
 */
public class WorldModelViewer extends JComponent {
    private static final Color BACKGROUND = new Color(0x1B898D);

    private List<ViewLayer> layers;
    private List<RenderedObject> objects;

    /**
       Construct a new WorldModelViewer.
     */
    public WorldModelViewer() {
        layers = new ArrayList<ViewLayer>();
        objects = new ArrayList<RenderedObject>();
        addMouseListener(new ViewerMouseListener());
    }

    /**
       Add a view layer.
       @param layer The layer to add.
     */
    public void addLayer(ViewLayer layer) {
        layers.add(layer);
    }

    /**
       Remove a view layer.
       @param layer The layer to remove.
     */
    public void removeLayer(ViewLayer layer) {
        layers.remove(layer);
    }

    @Override
    public void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        objects.clear();
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, width, height);
        for (ViewLayer next : layers) {
            objects.addAll(next.render(g, width, height));
        }
    }

    private List<RenderedObject> getObjectsAtPoint(int x, int y) {
        List<RenderedObject> result = new ArrayList<RenderedObject>();
        for (RenderedObject next : objects) {
            if (next.getShape().contains(x, y)) {
                result.add(next);
            }
        }
        return result;
    }

    private class ViewerMouseListener implements MouseListener {
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {
            Point p = e.getPoint();
            System.out.println("Click at " + p);
            for (RenderedObject next : getObjectsAtPoint(p.x, p.y)) {
                System.out.println(next.getObject());
            }
        }
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}