package dev.slickcollections.kiwizin.cmd;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TagCommand extends Commands {

    public TagCommand() {
        super("tag");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando é exclusivo para jogadores.");
            return;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
            if (profile.playingGame()) {
                player.sendMessage("§cSó é possível alterar a tag no lobby.");
                return;
            }

            if (args.length == 0) {
                player.spigot().sendMessage(getTagMessage(player));
                return;
            }

            String tag = args[0];
            Role findRole = Role.getRoleByName(tag);
            player.sendMessage("§aTag atualizada com sucesso para " + findRole.getName() + "§a!");
            profile.getTagContainer().updateTag(profile, findRole);
        }
    }

    private TextComponent getTagMessage(Player player) {
        TextComponent principal = new TextComponent("§aSuas tags disponíveis: ");
        List<Role> roles = Role.listRoles().stream().filter(role -> player.hasPermission(role.getPermission())).collect(Collectors.toList());
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            TextComponent roleComponent = new TextComponent();
            if (i == 0) {
                roleComponent.setText(role.getName());
            } else {
                roleComponent.setText("§7, " + role.getName());
            }

            roleComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + StringUtils.stripColors(role.getName())));
            roleComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§fPreview: " + role.getPrefix() + player.getName())));
            principal.addExtra(roleComponent);
        }

        return principal;
    }
}
