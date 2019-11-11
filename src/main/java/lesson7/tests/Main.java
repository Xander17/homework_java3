package lesson7.tests;

import lesson7.tests.annotation.AfterSuite;
import lesson7.tests.annotation.BeforeSuite;
import lesson7.tests.annotation.Test;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        Tests.start("lesson7.tests.Main");
        System.out.println("----");
        Tests.start(Main.class);
    }

    @Test(priority = 100)
    private void method1() {
        System.out.println("method1");
    }

    @Test(priority = 10)
    private void method2() {
        System.out.println("method2");
    }

    @Test(priority = 5)
    private void method3() {
        System.out.println("method3");
    }

    @Test(priority = 4)
    private void method4() {
        System.out.println("method4");
    }

    @Test(priority = 3)
    private void method5() {
        System.out.println("method5");
    }

    @Test(priority = 3)
    private void method6() {
        System.out.println("method6");
    }

    @Test(priority = 5)
    private void method7() {
        System.out.println("method7");
    }

    @Test(priority = 2)
    private void method8() {
        System.out.println("method8");
    }

    @Test(priority = 10)
    private void method9() {
        System.out.println("method9");
    }

    @Test(priority = -5)
    private void method10() {
        System.out.println("method10");
    }

    @BeforeSuite
    private void methodBefore() {
        System.out.println("methodBefore");
    }

    @AfterSuite
    private void methodAfter() {
        System.out.println("methodAfter");
    }
}
