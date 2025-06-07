package com.uab.taller.store.service.interfaces;

import java.util.List;

public interface IGenericRepository<T> {
    List<T> findAll();

    T save(T entity);

    T findById(Long id);

    void deleteById(Long id);

    T update(T entity);
}
