package dev.slickcollections.kiwizin.listeners;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TagChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Profile profile;
    private final Role role;

    public TagChangeEvent(Profile profile, Role role) {
        this.profile = profile;
        this.role = role;
    }

    public Profile getProfile() {
        return profile;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
