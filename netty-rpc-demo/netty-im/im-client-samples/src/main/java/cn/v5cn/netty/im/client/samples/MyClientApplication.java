package cn.v5cn.netty.im.client.samples;

import java.util.Scanner;

public class MyClientApplication {

    private final static String CONNECTOR_HOST = "127.0.0.1";
    private final static Integer CONNECTOR_PORT = 19081;
    private final static String REST_URL = "http://127.0.0.1:8082";

    public static void main(String[] args) {
        System.out.println("please login");

        Scanner scan = new Scanner(System.in);

        String username = scan.next();
        String password = scan.next();

        MyClient myClient = new MyClient(CONNECTOR_HOST, CONNECTOR_PORT, REST_URL, username, password);

        System.out.println("\r\nlogin successfully (^_^)\r\n");

        myClient.printUserInfo();

        System.out.println("\r\nnow send msg to your friends\r\n\r\n");

        while (scan.hasNext()) {
            String userId = scan.next();
            String text = scan.next();
            myClient.send(userId, text);
        }
        scan.close();
    }
}
