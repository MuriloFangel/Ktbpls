package dev.slickcollections.kiwizin.thebridge.listeners.player;

import dev.slickcollections.kiwizin.listeners.TagChangeEvent;
import dev.slickcollections.kiwizin.thebridge.utils.tagger.TagUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerTagChangeListeners implements Listener {

    @EventHandler
    public void playerTagChangeEvent(TagChangeEvent event) {
        Player player = event.getProfile().getPlayer();
        TagUtils.setTag(player, event.getRole());
    }

}
