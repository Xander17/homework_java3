package lesson7.tests;

import lesson7.tests.annotation.AfterSuite;
import lesson7.tests.annotation.BeforeSuite;
import lesson7.tests.annotation.Test;

import java.lang.reflect.Method;
import java.util.*;

public class Tests {

    private static ArrayList<Method> methods;
    private static Object object;

    public static void start(Class c) throws ReflectiveOperationException {
        object = c.getDeclaredConstructors()[0].newInstance();
        fillMethodsQueue(c);
        runMethods();
    }

    public static void start(String name) throws ReflectiveOperationException {
        start(Class.forName(name));
    }

    private static void fillMethodsQueue(Class c) {
        Method[] declaredMethods = c.getDeclaredMethods();
        methods = new ArrayList<>();
        Method before = null;
        Method after = null;
        for (Method method : declaredMethods) {
            if (method.getAnnotations().length == 0) continue;
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (before != null) throw new RuntimeException("Only one annotation @BeforeSuite is allowed");
                before = method;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (after != null) throw new RuntimeException("Only one annotation @AfterSuite is allowed");
                after = method;
            }
            if (method.isAnnotationPresent(Test.class)) methods.add(method);
        }
        methods.sort(Tests::compare);
        methods.add(0, before);
        methods.add(after);
    }

    private static void runMethods() throws ReflectiveOperationException {
        for (Method method : methods) {
            method.setAccessible(true);
            method.invoke(object);
        }
    }

    private static int compare(Method o1, Method o2) {
        if (!o1.isAnnotationPresent(Test.class) || !o2.isAnnotationPresent(Test.class)) return 0;
        int priority1 = o1.getAnnotation(Test.class).priority();
        int priority2 = o2.getAnnotation(Test.class).priority();
        if (priority1 > 10) priority1 = 10;
        else if (priority1 < 1) priority1 = 1;
        if (priority2 > 10) priority2 = 10;
        else if (priority2 < 1) priority2 = 1;
        return priority2 - priority1;
    }
}
