package com.pedrocosta.exchangelog.batch;

import com.pedrocosta.springutils.PackageUtils;
import com.pedrocosta.springutils.output.Log;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * Factory that creates a instance of {@link ScheduledTask}.
 *
 * @param <T> Subtype of {@link ScheduledTask}
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Component
public class JobFactory<T extends ScheduledTask<?,?>> {

    private final ApplicationContext context;

    public JobFactory(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Create a new instance of scheduled job based on class parameter.
     *
     * @param clazz It can be service's class itself or any other class of the same prefix
     *
     * @return {@link ScheduledTask} instance.
     */
    public T create(Class<? extends T> clazz) {
        T scheduler = null;

        try {
            scheduler = clazz.getConstructor(new Class[0]).newInstance();
            scheduler.setContext(context)
                    .setSchedBatchJob(new ScheduledJob());
        } catch (Exception e) {
            Log.error(this, e);
        }

        return scheduler;
    }

    /**
     * Create a new instance of scheduled job based on parameter.
     *
     * @param name Name of te task.
     */
    public T create(String name) {
        T scheduler = null;

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(com.pedrocosta.exchangelog.batch.annotations.ScheduledTask.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(getPackageName())) {
            if (bd.getBeanClassName() == null) {
                continue;
            }

            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                String taskName = clazz.getAnnotation(com.pedrocosta.exchangelog.batch.annotations.ScheduledTask.class).value();
                if (taskName != null && (taskName.equals(name) || clazz.getSimpleName().replace("Task", "").equalsIgnoreCase(name))) {
                    scheduler = create((Class<T>) clazz);
                }
                break;
            } catch (Exception e) {
                Log.error(this, e);
            }
        }

        return scheduler;
    }

    private String getPackageName() {
        try {
            return PackageUtils.getProjectPackage(context).getName();
        } catch (ClassNotFoundException e) {
            Log.error(this, e);
        }
        return "";
    }
}
