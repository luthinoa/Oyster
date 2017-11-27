package tests;

import org.junit.*;

/**
 * Created by Noa Luthi on 18/11/2017.
 */
public class JourneyTest {

    public JourneyPageObject journeyPageObject;

    public JourneyTest() {
        journeyPageObject = new JourneyPageObject();
    }

    /*
    This test assert the behavior of touching a card on the scanner
    at beginning and end of a journey, charging it, and asserting that correct charging has been made.
     */
    @Test
    public void assertTouchingAtStationsAndChargeOfJourney() {

        journeyPageObject.assertInitialTouch();
        journeyPageObject.assertEndTouch();
        journeyPageObject.assertJourneyPrice();
    }



}
