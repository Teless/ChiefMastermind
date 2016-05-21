package com.domain;

import org.bson.types.ObjectId;

public interface MasterEntity {

    ObjectId getId();

    void setId(ObjectId id);

}
