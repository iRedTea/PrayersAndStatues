package me.redtea.prayersandstatues.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.redtea.prayersandstatues.PrayersAndStatues;
import me.redtea.prayersandstatues.PrayersCore;
import me.redtea.prayersandstatues.tools.Config;
import org.bukkit.Material;

import java.util.HashMap;

@Singleton
public class ItemLocalizeUtil {
    private HashMap<String, String> map; //material, localize

    @Inject
    public ItemLocalizeUtil(Config config) {
        map = config.getItemsLocalize();
    }

    public String getLocalizedName(String material) {
        if(map.containsKey(material)) return map.get(material);
        else return material;
    }
}
