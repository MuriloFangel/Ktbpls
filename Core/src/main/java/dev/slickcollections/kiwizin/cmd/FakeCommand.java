package dev.slickcollections.kiwizin.cmd;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.Manager;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.fake.FakeManager;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static dev.slickcollections.kiwizin.player.fake.FakeManager.ALEX;
import static dev.slickcollections.kiwizin.player.fake.FakeManager.STEVE;

public class FakeCommand extends Commands {

  public FakeCommand() {
    super("fake", "faker", "fakel");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }

    Player player = (Player) sender;
    if (!player.hasPermission("kcore.cmd.fake") || (label.equalsIgnoreCase("fakel") && !player.hasPermission("kcore.cmd.fakelist"))) {
      player.sendMessage("§cSomente YouTuber ou superior podem executar este comando.");
      return;
    }

    Profile profile = Profile.getProfile(player.getName());
    if (label.equalsIgnoreCase("fake")) {
      if (profile != null && profile.playingGame()) {
        player.sendMessage("§cVocê não pode utilizar este comando no momento.");
        return;
      }

      if (FakeManager.getRandomNicks().stream().noneMatch(FakeManager::isUsable)) {
        player.sendMessage(" \n §c§lALTERAR NICKNAME\n \n §cNenhum nickname está disponível para uso no momento.\n ");
        return;
      }

      if (args.length > 0) {
        String fakeName = args[0];
        FakeManager.applyFake(player, fakeName, "Membro", "you");
      } else {
        KConfig config = Core.getInstance().getConfig("utils");
        FakeManager.applyFake(player, config.getStringList("fake.randoms").get(ThreadLocalRandom.current().nextInt(config.getStringList("fake.randoms").size())), "Membro", "you");
      }
    } else if (label.equalsIgnoreCase("faker")) {
      if (profile != null && profile.playingGame()) {
        player.sendMessage("§cVocê não pode utilizar este comando no momento.");
        return;
      }

      if (!FakeManager.isFake(player.getName())) {
        player.sendMessage("§cVocê não está utilizando um nickname falso.");
        return;
      }

      FakeManager.removeFake(player);
    } else {
      List<String> nicked = FakeManager.listNicked();
      StringBuilder sb = new StringBuilder();
      for (int index = 0; index < nicked.size(); index++) {
        sb.append("§c").append(nicked.get(index)).append(" §fé na verdade ").append("§akcorefakereal:").append(nicked.get(index)).append(index + 1 == nicked.size() ? "" : "\n");
      }

      nicked.clear();
      if (sb.length() == 0) {
        sb.append("§cNão há nenhum usuário utilizando um nickname falso.");
      }

      player.sendMessage(" \n§eLista de nicknames falsos:\n \n" + sb + "\n ");
    }
  }
}
