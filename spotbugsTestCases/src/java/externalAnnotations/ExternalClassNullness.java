package externalAnnotations;

public class ExternalClassNullness {
    // external annotation: CheckForNull
    String nullString() {
        return null;
    }
    // external annotation: NonNull
    String nonNullString() {
        return null;
    }

    void foo() {
        System.out.println(nullString().length());
        System.out.println(nonNullString().length());
    }
}
