package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;

import java.util.Collection;
import java.util.List;

public interface RepositoryService<T> extends CoreService {
    T save (T obj) throws SaveDataException;
    List<T> saveAll(Collection<T> col) throws SaveDataException;
    T find(long id);
    List<T> findAll();
}
