import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void whenTopicAbsent() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        Resp result1 = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        assertThat(result1.text(), is("Error topic don't exist"));
    }

    @Test
    public void whenCreateTopic() {
        TopicService topicService = new TopicService();
        String paramForSubscriber1 = "client407";
        Resp result = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        assertThat(result.text(), is("Create new topic"));
    }

    @Test
    public void whenTreeAddAndTwoGet() {
        TopicService topicService = new TopicService();
        String paramForPublisher18 = "temperature=18";
        String paramForPublisher20 = "temperature=20";
        String paramForPublisher32 = "temperature=32";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher32)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher20)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher18)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
       topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=32"));
        assertThat(result2.text(), is("temperature=20"));
    }
}
