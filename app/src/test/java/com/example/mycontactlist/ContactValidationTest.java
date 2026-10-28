package com.example.mycontactlist;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContactValidatorTest {
    @Test
    public void validateAcceptsGoodContact() {
        Contact contact = new Contact();
        contact.setFirstName("Jamie");
        contact.setLastName("Lee");
        contact.setPhone("+1 (555) 123-4567");
        contact.setEmail("jamie@example.com");

        ContactValidator.ValidationResult result = ContactValidator.validate(contact);
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    public void validateRejectsBadPhoneAndEmail() {
        Contact contact = new Contact();
        contact.setFirstName("A");
        contact.setLastName("B");
        contact.setPhone("abc");
        contact.setEmail("not-an-email");

        ContactValidator.ValidationResult result = ContactValidator.validate(contact);
        assertFalse(result.isValid());
        assertEquals(2, result.getErrors().size());
    }
}

public class ContactUtilsTest {
    @Test
    public void sortContactsByNameAscending() {
        List<Contact> contacts = new ArrayList<>();
        Contact b = new Contact("Bob", "Brown", "123", "", "", "", null, 2L);
        Contact a = new Contact("Alice", "Adams", "456", "", "", "", null, 1L);
        contacts.add(b);
        contacts.add(a);

        ContactUtils.sortContacts(contacts, ContactUtils.SORT_NAME_ASC);
        assertEquals("Adams", contacts.get(0).getLastName());
        assertEquals("Brown", contacts.get(1).getLastName());
    }
}
