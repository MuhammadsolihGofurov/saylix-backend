package api.saylix.uz.services;

import api.saylix.uz.enums.AppLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AppLanguageService {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public String getMessage(String messageKey, AppLanguage language) {
        return messageSource.getMessage(messageKey, null, new Locale(language.name()));
    }


}
