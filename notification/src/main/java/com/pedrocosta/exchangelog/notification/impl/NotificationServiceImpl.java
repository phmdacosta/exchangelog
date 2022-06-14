package com.pedrocosta.exchangelog.notification.impl;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.exchangelog.notification.NotificationService;
import com.pedrocosta.exchangelog.notification.persistence.NotificationRepository;
import com.pedrocosta.exchangelog.notification.sender.NotificationSender;
import com.pedrocosta.exchangelog.notification.sender.NotificationSenderFactory;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public final class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationSenderFactory senderFactory;
    private final Environment env;

    public NotificationServiceImpl(NotificationRepository repository, NotificationSenderFactory senderFactory, Environment env) {
        this.repository = repository;
        this.senderFactory = senderFactory;
        this.env = env;
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
    public Notification saveAndNotify(Notification notification) throws SaveDataException {
        Notification saved = this.save(notification);
        this.notify(notification);
        return saved;
    }

    @Override
    public List<Notification> saveAndNotifyAll(List<Notification> notifications) throws SaveDataException {
        List<Notification> result = new ArrayList<>();
        SaveDataException exception = null;
        try {
            result.addAll(this.saveAll(notifications));
        } catch (SaveDataException e) {
            exception = e;
        }

        //Try to send
        for (Notification toSend : result) {
            if (toSend.getId() != 0)
                this.notify(toSend);
        }

        if (exception != null)
            throw exception;

        return result;
    }

    @Override
    public Notification find(long id) {
        return repository.getOne(id);
    }

    @Override
    public List<Notification> findAll() {
        return repository.findAll();
    }

    @Override
    public void notify(Notification notification) {
        if (Boolean.parseBoolean(env.getProperty("send.notification"))) {
            NotificationSender sender = senderFactory.create(
                    notification);
            try {
                sender.send(notification);
                notification.setSent(true);
            } catch (Exception e) {
                notification.setSent(false);
            }

            try {
                this.save(notification);
            } catch (SaveDataException e) {
                Log.error(this, Messages.get("error.not.saved.after.send",
                        String.valueOf(notification.getId()),
                        notification.isSent() ? "sent" : "not sent"));
                Log.error(this, e);
            }
        }
    }
}
