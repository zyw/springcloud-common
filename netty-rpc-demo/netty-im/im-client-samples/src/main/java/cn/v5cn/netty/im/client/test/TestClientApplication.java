package cn.v5cn.netty.im.client.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestClientApplication {

    public static void main(String[] args) {
        List<TestClient> testClients = new ArrayList<>();
        String[] usernameForTest = {
                "Adela", "Alice", "Bella", "Cynthia", "Freda", "Honey",
                "Irene", "Iris", "Joa", "Juliet", "Lisa", "Mandy", "Nora",
                "Olive", "Tom", "xianyy", "yuanrw",
        };

        // login all user
        for(int i = 0; i < 17; i++) {
            testClients.add(new TestClient("127.0.0.1", 9081,
                    "http://127.0.0.1:8082", usernameForTest[i], "123abc"));
        }

        // print test result every 5 seconds
        final ScheduledExecutorService printExecutor = Executors.newScheduledThreadPool(1);

        doInExecutor(printExecutor, 5, () -> {
            System.out.println("\n\n");

            System.out.println(String.format("sentMsg: %d, readMsg: %d, hasSentAck: %d, hasDeliveredAck: %d, hasReadAct: %d, hasException: %d",
                        TestClient.sendMsg.get(), TestClient.readMsg.get(), TestClient.hasSentAck.get(),
                        TestClient.hasDeliveredAck.get(), TestClient.hasReadAck.get(), TestClient.hasException.get()
                    ));

            System.out.println("\n\n");
        });
    }

    private static void doInExecutor(ScheduledExecutorService executorService, int period, Runnable doFunction) {
        executorService.scheduleAtFixedRate(doFunction, 0, period, TimeUnit.SECONDS);
    }
}
