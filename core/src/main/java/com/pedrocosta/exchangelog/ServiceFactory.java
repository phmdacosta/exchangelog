package com.pedrocosta.exchangelog;

import com.pedrocosta.springutils.PackageUtils;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
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
        return env.getProperty("project.package");
    }

    /**
     * Create a new instance of service based on class parameter.
     *
     * @param clazz Service's class itself or any other class of the same prefix
     * @param <T>   Type of service
     *
     * @return  Service instance
     */
    public <T> T create(Class<T> clazz) {
        if (!clazz.getSimpleName().endsWith(SUFFIX)
                && !clazz.getSimpleName().endsWith(SUFFIX.concat("Impl")) ) {
            return (T) create(clazz.getSimpleName());
        }
        return (T) context.getBean(clazz);
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
                if (!type.endsWith(SUFFIX)) {
                    type = type + SUFFIX;
                }
                Class clazz = Class.forName(pack.getName() + "." + type);
                service = (CoreService) context.getBean(clazz);
                break;
            } catch (ClassNotFoundException e) {
                // Continue to search
            } catch (ClassCastException e) {
                Log.error(this, e);
            }
        }

        if (service == null)
            Log.error(this, Messages.get("service.not.found", type));

        return  service;
    }
}
