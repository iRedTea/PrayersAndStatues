package me.redtea.prayersandstatues.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AllArgsConstructor;
import me.redtea.prayersandstatues.services.DatabaseManager;
import me.redtea.prayersandstatues.services.GodsManager;
import me.redtea.prayersandstatues.services.GuiManager;
import me.redtea.prayersandstatues.services.IUserFactory;
import me.redtea.prayersandstatues.types.God;
import me.redtea.prayersandstatues.types.User;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class PlayerHandler implements Listener {
    private final GodsManager godsManager;

    private final GuiManager guiManager;

    private final DatabaseManager databaseManager;

    private final IUserFactory factory;

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            for(God god : godsManager.getGods()) {
                if(god.getStatue().block().getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
                    int radius = god.getStatue().radius();
                    final Block b = god.getStatue().block();
                    int dist = (int) player.getLocation().distance(b.getLocation());
                    if(dist <= radius){
                        guiManager.openGodGui(player, god);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        User user = databaseManager.getUserData(event.getPlayer().getUniqueId().toString());
        if(user == null) {
            databaseManager.insertUserData(factory.createUser(event.getPlayer().getUniqueId().toString(), new HashMap<>()));
        }
    }
}
