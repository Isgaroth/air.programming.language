package air.modules;

import air.lib.*;
import air.modules.core.Module;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Art implements Module {

    private static final String PATH = "D:/&projects/ide/Intellij IDEA/air.plugin/resources/icons/air.logo.png";
    private static final ImageIcon ICON = new ImageIcon(PATH);
    private static final NumberValue MINUS_ONE = new NumberValue(-1);

    private static JFrame frame;
    private static CanvasPanel panel;
    private static Graphics2D graphics;
    private static BufferedImage img;

    private static NumberValue pressedKey;
    private static ArrayValue mouseMotion;

    @Override
    public void init() {
        FunctionSet.setKey("window", new CreateWindow());
        FunctionSet.setKey("pressedKey", new KeyPressed());
        FunctionSet.setKey("mouseMotion", new MouseMotion());
        FunctionSet.setKey("line", consumerConvert(Art::line));
        FunctionSet.setKey("oval", consumerConvert(Art::oval));
        FunctionSet.setKey("fillOval", consumerConvert(Art::fillOval));
        FunctionSet.setKey("rectangle", consumerConvert(Art::rectangle));
        FunctionSet.setKey("fillRectangle", consumerConvert(Art::fillRectangle));
        FunctionSet.setKey("clip", consumerConvert(Art::clip));
        FunctionSet.setKey("text", new DrawString());
        FunctionSet.setKey("color", new SetColor());
        FunctionSet.setKey("repaint", new Repaint());

        Variables.setKey("UP", new NumberValue(KeyEvent.VK_UP));
        Variables.setKey("DOWN", new NumberValue(KeyEvent.VK_DOWN));
        Variables.setKey("LEFT", new NumberValue(KeyEvent.VK_LEFT));
        Variables.setKey("RIGHT", new NumberValue(KeyEvent.VK_RIGHT));
        Variables.setKey("ENTER", new NumberValue(KeyEvent.VK_ENTER));
        Variables.setKey("ESCAPE", new NumberValue(KeyEvent.VK_ESCAPE));

        pressedKey = MINUS_ONE;
        mouseMotion = new ArrayValue(new Value[] { NumberValue.ZERO, NumberValue.ZERO });
    }

    @FunctionalInterface
    private interface Consumer {
        void accept(int i1, int i2, int i3, int i4);
    }

    private static void line(int x1, int y1, int x2, int y2) {
        graphics.drawLine(x1, y1, x2, y2);
    }

    private static void oval(int x, int y, int w, int h) {
        graphics.drawOval(x, y, w, h);
    }

    private static void fillOval(int x, int y, int w, int h) {
        graphics.fillOval(x, y, w, h);
    }

    private static void rectangle(int x, int y, int w, int h) {
        graphics.drawRect(x, y, w, h);
    }

    private static void fillRectangle(int x, int y, int w, int h) {
        graphics.fillRect(x, y, w, h);
    }

    private static void clip(int x, int y, int w, int h) {
        graphics.setClip(x, y, w, h);
    }

    private static Function consumerConvert(Consumer consumer) {
        return args -> {
            if (args.length != 4) throw new RuntimeException("Four args expected");
            int x = (int) args[0].asNumber(), y = (int) args[1].asNumber();
            int w = (int) args[2].asNumber(), h = (int) args[3].asNumber();
            consumer.accept(x, y, w, h);
            return NumberValue.ZERO;
        };
    }

    private static class CanvasPanel extends JPanel {

        public CanvasPanel(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            graphics = img.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setFocusable(true);
            requestFocus();
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent event) {
                    pressedKey = new NumberValue(event.getKeyCode());
                }
                @Override
                public void keyReleased(KeyEvent event) {
                    pressedKey = MINUS_ONE;
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    mouseMotion.set(0, new NumberValue(e.getX()));
                    mouseMotion.set(1, new NumberValue(e.getY()));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    }

    private static class CreateWindow implements Function {

        @Override
        public Value launch(Value... args) {
            String title = "";
            int width = 640;
            int height = 480;
            switch (args.length) {
                case 1:
                    title = args[0].asString();
                    break;
                case 2:
                    width = (int) args[0].asNumber();
                    height = (int) args[1].asNumber();
                    break;
                case 3:
                    title = args[0].asString();
                    width = (int) args[1].asNumber();
                    height = (int) args[2].asNumber();
                    break;
            }
            panel = new CanvasPanel(width, height);
            frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setIconImage(ICON.getImage());
            frame.add(panel);
            frame.pack();
            frame.setVisible(true);
            return NumberValue.ZERO;
        }
    }

    private static class KeyPressed implements Function {

        @Override
        public Value launch(Value... args) {
            return pressedKey;
        }
    }

    private static class MouseMotion implements Function {

        @Override
        public Value launch(Value... args) {
            return mouseMotion;
        }
    }

    private static class DrawString implements Function {

        @Override
        public Value launch(Value... arguments) {
            if (arguments.length != 3) throw new RuntimeException("Three arguments expected");
            int x = (int) arguments[1].asNumber(), y = (int) arguments[2].asNumber();
            graphics.drawString(arguments[0].asString(), x, y);
            return NumberValue.ZERO;
        }
    }

    private static class Repaint implements Function {

        @Override
        public Value launch(Value... args) {
            panel.invalidate();
            panel.repaint();
            return NumberValue.ZERO;
        }
    }

    private static class SetColor implements Function {

        @Override
        public Value launch(Value... args) {
            if (args.length == 1) {
                graphics.setColor(new Color((int) args[0].asNumber()));
                return NumberValue.ZERO;
            }
            int r = (int) args[0].asNumber();
            int g = (int) args[1].asNumber();
            int b = (int) args[2].asNumber();
            graphics.setColor(new Color(r, g, b));
            return NumberValue.ZERO;
        }

    }
}