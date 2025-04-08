public class DataPacket {
    private int sequenceNumber;    // 数据包序列号
    private int ackNumber;         // 确认号
    private byte[] data;          // 数据内容
    private boolean isAck;        // 是否为确认包
    private int windowSize;       // 窗口大小

    public DataPacket(int sequenceNumber, byte[] data) {
        this.sequenceNumber = sequenceNumber;
        this.data = data;
        this.isAck = false;
    }

    public DataPacket(int ackNumber, boolean isAck) {
        this.ackNumber = ackNumber;
        this.isAck = isAck;
    }

    // Getters and Setters
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getAckNumber() {
        return ackNumber;
    }

    public void setAckNumber(int ackNumber) {
        this.ackNumber = ackNumber;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isAck() {
        return isAck;
    }

    public void setAck(boolean ack) {
        isAck = ack;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }
}