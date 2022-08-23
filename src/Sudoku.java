
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Sudoku {

    static JFrame frame;
    static Panal p;
    private static int[][] grid;
    private static int[][] temp;
    private static Random ran = new Random();
    private static int level;
    private static final int WindowWidth = 1000;
    private static final int WindowHeight = 1000;
    public static byte step = 0;
    public static String levelgame;
    public static File file1 = new File("Matrixsavegameuser.txt");
    public static File file3 = new File("Matrixexit.txt");

    public static void main(String[] args) {
        grid = new int[9][9];
        temp = new int[9][9];
        frame = new JFrame();
        frame.setTitle("Trò chơi Sudoku - Nguyễn Quang Minh B1906520");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        p = new Panal();
        frame.setContentPane(p);
        frame.setVisible(true);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("sudoku.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                    "Không tìn thấy file logo phần mềm\n",
                    "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        frame.setResizable(false);
        frame.setIconImage(image);
        frame.setSize(WindowWidth, WindowHeight);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setLocationRelativeTo(null);
        try {
            try {
                if (file3.exists()) {
                    if (p.readNewMatrixsaveexit(file3)) {
                        p.timer.start();
                        p.slove.setEnabled(true);
                        p.reset.setEnabled(true);
                        p.save.setEnabled(true);
                    } else {
                        p.restgame();
                    }
                } else {
                    p.slove.setEnabled(false);
                    p.reset.setEnabled(false);
                    p.save.setEnabled(false);
                }
                if (file1.exists()) {
                    p.reloadsave.setEnabled(true);
                } else {
                    p.reloadsave.setEnabled(false);
                }
            } catch (Exception ex) {
            }
        } catch (NullPointerException ex) {
        }
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JFrame frame = (JFrame) e.getSource();
                p.timer.stop();
                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "Bạn chắc chắn thoát khỏi trò chơi?",
                        "Thoát khỏi trò chơi",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    if (!p.slove.isEnabled()) {
                        file3.deleteOnExit();
                        System.exit(0);
                    } else {
                        boolean testsaveexit = true;
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                if (!p.boxes[i][j].getText().matches("[1-9]")) {
                                    if (!p.boxes[i][j].getText().isEmpty()) {
                                        testsaveexit = false;
                                    }
                                }
                            }
                        }
                        if (testsaveexit) {
                            p.saveFileexit();
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Ma trận không hợp lệ. Lưu không thành công.", "Báo lỗi!", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    p.timer.start();
                }
            }
        });
        frame.setVisible(true);
    }

    public static void newGame() {
        int k = 0;
        ArrayList<Integer> randomnumber = getRandomNum();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = 0;
                if (((j + 2) % 2) == 0 && ((i + 2) % 2) == 0) {
                    grid[i][j] = randomnumber.get(k);
                    k++;
                    if (k == 9) {
                        k = 0;
                    }
                }
            }
        }
        if (search(grid)) {
            if (level == 3) {
                levelgame = "Dễ";
            } else if (level == 4) {
                levelgame = "Trung bình";
            } else if (level == 5) {
                levelgame = "Khó";
            } else {
                levelgame = "Cực khó";
            }
            JOptionPane.showMessageDialog(p.center, "Tạo màn chơi thành công với độ khó: " + levelgame, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        int rann = ran.nextInt(level);
        int c = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                temp[i][j] = 0;
                if (c < rann) {
                    c++;
                    continue;
                } else {
                    rann = ran.nextInt(level);
                    c = 0;
                    temp[i][j] = grid[i][j];
                }
            }
        }
        p.setarray(grid, temp);
        p.setTextLable();
    }

    public static int[][] getFreeCellList(int[][] grid) {
        int numberOfFreeCells = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    numberOfFreeCells++;
                }
            }
        }
        int[][] freeCellList = new int[numberOfFreeCells][2];
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    freeCellList[count][0] = i;
                    freeCellList[count][1] = j;
                    count++;
                }
            }
        }
        return freeCellList;
    }

    public static boolean search(int[][] grid) {
        int[][] freeCellList = getFreeCellList(grid);
        int k = 0;
        boolean found = false;

        while (!found) {
            //nhận từng phần tử rỗng
            int i = freeCellList[k][0];
            int j = freeCellList[k][1];
            //nếu phần tử bằng 0 đưa ra 1 cho kiểm tra đầu tiên
            if (grid[i][j] == 0) {
                grid[i][j] = 1;
            }
            //bây giờ kiểm tra 1 nếu có sẵn
            if (isAvaible(i, j, grid)) {
                //nếu freeCellList bằng k  ==> bảng đã giải
                if (k + 1 == freeCellList.length) {
                    found = true;
                } else {
                    k++;
                }
            } 
            else if (grid[i][j] < 9) {//tăng phần tử lên 1 
                grid[i][j] = grid[i][j] + 1;
            } else { //nếu giá trị phần tử bằng 9 quay lui về phần tử trước đó
                while (grid[i][j] == 9) {
                    grid[i][j] = 0;
                    if (k == 0) {
                        return false;
                    }
                    k--;// quay lại phần tử sau
                    i = freeCellList[k][0];
                    j = freeCellList[k][1];
                }
                grid[i][j] = grid[i][j] + 1;
            }
        }

        return true;
    }

    public static boolean isAvaible(int i, int j, int[][] grid) {
        for (int column = 0; column < 9; column++) {
            if (column != j && grid[i][column] == grid[i][j]) {
                return false;
            }
        }
        for (int row = 0; row < 9; row++) {
            if (row != i && grid[row][j] == grid[i][j]) {
                return false;
            }
        }
        for (int row = (i / 3) * 3; row < (i / 3) * 3 + 3; row++) {
            for (int col = (j / 3) * 3; col < (j / 3) * 3 + 3; col++) {
                if (row != i && col != j && grid[row][col] == grid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Integer> getRandomNum() {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (Integer i = 1; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    public static void setlevel(int lev) {
        level = lev;
    }
}
