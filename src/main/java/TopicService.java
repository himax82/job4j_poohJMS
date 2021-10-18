import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class TopicService implements Service {

    private static final String POST = "POST";
    private static final String GET = "GET";
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, LinkedBlockingQueue<String>>> queue
            = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        if (POST.equals(req.httpRequestType())) {
            if (queue.get(req.getSourceName()) == null) {
                return new Resp("Error topic don't exist", "204 Error");
            }
            for (ConcurrentHashMap<String, LinkedBlockingQueue<String>> m: queue.values()) {
                for (LinkedBlockingQueue<String> q : m.values()) {
                    q.offer(req.getParam());
                }
            }
            return new Resp("Parameters added all users", "202");
        } else if (GET.equals(req.httpRequestType())) {
            if (queue.get(req.getSourceName()) == null) {
                ConcurrentHashMap<String, LinkedBlockingQueue<String>> map = new ConcurrentHashMap<>();
                map.putIfAbsent(req.getParam(), new LinkedBlockingQueue<>());
                queue.putIfAbsent(req.getSourceName(), map);
                return new Resp("Create new topic", "202");
            } else {
                queue.get(req.getSourceName())
                        .putIfAbsent(req.getParam(), new LinkedBlockingQueue<>());
                String result = queue.get(req.getSourceName()).get(req.getParam()).poll();
                if (result == null) {
                    return new Resp("", "204 No Content");
                }
                return new Resp(result, "200 OK");
            }
        }
        return null;
    }
}
