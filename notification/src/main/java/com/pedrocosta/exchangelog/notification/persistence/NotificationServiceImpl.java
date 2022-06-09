package com.pedrocosta.exchangelog.notification.persistence;

import com.pedrocosta.exchangelog.base.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.utils.output.Messages;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public final class NotificationServiceImpl implements NotificationService {

    private NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification save(Notification notification) throws SaveDataException {
        Notification result = repository.save(notification);
        if (result.getId() == 0) {
            throw new SaveDataException(Messages.get("error.not.saved",
                    "notification"));
        }
        return result;
    }

    @Override
    public List<Notification> saveAll(Collection<Notification> col) throws SaveDataException {
        List<Notification> saved = repository.saveAll(col);
        List<Notification> notSaved = new ArrayList<>();

        for (Notification notif : saved) {
            if (notif.getId() == 0)
                notSaved.add(notif);
        }

        if (!notSaved.isEmpty()) {
            SaveDataException exception = new SaveDataException(
                    Messages.get("error.not.saved", "notifications"));
            exception.setData(notSaved);
            throw exception;
        }
        return saved;
    }

    @Override
    public Notification find(long id) {
        return repository.getOne(id);
    }

    @Override
    public List<Notification> findAll() {
        return repository.findAll();
    }
}
