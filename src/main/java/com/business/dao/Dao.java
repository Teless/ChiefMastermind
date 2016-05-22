package com.business.dao;

import com.domain.MasterEntity;
import org.bson.types.ObjectId;

import java.util.List;

public interface Dao<T extends MasterEntity> {

    ObjectId save(T entity);

    T find(String id);

    List<T> list();

    boolean remove(ObjectId id);

}
