package bexysuttx.jmemcached_server;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;


public class TestUtils {

    public static void setLoggerMockViaReflection(Class<?> clazz, Logger logger) throws IllegalAccessException {
        Field loggerField = FieldUtils.getField(clazz, "LOGGER", true);
        FieldUtils.removeFinalModifier(loggerField);
        loggerField.set(clazz, logger);
    }
}
