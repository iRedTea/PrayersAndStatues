package me.redtea.prayersandstatues.db.impl.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import me.redtea.prayersandstatues.PrayersAndStatues;
import me.redtea.prayersandstatues.db.IDatabase;
import me.redtea.prayersandstatues.db.adapter.UserSerializer;
import me.redtea.prayersandstatues.services.IUserFactory;
import me.redtea.prayersandstatues.types.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonData implements IDatabase {

    private final Gson GSON;

    private final @NonNull PrayersAndStatues plugin;

    private Map<String, User> dataMap = new HashMap<>();

    @Inject
    @SneakyThrows
    public JsonData(@NonNull PrayersAndStatues plugin, IUserFactory userFactory) {
        this.plugin = plugin;
        GSON = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserSerializer(userFactory))
                .setPrettyPrinting()
                .create();

        File file = new File(plugin.getDataFolder(), "database.json");

        if(file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {
                val token = new TypeToken<Map<String, User>>(){};

                this.dataMap = GSON.fromJson(fileReader, token.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dataMap = new HashMap<>();
            file.createNewFile();
            saveData();
        }
    }

    @Override
    public User getData(@NonNull String uuid) {
        this.dataMap.putIfAbsent(uuid, null);
        return this.dataMap.get(uuid);
    }

    @Override
    public void saveData(@NonNull String uuid, User value) {
        this.dataMap.putIfAbsent(uuid, null);
        this.dataMap.put(uuid, value);
    }

    @Override
    public void saveData() {
        File file = new File(this.plugin.getDataFolder(), "database.json");
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(this.dataMap, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
