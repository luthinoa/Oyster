package tests;

import com.oyster.*;
import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.Customer;

import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.Assert;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Noa Luthi on 18/11/2017.
 */

 /*
   This test will check that connecting, registering and generating eventLog works correctly.
   We will do this by creating a false journey and asserting different stages of the "tapping".
 */

public class TouchPageObject {

    private OysterCard myCard;
    private Set<UUID> currentlyTravelling;
    private TestOysterCard testOysterCard;
    private OysterCardReader paddingtonReader;
    private OysterCardReader bakerStreetReader;
    private TravelTracker travelTracker;

    public TouchPageObject(){

        testOysterCard = new TestOysterCard();
        myCard = testOysterCard.getMyCard();
        travelTracker = testOysterCard.getTravelTracker();
        paddingtonReader = testOysterCard.getPaddingtonReader();
        bakerStreetReader = testOysterCard.getBakerStreetReader();
        currentlyTravelling = travelTracker.getCurrentlyTravelling();
    }

    /*
       touch oyster card at paddington, and assert that the cardID has been added to the
       currently travelling set, and is Re
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
}
