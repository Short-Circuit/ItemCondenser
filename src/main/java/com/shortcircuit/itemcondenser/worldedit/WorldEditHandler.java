package com.shortcircuit.itemcondenser.worldedit;

/* 
 * @author ShortCircuit908
 */
import com.shortcircuit.itemcondenser.ItemCondenser;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.eventbus.Subscribe;
 
public class WorldEditHandler {
    private ItemCondenser plugin;
    public WorldEditHandler(ItemCondenser plugin) {
        this.plugin = plugin;
    }
    @Subscribe
    public void wrapForLogging(EditSessionEvent event) {
        Actor actor = event.getActor();
        if (actor != null && actor.isPlayer()) {
            event.setExtent(new WorldEditListener(actor, event.getExtent(), plugin));
        }
    }
}
