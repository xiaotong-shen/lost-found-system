package data_access;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class FirebasePostDataAccessObjectTest {

    @Test
    void classIsLoadable() {
        assertNotNull(FirebasePostDataAccessObject.class);
    }

    @Test
    void allDeclaredMethodsAreReflectivelyAccessible() {
        Method[] methods = FirebasePostDataAccessObject.class.getDeclaredMethods();
        assertNotNull(methods);
        for (Method m : methods) {
            assertNotNull(m.getName());
            assertNotNull(m.getReturnType());
            assertNotNull(m.getParameterTypes());
        }
    }
}
