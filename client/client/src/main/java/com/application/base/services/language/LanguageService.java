package com.application.base.services.language;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageService {

    private String locale;

    public LanguageService(String locale) {
        this.locale = locale;
    }

    public String getString(String key) {
        return ResourceBundle.getBundle("language/strings", new Locale(locale)).getString(key);
    }

    public String getLocale() {
        return locale;
    }
}
