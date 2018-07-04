package com.dentalclinic.capstone.admin.api;

/**
 * Created by lucky on 13-Sep-17.
 */

public class APIServiceManager {

    public static <T> T getService(final Class<T> tClass) {
        return RetrofitClient.getClient().create(tClass);
    }

}
