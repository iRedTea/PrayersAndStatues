package me.redtea.prayersandstatues.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.NonNull;
import me.redtea.prayersandstatues.tools.Config;
import me.redtea.prayersandstatues.types.God;

import java.util.ArrayList;

@Singleton
public class GodsManager {

    @Inject
    public GodsManager(Config config) {
        this.gods = config.getGods();
    }

    @Getter
    private final ArrayList<God> gods;

    public God getGodByName(@NonNull String name) {
        for(God g : gods) {
            if(g.getName().equalsIgnoreCase(name)) return g;
        }
        throw new RuntimeException("God not found");
    }


}
