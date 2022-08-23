
import com.sun.tools.javac.jvm.ByteCodes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import java.awt.FlowLayout;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.lang.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JFrame;

public class Panal extends javax.swing.JPanel {

    Sudoku game;
    JPanel pnlAlign = new JPanel();
    public Timer timer;
    public static JTextField[][] boxes;
    private JLabel label = new JLabel("Thời gian: 00:00");
    private JPanel[][] paneles;
    public JPanel center, bPanel;
    public JButton nBtn, cBtn, eBtn, hardBtn, midBtn, easyBtn, slove, about, save, load, info, reset, delete, superhardBtn, tutorial, reloadsave;
    private int[][] temp = new int[9][9];
    private int[][] grid = new int[9][9];
    private int counter;
    ButtonGroup bgNumbers;
    JToggleButton[] btnNumbers;
    File file1 = new File("Matrixsavegameuser.txt");
    File file2 = new File("Matrixreset.txt");
    File file3 = new File("Matrixexit.txt");

    public JTextField newtextfield() {
        JTextField j = new JTextField("");
        j.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        j.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));
        j.setHorizontalAlignment(JTextField.CENTER);
        j.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (j.isEditable()) {
                    ((JTextField) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.decode("#f6ea80")));
                    ((JTextField) e.getSource()).setBackground(Color.decode("#f6ea80"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (j.isEditable()) {
                    ((JTextField) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.lightGray));
                    ((JTextField) e.getSource()).setBackground(Color.white);
                }
            }
        });
        j.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (j.isEditable()) {
                    ((JTextField) e.getSource()).setForeground(Color.decode("#0c4"));
                } else {
                    ((JTextField) e.getSource()).setForeground(Color.black);
                }
            }
        });
        return j;
    }

    public Panal() {
        initComponents();
        center = new JPanel();
        center.setLayout(new GridLayout(3, 3)); // tạo grid 3*3 
        center.setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        add(center); //thêm bảng điều khiển chính vào khung
        boxes = new JTextField[9][9];
        paneles = new JPanel[3][3];
        label.setForeground(Color.black);
        /*------------------------Tạo ra các các vùng -------------------------------------*/
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                paneles[i][j] = new JPanel();
                paneles[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                paneles[i][j].setLayout(new GridLayout(3, 3));
                center.add(paneles[i][j]);
            }
        }
        /*------------------------Tạo ra các text boxes-------------------------------------*/
        for (int n = 0; n < 9; n++) {
            for (int i = 0; i < 9; i++) {
                boxes[n][i] = newtextfield();
                int fm = (n + 1) / 3;
                if ((n + 1) % 3 > 0) {
                    fm++;
                }
                int cm = (i + 1) / 3;
                if ((i + 1) % 3 > 0) {
                    cm++;
                }
                paneles[fm - 1][cm - 1].add(boxes[n][i]);
            }
        }
        /*------------------------panal cho buttons -------------------------------------*/
        bPanel = new JPanel();
        bPanel.setLayout(new GridLayout(6, 1, 10, 10));
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                label.setText(TimeFormat(counter));
                counter++;
            }
        };

        nBtn = new JButton("Chơi mới");
        nBtn.setSize(20, 50);
        counter = 0;
        timer = new Timer(1000, action);
        nBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int randomlevel = ThreadLocalRandom.current().nextInt(3, 7);
                timer.stop();
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                Sudoku.setlevel(randomlevel);
                Sudoku.newGame();
                saveFilereset();
                counter = 0;
                timer.start();
                slove.setEnabled(true);
                reset.setEnabled(true);
                save.setEnabled(true);
                cBtn.setEnabled(true);
                delete.setEnabled(true);
            }
        });
        delete = new JButton("Xóa màn chơi");
        delete.setSize(20, 50);
        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                counter = 0;
                label.setText(TimeFormat(counter));
                timer.stop();
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                file2.delete();
                slove.setEnabled(false);
                reset.setEnabled(false);
                save.setEnabled(false);
                cBtn.setEnabled(false);
                delete.setEnabled(false);
            }
        });
        reset = new JButton("Reset màn chơi");
        reset.setSize(20, 50);
        reset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                readNewMatrixreset(file2);
                timer.start();
                slove.setEnabled(true);
                save.setEnabled(true);
                cBtn.setEnabled(true);
            }
        });
        reloadsave = new JButton("Load save game");
        reloadsave.setSize(20, 50);
        reloadsave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                if (readNewMatrixsaveuser(file1)) {
                    timer.start();
                    saveFilereset();
                    slove.setEnabled(true);
                    reset.setEnabled(true);
                    save.setEnabled(true);
                    cBtn.setEnabled(true);
                    delete.setEnabled(true);
                } else {
                    restgame();
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            boxes[i][j].setText("");
                        }
                    }
                }

            }
        });
        cBtn = new JButton("Kiểm Tra");
        cBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (!boxes[i][j].isEditable()) {
                            continue;
                        } else if (boxes[i][j].getText().equals(String.valueOf(grid[i][j]))) {
                            boxes[i][j].setBackground(Color.decode("#C0DCD9"));
                        } else if (boxes[i][j].getText().isEmpty()) {
                            boxes[i][j].setBackground(Color.WHITE);
                            continue;
                        } else {
                            boxes[i][j].setBackground(Color.red);
                        }
                    }
                }
                String check = new String();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (boxes[i][j].getText().equals(String.valueOf(grid[i][j])) && !boxes[i][j].getText().isEmpty()) {
                            check = check + 1;
                        } else {
                            check = check + 0;
                        }
                    }
                }
                if (check.equals("111111111111111111111111111111111111111111111111111111111111111111111111111111111")) {
                    timer.stop();
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            boxes[i][j].setEditable(false);
                        }
                    }
                    slove.setEnabled(false);
                    save.setEnabled(false);
                    cBtn.setEnabled(false);
                    JOptionPane.showMessageDialog(center, "Chức mừng bạn đã giải thành công màn chơi.", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        eBtn = new JButton("Thoát");
        eBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();

                int result = JOptionPane.showConfirmDialog(
                        center,
                        "Bạn chắc chắn thoát khỏi trò chơi?",
                        "Thoát khỏi trò chơi",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    if (!slove.isEnabled()) {
                        file3.deleteOnExit();
                        System.exit(0);
                    } else {
                        boolean testsaveexit = true;
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                if (!boxes[i][j].getText().matches("[1-9]")) {
                                    if (!boxes[i][j].getText().isEmpty()) {
                                        testsaveexit = false;
                                    }
                                }
                            }
                        }
                        if (testsaveexit) {
                            saveFileexit();
                            System.exit(0);
                        } else {
                            JOptionPane.showMessageDialog(center, "Ma trận không hợp lệ. Lưu không thành công.", "Báo lỗi!", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    timer.start();
                }
            }
        }
        );
        superhardBtn = new JButton("Cực Khó");
        superhardBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                counter = 0;
                timer.stop();
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                Sudoku.setlevel(6);
                Sudoku.newGame();
                saveFilereset();
                timer.start();
                slove.setEnabled(true);
                reset.setEnabled(true);
                save.setEnabled(true);
                cBtn.setEnabled(true);
                delete.setEnabled(true);
            }
        }
        );
        hardBtn = new JButton("Khó");
        hardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                counter = 0;
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                Sudoku.setlevel(5);
                Sudoku.newGame();
                saveFilereset();
                timer.start();
                slove.setEnabled(true);
                reset.setEnabled(true);
                save.setEnabled(true);
                cBtn.setEnabled(true);
                delete.setEnabled(true);
            }
        });
        midBtn = new JButton("Trung Bình");
        midBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter = 0;
                timer.stop();
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                Sudoku.setlevel(4);
                Sudoku.newGame();
                timer.start();
                saveFilereset();
                slove.setEnabled(true);
                reset.setEnabled(true);
                save.setEnabled(true);
                cBtn.setEnabled(true);
                delete.setEnabled(true);
            }
        });
        easyBtn = new JButton("Dễ");
        easyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter = 0;
                timer.stop();
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                Sudoku.setlevel(3);
                Sudoku.newGame();
                saveFilereset();
                timer.start();
                slove.setEnabled(true);
                reset.setEnabled(true);
                save.setEnabled(true);
                cBtn.setEnabled(true);
                delete.setEnabled(true);
            }
        });
        slove = new JButton("Kết Quả");
        slove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                label.setText(TimeFormat(counter));
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText(String.valueOf(grid[i][j]));
                        boxes[i][j].setEditable(false);
                        if (boxes[i][j].getBackground() == Color.red) {
                            boxes[i][j].setBackground(Color.WHITE);
                        }
                    }
                }
                slove.setEnabled(false);
                save.setEnabled(false);
                cBtn.setEnabled(false);

            }
        });
        about = new JButton("Luật chơi Sudoku");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(center,
                        "Luật chơi của Sudoku là điền kín những ô còn lại với điều kiện:\n"
                        + "+ Các hàng ngang: Phải có đủ các số từ 1 đến 9, không trùng số và không cần đúng thứ tự.\n"
                        + "+ Các hàng dọc: Đảm bảo có đủ các số từ 1-9, không trùng số, không cần theo thứ tự.\n"
                        + "+ Mỗi vùng 3 x 3: Phải có đủ các số từ 1-9 và không trùng số nào trong cùng 1 vùng 3 x3.\n",
                        "Luật chơi Sudoku", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        info = new JButton("Thông tin về tác giả");
        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(center,
                        "Họ và tên: Nguyễn Quang Minh.\n"
                        + "MSSV: B1906520.\n"
                        + "Ngành: Kỹ thuật phần mềm.\n"
                        + "Lớp: DI1996A3.",
                        "Thông tin tác giả", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        tutorial = new JButton("Hướng dẫn sử dụng phần mềm");
        tutorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(center,
                        "Hướng dẫn người dùng sử dụng phần mềm:\n"
                        + "+ Người dùng chọn chơi mới để tạo ra màn chơi với độ khó và thời gian ngẫu nhiên.\n"
                        + "+ Người chơi chọn độ khó thì phần mềm sẽ tạo ra màn chơi tương ứng với độ khó đó: dễ, trung bình, khó và cực khó.\n"
                        + "+ Người chơi chọn kiểm tra để kiểm tra kết quả của người chơi nhập.\n"
                        + "+ Người chơi chọn kết quả để hiện ra kết quả của màn chơi.\n"
                        + "+ Người chơi chọn lưu game để lưu lại quá trình chơi của người chơi.\n"
                        + "+ Người chơi chọn load save game để tải lại quá trình chơi của người chơi.\n"
                        + "+ Người chơi chọn reset màn chơi để reset lại màn chơi\n"
                        + "+ Người chơi chọn import file lên để kiểm tra tệp tin ma trận sudoku của người dùng nếu ma trận tệp tin đúng thì sẽ biến nó thành 1 màn chơi còn nếu ma trận sai thì sẽ thông báo ma trận sai.\n"
                        + "+ Người chơi chọn xóa màn chơi để xóa các các số của màn chơi hiện tại và đưa khu vực chơi về trạng thái ban đầu.\n"
                        + "+ Người chơi chọn reset màn chơi để reset lại màn chơi\n"
                        + "+ Người chơi chọn thoát để thoát hỏi trò chơi.\n"
                        + "+ Người chơi chọn luật chơi để hiện ra luật chơi của sudoku.\n"
                        + "+ Người chơi chọn thông tin tác giả để thông tin của người viết ra phần mềm.\n"
                        + "+ Người chơi chọn hướng dẫn sử dụng phần mềm để hiện ra hướng dẫn sử dụng phần mềm.\n"
                        + "+ Khi người dùng thoát khỏi trò chơi thì trò chơi sẽ tự động save lại quá trình chơi của người chơi và tự động tải lại save đó khi người dùng mở lại trò chơi\n"
                        + "+ Lưu lý: các file import nếu file import là file quá trình chơi của người chơi thì dữ liệu các ô chưa nhập phải để giá trị là 0, các ô đã nhập phải có chữ a phía sau số đó còn lại là của màn chơi đó.",
                        "Hướng dẫn sử dụng phần mềm", JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        save = new JButton("Lưu game");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean testSaveuser = true;
                timer.stop();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (!boxes[i][j].getText().matches("[1-9]")) {
                            if (!boxes[i][j].getText().isEmpty()) {
                                testSaveuser = false;
                            }
                        }
                    }
                }
                if (testSaveuser) {
                    saveFileuser();
                    timer.start();
                    reloadsave.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(center, "Ma trận không hợp lệ. Lưu không thành công.", "Báo lỗi!", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });
        load = new JButton("Import file");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    restgame();
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            boxes[i][j].setText("");
                        }
                    }
                    if (readNewMatrixuserinput(selectedFile)) {
                        saveFilereset();
                        slove.setEnabled(true);
                        reset.setEnabled(true);
                        save.setEnabled(true);
                        cBtn.setEnabled(true);
                        delete.setEnabled(true);
                        timer.start();
                    } else {
                        restgame();
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                boxes[i][j].setText("");
                            }
                        }
                        slove.setEnabled(false);
                        reset.setEnabled(false);
                        save.setEnabled(false);
                    }
                } else {
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (!boxes[i][j].getText().isEmpty()) {
                                timer.start();
                            }
                        }
                    }
                }
            }
        });
        pnlAlign.setLayout(new BoxLayout(pnlAlign, BoxLayout.PAGE_AXIS));
        add(pnlAlign, BorderLayout.NORTH);
        JPanel pnlDifferent = new JPanel(new FlowLayout(FlowLayout.LEADING));
        pnlDifferent.setBorder(BorderFactory.createTitledBorder(" Độ Khó "));
        pnlAlign.add(pnlDifferent);
        pnlDifferent.add(easyBtn);
        pnlDifferent.add(midBtn);
        pnlDifferent.add(hardBtn);
        pnlDifferent.add(superhardBtn);
        JPanel pnlOptions = new JPanel(new FlowLayout(FlowLayout.LEADING));
        pnlOptions.setBorder(BorderFactory.createTitledBorder(" Tùy Chọn "));
        pnlAlign.add(pnlOptions);
        pnlOptions.add(nBtn);
        pnlOptions.add(cBtn);
        pnlOptions.add(slove);
        pnlOptions.add(save);
        pnlOptions.add(reloadsave);
        pnlOptions.add(load);
        pnlOptions.add(delete);
        pnlOptions.add(reset);
        pnlOptions.add(eBtn);
        JPanel pnlInfor = new JPanel(new FlowLayout(FlowLayout.LEADING));
        pnlInfor.setBorder(BorderFactory.createTitledBorder(" Thông Tin "));
        pnlAlign.add(pnlInfor);
        pnlInfor.add(about);
        pnlInfor.add(info);
        pnlInfor.add(tutorial);
        JPanel pnltimecount = new JPanel(new FlowLayout(FlowLayout.LEADING));
        pnltimecount.setBorder(BorderFactory.createTitledBorder(" Thời Gian "));
        pnlAlign.add(pnltimecount);
        pnltimecount.add(label);
        if (file3.exists()) {
            slove.setEnabled(true);
            reset.setEnabled(true);
            save.setEnabled(true);
            delete.setEnabled(true);
            cBtn.setEnabled(true);

        } else {
            slove.setEnabled(false);
            reset.setEnabled(false);
            save.setEnabled(false);
            delete.setEnabled(false);
            cBtn.setEnabled(false);
        }
        if (file1.exists()) {
            reloadsave.setEnabled(true);
        } else {
            reloadsave.setEnabled(false);
        }

    }

    public void setarray(int[][] grid, int[][] temp) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.temp[i][j] = temp[i][j];
                this.grid[i][j] = grid[i][j];
            }
        }
    }

    public void setTextLable() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.temp[i][j] != 0) {
                    boxes[i][j].setText(String.valueOf(this.temp[i][j]));
                    boxes[i][j].setEditable(false);
                    boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                } else {
                    boxes[i][j].setText("");
                }
            }
        }
    }

    public void saveFileuser() {
        try (PrintWriter pw = new PrintWriter(file1)) {
            ArrayList<String> S = new ArrayList<String>();
            String stringMatrix = new String();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if ((boxes[i][j].getBackground() == Color.WHITE || boxes[i][j].getBackground() == Color.RED || boxes[i][j].getBackground() == Color.decode("#C0DCD9")) && !boxes[i][j].getText().isEmpty()) {
                        stringMatrix = stringMatrix + boxes[i][j].getText() + 'a';
                    } else if (boxes[i][j].getText().isEmpty()) {
                        stringMatrix = stringMatrix + 0;
                    } else {
                        stringMatrix = stringMatrix + boxes[i][j].getText();
                    }
                }
            }
            stringMatrix = stringMatrix + (counter - 1);
            S.add(stringMatrix);
            for (String i : S) {
                pw.println(i);
            }
            JOptionPane.showMessageDialog(center, "Đã lưu.", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(center, "Lưu không thành công.", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void saveFileexit() {
        try (PrintWriter pw = new PrintWriter(file3)) {
            ArrayList<String> S = new ArrayList<String>();
            String stringMatrix = new String();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if ((boxes[i][j].getBackground() == Color.WHITE || boxes[i][j].getBackground() == Color.RED || boxes[i][j].getBackground() == Color.decode("#C0DCD9")) && !boxes[i][j].getText().isEmpty()) {
                        stringMatrix = stringMatrix + boxes[i][j].getText() + 'a';
                    } else if (boxes[i][j].getText().isEmpty()) {
                        stringMatrix = stringMatrix + 0;
                    } else {
                        stringMatrix = stringMatrix + boxes[i][j].getText();
                    }
                }
            }
            stringMatrix = stringMatrix + (counter - 1);
            S.add(stringMatrix);
            for (String i : S) {
                pw.println(i);
            }

        } catch (Exception e) {
        }
        String matrix = new String();
        try (Scanner sc = new Scanner(file3)) {
            matrix = sc.nextLine();
            if (matrix.substring(0, 81).equals("000000000000000000000000000000000000000000000000000000000000000000000000000000000")) {
                file3.deleteOnExit();
            }
        } catch (Exception e) {

        }

    }

    public void saveFilereset() {
        try (PrintWriter pw = new PrintWriter(file2)) {
            ArrayList<String> S = new ArrayList<String>();
            String stringMatrix = new String();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if ((boxes[i][j].getBackground() == Color.WHITE && !boxes[i][j].getText().isEmpty()) || boxes[i][j].getText().isEmpty()) {
                        stringMatrix = stringMatrix + 0;
                    } else {
                        stringMatrix = stringMatrix + boxes[i][j].getText();
                    }
                }
            }
            stringMatrix = stringMatrix + (counter - 1);
            S.add(stringMatrix);
            for (String i : S) {
                pw.println(i);
            }
        } catch (Exception e) {
        }
    }

    public static void restgame() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boxes[i][j].setForeground(Color.black);
                boxes[i][j].setEditable(true);
                boxes[i][j].setBackground(Color.WHITE);
            }
        }
    }

    public boolean readNewMatrixuserinput(File file) {
        String matrix = new String();
        try (Scanner sc = new Scanner(file)) {
            try {
                matrix = sc.nextLine();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        grid[i][j] = matrix.charAt(i * 9 + j) - 48;
                        if (i == 0) {
                            if (j == 0) {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        for (int x = 0; x < 9; x++) {
                                            for (int y = 0; y < 9; y++) {
                                                boxes[x][y].setText("");
                                            }
                                        }
                                        JOptionPane.showMessageDialog(center, "File ma trận người dùng nhập vào sai.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                        return false;
                                    }
                                }
                            } else if (j == 8) {
                                grid[i + 1][0] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i + 1][0] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        for (int x = 0; x < 9; x++) {
                                            for (int y = 0; y < 9; y++) {
                                                boxes[x][y].setText("");
                                            }
                                        }
                                        JOptionPane.showMessageDialog(center, "File ma trận người dùng nhập vào sai.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                        return false;
                                    }
                                }
                            } else {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else if (grid[i][j] == 0) {
                                    boxes[i][j].getText().isEmpty();
                                } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                    boxes[i][j].setEditable(false);
                                } else {
                                    restgame();
                                    for (int x = 0; x < 9; x++) {
                                        for (int y = 0; y < 9; y++) {
                                            boxes[x][y].setText("");
                                        }
                                    }
                                    JOptionPane.showMessageDialog(center, "File ma trận người dùng nhập vào sai.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                    return false;
                                }
                            }
                        } else {
                            if (j == 0) {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        for (int x = 0; x < 9; x++) {
                                            for (int y = 0; y < 9; y++) {
                                                boxes[x][y].setText("");
                                            }
                                        }
                                        JOptionPane.showMessageDialog(center, "File ma trận người dùng nhập vào sai.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                        return false;
                                    }
                                }
                            } else if (j == 8) {
                                grid[i + 1][0] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i + 1][0] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;

                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        for (int x = 0; x < 9; x++) {
                                            for (int y = 0; y < 9; y++) {
                                                boxes[x][y].setText("");
                                            }
                                        }
                                        JOptionPane.showMessageDialog(center, "File ma trận người dùng nhập vào sai.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                        return false;
                                    }
                                }
                            } else {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;

                                } else if (grid[i][j] == 0) {
                                    boxes[i][j].getText().isEmpty();
                                } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                    boxes[i][j].setEditable(false);
                                } else {
                                    restgame();
                                    for (int x = 0; x < 9; x++) {
                                        for (int y = 0; y < 9; y++) {
                                            boxes[x][y].setText("");
                                        }
                                    }
                                    JOptionPane.showMessageDialog(center, "File ma trận người dùng nhập vào sai.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                    return false;
                                }
                            }

                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(center, "Không thể mở File.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            if (!Sudoku.search(grid)) {
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                JOptionPane.showMessageDialog(center, "File ma trận người dùng nhập vào sai.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                return false;
            } else {
                if (matrix.substring(81, matrix.length()) != null) {
                    counter = Integer.parseInt(matrix.substring(81, matrix.length()));
                } else {
                    counter = 0;
                }
                Sudoku.search(grid);
                return true;
            }
        } catch (Exception ex) {

        }
        return true;
    }

    public void readNewMatrixreset(File file2) {
        String matrix = new String();
        try (Scanner sc = new Scanner(file2)) {
            matrix = sc.nextLine();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    grid[i][j] = matrix.charAt(i * 9 + j) - 48;
                    if (grid[i][j] == 0) {
                        boxes[i][j].getText().isEmpty();
                    } else {
                        boxes[i][j].setText(grid[i][j] + "");
                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                        boxes[i][j].setEditable(false);
                    }
                }
            }
            Sudoku.search(grid);
            counter = 0;
        } catch (FileNotFoundException e) {
        }
    }

    public boolean readNewMatrixsaveuser(File file1) {
        String matrix = new String();
        try (Scanner sc = new Scanner(file1)) {
            try {
                matrix = sc.nextLine();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        grid[i][j] = matrix.charAt(i * 9 + j) - 48;
                        if (i == 0) {
                            if (j == 0) {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        for (int x = 0; x < 9; x++) {
                                            for (int y = 0; y < 9; y++) {
                                                boxes[x][y].setText("");
                                            }
                                        }
                                        JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                        return false;
                                    }
                                }
                            } else if (j == 8) {
                                grid[i + 1][0] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i + 1][0] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        for (int x = 0; x < 9; x++) {
                                            for (int y = 0; y < 9; y++) {
                                                boxes[x][y].setText("");
                                            }
                                        }
                                        JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                        return false;
                                    }
                                }
                            } else {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else if (grid[i][j] == 0) {
                                    boxes[i][j].getText().isEmpty();
                                } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                    boxes[i][j].setEditable(false);
                                } else {
                                    JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                    return false;
                                }
                            }
                        } else {
                            if (j == 0) {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                        return false;
                                    }
                                }
                            } else if (j == 8) {
                                grid[i + 1][0] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i + 1][0] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;

                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                        return false;
                                    }
                                }
                            } else {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;

                                } else if (grid[i][j] == 0) {
                                    boxes[i][j].getText().isEmpty();
                                } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                    boxes[i][j].setEditable(false);
                                } else {
                                    JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                    return false;
                                }
                            }

                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(center, "Không thể mở File save.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

        }
        try {
            if (!Sudoku.search(grid)) {
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                return false;
            } else {
                if (matrix.substring(81, matrix.length()) != null) {
                    counter = Integer.parseInt(matrix.substring(81, matrix.length()));
                } else {
                    JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                Sudoku.search(grid);
                return true;
            }
        } catch (Exception ex) {

        }
        return true;
    }

    public boolean readNewMatrixsaveexit(File file3) {
        String matrix = new String();
        try (Scanner sc = new Scanner(file3)) {
            try {
                matrix = sc.nextLine();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        grid[i][j] = matrix.charAt(i * 9 + j) - 48;
                        if (i == 0) {
                            if (j == 0) {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        for (int x = 0; x < 9; x++) {
                                            for (int y = 0; y < 9; y++) {
                                                boxes[x][y].setText("");
                                            }
                                        }
                                        JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                        return false;
                                    }
                                }
                            } else if (j == 8) {
                                grid[i + 1][0] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i + 1][0] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        for (int x = 0; x < 9; x++) {
                                            for (int y = 0; y < 9; y++) {
                                                boxes[x][y].setText("");
                                            }
                                        }
                                        JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

                                        return false;
                                    }
                                }
                            } else {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else if (grid[i][j] == 0) {
                                    boxes[i][j].getText().isEmpty();
                                } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                    boxes[i][j].setEditable(false);
                                } else {
                                    JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                    return false;
                                }
                            }
                        } else {
                            if (j == 0) {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;
                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        restgame();
                                        JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                        return false;
                                    }
                                }
                            } else if (j == 8) {
                                grid[i + 1][0] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i + 1][0] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;

                                } else {
                                    if (grid[i][j] == 0) {
                                        boxes[i][j].getText().isEmpty();
                                    } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                        boxes[i][j].setText(grid[i][j] + "");
                                        boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                        boxes[i][j].setEditable(false);
                                    } else {
                                        JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                        return false;
                                    }
                                }
                            } else {
                                grid[i][j + 1] = matrix.charAt(i * 9 + j + 1) - 48;
                                if (grid[i][j + 1] == 49) {
                                    matrix = matrix.replaceFirst("a", "");
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.WHITE);
                                    boxes[i][j].setEditable(true);
                                    grid[i][j] = 0;

                                } else if (grid[i][j] == 0) {
                                    boxes[i][j].getText().isEmpty();
                                } else if (grid[i][j] > 0 && grid[i][j] < 10) {
                                    boxes[i][j].setText(grid[i][j] + "");
                                    boxes[i][j].setBackground(Color.decode("#C0DCC0"));
                                    boxes[i][j].setEditable(false);
                                } else {
                                    JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                                    return false;
                                }
                            }

                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(center, "Không thể mở File save.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);

        }
        try {
            if (!Sudoku.search(grid)) {
                restgame();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boxes[i][j].setText("");
                    }
                }
                JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                return false;
            } else {
                if (matrix.substring(81, matrix.length()) != null) {
                    counter = Integer.parseInt(matrix.substring(81, matrix.length()));
                } else {
                    JOptionPane.showMessageDialog(center, "File save bị lỗi.", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                Sudoku.search(grid);
                return true;
            }
        } catch (Exception ex) {

        }
        return true;
    }

    private String TimeFormat(int count) {
        return String.format("Thời gian: %02d:%02d", count / 60, count % 60);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
