package com.business.dao;

import com.domain.MasterEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class DaoImpl<T extends MasterEntity> implements Dao<T> {

    protected final Datastore datastore;

    public DaoImpl(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public ObjectId save(T entity) {
        ObjectId id = (ObjectId) datastore.save(entity).getId();

        entity.setId(id);

        return id;
    }

    @Override
    public T find(String id) {
        List<T> result = datastore.createQuery(getGenericType())
                .field("id").equal(new ObjectId(id))
                .asList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<T> list() {
        return datastore.createQuery(getGenericType()).asList();
    }

    private Class<T> getGenericType() {
        return (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
