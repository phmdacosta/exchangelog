package com.pedrocosta.exchangelog.services;

import java.util.Collection;
import java.util.List;

public interface RepositoryService<T> extends CoreService {
    public T save (T obj);
    public List<T> saveAll(Collection<T> col);
    public T find(long id);
    public List<T> findAll();
}
