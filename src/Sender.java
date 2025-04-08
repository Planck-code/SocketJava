import java.util.Timer;
import java.util.TimerTask;

public class Sender {
    private WindowBuffer window;
    private Channel channel;
    private Timer timer;
    private static final int TIMEOUT = 1000;  // 超时时间(ms)

    public Sender(Channel channel) {
        this.window = new WindowBuffer();
        this.channel = channel;
        this.timer = new Timer();
    }

    public void send(byte[] data) {
        if (window.canSend()) {
            int seqNum = window.getNextSeqNum();
            DataPacket packet = new DataPacket(seqNum, data);
            window.addPacket(packet);
            channel.sendPacket(packet);
            
            // 设置超时重传
            scheduleTimeout(seqNum);
            
            System.out.println("Sent packet: " + seqNum);
        } else {
            System.out.println("Window is full, waiting for ACKs...");
        }
    }

    public void receiveAck(DataPacket ackPacket) {
        int ackNum = ackPacket.getAckNumber();
        System.out.println("Received ACK: " + ackNum);
        window.receiveAck(ackNum);
    }

    private void scheduleTimeout(int seqNum) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (seqNum >= window.getBase() && seqNum < window.getNextSeqNum()) {
                    System.out.println("Timeout for packet: " + seqNum);
                    DataPacket packet = window.getPacket(seqNum);
                    if (packet != null) {
                        channel.sendPacket(packet);
                        scheduleTimeout(seqNum);  // 重新设置超时
                    }
                }
            }
        };
        timer.schedule(task, TIMEOUT);
    }

    public void stop() {
        timer.cancel();
    }

    public WindowBuffer getWindow() {
        return window;
    }
}