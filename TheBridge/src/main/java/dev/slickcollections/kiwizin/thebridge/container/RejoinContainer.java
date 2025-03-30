package dev.slickcollections.kiwizin.thebridge.container;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.concurrent.TimeUnit;

public class RejoinContainer extends AbstractContainer {

    public RejoinContainer(DataContainer dataContainer) {
        super(dataContainer);
        JSONObject infos = this.dataContainer.getAsJsonObject();
        if (infos.isEmpty()) {
            infos.put("map", "");
            infos.put("time", 0L);
            this.dataContainer.set(infos.toJSONString());
        }
    }

    public TheBridge getRejoinGame(Player player) {
        JSONObject infos = this.dataContainer.getAsJsonObject();
        String map = infos.get("map").toString();
        if (!map.isEmpty()) {
            long remainingTime = (long) infos.get("time");
            if (remainingTime - System.currentTimeMillis() > 0) {
                TheBridge game = TheBridge.getByWorldName(map);
                if (game != null && game.getState().equals(GameState.EMJOGO) && game.listTeams().stream().anyMatch(theBridgeTeam -> theBridgeTeam.hasMember(player))) {
                    return game;
                }
            }
        }

        return null;
    }

    public void setRejoinGame(TheBridge game) {
        JSONObject infos = this.dataContainer.getAsJsonObject();
        infos.put("map", game.getConfig().getBridgeCube().getWorld());
        infos.put("time", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3));
        this.dataContainer.set(infos.toJSONString());
    }

    public void clearRejoinGame() {
        JSONObject infos = this.dataContainer.getAsJsonObject();
        infos.put("map", "");
        infos.put("time", 0L);
        this.dataContainer.set(infos.toJSONString());
    }
}
