package org.developfreedom.ccdroid.app;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Shubham Chaudhary on 25/03/16
 */
public class BasicTest {
    @Test public void sanityCheck() throws Exception {
        assertThat(true).isTrue();
        assertThat(false).isFalse();
        assertThat(1+1).isEqualTo(2);
    }
}
