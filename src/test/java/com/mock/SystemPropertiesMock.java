package com.mock;

import com.business.SystemProperties;

public class SystemPropertiesMock implements SystemProperties {

    @Override
    public String getMongoDbName() {
        return "ChiefMastermindTest";
    }

    @Override
    public String getMongoHost() {
        return "localhost";
    }

    @Override
    public int getMongoPort() {
        return 27017;
    }

}
