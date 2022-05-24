package com.pedrocosta.exchangelog.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.pedrocosta.exchangelog.utils.AppProperties;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.PackageUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class AdapterFactory implements TypeAdapterFactory {
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
        return getAdapterName(packageName, clazz.getSimpleName(), type);
    }

    /**
     * Build adapter class name with its package.
     * @param packageName   Name of package of adapter classes
     * @param simpleName    Simple name of the object's class to be deserialized
     * @param type          If we use different types of adapters with different implementation,
     *                      use this parameter to define which type are looking for
     * @param <T>           Type class of object to be deserialized
     *
     * @return  Adapter complete name with package.
     */
    protected final <T> String getAdapterName(String packageName, String simpleName, String type) {
        String typeCap = "";
        if (type != null && !type.isBlank()) {
            typeCap = StringUtils.capitalize(type);
        }
        return packageName + "." + typeCap + simpleName + getAdapterSuffix();
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
                adapter = findAdapter(getAdapterName(pack.getName(), clazz, type));
                if (adapter != null) {
                    break;
                }
            } catch (Exception e) {
                Log.warn(this, e.getMessage());
            }
        }

        return adapter;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        TypeAdapter<T> adapter = null;

        // Look for all subpackages into service
        List<Package> subPackages = PackageUtils.getSubPackages(getAdapterPackage());
        String[] names = typeToken.getType().getTypeName().split("\\.");
        String simpleName = names[names.length - 1];

        for (Package pack : subPackages) {
            try {
                adapter = findAdapter(getAdapterName(pack.getName(), simpleName, null));
                if (adapter != null) {
                    break;
                }
            } catch (Exception e) {
                Log.warn(this, e.getMessage());
            }
        }

        return adapter;
    }

    private <T> TypeAdapter<T> findAdapter(String name) throws ClassNotFoundException {
        TypeAdapter<T> adapter = null;
        try {
            Class<TypeAdapter<T>> adapterClass = (Class<TypeAdapter<T>>) Class.forName(name);
            if (adapterClass.getConstructors().length > 0) {
                Class<?>[] paramTypes = adapterClass.getConstructors()[0].getParameterTypes();
//                    if (paramTypes.length > 0) {
//                        adapter = adapterClass.getConstructor(paramTypes)
//                                .newInstance(new GsonUtils(new AdapterFactory()));
//                    } else {
                adapter = adapterClass.getConstructor(new Class[0]).newInstance();
//                    }
            }
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }
        return adapter;
    }
}
