package tests;
import com.tfl.billing.*;

/**
 * Created by Noa Luthi on 18/11/2017.
 */

public class TravelTrackerPageObject {

    private List<Customer> testCustomers = new ArrayList<Customer>();
    private final List<JourneyEvent> testEventLog = new ArrayList<JourneyEvent>();

    /*
    This test is going to check the process of scanning an oyster card and adding
    the correct journeys to a specific customer's journey log.
     */


    /*
    This method iterates through the customers database we created and
    creates a list of journeys for each customer.
     */
    public void createJourneyListForEachCustomer() {

        testCustomers =

        for (Customer customer:testCustomers) {
            TravelTracker travelTracker = new TravelTracker();
            travelTracker.cardScanned(customer.getCardID(),customer.getReaderID());
        }
    }

    /*
    create list of all journeys of all customers.
    each customer will have 3 journeys.
     */
    public void createTestEventLogForAllJourneys() {

        for (int i=0;i<30;i++) {

            int customerIndex = i%3;
            UUID customerIDCard = testCustomers.get(customerIndex).getCardID();
            UUID customerReaderCard = testCustomers.get(customerIndex).getReaderID();
            JourneyStart journeyStart = new JourneyStart(customerIDCard,customerReaderCard);
            JourneyEnd journeyEnd = new JourneyEnd(customerIDCard, customerReaderCard);
            testEventLog.add(journeyStart);
            testEventLog.add(journeyEnd);
        }
    }

    /*
    asserting that the test grouping of events (by cardID) corresponds to the testing data.
     */
    public void assertEventLog() {

        TravelTracker travelTracker = new TravelTracker();

        for (JourneyEvent journey: testEventLog) {
            travelTracker.cardScanned(journey.cardId(),journey.readerId());
        }

        List<JourneyEvent> eventLog = travelTracker.getEventLog();

        //assert that testEventLog and eventLog contain the same journeys.
        Assert.assertEquals(eventLog,testEventLog);
    }

    public static void main(String args[]) {
        CardScannedPageObject cardScannedPageObject = new CardScannedPageObject();
        cardScannedPageObject.generateCustomers();
        cardScannedPageObject.createJourneyListForEachCustomer();
        cardScannedPageObject.createJourneyListForEachCustomer();
        cardScannedPageObject.createTestEventLogForAllJourneys();
        cardScannedPageObject.assertEventLog();
    }
}
