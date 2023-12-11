import javax.swing.JFrame;

public class JFrameManager {

    private static int count;

    public static void register(JFrame jFrame) {
        if (jFrame.isVisible()) {
            count++;
        }
    }

    public static void unregister(JFrame jFrame) {
        if (!jFrame.isVisible()) {
            count--;
        }
    }

    public static int getCount() {
        return count;
    }
}
