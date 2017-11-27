package tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.external.CustomerDatabase;

import org.junit.Assert;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Noa Luthi on 18/11/2017.
 */

 /*
   This test will check that connecting, registering and generating eventLog works correctly.
   We will do this by creating a false journey and asserting different stages of the "tapping".
 */

public class JourneyPageObject {

    private OysterCard myCard;
    private Set<UUID> currentlyTravelling;
    private OysterCardForTesting testOysterCard;
    private OysterCardReader paddingtonReader;
    private OysterCardReader bakerStreetReader;
    private TravelTracker travelTracker;

    static final BigDecimal OFF_PEAK_JOURNEY_PRICE = new BigDecimal(2.40);
    static final BigDecimal PEAK_JOURNEY_PRICE = new BigDecimal(3.20);

    public JourneyPageObject(){

        testOysterCard = new OysterCardForTesting();
        myCard = testOysterCard.getMyCard();
        travelTracker = testOysterCard.getTravelTracker();
        paddingtonReader = testOysterCard.getPaddingtonReader();
        bakerStreetReader = testOysterCard.getBakerStreetReader();
        currentlyTravelling = travelTracker.getCurrentlyTravelling();
    }

    /*
       touch oyster card at paddington, and assert that the cardID has been added to the
       currently travelling set, and is Registered.
    */

   public void assertInitialTouch() {

       paddingtonReader.touch(myCard);

       JourneyEvent expectedJourneyStart = new JourneyStart(myCard.id(),paddingtonReader.id());
       UUID expectedJourneyStartID = expectedJourneyStart.cardId();

       JourneyEvent eventFromEventLog = travelTracker.getEventLog().get(0);
       UUID eventFromEventLogID = eventFromEventLog.cardId();

        //assert that the event log contains a JourneyStart with the same cardID.
       Assert.assertTrue(expectedJourneyStartID.equals(eventFromEventLogID));


       CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
       //assert that card is registered.
       Assert.assertTrue(customerDatabase.isRegisteredId(myCard.id()));

       //assert the cardId is in currently traveling.
       Assert.assertTrue(currentlyTravelling.contains(myCard.id()));
   }

    /*
       touch oyster card at baker street, and assert that the cardID has been removed from the
       currently travelling set.
    */

   public void assertEndTouch() {

       bakerStreetReader.touch(myCard);

       JourneyEvent expectedJourneyEnd = new JourneyEnd(myCard.id(),bakerStreetReader.id());
       UUID expectedJourneyEndID = expectedJourneyEnd.cardId();

       JourneyEvent eventFromEventLog = travelTracker.getEventLog().get(1);
       UUID eventFromEventLogID = eventFromEventLog.cardId();

       //assert that the event log contains a JourneyEnd.
       Assert.assertTrue(expectedJourneyEndID.equals(eventFromEventLogID));

       //assert that the cardID is no longer in currently travelling.
       Assert.assertTrue(!currentlyTravelling.contains(myCard.id()));
   }

   /*
   Call charge accounts from our program, to charge the journey from Paddington to Baker Street, and assert
   that the correct charge has been applied to the journey.
   If it's Peak at time of testing - then compare to peak charge value, and vice versa when off peak.
    */

   public void assertJourneyPrice(){

       //charge accounts (which hold the journey from paddington to baker street).
       travelTracker.chargeAccounts();

       //get the charging value of the journey from travelTracker.
       BigDecimal testJourneyChargeValue = travelTracker.getCustomerTotal();


       //check what is current hour of the day, to know if to assert to peak/off peak journey.
       Calendar calendar = Calendar.getInstance();
       int hourOfDayAtTest = calendar.HOUR_OF_DAY;

       //if current hour of the day is peak, compare to peak set charging value.
       if (hourOfDayAtTest>=6 && hourOfDayAtTest<=10 || hourOfDayAtTest>=17 && hourOfDayAtTest<=20) {
           Assert.assertTrue(PEAK_JOURNEY_PRICE.doubleValue()==testJourneyChargeValue.doubleValue());
       }

       //if current hour of the day is off peak, compare to off peak set charging value.
       else {
           Assert.assertTrue(OFF_PEAK_JOURNEY_PRICE.doubleValue()==testJourneyChargeValue.doubleValue());
       }
   }
}
