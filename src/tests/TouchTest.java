package tests;

import com.tfl.billing.TravelTracker;
import org.junit.*;

/**
 * Created by Noa Luthi on 18/11/2017.
 */
public class TouchTest {

    public TouchTest() {
    }

    /*
    This test assert the behavior of touching a card on the scanner
    at beginning and end of a journey
     */
    @Test
    public void testTouchingCardAtStartAndEnd() {

        TouchPageObject touchPageObject = new TouchPageObject();
        touchPageObject.generateOysterCard();
        touchPageObject.assertInitialTouch();
        touchPageObject.assertEndTouch();
    }



}
