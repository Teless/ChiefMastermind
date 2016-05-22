package com.business;

import org.bson.types.ObjectId;

public class GameKeyGenerator {

    public String generateGameKey() {
        return new ObjectId().toString();
    }

}
