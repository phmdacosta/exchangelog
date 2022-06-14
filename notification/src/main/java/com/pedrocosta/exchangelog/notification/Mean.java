package com.pedrocosta.exchangelog.notification;

import com.pedrocosta.springutils.output.Log;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public enum Mean {
    EMAIL, APP, SMS;

    @Nullable
    public static Mean get(final String s) {
        String param = s;
        if (param == null) {
            return null;
        }
        if (param.contains("-")) {
            param = s.replace("-", "");
        }
        try {
            return Mean.valueOf(param.toUpperCase());
        } catch (final IllegalArgumentException e) {
            Log.error(null, e);
        }
        return null;
    }

    public String capitalizeName() {
        return StringUtils.capitalize(this.name().toLowerCase());
    }
}
