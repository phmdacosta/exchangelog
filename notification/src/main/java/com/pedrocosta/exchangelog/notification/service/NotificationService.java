package com.pedrocosta.exchangelog.notification.service;

import com.pedrocosta.exchangelog.RepositoryService;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.notification.Notification;

import java.util.List;

public interface NotificationService extends RepositoryService<Notification> {
    Notification saveAndNotify(Notification notification) throws SaveDataException;
    List<Notification> saveAndNotifyAll(List<Notification> notifications) throws SaveDataException;
    void notify(Notification notification);
}
