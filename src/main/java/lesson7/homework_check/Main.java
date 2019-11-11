package lesson7.homework_check;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        System.out.println("Введите путь:");
        File filepath = new File(reader.readLine());
        String[] list = filepath.list();
        if (list == null || list.length == 0) return;
        for (String file : list) {
            Class c;
            if ((c = getClassName(filepath, file)) == null) continue;
            Object o;
            if ((o = getObject(c)) == null) continue;
            System.out.println("Тест класса " + c.getCanonicalName());
            initTests(c, o);
        }
        reader.close();
    }

    private static Class getClassName(File filepath, String file) throws IOException {
        String[] name = file.split("\\.");
        if (name.length < 2 || !name[1].equalsIgnoreCase("class")) return null;
        return getClassFromFile(filepath, name[0]);
    }

    private static Class getClassFromFile(File filepath, String filename) throws IOException {
        Class c;
        try {
            c = URLClassLoader.newInstance(new URL[]{filepath.toURI().toURL()}).loadClass(filename);
        } catch (NoClassDefFoundError e) {
            System.out.println("Для класса " + filename + " необходимо ввести имя пакета:");
            String pack = reader.readLine();
            try {
                c = URLClassLoader.newInstance(new URL[]{filepath.toURI().toURL()}).loadClass(pack + "." + filename);
            } catch (NoClassDefFoundError | ClassNotFoundException ex) {
                return null;
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
        return c;
    }

    private static Object getObject(Class c) {
        try {
            return c.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("\tПроблема с созданием объекта");
            return null;
        }
    }

    private static void initTests(Class c, Object o) {
        startTest(c, o, "calculate",
                new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE},
                Float.TYPE);
        startTest(c, o, "checkTwoNumbers",
                new Class[]{Integer.TYPE, Integer.TYPE},
                Boolean.TYPE);
        startTest(c, o, "isNegative",
                new Class[]{Integer.TYPE},
                Boolean.TYPE);
        startTest(c, o, "isLeapYear",
                new Class[]{Integer.TYPE},
                Boolean.TYPE);
    }

    private static void startTest(Class c, Object o, String methodName, Class[] parameters, Class returnType) {
        try {
            Method m = c.getDeclaredMethod(methodName, parameters);
            System.out.println("\tТест метода " + methodName);
            setTestForMethod(m, o, methodName);
        } catch (NoSuchMethodException e) {
            System.out.println("\tНет метода " + methodName + ". Пробуем найти похожие...");
            getProperMethods(c, o, methodName, parameters, returnType);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void setTestForMethod(Method m, Object o, String methodName) throws InvocationTargetException, IllegalAccessException {
        m.setAccessible(true);
        if (methodName.equals("calculate")) testCalculate(m, o);
        else if (methodName.equals("checkTwoNumbers")) testCheckTwoNumbers(m, o);
        else if (methodName.equals("isNegative")) testIsNegative(m, o);
        else if (methodName.equals("isLeapYear")) testIsLeapYear(m, o);
    }

    private static void testCalculate(Method m, Object o) throws InvocationTargetException, IllegalAccessException {
        System.out.println("\t\t" + ((float) m.invoke(o, 1f, 1f, 1f, 1f) == 2f));
        System.out.println("\t\t" + ((float) m.invoke(o, 1f, 2f, 3f, 4f) == 2.75f));
        System.out.println("\t\t" + ((float) m.invoke(o, 4f, 3f, 2f, 1f) == 20f));
        System.out.println("\t\t" + ((float) m.invoke(o, 1.5f, 2.5f, 3.5f, 5f) == 4.8f));
    }

    private static void testCheckTwoNumbers(Method m, Object o) throws InvocationTargetException, IllegalAccessException {
        System.out.println("\t\t" + ((boolean) m.invoke(o, 5, 4) == false));
        System.out.println("\t\t" + ((boolean) m.invoke(o, 12, 7) == true));
        System.out.println("\t\t" + ((boolean) m.invoke(o, 11, -2) == false));
        System.out.println("\t\t" + ((boolean) m.invoke(o, 50, -35) == true));
    }

    private static void testIsNegative(Method m, Object o) throws InvocationTargetException, IllegalAccessException {
        System.out.println("\t\t" + ((boolean) m.invoke(o, 5) == false));
        System.out.println("\t\t" + ((boolean) m.invoke(o, 0) == false));
        System.out.println("\t\t" + ((boolean) m.invoke(o, -99) == true));
        System.out.println("\t\t" + ((boolean) m.invoke(o, 100) == false));
    }

    private static void testIsLeapYear(Method m, Object o) throws InvocationTargetException, IllegalAccessException {
        System.out.println("\t\t" + ((boolean) m.invoke(o, 2000) == true));
        System.out.println("\t\t" + ((boolean) m.invoke(o, 2100) == false));
        System.out.println("\t\t" + ((boolean) m.invoke(o, 1616) == true));
        System.out.println("\t\t" + ((boolean) m.invoke(o, 1111) == false));
    }

    private static void getProperMethods(Class c, Object o, String methodName, Class[] parameters, Class returnType) {
        Method[] methods = c.getDeclaredMethods();
        boolean isExist = false;
        for (Method method : methods) {
            if (method.getName().equals("main") || method.getReturnType() == Void.TYPE) continue;
            if (checkParameters(method.getParameterTypes(), parameters) && method.getReturnType() == returnType) {
                isExist = true;
                System.out.println("\tНайден похожий по сигнатуре метод " + method.getName());
                try {
                    setTestForMethod(method, o, methodName);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!isExist) System.out.println("\tПохожих по сигнатуре методов не найдено");
    }

    private static boolean checkParameters(Class[] methodParams, Class[] paramsLookingFor) {
        if (methodParams.length != paramsLookingFor.length) return false;
        for (int i = 0; i < methodParams.length; i++) {
            if (methodParams[i] != paramsLookingFor[i]) return false;
        }
        return true;
    }
}
