package Domain;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static AtomicInteger orderIdCounter = new AtomicInteger(0);

    public static int generateOrderId() {
        return orderIdCounter.incrementAndGet();
    }
}