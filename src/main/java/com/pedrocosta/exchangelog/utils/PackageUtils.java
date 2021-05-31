package com.pedrocosta.exchangelog.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PackageUtils {

    /**
     * Get sub packages from a super one.
     *
     * @param packageName Name of super package
     *
     * @return List of {@link Package} objects.
     */
    public static List<Package> getSubPackages(String packageName) {
        return Arrays.stream(Package.getPackages())
                .filter(pack ->
                        pack.getName().startsWith(packageName) //filter by name
                ).collect(Collectors.toList());
    }
}
