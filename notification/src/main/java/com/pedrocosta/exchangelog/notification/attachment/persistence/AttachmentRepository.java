package com.pedrocosta.exchangelog.notification.attachment.persistence;

import com.pedrocosta.exchangelog.notification.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
