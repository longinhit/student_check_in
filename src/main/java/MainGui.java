import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class MainGui {
    private JFrame frame;
    private JPanel panel;
    private int locationX;
    private int locationY;

    private JLabel classNameLabel;
    private JLabel idLabel;
    private JLabel nameLabel;

    private JButton loadDataButton;
    private JButton randomButton;
    private JButton stopButton;

    List<StudentInfo> studentInfos;

    private int isStop;
    private int stopIndex;

    public MainGui() {
        initDialog();
    }

    private void initDialog() {

        // 窗口框架
        frame = new JFrame();
        frame.setTitle("课堂点名工具");

        frame.setSize(600, 300);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        locationX = (screenSize.width - frameSize.width) / 2;
        locationY = (screenSize.height - frameSize.height) / 2;

        frame.setLocation(locationX, locationY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1));

        this.panel = new JPanel();
        frame.add(this.panel);
        this.panel.setLayout(null);
        this.initLabel();
        this.initButton();
        frame.getContentPane().add(this.panel);
        frame.setVisible(true);
    }


    /**
     * 初始化班级，学号、姓名标签
     */
    private void initLabel() {

        this.classNameLabel = new JLabel("班级");
        this.idLabel = new JLabel("学号");
        this.nameLabel = new JLabel("姓名");
        classNameLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        idLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 30));

        classNameLabel.setBounds(locationX - 400, locationY - 260, 250, 80);
        idLabel.setBounds(locationX - 200, locationY - 260, 250, 80);
        nameLabel.setBounds(locationX, locationY - 260, 250, 80);

        this.panel.add(classNameLabel);
        this.panel.add(idLabel);
        this.panel.add(nameLabel);

    }

    /**
     * 初始化按钮标签
     */
    private void initButton() {
        this.loadDataButton = new JButton("加载名单");
        this.randomButton = new JButton("开始");
        this.stopButton = new JButton("停止");

        loadDataButton.setFont(new Font("微软雅黑", Font.BOLD, 25));
        randomButton.setFont(new Font("微软雅黑", Font.BOLD, 25));
        stopButton.setFont(new Font("微软雅黑", Font.BOLD, 25));

        loadDataButton.setBounds(locationX - 400, locationY - 160, 150, 80);
        randomButton.setBounds(locationX - 200, locationY - 160, 150, 80);
        stopButton.setBounds(locationX, locationY - 160, 150, 80);


        loadDataButton.addActionListener(e -> {
            CsvLoader csLoader = new CsvLoader("data/students.csv");
            long successLoadCnt = csLoader.loadCsv();
            studentInfos = csLoader.getStudentInfos();
            JOptionPane.showMessageDialog(null, "成功加载" + successLoadCnt + "学生", "加载学生名单结果", JOptionPane.ERROR_MESSAGE);

            randomButton.setEnabled(true);
            stopButton.setEnabled(false);
        });

        randomButton.addActionListener(e -> {
            if (this.studentInfos.size() == 0) {
                stopButton.setEnabled(false);
                randomButton.setEnabled(false);
                JOptionPane.showMessageDialog(null, "所有同学都点过名了", "点名完成", JOptionPane.ERROR_MESSAGE);
                return;
            }
            randomButton.setEnabled(false);
            stopButton.setEnabled(true);
            new Thread(() -> {
                this.isStop = 0;
                Random random = new Random(System.currentTimeMillis());
                while (this.isStop == 0) {
                    stopIndex = random.nextInt(this.studentInfos.size());
                    String className = this.studentInfos.get(stopIndex).getClassName();
                    String id = this.studentInfos.get(stopIndex).getId();
                    String name = this.studentInfos.get(stopIndex).getName();
                    classNameLabel.setText(className);
                    idLabel.setText(id);
                    nameLabel.setText(name);
                    Color color;
                    if (stopIndex % 3 == 0) {
                        color = Color.red;
                    } else if (stopIndex % 3 == 1) {
                        color = Color.green;
                    } else {
                        color = Color.blue;
                    }
                    classNameLabel.setForeground(color);
                    idLabel.setForeground(color);
                    nameLabel.setForeground(color);
                    try {
                        Thread.sleep(100);    //延时2秒
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            },"线程1").start();
        });
        stopButton.addActionListener(e -> {
            this.isStop = 1;
            System.out.println(studentInfos.get(stopIndex).toString());
            this.studentInfos.remove(stopIndex);
            stopButton.setEnabled(false);
            randomButton.setEnabled(true);
        });

        stopButton.setEnabled(false);

        this.panel.add(loadDataButton);
        this.panel.add(randomButton);
        this.panel.add(stopButton);
    }


}
