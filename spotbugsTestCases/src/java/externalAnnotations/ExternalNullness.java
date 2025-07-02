package externalAnnotations;

public class ExternalNullness {
    // external annotation: CheckForNull
    String nullFiled = "hello";
    // external annotation: NonNull
    String nonNullFiled = "world";

    // external annotation: CheckForNull
    String nullString() {
        return null;
    }
    // external annotation: NonNull
    String nonNullString() {
        return null;
    }

    void foo() {
        System.out.println(nullFiled.length());
        System.out.println(nonNullFiled.length());
    }

    void bar() {
        System.out.println(nullString().length());
        System.out.println(nonNullString().length());
    }
}
