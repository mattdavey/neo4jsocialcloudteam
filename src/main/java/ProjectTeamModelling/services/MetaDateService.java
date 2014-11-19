package ProjectTeamModelling.services;

import java.util.HashMap;

public class MetaDateService {
    class ComponetStore {
        HashMap<String, ComponetStore> storage = new HashMap<>();
    }

    final private ComponetStore rootStorage = new ComponetStore();

    public void add(final String metadataPath) {
        final String[] pathComponents = metadataPath.split("//");

        ComponetStore parent = rootStorage;
        for (final String component : pathComponents) {
            ComponetStore store = parent.storage.get(component);
            if (store == null) {
                store = new ComponetStore();
                parent.storage.put(component, store);
            }

            parent = store;
        }
    }

    public boolean validateMetaData(final String metadataPath) {
        final String[] pathComponents = metadataPath.split("//");

        ComponetStore parent = rootStorage;
        for (final String component : pathComponents) {
            ComponetStore store = parent.storage.get(component);
            if (store == null) {
                return false;
            }

            parent = store;
        }

        return true;
    }
}
