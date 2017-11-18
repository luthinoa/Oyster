package tests;
import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.Assert;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Noa Luthi on 18/11/2017.
 */

 /*
   This test will check that connecting, registering and generating eventLog works correctly.
   We will do this by creating a false journey and asserting different stages of the "tapping".
 */

public class TravelTrackerPageObject {


    private OysterCard myCard;
    private OysterCardReader paddingtonReader;
    private OysterCardReader bakerStreetReader;
    private TravelTracker travelTracker;
    private Set<UUID> currentlyTravelling;

    public TravelTrackerPageObject() {

        currentlyTravelling = travelTracker.getCurrentlyTravelling();
    }

    /*
       Creating an oyster card.
       Journey from Paddington to Baker Street.
       Connecting the card reader in each station.
    */
    public void generateOysterCard() {

       myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
       paddingtonReader = OysterReaderLocator.atStation(Station.PADDINGTON);
       bakerStreetReader = OysterReaderLocator.atStation(Station.BAKER_STREET);
       travelTracker = new TravelTracker();
       travelTracker.connect(paddingtonReader, bakerStreetReader);
   }

    /*
       touch oyster card at paddington, and assert that the cardID has been added to the
       currently travelling set, and is Re
    */

   public void assertInitialTouch() {

       paddingtonReader.touch(myCard);

       JourneyEvent expectedJourneyStart = new JourneyStart(myCard.id(),paddingtonReader.id());

        //assert that the event log contains a JourneyStart.
       Assert.assertTrue(travelTracker.getEventLog().contains(expectedJourneyStart));

       //assert that card is registered.
       Assert.assertTrue(CustomerDatabase.getInstance().isRegisteredId(myCard.id()));

       //assert that currently travelling list contains my card.
       Assert.assertTrue(currentlyTravelling.contains(myCard.id()));
   }

   public void assertEndTouch() {

       bakerStreetReader.touch(myCard);

       JourneyEvent expectedJourneyEnd = new JourneyEnd(myCard.id(),bakerStreetReader.id());

       //assert that the event log contains a JourneyEnd.
       Assert.assertTrue(travelTracker.getEventLog().contains(expectedJourneyEnd));

       //assert that the cardID is no longer in currently travelling.
       Assert.assertTrue(!currentlyTravelling.contains(myCard.id()));
   }
}
