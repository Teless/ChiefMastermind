package com.business.dao;

import com.business.SystemProperties;
import com.mock.SystemPropertiesMock;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(CdiRunner.class)
@AdditionalClasses(SystemPropertiesMock.class)
public class DatastoreFactoryTest {

    @Inject
    private SystemProperties systemProperties;

    @Test
    public void testNewInstance() throws Exception {
        new DatastoreFactory();
    }

    @Test
    public void testGetDatastore() throws Exception {
        DatastoreFactory datastoreFactory = new DatastoreFactory(systemProperties);

        Datastore datastore = datastoreFactory.getDatastore();

        String dbName = datastore.getDB().getName();

        assertEquals(systemProperties.getMongoDbName(), dbName);
    }

}