package com.pedrocosta.exchangelog.services;

import java.util.Collection;
import java.util.List;

public interface RepositoryService<T> extends CoreService {
    public ServiceResponse<T> save (T obj);
    public ServiceResponse<List<T>> saveAll(Collection<T> col);
    public ServiceResponse<T> find(long id);
    public ServiceResponse<List<T>> findAll();
}
