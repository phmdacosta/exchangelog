package com.pedrocosta.exchangelog.notification.test;

import com.pedrocosta.exchangelog.base.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.exchangelog.notification.persistence.NotificationRepository;
import com.pedrocosta.exchangelog.notification.persistence.NotificationService;
import com.pedrocosta.exchangelog.notification.persistence.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private Notification notification;

    @Mock
    private NotificationRepository repository;
    private NotificationService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new NotificationServiceImpl(repository);

        notification = new Notification();
        notification.setId(1L);
    }

    @Test
    public void testSaveNotification_success() throws Exception {
        Notification notif = new Notification();
        when(repository.save(notif)).thenReturn(notification);

        Notification saved = service.save(notif);
        verify(repository).save(notif);

        assert saved != null;
        assert saved.getId() != 0;
    }

    @Test
    public void testSaveNotification_error() {
        Notification notif = new Notification();
        when(repository.save(notif)).thenReturn(new Notification());

        SaveDataException exception = assertThrows(SaveDataException.class, () -> {
            service.save(notif);
        });
        verify(repository).save(notif);

        String expectedMessage = "Could not register notification.";
        assert exception.getMessage().contains(expectedMessage);
    }

    @Test
    public void testSaveAllNotifications_success() throws Exception {
        List<Notification> resultList = new ArrayList<>();
        resultList.add(new Notification());
        resultList.add(new Notification());
        resultList.add(new Notification());
        for (int i = 0; i < resultList.size(); i++) {
            resultList.get(i).setId(i+1);
        }

        List<Notification> paramList = new ArrayList<>();
        paramList.add(new Notification());
        paramList.add(new Notification());
        paramList.add(new Notification());

        when(repository.saveAll(paramList)).thenReturn(resultList);

        List<Notification> savedList = service.saveAll(paramList);
        verify(repository).saveAll(paramList);

        assert savedList != null && !savedList.isEmpty();
        for (Notification n : savedList) {
            assert n.getId() != 0;
        }
    }

    @Test
    public void testSaveAllNotifications_error() {
        List<Notification> resultList = new ArrayList<>();
        resultList.add(new Notification());
        resultList.add(new Notification());
        resultList.add(new Notification());

        List<Notification> paramList = new ArrayList<>();
        paramList.add(new Notification());
        paramList.add(new Notification());
        paramList.add(new Notification());

        when(repository.saveAll(paramList)).thenReturn(resultList);

        SaveDataException exception = assertThrows(SaveDataException.class, () -> {
            service.saveAll(paramList);
        });
        verify(repository).saveAll(paramList);

        String expectedMessage = "Could not register notifications.";

        assert exception.getMessage().contains(expectedMessage);
        assert resultList.equals(exception.getData());
    }

    @Test
    public void testGetNotification_success() {
        when(repository.getOne(1L)).thenReturn(notification);
        Notification gotNotif = service.find(1L);
        verify(repository).getOne(1L);
        assert gotNotif != null;
    }

    @Test
    public void testGetAllNotifications_success() {
        List<Notification> list = new ArrayList<>();
        list.add(notification);
        when(repository.findAll()).thenReturn(list);
        List<Notification> gotList = service.findAll();
        verify(repository).findAll();
        assert gotList != null && !gotList.isEmpty();
    }
}
