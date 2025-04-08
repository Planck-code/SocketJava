import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Channel {
    private BlockingQueue<DataPacket> packetQueue;
    private Random random;
    private double lossRate;           // 丢包率
    private int propagationDelay;      // 传播延迟(ms)

    public Channel(double lossRate, int propagationDelay) {
        this.packetQueue = new LinkedBlockingQueue<>();
        this.random = new Random();
        this.lossRate = lossRate;
        this.propagationDelay = propagationDelay;
    }

    public void sendPacket(DataPacket packet) {
        // 模拟丢包
        if (random.nextDouble() < lossRate) {
            System.out.println("Packet lost: " + packet.getSequenceNumber());
            return;
        }

        // 模拟传播延迟
        new Thread(() -> {
            try {
                Thread.sleep(propagationDelay);
                packetQueue.put(packet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public DataPacket receivePacket() throws InterruptedException {
        return packetQueue.take();
    }

    public boolean hasPacket() {
        return !packetQueue.isEmpty();
    }

    // Getters and Setters
    public double getLossRate() {
        return lossRate;
    }

    public void setLossRate(double lossRate) {
        this.lossRate = lossRate;
    }

    public int getPropagationDelay() {
        return propagationDelay;
    }

    public void setPropagationDelay(int propagationDelay) {
        this.propagationDelay = propagationDelay;
    }
}