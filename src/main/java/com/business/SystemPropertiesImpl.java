package com.business;

import br.com.caelum.vraptor.environment.Property;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.SimpleDateFormat;

@ApplicationScoped
public class SystemPropertiesImpl implements SystemProperties {

    @Inject
    @Property(value = "mongo.db", defaultValue = "ChiefMastermind")
    private String MONGO_DB_PROPERTY;

    @Inject
    @Property(value = "mongo.host", defaultValue = "localhost")
    private String MONGO_HOST_PROPERTY;

    @Inject
    @Property(value = "mongo.port", defaultValue = "27017")
    private String MONGO_PORT_PROPERTY;

    private int mongoPort;

    @PostConstruct
    public void postConstructor() {
        mongoPort = Integer.parseInt(MONGO_PORT_PROPERTY);
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
