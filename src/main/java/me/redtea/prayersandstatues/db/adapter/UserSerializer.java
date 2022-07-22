package me.redtea.prayersandstatues.db.adapter;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.redtea.prayersandstatues.services.IUserFactory;
import me.redtea.prayersandstatues.types.User;

import java.lang.reflect.Type;
import java.util.*;

@RequiredArgsConstructor
public class UserSerializer implements JsonSerializer<User>, JsonDeserializer<User> {
    private final IUserFactory factory;

    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if(jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            HashMap<String, Map<String, Integer>> data = new HashMap<>();
            String uuid = null;
            ArrayList<String> effects = null;

            if(jsonObject.has("uuid")) {
                uuid = jsonObject.get("uuid").getAsString();
            }

            try {
                if(jsonObject.has("gods")) {
                    val token = new TypeToken<HashMap<String, Map<String, Integer>>>() {};
                    data = context.deserialize(jsonObject.get("gods").getAsJsonObject(), token.getType());
                }
            } catch (Throwable e) {
                data = new HashMap<>();
            }


            return factory.createUser(uuid, data);
        }
        return null;
    }

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext context) {
        if(type != null) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.add("gods", context.serialize(user.getData()));

            return jsonObject;
        }
        return null;
    }
}
