package com.oddle.app.weather.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class DateHelperTest {

    @Test
    void constructorIsPrivate() throws Exception {
        Constructor<DateHelper> constructor = DateHelper.class.getDeclaredConstructor();

        Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}
