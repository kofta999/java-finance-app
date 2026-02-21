package com.kofta.app.common.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<T, ID extends Serializable> {
    Optional<T> findById(ID id);
    List<T> findAll();
    void save(T entity);
}
