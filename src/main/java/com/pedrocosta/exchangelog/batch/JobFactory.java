package com.pedrocosta.exchangelog.batch;

import com.pedrocosta.exchangelog.models.ScheduledJob;
import com.pedrocosta.exchangelog.services.CoreService;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.PackageUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Factory that creates a instance of {@link ScheduledTask}.
 *
 * @param <T> Subtype of {@link ScheduledTask}
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Component
public class JobFactory<T extends ScheduledTask> {

    private final String SUFFIX = "Task";

    private final Environment env;
    private final ApplicationContext context;

    public JobFactory(ApplicationContext context) {
        this.context = context;
        this.env = context.getEnvironment();
    }

    private String getPackage() {
        return env.getProperty("project.package") + ".batch.jobs";
    }

    /**
     * Create a new instance of scheduled job based on class parameter.
     *
     * @param clazz It can be service's class itself or any other class of the same prefix
     *
     * @return {@link CoreService} instance.
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
     * @param name Type of the service. It can be any prefix of the service's class name.
     *             Ex: If the target is FixerService, pass 'Fixer'.
     *
     * @return {@link CoreService} instance.
     */
    public T create(String name) {
        T scheduler = null;

        // Look for all subpackages into service
        List<Package> subPackages = PackageUtils.getSubPackages(getPackage());

        for (Package pack : subPackages) {
            try {
                Class<?> clazz = Class.forName(pack.getName() + "." +
                        StringUtils.capitalize(name) + SUFFIX);
                scheduler = create((Class<T>) clazz);
                break;
            } catch (ClassNotFoundException e) {
                // Continue to search
            } catch (Exception e) {
                Log.error(this, e);
            }
        }

        return scheduler;
    }
}
