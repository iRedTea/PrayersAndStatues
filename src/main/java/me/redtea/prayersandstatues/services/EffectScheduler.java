package me.redtea.prayersandstatues.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AllArgsConstructor;
import me.redtea.prayersandstatues.PrayersAndStatues;
import me.redtea.prayersandstatues.types.User;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class EffectScheduler {
    private final PrayersAndStatues plugin;

    private final DatabaseManager databaseManager;
    private final GodsManager godsManager;

    public void init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach((p) ->  {
                User u = databaseManager.getUserData(p.getUniqueId().toString());
                System.out.println(u.getEffects());
                if(u.getEffects() != null && !u.getEffects().isEmpty()) {
                    u.getEffects().entrySet().forEach((e) -> {p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(e.getKey()),
                            20*30, e.getValue()));});
                }
            });
        }, 0L, 20*30L);
    }
}
