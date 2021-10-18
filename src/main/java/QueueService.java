
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueService implements Service {

    private static final String POST = "POST";
    private static final String GET = "GET";
    private final ConcurrentHashMap<String, BlockingQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if (POST.equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new LinkedBlockingDeque<>());
            queue.get(req.getSourceName()).offer(req.getParam());
                return new Resp("Parameters is added", "200 OK");
        } else if (GET.equals(req.httpRequestType())) {
            if (queue.isEmpty()) {
                return new Resp("There is no content to send for this request.", "204 No Content");
            }
            String result = queue.get(req.getSourceName()).poll();
            if (result == null) {
                return new Resp("There is no content to send for this " + req.getSourceName(), "204 No Content");
            }
            return new Resp(result, "200 OK");
        }
        return new Resp("", "Error");
    }
}