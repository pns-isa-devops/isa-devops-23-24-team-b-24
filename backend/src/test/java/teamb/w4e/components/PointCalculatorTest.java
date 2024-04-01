package teamb.w4e.components;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.interfaces.PointAdder;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class PointCalculatorTest {

    @Autowired
    private PointAdder pointAdder;


    @Test
    void convertPriceToPoints() {
        assertEquals(2, pointAdder.convertPriceToPoints(10));
        assertNotEquals(100, pointAdder.convertPriceToPoints(20));
    }

    @Test
    void convertPointsToPrice() {
        assertEquals(90, pointAdder.convertPointsToPrice(100));
        assertNotEquals(10, pointAdder.convertPointsToPrice(200));
    }
}
