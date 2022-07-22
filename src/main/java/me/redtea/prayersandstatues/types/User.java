package me.redtea.prayersandstatues.types;


import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.Getter;
import lombok.NonNull;
import me.redtea.prayersandstatues.services.GodsManager;

import java.util.HashMap;
import java.util.Map;

@Getter
public class User {

    private final String uuid;

    private HashMap<String, Map<String, Integer>> data;

    private GodsManager godsManager;

    @AssistedInject
    public User(@Assisted String uuid, @Assisted HashMap<String, Map<String, Integer>> data, GodsManager godsManager) {
        this.uuid = uuid;
        this.data = data;
        this.godsManager = godsManager;
    }

    public void setLevel(@NonNull String god, @NonNull String tree, int level) {
        data.putIfAbsent(god, new HashMap<>());
        data.get(god).put(tree, level);
    }

    //EFFECT:LVL
    public HashMap<String, Integer> getEffects() {
        HashMap<String, Integer> result = new HashMap<>();
        data.forEach((key1, value1) -> value1.forEach((key, value) -> godsManager.getGodByName(key1).getTrees().forEach((tree)
                -> {if (tree.getTag().equalsIgnoreCase(key)) { tree.getPrayers().forEach((p) -> { if (p.getLevel() == value)
                    result.put(p.getEffect().split(":")[0], Integer.parseInt(p.getEffect().split(":")[1])); }); } })));
        return result;
    }
}