package com.pedrocosta.exchangelog.notification;

import com.pedrocosta.springutils.exception.NotSupportedException;
import com.pedrocosta.springutils.jsonmanager.adapter.UtilsTypeAdapter;
import com.pedrocosta.springutils.jsonmanager.adapter.annotation.JsonAdapter;
import com.pedrocosta.springutils.jsonmanager.stream.JsonReader;
import com.pedrocosta.springutils.jsonmanager.stream.JsonWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@JsonAdapter(type = Notification.class)
public class NotificationAdapter extends UtilsTypeAdapter<Notification> {
    protected final String ID = "id";
    protected final String MEAN = "mean";
    protected final String FROM = "from";
    protected final String TO = "to";
    protected final String SUBJECT = "subject";
    protected final String BODY = "body";

    /**
     * {
     *     "id":1,
     *     "mean":"EMAIL",
     *     "from":"from@mail.com",
     *     "to":["to_01@mail.com", "to_02@mail.com", "to_03@mail.com"],
     *     "subject":"My subject",
     *     "body":"My e-mail body"
     * }
     */
    @Override
    public void write(JsonWriter writer, Notification obj) throws IOException, NotSupportedException {
        writer.add(ID, obj.getId());
        writer.add(MEAN, obj.getMean());
        writer.add(FROM, obj.getFrom());
        writer.addJson(TO, obj.getTo(), getJsonUtils());
        writer.add(SUBJECT, obj.getSubject());
        writer.add(BODY, obj.getMessage());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Notification read(JsonReader reader) throws IOException, NotSupportedException {
        Notification notification = new Notification();
        Long id = reader.get(ID, Long.class);
        notification.setId(id == null ? 0 : id);
        notification.setMean(reader.get(MEAN, String.class));
        notification.setFrom(reader.get(FROM, String.class));
        notification.setTo(reader.getSet(TO, getJsonUtils(), String.class));
//        notification.setTo(getJsonUtils().fromJson(reader.get(TO, String.class), Set.class));
        notification.setSubject(reader.get(SUBJECT, String.class));
        notification.setMessage(reader.get(BODY, String.class));
        return notification;
    }
}
