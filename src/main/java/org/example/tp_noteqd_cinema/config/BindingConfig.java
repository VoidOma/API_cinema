package org.example.tp_noteqd_cinema.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * Global binder to sanitize String inputs coming from form-data/x-www-form-urlencoded.
 * - Trims surrounding whitespaces
 * - Strips surrounding double quotes if present (e.g., "alice@gmail.com" -> alice@gmail.com)
 * This helps validation like @Email to work correctly when users accidentally include quotes in form fields.
 */
@ControllerAdvice
public class BindingConfig {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new QuoteTrimmingStringEditor());
    }

    static class QuoteTrimmingStringEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null) {
                setValue(null);
                return;
            }
            String value = text.trim();
            if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            setValue(value);
        }

        @Override
        public String getAsText() {
            Object value = getValue();
            return value != null ? value.toString() : "";
            }
    }
}
