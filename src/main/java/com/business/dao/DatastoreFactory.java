package com.business.dao;

import com.business.SystemProperties;
import com.domain.Player;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class DatastoreFactory {

    private final Datastore datastore;

    public DatastoreFactory() {
        this(null);
    }

    @Inject
    public DatastoreFactory(SystemProperties systemProperties) {
        if (systemProperties == null) {
            datastore = null;
        } else {
            Morphia morphia = new Morphia();
            morphia.mapPackageFromClass(Player.class);

            MongoClient mongoClient = new MongoClient(systemProperties.getMongoHost(), systemProperties.getMongoPort());
            datastore = morphia.createDatastore(mongoClient, systemProperties.getMongoDbName());
            datastore.ensureIndexes();
        }
    }

    @Produces
    @ApplicationScoped
    public Datastore getDatastore() {
        return datastore;
    }

}
