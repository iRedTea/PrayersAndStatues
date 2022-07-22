package me.redtea.prayersandstatues.tools;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import me.redtea.prayersandstatues.PrayersAndStatues;
import me.redtea.prayersandstatues.types.God;
import me.redtea.prayersandstatues.types.Prayer;
import me.redtea.prayersandstatues.types.Statue;
import me.redtea.prayersandstatues.types.UpgradeTree;
import me.redtea.prayersandstatues.services.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Singleton
public class Config {
    @Getter
    private ArrayList<God> gods;

    @Getter
    private HashMap<String, String> itemsLocalize;

    @Inject
    public Config(PrayersAndStatues plugin, TimeUtil timeUtil) {
        load(plugin, timeUtil);
    }

    public void load(PrayersAndStatues plugin, TimeUtil timeUtil) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        itemsLocalize = new HashMap<>();
        plugin.getConfig().getStringList("itemsLocalization").forEach((s) -> {
            itemsLocalize.put(s.split(":")[0], s.split(":")[1]);
        });

        gods = new ArrayList<>();

        ConfigurationSection godsSection = plugin.getConfig().getConfigurationSection("gods");
        assert godsSection != null;
        godsSection.getKeys(false).forEach((god) -> {
            Statue statue = new Statue(
                    new Location(Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("gods." + god +
                            ".statue.world"))),
                            plugin.getConfig().getInt("gods." + god + ".statue.x"),
                            plugin.getConfig().getInt("gods." + god + ".statue.y"),
                            plugin.getConfig().getInt("gods." + god + ".statue.z")).getBlock(),
                    plugin.getConfig().getInt("gods." + god + ".statue.radius")
            );

            long cooldown = timeUtil.getTicksFromString(Objects.requireNonNull(plugin.getConfig().getString("gods."
                    + god + ".prayerCoolDown")));

            //god - name

            ArrayList<UpgradeTree> trees = new ArrayList<>();
            ConfigurationSection treesSection = plugin.getConfig().getConfigurationSection("gods." + god + ".trees");
            assert treesSection != null;
            treesSection.getKeys(false).forEach((tree) -> {
                //TODO test this
                String treeName = treesSection.getString(tree + ".name");
                String treeIcon = treesSection.getString(tree + ".icon");
                int treeSlot = treesSection.getInt(tree + ".slot");

                ArrayList<Prayer> prayers = new ArrayList<>();
                ConfigurationSection prayersSection = treesSection.getConfigurationSection(tree + ".levels");
                assert prayersSection != null;
                prayersSection.getKeys(false).forEach((prayer) -> {
                    int level = Integer.parseInt(prayer);
                    String effect = prayersSection.getString(prayer + ".EFFECT");
                    HashMap<String, Integer> items = new HashMap<>();
                    List<String> itemsStrList = prayersSection.getStringList(prayer + ".ITEMS");
                    for(String s : itemsStrList) {
                        items.put(s.split(", ")[0], Integer.parseInt(s.split(", ")[1]));
                    }
                    prayers.add(new Prayer(level, items, effect));
                });

                trees.add(new UpgradeTree(tree, treeName, treeIcon, treeSlot, prayers));
            });

            gods.add(new God(god, statue, trees, cooldown));
        });
    }
}
