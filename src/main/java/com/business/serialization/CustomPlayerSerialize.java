package com.business.serialization;

import com.domain.Player;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CustomPlayerSerialize implements JsonSerializer<Player> {

    @Override
    public JsonElement serialize(Player player, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(player.getName());
    }

}
