package com.pedrocosta.exchangelog.models;

import com.sun.istack.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "batch_job")
public class ScheduledJob implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "cron")
    private String scheduleCron;
    @Column(name = "exec_weekend", nullable = false)
    private boolean execWeekend;
    @Column(name = "exec_day")
    private Date execDay;
    @Column(name = "exec_time")
    private String execTime;
    private boolean enabled;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<BatchJobParameter> parameters;
    @OneToOne(targetEntity = ScheduledJob.class)
    @JoinColumn(name = "next_job_id")
    private Long nextJobId;

    public ScheduledJob() {
        this.enabled = true;
        this.parameters = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public ScheduledJob setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ScheduledJob setName(String name) {
        this.name = name;
        return this;
    }

    public String getScheduleCron() {
        return scheduleCron;
    }

    public ScheduledJob setScheduleCron(String scheduleCron) {
        this.scheduleCron = scheduleCron;
        return this;
    }

    public boolean isExecWeekend() {
        return execWeekend;
    }

    public ScheduledJob setExecWeekend(boolean execWeekend) {
        this.execWeekend = execWeekend;
        return this;
    }

    public Date getExecDay() {
        return execDay;
    }

    public ScheduledJob setExecDay(Date execDay) {
        this.execDay = execDay;
        return this;
    }

    public String getExecTime() {
        return execTime;
    }

    public ScheduledJob setExecTime(String execTime) {
        this.execTime = execTime;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ScheduledJob setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public List<BatchJobParameter> getParameters() {
        return parameters;
    }

    public ScheduledJob setParameters(List<BatchJobParameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    @Nullable
    public Long getNextJobId() {
        return nextJobId;
    }

    public ScheduledJob setNextJobId(Long nextJobId) {
        this.nextJobId = nextJobId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledJob batchJob = (ScheduledJob) o;
        return Objects.equals(name, batchJob.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, scheduleCron, parameters);
    }

    @Override
    protected ScheduledJob clone() throws CloneNotSupportedException {
        ScheduledJob cloned = (ScheduledJob) super.clone();
        cloned.setId(this.id);
        cloned.setName(this.name);
        cloned.setScheduleCron(this.scheduleCron);
        cloned.setExecWeekend(this.execWeekend);
        cloned.setExecDay((Date) this.execDay.clone());
        cloned.setExecTime(this.execTime);
        cloned.setEnabled(this.enabled);
        for (BatchJobParameter parameter : this.parameters) {
            cloned.getParameters().add(parameter.clone());
        }
        return cloned;
    }

    @Override
    public String toString() {
        return "ScheduledJob{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scheduleCron='" + scheduleCron + '\'' +
                ", execWeekend=" + execWeekend +
                ", execDay=" + execDay +
                ", execTime='" + execTime + '\'' +
                ", enabled=" + enabled +
                ", parameters=" + parameters +
                ", nextJobId=" + nextJobId +
                '}';
    }
}
