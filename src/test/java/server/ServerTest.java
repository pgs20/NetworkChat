package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ServerTest {
    @Test
    public void testServerConnection() {
        Server server = new Server();
        assertTrue(server != null);
    }

    @Test
    public void testServerPort() {
        Server server = new Server();
        assertEquals(8888, server.getPort());
    }
}
