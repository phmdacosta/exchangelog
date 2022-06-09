package com.pedrocosta.exchangelog.base;

import com.pedrocosta.exchangelog.base.exceptions.SaveDataException;

import java.util.Collection;
import java.util.List;

public interface RepositoryService<T> extends CoreService {
    T save (T obj) throws SaveDataException;
    List<T> saveAll(Collection<T> col) throws SaveDataException;
    T find(long id);
    List<T> findAll();
}
