package com.business.dao;

import com.domain.Player;
import org.mongodb.morphia.Datastore;

import javax.inject.Inject;

public class PlayerDaoImpl extends DaoImpl<Player> implements PlayerDao {

    /**
     * @deprecated CDI
     */
    @Deprecated
    public PlayerDaoImpl() {
        super(null);
    }

    @Inject
    public PlayerDaoImpl(Datastore datastore) {
        super(datastore);
    }

}
