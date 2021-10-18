public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String type;
        String source;
        String param;
        String[] array = content.split("/");
        String pooh = array[1];
        String ln = System.lineSeparator();
        if (array[0].startsWith("POST")) {
            type = "POST";
            source = array[2].substring(0, array[2].indexOf(" "));
            String[] p = content.split(ln);
            param = p[p.length - 1];
        } else if (array[0].startsWith("GET")) {
            type = "GET";
            if (pooh.equals("queue")) {
                source = array[2].substring(0, array[2].indexOf(" "));
                param = "";
            } else if (pooh.equals("topic")) {
                source = array[2];
                param = array[3].substring(0, array[3].indexOf(" "));
            } else {
                throw new IllegalArgumentException("Arguments invalid!");
            }
        } else {
            throw new IllegalArgumentException("Arguments invalid!");
        }
        return new Req(type, pooh, source, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
