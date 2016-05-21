package com.business;

import br.com.caelum.vraptor.environment.Property;

import javax.inject.Inject;

public class SystemPropertiesImpl implements SystemProperties {

    @Inject
    @Property(value = "mongo.db", defaultValue = "ChiefMastermind")
    private String MONGO_DB_PROPERTY;

    @Inject
    @Property(value = "mongo.host", defaultValue = "mongodb://localhost")
    private String MONGO_HOST_PROPERTY;

    @Inject
    @Property(value = "mongo.port", defaultValue = "mongodb://localhost")
    private String MONGO_PORT_PROPERTY;

    private final int mongoPort;

    public SystemPropertiesImpl() {
        if (MONGO_PORT_PROPERTY == null) {
            throw new IllegalStateException("MongoDB port was not defined");
        } else {
            mongoPort = Integer.parseInt(MONGO_PORT_PROPERTY);
        }
    }

    @Override
    public String getMongoDbName() {
        return MONGO_DB_PROPERTY;
    }

    @Override
    public String getMongoHost() {
        return MONGO_HOST_PROPERTY;
    }

    @Override
    public int getMongoPort() {
        return mongoPort;
    }
}
