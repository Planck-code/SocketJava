public class SlidingWindowTest {
    public static void main(String[] args) {
        // 创建模拟网络通道，设置丢包率为0.2，传播延迟为100ms
        Channel channel = new Channel(0.2, 100);

        // 创建发送方和接收方
        Sender sender = new Sender(channel);
        Receiver receiver = new Receiver(channel);

        // 启动接收线程
        Thread receiveThread = new Thread(() -> {
            try {
                while (true) {
                    if (channel.hasPacket()) {
                        DataPacket packet = channel.receivePacket();
                        if (packet.isAck()) {
                            sender.receiveAck(packet);
                        } else {
                            receiver.receivePacket(packet);
                        }
                    }
                    Thread.sleep(50);  // 短暂休眠，避免过度消耗CPU
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();

        // 测试数据发送
        try {
            // 发送一系列测试数据
            String[] testMessages = {
                "Message 1: Hello",
                "Message 2: Sliding",
                "Message 3: Window",
                "Message 4: Protocol",
                "Message 5: Test"
            };

            for (String message : testMessages) {
                sender.send(message.getBytes());
                Thread.sleep(200);  // 间隔发送，便于观察
            }

            // 等待一段时间，确保所有数据都被处理
            Thread.sleep(5000);

            // 停止发送方的定时器
            sender.stop();
            // 停止接收线程
            receiveThread.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}