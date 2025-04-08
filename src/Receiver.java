public class Receiver {
    private WindowBuffer window;
    private Channel channel;

    public Receiver(Channel channel) {
        this.window = new WindowBuffer();
        this.channel = channel;
    }

    public void receivePacket(DataPacket packet) {
        if (window.isExpectedPacket(packet)) {
            System.out.println("Received packet: " + packet.getSequenceNumber());
            window.receivePacket(packet);
            
            // 发送确认包
            sendAck(packet.getSequenceNumber());
            
            // 处理接收到的数据
            processData(packet.getData());
        } else {
            // 对于失序的包，重新发送上一个确认
            sendAck(window.getExpectedSeqNum() - 1);
            System.out.println("Out of order packet received: " + packet.getSequenceNumber() + 
                             ", expected: " + window.getExpectedSeqNum());
        }
    }

    private void sendAck(int ackNum) {
        DataPacket ackPacket = new DataPacket(ackNum, true);
        channel.sendPacket(ackPacket);
        System.out.println("Sent ACK: " + ackNum);
    }

    private void processData(byte[] data) {
        // 在实际应用中，这里可以处理接收到的数据
        // 例如写入文件或转发给应用层
        if (data != null) {
            String message = new String(data);
            System.out.println("Processed data: " + message);
        }
    }

    public WindowBuffer getWindow() {
        return window;
    }
}