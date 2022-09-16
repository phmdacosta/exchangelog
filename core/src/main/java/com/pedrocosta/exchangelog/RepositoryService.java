package com.pedrocosta.exchangelog;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import javassist.NotFoundException;

import java.util.Collection;
import java.util.List;

/**
 * Service interface with all must have methods for data access.
 *
 * @author Pedro H. M . da Costa
 * @since 1.0
 */
public interface RepositoryService<T> extends CoreService {
    T save (T obj) throws SaveDataException;
    List<T> saveAll(Collection<T> col) throws SaveDataException;
    T find(long id) throws NotFoundException;
    List<T> findAll();
}
