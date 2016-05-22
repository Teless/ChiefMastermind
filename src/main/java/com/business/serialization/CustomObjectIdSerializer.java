package com.business.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

public class CustomObjectIdSerializer implements JsonSerializer<ObjectId> {

    @Override
    public JsonElement serialize(ObjectId id, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(id.toString());
    }

}
