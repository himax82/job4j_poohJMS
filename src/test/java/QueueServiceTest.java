import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenTwoPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod18 = "temperature=18";
        String paramForPostMethod20 = "temperature=20";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod18)
        );
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod20)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenGetQueue() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("There is no content to send for this request."));
    }

    @Test
    public void whenOnePostThenTwoGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod18 = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod18)
        );
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result2.text(), is("There is no content to send for this weather"));
    }
}