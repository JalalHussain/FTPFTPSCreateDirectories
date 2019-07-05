package com.barzi.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * <pre>
 *     This class is a utility class to fetch/get the properties for this application specified in the application.properties file.
 * </pre>
 *
 * @author barzi
 */
public class ConfigTag {
    private static final Logger logger = LoggerFactory.getLogger(ConfigTag.class); //Logger instance for logging.

    private static String APPLICATION_PROPS_FILE_NAME = "application";  // The property resource file name .
    private static ResourceBundle APPLICATION_PROPS;    // The resource bundle object to hold the properties.

    /**
     * This block will initialize the resource bundle APPLICATION_PROPS in order the value(s) can be
     * accessed by key(s).
     */
    static {
        APPLICATION_PROPS = ResourceBundle.getBundle(APPLICATION_PROPS_FILE_NAME);
    }

    /**
     * This method returs resolved value against a provided key. It will return an empty string if the
     * no vlaue reslved against that key.
     *
     * @param key A string key against which a value will be resolved.
     * @return Returns a value agains a string key.
     */
    public static String getProperty(String key) {
        String propertyValue = APPLICATION_PROPS.getString(key);
        if (propertyValue == null) {
            logger.error(String.format("The resource has not been initialized hence no property is resolved against key {0}", key));
            propertyValue = "";
        }
        return propertyValue;
    }

    /**
     * @param key          A string key against which a value will be resolved.
     * @param defaultValue If a value against a key isn't present then the default provided value will be returned.
     * @return Returns a value agains a string key.
     */
    public static String getProperty(String key, String defaultValue) {
        String propertyValue = APPLICATION_PROPS.getString(key);
        if (propertyValue == null) {
            return defaultValue;
        } else {
            return propertyValue;
        }
    }

}
