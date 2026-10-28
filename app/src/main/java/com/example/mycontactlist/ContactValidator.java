package com.example.mycontactlist;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ContactValidator {
    public static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+?\\d{1,3}[-.\\s]?)?(\\(?\\d{2,4}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4,5}$");

    public static ValidationResult validate(Contact contact) {
        List<String> errors = new ArrayList<>();

        if (TextUtils.isEmpty(contact.getFirstName().trim())) {
            errors.add("First name is required.");
        }
        if (TextUtils.isEmpty(contact.getLastName().trim())) {
            errors.add("Surname is required.");
        }
        if (TextUtils.isEmpty(contact.getPhone().trim())) {
            errors.add("Phone number is required.");
        } else if (!PHONE_PATTERN.matcher(contact.getPhone().trim()).matches()) {
            errors.add("Phone format looks invalid.");
        }

        String email = contact.getEmail() == null ? "" : contact.getEmail().trim();
        if (!TextUtils.isEmpty(email) && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add("Email format looks invalid.");
        }

        return new ValidationResult(errors);
    }

    public static class ValidationResult {
        private final List<String> errors;

        public ValidationResult(List<String> errors) {
            this.errors = errors;
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
