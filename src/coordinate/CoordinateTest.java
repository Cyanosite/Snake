package coordinate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoordinateTest {
    Coordinate a;
    Coordinate b;
    Coordinate c;
    Coordinate d;

    @Before
    public void setUp() {
        a = new Coordinate(1, 1);
        b = new Coordinate(a);
        c = new Coordinate(b.x + 1, b.y + 1);
        d = new Coordinate(c);
    }

    @Test
    public void testEquals() {
        assertEquals(a, b);
        assertEquals(c, d);
    }
}