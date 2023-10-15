package ru.openblocks.management.config.http;


public class CookieConfig {

    /**
     * Название куки для выбранного региона.
     */
    public static final String SELECTED_REGION_COOKIE_NAME = "selectedRegion";

    /**
     * Название куки для JWT-токена.
     */
    public static final String JWT_TOKEN_COOKIE_NAME = "jwtToken";

    /**
     * Срок в днях для хранения куки для выбранного региона.
     */
    public static final Long SELECTED_REGION_COOKIE_DAYS_TO_REMEMBER = 3000L;

    /**
     * Срок в днях для хранения куки JWT-токена.
     */
    public static final Long JWT_TOKEN_COOKIE_DAYS_TO_REMEMBER = 30L;
}

