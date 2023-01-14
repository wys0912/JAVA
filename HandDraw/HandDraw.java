import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class HandDraw {
    // 定义窗口对象
    Frame frame = new Frame("Simple HandDraw");
    // 定义画图区的宽高
    private final int AREA_WIDTH = 500;
    private final int AREA_HEIGHT = 400;
    // 定义一个右键菜单 设置画笔颜色
    private PopupMenu colorMenu = new PopupMenu();
    private MenuItem redItem = new MenuItem("red");
    private MenuItem greenItem = new MenuItem("green");
    private MenuItem blueItem = new MenuItem("blue");
    // 定义变量记录画笔颜色
    private Color forceColor = Color.BLACK;
    // 创建一个 BufferedImage 对象
    BufferedImage image = new BufferedImage(AREA_WIDTH, AREA_HEIGHT, BufferedImage.TYPE_INT_RGB);
    // 通过位图获取关联的对象
    Graphics g = image.getGraphics();
    // 记录鼠标坐标
    private int preX = -1;
    private int preY = -1;

    // 自定义一个类继承 Canvas
    private class MyCanvas extends Canvas {
        @Override
        public void paint(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }
    }
    MyCanvas drawArea = new MyCanvas();

    public void init() {
        // 组装视图
        ActionListener listener = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                switch( actionCommand ) {
                    case "red": forceColor = Color.RED;
                        break;
                    case "green": forceColor = Color.GREEN;
                        break;
                    case "blue": forceColor = Color.BLUE;
                        break;
                }
            }
        };

        redItem.addActionListener(listener);
        greenItem.addActionListener(listener);
        blueItem.addActionListener(listener);

        colorMenu.add(redItem);
        colorMenu.add(greenItem);
        colorMenu.add(blueItem);

        // 把 colorMenu 设置给绘图区域
        drawArea.add(colorMenu);
        drawArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                boolean popupTrigger = e.isPopupTrigger();
                if( popupTrigger ) {
                    colorMenu.show(drawArea, e.getX(), e.getY());
                }
                preX = -1;
                preY = -1;
            }
        });

        // 设置位图背景为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);
        // 通过监听鼠标移动完成线条绘制
        drawArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 画线条
                if( preX>0 && preY>0 ) {
                    g.setColor(forceColor);
                    g.drawLine(preX, preY, e.getX(), e.getY());
                }

                preX = e.getX();
                preY = e.getY();

                drawArea.repaint();
            }
        });

        drawArea.setPreferredSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        frame.add(drawArea);

        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new HandDraw().init();
    }
}
