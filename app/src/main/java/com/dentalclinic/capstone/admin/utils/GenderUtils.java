package com.dentalclinic.capstone.admin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GenderUtils {

    private static final String MALE = "MALE";
    private static final String FORMAT_MALE = "Nam";
    private static final String FEMALE = "FEMALE";
    private static final String FORMAT_FEMALE = "Nữ";
    private static final String OTHER = "OTHER";
    private static final String FORMAT_OTHER = "Khác";

    public static String toString(String gender) {
        String rs = FORMAT_OTHER;
        switch (gender) {
            case MALE:
                rs = FORMAT_MALE;
                break;
            case FEMALE:
                rs = FORMAT_FEMALE;
                break;
            case OTHER:
                rs = FORMAT_OTHER;
                break;
        }
        return rs;
    }
}
