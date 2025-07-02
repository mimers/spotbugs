package edu.umd.cs.findbugs.nullness;

import edu.umd.cs.findbugs.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

public class ExternalAnnotationTest extends AbstractIntegrationTest {
    @Test
    void testClassMemberAnnotation() {
        performAnalysis("externalAnnotations/ExternalNullness.class");
        assertBugInMethodCount("NP_NULL_ON_SOME_PATH", "externalAnnotations.ExternalNullness", "foo", 1);
        assertBugInMethodCount("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", "externalAnnotations.ExternalNullness", "bar", 1);
    }

    @Test
    void testClassAnnotation() {
        performAnalysis("externalAnnotations/ExternalClassNullness.class");
        assertBugInMethodCount("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", "externalAnnotations.ExternalClassNullness", "foo", 1);
    }
}
