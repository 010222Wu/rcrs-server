package rescuecore2.standard.components;

import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.components.AbstractViewer;

/**
   Abstract base class for standard viewers.
*/
public abstract class StandardViewer extends AbstractViewer<StandardWorldModel> {
    @Override
    protected StandardWorldModel createWorldModel() {
        return new StandardWorldModel();
    }

    @Override
    protected void postConnect() {
        super.postConnect();
        model.index(config.getIntValue(StandardWorldModel.MESH_SIZE_KEY, StandardWorldModel.DEFAULT_MESH_SIZE));
    }
}