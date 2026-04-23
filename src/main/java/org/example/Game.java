package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Game extends JPanel {
    private Block[][] blocks;
    private TypeOfBlock turn;
    private int yellowWins;
    private int redWins;
    private Timer timer;
    private Thread timerThread;
    private int seconds;
    private int minutes;
    private int hours;
    private boolean gameIsEnd;


    public void Run(){
        turn = TypeOfBlock.Red;
        yellowWins = 0;
        redWins = 0;

        initialize();
        makeWindow();
    }

    private void initialize(){
        gameIsEnd = false;
        timer = new Timer();
        timerThread = new Thread(timer);
        timerThread.start();


        blocks = new Block[6][7];
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++){
                blocks[j][i] = new Block(j, i);
            }
        }

        Thread game = new Thread(() -> {
            while (true) {
                try {
//                    SwingUtilities.invokeLater(this::repaint);
                    repaint();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        game.start();
        //blocks[0][0].setType(TypeOfBlock.Red);
        //blocks[1][0].setType(TypeOfBlock.Yellow);



    }

    private void changeTurn(){
        if(turn == TypeOfBlock.Yellow){
            turn = TypeOfBlock.Red;
        }else {
            turn = TypeOfBlock.Yellow;
        }
        System.out.println("Ход сменён на " + turn.toString());
    }

    private void placeBlock(int col){

            for (int i = 0; i < 6; i++) {
                //if(blocks[i][col].type == TypeOfBlock.None){
                    if (blocks[0][col].type != TypeOfBlock.None) {
                       System.out.println("Колонка полная, ход остался");
                       break;
                    }

                    if (blocks[i][col].type != TypeOfBlock.None) {
                        blocks[i - 1][col].type = turn;
                        checkField();
                        changeTurn();
                        System.out.println("Блок поставлен на другой блок");
                        break;
                    }
                    if (i == 5) {
                        blocks[i][col].type = turn;
                        checkField();
                        changeTurn();
                        System.out.println("Блок поставлен в самый низ");
                        break;

                }

                //}

            }

        System.out.println("---");
    }

    private void win(){
        if(turn == TypeOfBlock.Yellow){
            yellowWins+=1;
        }else{
            redWins+=1;
        }
        initialize();
    }

    private void checkField(){

        // Горизонтали
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c <= 7 - 4; c++) {
                if (blocks[r][c].type == turn && blocks[r][c+1].type == turn &&
                        blocks[r][c+2].type == turn && blocks[r][c+3].type == turn) {
                            gameIsEnd = true;
                            changeTurn();
                };
            }
        }
        // Вертикали
        for (int c = 0; c < 7; c++) {
            for (int r = 0; r <= 6 - 4; r++) {
                if (blocks[r][c].type == turn && blocks[r+1][c].type == turn &&
                        blocks[r+2][c].type == turn && blocks[r+3][c].type == turn) {
                    gameIsEnd = true;
                    changeTurn();

                };
            }
        }
        // Диагонали вниз-вправо
        for (int r = 0; r <= 6 - 4; r++) {
            for (int c = 0; c <= 7 - 4; c++) {
                if (blocks[r][c].type == turn && blocks[r+1][c+1].type == turn &&
                        blocks[r+2][c+2].type == turn && blocks[r+3][c+3].type == turn) {
                    gameIsEnd = true;
                    changeTurn();
                };
            }
        }
        // Диагонали вниз-влево
        for (int r = 0; r <= 6 - 4; r++) {
            for (int c = 3; c < 7; c++) {
                if (blocks[r][c].type == turn && blocks[r+1][c-1].type == turn &&
                        blocks[r+2][c-2].type == turn && blocks[r+3][c-3].type == turn) {
                    gameIsEnd = true;
                    changeTurn();
                };
            }
        }

    }



    @Override
    public void paint(Graphics g){
        super.paintComponent(g);

        if(!gameIsEnd) {


            g.setColor(new Color(30, 144, 255));
            g.fillRect(50, 100, 510, 440);
            g.setColor(new Color(65, 105, 225));
            g.drawRect(50, 100, 510, 440);


            //Отрисовка таймера
            g.setColor(new Color(255, 255, 255));

            hours = timer.getSeconds() / 3600;
            minutes = (timer.getSeconds() - (3600 * hours)) / 60;
            seconds = timer.getSeconds() % 60;

            int[] time = new int[3];
            time[0] = hours;
            time[1] = minutes;
            time[2] = seconds;
            String[] timeString = new String[3];
            for (int i = 0; i < 3; i++) {
                if (time[i] == 0) {
                    timeString[i] = "00";
                } else if (time[i] > 0 && time[i] < 10) {
                    timeString[i] = "0" + time[i];
                } else if (time[i] >= 10) {
                    timeString[i] = Integer.toString(time[i]);
                }

            }
            for (int i = 0; i < 3; i++) {
                g.drawString(timeString[i], getWidth() / 2 - 50 + (i * 50), 20);
            }
            g.drawString(":", getWidth() / 2 - 25, 20);
            g.drawString(":", getWidth() / 2 + 25, 20);
            g.setColor(new Color(255, 255, 255));


            //Отрисовка "Сейчас ходит"
            g.setColor(new Color(174, 221, 223));
            g.fillRect(50, 30, 510, 40);
            g.setColor(new Color(65, 105, 225));
            g.setFont(Font.decode("Arial-Bold-20"));
            g.drawString("Сейчас ходит: ", 58, 55);
            switch (turn) {
                case TypeOfBlock.None -> {
                    g.setColor(getBackground());
                    break;
                }
                case Red -> {
                    g.setColor(new Color(220, 20, 60, 255));
                    g.fillOval(200, 35, 30, 30);
                    g.setColor(new Color(198, 28, 58));
                    g.fillOval(203, 38, 24, 24);
                    g.setColor(new Color(211, 63, 89));
                    g.fillOval(206, 41, 12, 12);
                    break;
                }
                case Yellow -> {
                    g.setColor(new Color(255, 215, 0));
                    g.fillOval(200, 35, 30, 30);
                    g.setColor(new Color(255, 186, 0));
                    g.fillOval(203, 38, 24, 24);
                    g.setColor(new Color(255, 229, 84));
                    g.fillOval(206, 41, 12, 12);
                    break;
                }
            }


            //Отрисовка счёта
            g.setColor(new Color(65, 105, 225));
            g.setFont(Font.decode("Arial-Bold-20"));
            g.drawString("Счёт: ", 260, 55);
            g.setColor(new Color(220, 20, 60, 255));
            g.setFont(Font.decode("Arial-Bold-24"));
            g.drawString(Integer.toString(redWins), 319, 58);
            g.setColor(new Color(65, 105, 225));
            g.setFont(Font.decode("Arial-Bold-20"));
            g.drawString(":", 333, 55);
            g.setColor(new Color(255, 128, 0, 255));
            g.setFont(Font.decode("Arial-Bold-24"));
            g.drawString(Integer.toString(yellowWins), 340, 58);


            //Кнопка сброса
            g.setColor(new Color(106, 131, 204));
            g.fillRect(405, 35, 150, 30);
            g.setColor(new Color(174, 221, 223));
            g.drawString("Сброс игры", 410, 56);


            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    switch (blocks[j][i].type) {
                        case TypeOfBlock.None -> {
                            g.setColor(getBackground());
                            g.fillOval(70 + 70 * i, 120 + 70 * j, 50, 50);
                            break;
                        }
                        case Red -> {
                            g.setColor(new Color(220, 20, 60, 255));
                            g.fillOval(70 + 70 * i, 120 + 70 * j, 50, 50);
                            g.setColor(new Color(198, 28, 58));
                            g.fillOval(75 + 70 * i, 125 + 70 * j, 40, 40);
                            g.setColor(new Color(211, 63, 89));
                            g.fillOval(82 + 70 * i, 132 + 70 * j, 20, 20);
                            break;
                        }
                        case Yellow -> {
                            g.setColor(new Color(255, 215, 0));
                            g.fillOval(70 + 70 * i, 120 + 70 * j, 50, 50);
                            g.setColor(new Color(255, 186, 0));
                            g.fillOval(75 + 70 * i, 125 + 70 * j, 40, 40);
                            g.setColor(new Color(255, 229, 84));
                            g.fillOval(82 + 70 * i, 132 + 70 * j, 20, 20);
                            break;
                        }
                    }
                }
            }
        } else {
            g.setColor(new Color(207, 180, 183, 255));
            g.fillRect(50, 50, getWidth()-100, getHeight()-100);
            g.setColor(new Color(0, 0, 0));
            g.setFont(Font.decode("Arial-Bold-40"));
            g.drawString("ПОБЕДА " + turn.toString().toUpperCase(), 100, getHeight()/2 );
        }
    }

    private void makeWindow(){
        JFrame frame = new JFrame();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!gameIsEnd){
                 if (e.getX() > 60 && e.getX() < 550 && e.getY() > 100 && e.getY() < 540) {
                      System.out.println("Нажато " + e.getX() + " это ряд номер " + (e.getX()-60)/70);
                      placeBlock((e.getX()-60)/70);
                   } else if (e.getX() > 405 && e.getX() < 555 && e.getY() > 35 && e.getY() < 65) {
                    System.out.println("""
                                    ---
                                Сброс игры
                                ---""");
                        initialize();
                    }
                }else{
                    win();
                }

                repaint();
            }
        });

        this.setBackground(new  Color(74, 71, 71));
        frame.add(this);
        frame.setSize(615, 590);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
    }
}
