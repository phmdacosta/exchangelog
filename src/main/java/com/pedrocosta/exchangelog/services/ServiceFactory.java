package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import com.pedrocosta.exchangelog.utils.PackageUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceFactory {

    private final String SUFFIX = "Service";

     private final Environment env;
     private final ApplicationContext context;

    public ServiceFactory(ApplicationContext context, Environment env) {
        this.context = context;
        this.env = env;
    }

    private String getPackage() {
        return env.getProperty("project.package") + ".services";
    }

    /**
     * Create a new instance of service based on class parameter.
     *
     * @param clazz It can be service's class itself or any other class of the same prefix
     *
     * @return {@link CoreService} instance.
     */
    public CoreService create(Class clazz) {
        if (!clazz.getSimpleName().endsWith(SUFFIX)) {
            return create(clazz.getSimpleName());
        }
        return (CoreService) context.getBean(clazz);
    }

    /**
     * Create a new instance of service based on parameter.
     *
     * @param type Type of the service. It can be any prefix of the service's class name.
     *             Ex: If the target is FixerService, pass 'Fixer'.
     *
     * @return {@link CoreService} instance.
     */
    public CoreService create(String type) {
        CoreService service = null;

        // Look for all subpackages into service
        List<Package> subPackages = PackageUtils.getSubPackages(getPackage());

        for (Package pack : subPackages) {
            try {
                Class clazz = Class.forName(pack.getName() + "." + type + SUFFIX);
                service = (CoreService) context.getBean(clazz);
                break;
            } catch (ClassNotFoundException e) {
                // Continue to search
            }
        }

        if (service == null)
            Log.error(this, Messages.get("service.not.found", type));

        return  service;
    }
}
