package org.example;

public class ClassForTest {

    private int field1 = 1;    // 1
    private String field2 = "field2";     // 2


    public void someMethod() {
        int new_var = 10; // 3
        String new_var_2 = "some text"; // 4

        if (new_var == 10) {
            System.out.println("Java is work");
        } else {
            System.out.println("What?");
        }

        for (int i = 0; i < new_var; i++) { // 5
            System.out.println("Cycle iteration " + (i + 1));
        }
    }

}
