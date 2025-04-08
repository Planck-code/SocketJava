public class WindowBuffer {
    private static final int MAX_WINDOW_SIZE = 8;  // 最大窗口大小
    private DataPacket[] buffer;                   // 数据包缓冲区
    private int base;                              // 窗口基序号
    private int nextSeqNum;                        // 下一个待发送的序列号
    private boolean[] ackReceived;                 // 确认状态数组
    private int expectedSeqNum;                    // 期望接收的序列号

    public WindowBuffer() {
        this.buffer = new DataPacket[MAX_WINDOW_SIZE];
        this.ackReceived = new boolean[MAX_WINDOW_SIZE];
        this.base = 0;
        this.nextSeqNum = 0;
        this.expectedSeqNum = 0;
    }

    // 发送方方法
    public boolean canSend() {
        return nextSeqNum < base + MAX_WINDOW_SIZE;
    }

    public void addPacket(DataPacket packet) {
        if (canSend()) {
            int index = nextSeqNum % MAX_WINDOW_SIZE;
            buffer[index] = packet;
            ackReceived[index] = false;
            nextSeqNum++;
        }
    }

    public void receiveAck(int ackNum) {
        if (ackNum >= base && ackNum < nextSeqNum) {
            ackReceived[ackNum % MAX_WINDOW_SIZE] = true;
            // 滑动窗口
            while (base < nextSeqNum && ackReceived[base % MAX_WINDOW_SIZE]) {
                ackReceived[base % MAX_WINDOW_SIZE] = false;
                buffer[base % MAX_WINDOW_SIZE] = null;
                base++;
            }
        }
    }

    // 接收方方法
    public boolean isExpectedPacket(DataPacket packet) {
        return packet.getSequenceNumber() == expectedSeqNum;
    }

    public void receivePacket(DataPacket packet) {
        if (isExpectedPacket(packet)) {
            expectedSeqNum++;
        }
    }

    // Getters
    public int getBase() {
        return base;
    }

    public int getNextSeqNum() {
        return nextSeqNum;
    }

    public int getExpectedSeqNum() {
        return expectedSeqNum;
    }

    public DataPacket getPacket(int seqNum) {
        return buffer[seqNum % MAX_WINDOW_SIZE];
    }

    public int getWindowSize() {
        return MAX_WINDOW_SIZE;
    }
}