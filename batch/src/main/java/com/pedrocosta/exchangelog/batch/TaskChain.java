package com.pedrocosta.exchangelog.batch;

import java.util.Collection;
import java.util.LinkedHashSet;

public class TaskChain extends LinkedHashSet<ScheduledTask<?, ?>> {

    private String name;

    public void setName(String name) {
        this.name = name + "_chain";
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean add(ScheduledTask<?, ?> obj) {
        if (getName() == null) {
            setName(obj.getJobName());
        }

        return super.add(obj);
    }

    @Override
    public boolean addAll(Collection<? extends ScheduledTask<?, ?>> col) {
        if (col == null) {
            return false;
        }

        if (!col.isEmpty()) {
            ScheduledTask<?, ?> first = col.stream().findFirst().get();

            if (getName() == null) {
                setName(first.getJobName());
            }
        }

        return super.addAll(col);
    }
}
