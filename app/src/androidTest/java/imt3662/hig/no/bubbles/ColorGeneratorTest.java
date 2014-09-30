package imt3662.hig.no.bubbles;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Test case for the color generator.
 * Tests the basics of the generator.
 * Created by Martin on 14/09/30.
 */
public class ColorGeneratorTest extends AndroidTestCase {
    public void test() throws Exception {
        final int first = ColorRepository.getReference().getColor(1);
        final int second = ColorRepository.getReference().getColor(2);
        final int third = ColorRepository.getReference().getColor(1);
        final int fourth = ColorRepository.getReference().getColor(3);

        assertNotSame(first, second);
        assertEquals(first, third);
        assertNotSame(first, fourth);

        assertTrue(first != 0);
        assertTrue(second != 0);
        assertTrue(third != 0);
        assertTrue(fourth != 0);
    }
}
