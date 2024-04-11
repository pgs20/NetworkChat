package client;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ClientTest {
    @Test
    public void testSendMessage() {
        Client client = new Client();
        assertTrue(client != null);
    }
}
