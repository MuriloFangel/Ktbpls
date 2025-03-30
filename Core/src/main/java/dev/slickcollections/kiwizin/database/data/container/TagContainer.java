package dev.slickcollections.kiwizin.database.data.container;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.listeners.TagChangeEvent;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;

public class TagContainer extends AbstractContainer {

    public TagContainer(DataContainer dataContainer) {
        super(dataContainer);
    }

    public Role getSelectedRole() {
        return Role.getRoleByName(this.dataContainer.getAsString());
    }

    public void updateTag(Profile profile, Role role) {
        Bukkit.getPluginManager().callEvent(new TagChangeEvent(profile, role));
        this.dataContainer.set(StringUtils.stripColors(role.getName()));
    }
}
