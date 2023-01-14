package awt.PinBall;

import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class PinBall {
    private Frame frame = new Frame("PinBall Game");
    // 桌面的宽度和高度
    private final int TABLE_WIDTH = 300;
    private final int TABLE_HEIGHT = 400;
    // 球拍的宽度和高度
    private final int RACKET_WIDTH = 60;
    private final int RACKET_HEIGHT = 20;
    // 小球大小
    private final int BALL_SIZE = 16;
    // 小球坐标
    private int ball_X = 120;
    private int ball_Y = 20;
    // 小球在x y方向上移动的速度
    private int speed_X = 5;
    private int speed_Y = 10;
    // 球拍的坐标
    private int racket_X = 120;
    private final int racket_Y = 340;
    // 游戏是否结束
    private boolean isOver = false;
    // 定时器
    private Timer timer;

    // 自定义类继承Canvas充当画布
    private class MyCanvas extends Canvas{
        @Override
        public void paint(Graphics g) {
            // 在这里绘制内容
            if( isOver ) {  // 游戏结束
                g.setColor(Color.BLUE);
                g.setFont(new Font("Times", Font.BOLD, 30));
                g.drawString("Game Over!", 50, 200);
            } else {        // 游戏中
                // 绘制小球
                g.setColor(Color.RED);
                g.fillOval(ball_X, ball_Y, BALL_SIZE, BALL_SIZE);
                // 绘制球拍
                g.setColor(Color.PINK);
                g.fillRect(racket_X, racket_Y, RACKET_WIDTH, RACKET_HEIGHT);
            }
        }
    }

    // 创建绘画区域
    MyCanvas drawArea = new MyCanvas();

    public void init() {
        // 组装视图 实现游戏逻辑控制

        // 球拍坐标的变化
        KeyListener listener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if( keyCode==KeyEvent.VK_LEFT ) {   // <-
                    if( racket_X>10 ) {
                        racket_X -= 10;
                    } else {
                        racket_X = 0;
                    }
                } else if( keyCode==KeyEvent.VK_RIGHT ) {   // ->
                    if( racket_X<TABLE_WIDTH-RACKET_WIDTH ) {
                        racket_X += 10;
                    } else {
                        racket_X = TABLE_WIDTH - RACKET_WIDTH;
                    }
                }
            }
        };

        // 给 Frame 和 drawArea 绑定监听事件
        frame.addKeyListener(listener);
        drawArea.addKeyListener(listener);

        ActionListener task = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 根据边界修正速度
                if( ball_X<=0 || ball_X>=TABLE_WIDTH-BALL_SIZE ) {
                    speed_X = -speed_X;
                }
                if( ball_Y<=0 || (ball_Y>racket_Y-BALL_SIZE && ball_X>racket_X && ball_X<racket_X+RACKET_WIDTH)) {
                    speed_Y = -speed_Y;
                }
                if( ball_Y>racket_Y-BALL_SIZE && (ball_X<racket_X || ball_X>racket_X+RACKET_WIDTH) ) {
                    System.out.println(ball_X+" "+racket_X+" ");
                    // 停止计时器
                    timer.stop();
                    // 修改结束标记
                    isOver = true;
                    // 重绘界面
                    drawArea.repaint();
                }

                // 更新小球坐标
                ball_X += speed_X;
                ball_Y += speed_Y;

                drawArea.repaint();
            }

        };

        // 小球控制
        timer = new Timer(100, task);
        timer.start();

        drawArea.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
        frame.add(drawArea);

        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new PinBall().init();
    }
}
