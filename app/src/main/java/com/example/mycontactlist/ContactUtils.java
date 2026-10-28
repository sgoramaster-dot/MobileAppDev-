package com.example.mycontactlist;

import java.util.Comparator;
import java.util.List;

public class ContactUtils {
    public static final int SORT_NAME_ASC = 0;
    public static final int SORT_NAME_DESC = 1;
    public static final int SORT_NEWEST = 2;
    public static final int SORT_OLDEST = 3;

    public static void sortContacts(List<Contact> contacts, int sortMode) {
        Comparator<Contact> comparator;
        switch (sortMode) {
            case SORT_NAME_DESC:
                comparator = Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER)
                        .reversed();
                break;
            case SORT_NEWEST:
                comparator = Comparator.comparingLong(Contact::getCreatedAt).reversed();
                break;
            case SORT_OLDEST:
                comparator = Comparator.comparingLong(Contact::getCreatedAt);
                break;
            case SORT_NAME_ASC:
            default:
                comparator = Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER);
                break;
        }
        contacts.sort(comparator);
    }
}
