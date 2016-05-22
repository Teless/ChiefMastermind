package com.business.dao;

import com.business.SystemProperties;
import com.domain.Player;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class DatastoreFactory {

    private final Morphia morphia;
    private final MongoClient mongoClient;
    private final SystemProperties systemProperties;

    /**
     * @deprecated CDI
     */
    @Deprecated
    public DatastoreFactory() {
        this(null);
    }

    @Inject
    public DatastoreFactory(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;

        if (systemProperties == null) {
            morphia = null;
            mongoClient = null;
        } else {
            morphia = new Morphia();
            morphia.mapPackageFromClass(Player.class);

            mongoClient = new MongoClient(systemProperties.getMongoHost(), systemProperties.getMongoPort());
            Datastore datastore = morphia.createDatastore(mongoClient, systemProperties.getMongoDbName());
            datastore.ensureIndexes();
        }
    }

    @Produces
    @RequestScoped
    public Datastore getDatastore() {
        return morphia.createDatastore(mongoClient, systemProperties.getMongoDbName());
    }

}
