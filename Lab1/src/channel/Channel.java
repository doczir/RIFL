package channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class Channel {

    public <Handler extends Consumer<Message>, Message> void add(Class<? extends Message> msgType, Handler handler) {
        getInstance(msgType).add(handler);
    }

    public <Message> void broadcast(Message msg) {
        getInstance(msg.getClass()).broadcast(msg);
    }

    private InternalChannel getInstance(Class<?> clazz) {
        InternalChannel instance = instances.get(clazz);
        if (instance == null) {
            instance = new InternalChannel();
            instances.put(clazz, instance);
        }
        return instance;
    }

    private final Map<Class, InternalChannel<?>> instances = new HashMap<>();

    private class InternalChannel<Message> {

        private final List<Consumer<Message>> handlers;

        private InternalChannel() {
            handlers = new ArrayList<>();
        }

        public <Handler extends Consumer<Message>> void add(Handler handler) {
            handlers.add(handler);
        }

        public void broadcast(Message msg) {
            for (Consumer<Message> handler : handlers) {
                handler.accept(msg);
            }
        }
    }
}