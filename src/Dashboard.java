import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Dashboard {
    private JPanel contentPane;
    private JComboBox policySelector;
    private JSpinner timesliceSelector;
    private JSlider speedSlider;
    private JButton startButton;
    private JButton stepButton;
    private JTable processTable;
    private JButton addProcessButton;
    private JList ganttChart;
    private JLabel stepCounterLabel;
    private JLabel statusLabel;
    private JCheckBox autorunButton;
    private JLabel currentSpeedLabel;
    private JButton flushButton;

    private ActionListener startAction, stopAction;

    //Logic
    private Scheduler scheduler;
    private boolean autorunEnabled;
    private Timer nextCSTimer;
    private Timer clockTimer;

    private LinkedList<Process> processModelList;

    private int step = 0;
    private int speed;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException ulafe) {
            System.err.println(ulafe.getMessage());
        }
        JFrame frame = new JFrame("Dashboard");
        frame.setContentPane(new Dashboard().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Dashboard() {
        scheduler = new Scheduler();
        autorunEnabled = true;

        ActionListener schedulerUpdate = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schedulerStep();
            }
        };
        nextCSTimer = new Timer(0, schedulerUpdate);
        nextCSTimer.setRepeats(false);

        stepButton.addActionListener(schedulerUpdate);

        ActionListener chartUpdate = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chartUpdate();
            }
        };
        clockTimer = new Timer(0, chartUpdate);

        processModelList = scheduler.getProcessQueue();

        //Graphics elements
        policySelector.setModel(new DefaultComboBoxModel<String>(scheduler.getAvailablePolicies()));

        timesliceSelector.setModel(new SpinnerNumberModel(1, 1, null, 1));

        startAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        };
        stopAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        };
        startButton.addActionListener(startAction);

        autorunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAutorun();
            }
        });

        addProcessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addProcess();

            }
        });

        flushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduler.flushProcessQueue();
            }
        });

        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                speed = speedSlider.getValue();
                currentSpeedLabel.setText(String.valueOf(speed));
            }
        });

        ProcessTableModel ptmodel = new ProcessTableModel(processModelList);
        processTable.setModel(ptmodel);
    }

    public void start() {
        policySelector.setEnabled(false);
        timesliceSelector.setEnabled(false);
        speedSlider.setEnabled(false);

        stepButton.setEnabled(true);

        startButton.removeActionListener(startAction);
        startButton.setText("Stop");
        startButton.addActionListener(stopAction);

        stepCounterLabel.setText("0");

        scheduler.setPolicy(policySelector.getSelectedIndex());

        ganttChart.setListData(new Object[0]);   //Clean Gantt chart

        clockTimer.setDelay(speedSlider.getValue());

        if(autorunEnabled) {
            nextCSTimer.start();
        }

        scheduler.setTimeslice((Integer)timesliceSelector.getValue());
    }

    public void stop() {
        policySelector.setEnabled(true);
        timesliceSelector.setEnabled(true);
        speedSlider.setEnabled(true);

        stepButton.setEnabled(false);

        startButton.removeActionListener(stopAction);
        startButton.setText("Start");
        startButton.addActionListener(startAction);

        nextCSTimer.stop();
        clockTimer.stop();
    }

    public void schedulerStep() {
        Burst lastBurst = scheduler.nextBurst();
        if(lastBurst != null) {
            step += lastBurst.getTime();
            stepCounterLabel.setText(String.valueOf(step));
            if(autorunEnabled) {
                nextCSTimer.setDelay(speed * lastBurst.getTime());
                nextCSTimer.start();
            }
        } else {
            stop();
        }
        processTable.updateUI();
    }

    public void setAutorun() {
        autorunEnabled = autorunButton.isSelected();
        statusLabel.setText(autorunEnabled ? "running" : "not running");
    }

    public void addProcess() {
        ProcessDialog pd = new ProcessDialog();
        pd.setVisible(true);

        if(pd.isProcessValid()) {
            Process newp = pd.getProcess();
            scheduler.addProcess(newp);

            processTable.updateUI();
        }

    }

    public void chartUpdate() {

    }

}
