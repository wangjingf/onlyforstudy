package study.eventbus;

import java.util.Date;

public class EventService {
    public EventService(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void handleEvent(HelloEvent event) {
        if (event == null) {
            return;
        }
        System.out.println("handleEvent received: " + event);
    }

    @Subscribe
    public void doHelloEvent(HelloEvent event) {
        if (event == null) {
            return;
        }
        System.out.println("doHelloEvent received: " + event);
    }
    static class HelloEvent{
        private String greeting;

        private Date date;

        public void setGreeting(String greeting) {
            this.greeting = greeting;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "HelloEvent{" +
                    "greeting='" + greeting + '\'' +
                    ", date=" + date +
                    '}';
        }
    }
}
