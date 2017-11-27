package tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.TravelTracker;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.util.List;

/**
 * Created by Noa Luthi on 18/11/2017.
 */
public class OysterCardForTesting {

    private TravelTracker travelTracker;
    private OysterCard myCard;
    private OysterCardReader paddingtonReader;
    private OysterCardReader bakerStreetReader;

     /*
     Creating an oyster card for testing purposes.
     Journey from Paddington to Baker Street.
     Connecting the card reader in each station.
    */

    public OysterCardForTesting(){

        travelTracker = new TravelTracker();
        myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        paddingtonReader = OysterReaderLocator.atStation(Station.PADDINGTON);
        bakerStreetReader = OysterReaderLocator.atStation(Station.BAKER_STREET);
        travelTracker.connect(paddingtonReader, bakerStreetReader);
    }

    public OysterCard getMyCard() {return myCard;}

    public OysterCardReader getPaddingtonReader() {return paddingtonReader;}

    public OysterCardReader getBakerStreetReader() {return bakerStreetReader;}

    public TravelTracker getTravelTracker() {return travelTracker;}

}
