package nu.eats.ui.store;

import nu.eats.common.messaging.Topic;
import nu.eats.domain.store.StoreItem;

public final class StoreState {
    public static final Topic<StoreItem> ITEM_ADDED = new Topic<>();
    public static final Topic<StoreItem> ITEM_SELECTED = new Topic<>();

    private StoreState() {
    }
}
