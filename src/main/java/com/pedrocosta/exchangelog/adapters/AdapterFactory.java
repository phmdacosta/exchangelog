package com.pedrocosta.exchangelog.adapters;

import com.google.gson.TypeAdapter;
import com.pedrocosta.exchangelog.utils.AppProperties;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.PackageUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class AdapterFactory {
    private static final String ADAPTER_SUFFIX = "Adapter";
    private static final String ADAPTER_PACKAGE = "adapters";
    private static final String PROJECT_PACKAGE = "project.package";

    /**
     * Get suffix of adapter's name.
     */
    protected String getAdapterSuffix() {
        return ADAPTER_SUFFIX;
    }

    /**
     * Get package name of adapters.
     */
    protected String getAdapterPackage() {
        return AppProperties.get(PROJECT_PACKAGE) + "." + ADAPTER_PACKAGE;
    }

    /**
     * Build adapter class name with its package.
     *
     * @param packageName   Name of package of adapter classes
     * @param clazz         Class of object to be deserialized
     * @param type          If we use different types of adapters with different implementation,
     *                      use this parameter to define which type are looking for
     * @param <T>           Type class of object to be deserialized
     *
     * @return Adapter complete name with package.
     */
    protected final <T> String getAdapterName(String packageName, Class<T> clazz, String type) {
        String typeCap = "";

        if (type != null && !type.isBlank()) {
            typeCap = StringUtils.capitalize(type);
        }

        return packageName + "." + typeCap + clazz.getSimpleName()
                + getAdapterSuffix();
    }

    /**
     * Create a new instance of adapter.
     *
     * @param clazz Class of object to be deserialized
     * @param <T>   Type class of object to be deserialized
     *
     * @return {@link TypeAdapter} instance.
     */
    public <T> TypeAdapter<T> create(Class<T> clazz) {
        return create(clazz, null);
    }

    /**
     * Create a new instance of adapter.
     *
     * @param clazz Class of object to be deserialized
     * @param type  If we use different types of adapters with different implementation,
     *      *              use this parameter to define which type are looking for
     * @param <T>   Type class of object to be deserialized
     *
     * @return {@link TypeAdapter} instance.
     */
    public <T> TypeAdapter<T> create(Class<T> clazz, String type) {
        TypeAdapter<T> adapter = null;

        // Look for all subpackages into service
        List<Package> subPackages = PackageUtils.getSubPackages(getAdapterPackage());

        for (Package pack : subPackages) {
            try {
                Class<TypeAdapter<T>> adapterClass = (Class<TypeAdapter<T>>)
                        Class.forName(getAdapterName(pack.getName(), clazz, type));
                if (adapterClass.getConstructors().length > 0) {
                    Class<?>[] paramTypes = adapterClass.getConstructors()[0].getParameterTypes();
                    if (paramTypes.length > 0) {
                        adapter = adapterClass.getConstructor(paramTypes)
                                .newInstance(new GsonUtils(new AdapterFactory()));
                    } else {
                        adapter = adapterClass.getConstructor(new Class[0]).newInstance();
                    }
                }
                break;
            } catch (Exception e) {
                Log.warn(this, e.getMessage());
            }
        }

        return adapter;
    }
}
