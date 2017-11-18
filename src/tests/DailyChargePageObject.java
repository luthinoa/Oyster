package tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.Journey;
import com.tfl.billing.JourneyEvent;
import com.tfl.billing.JourneyStart;
import com.tfl.billing.TravelTracker;
import com.tfl.external.CustomerDatabase;

/**
 * Created by Noa Luthi on 18/11/2017.
 */
public class DailyChargePageObject {

    private OysterCard myCard;
    private OysterCardReader paddingtonReader;
    private OysterCardReader bakerStreetReader;
    private TravelTracker travelTracker;
    private TestOysterCard testOysterCard;

    public DailyChargePageObject(){

        travelTracker = new TravelTracker();
        testOysterCard = new TestOysterCard();
        paddingtonReader = testOysterCard.getPaddingtonReader();
        bakerStreetReader = testOysterCard.getBakerStreetReader();
        myCard = testOysterCard.getMyCard();


    }
    public void generateEventLog() {

    }
}
