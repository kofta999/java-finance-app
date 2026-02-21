package com.kofta.app.common.repository;

import com.kofta.app.common.result.Result;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<T, ID extends Serializable> {
    Optional<T> findById(ID id);
    List<T> findAll();
    <E extends EntityNotFoundError> Result<Void, E> save(T entity);
}
