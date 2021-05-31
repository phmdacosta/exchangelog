package com.pedrocosta.exchangelog.services;

import java.util.Collection;
import java.util.List;

public interface RepositoryService<T> extends CoreService {
    ServiceResponse<T> save (T obj);
    ServiceResponse<List<T>> saveAll(Collection<T> col);
    ServiceResponse<T> find(long id);
    ServiceResponse<List<T>> findAll();
}
