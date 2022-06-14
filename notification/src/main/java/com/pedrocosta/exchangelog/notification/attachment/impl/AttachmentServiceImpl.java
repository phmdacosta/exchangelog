package com.pedrocosta.exchangelog.notification.attachment.impl;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.notification.attachment.Attachment;
import com.pedrocosta.exchangelog.notification.attachment.AttachmentService;
import com.pedrocosta.exchangelog.notification.attachment.persistence.AttachmentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public final class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository repository;

    public AttachmentServiceImpl(AttachmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Attachment save(Attachment attachment) throws SaveDataException {
        return repository.save(attachment);
    }

    @Override
    public List<Attachment> saveAll(Collection<Attachment> col) throws SaveDataException {
        return repository.saveAll(col);
    }

    @Override
    public Attachment find(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Attachment> findAll() {
        return repository.findAll();
    }
}
