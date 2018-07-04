package com.dentalclinic.capstone.admin.utils;

public enum DateTimeFormat {
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    DATE_TIME_DB {
        public String toString() {
            return "yyyy-MM-dd HH:mm:ss";
        }
    }, /**
     * yyyy-MM-dd
     */
    DATE_TIME_DB_2 {
                public String toString() {
                    return "yyyy-MM-dd";
                }
            },
    /**
     * h:mm a dd-MM-yyyy
     */
    DATE_TIME_APP {
        public String toString() {
            return "h:mm a dd-MM-yyyy";
        }
    }, /**
     * dd/MM/yyyy
     */
    DATE_FOTMAT {
                public String toString() {
                    return "dd-MM-yyyy";
                }
            },
    /**
     * dd-MM-yyyy
     */
    DATE_APP {
        public String toString() {
            return "dd-MM-yyyy";
        }
    },
    /**
     * dd/MM/yyyy
     */
    DATE_APP_2 {
        public String toString() {
            return "dd/MM/yyyy";
        }
    },
    /**
     * empty string
     */
    DATE_APP_3 {
        public String toString() {
            return "";
        }
    };
}
