import com.rabbitmq.client.*;

import java.io.IOException;

public class Receiver {

    private final static String TASK_QUEUE_NAME = "UUID";
    private static final String EXCHANGE_NAME = "rabbitmqexchange";

    public static void main(String[] argv) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(TASK_QUEUE_NAME, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting with exchange for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                try{
                    doWork(message);
                } catch (InterruptException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [x] Done ");
                }
            }
        };
        boolean autoAck = true;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
    }

    private static void doWork(String task) throws InterruptedException
    {
        //

        String UUID = "123!@#SuperSecret#@!321";
/*
        String test="";
        for (char ch: task.toCharArray()) {
            test+=ch;
            //if (ch == '.') Thread.sleep(1000);
        }*/
        System.out.println("START\r\n");
        System.out.println("UUID: "+UUID+" task: "+task);
        System.out.println("WORKING HARD...\r\n");
        Thread.sleep(5000);
        System.out.println("END\r\n");
    }
}