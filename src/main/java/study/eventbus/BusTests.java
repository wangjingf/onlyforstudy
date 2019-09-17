package study.eventbus;

import junit.framework.TestCase;

import java.util.Date;

public class BusTests extends TestCase {
    private EventBus eventBus;

    private EventService eventService;
    @Override
    public void setUp() {
        eventBus = new EventBus();
        eventService = new EventService(eventBus);
    }

    public void testPostSeveralEvent() {
        EventService.HelloEvent helloEvent = new EventService.HelloEvent();
        helloEvent.setGreeting("你好啊");
        helloEvent.setDate(new Date());


        eventBus.post(helloEvent);
        while (true);
    }


}
