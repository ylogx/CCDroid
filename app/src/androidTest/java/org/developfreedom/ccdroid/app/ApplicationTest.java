package org.developfreedom.ccdroid.app;

import android.app.Application;
import android.test.ApplicationTestCase;
import org.junit.Test;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    public void testTrueIsTrue() {
        assertEquals(true, true);
        assertEquals(false, true);
    }
}