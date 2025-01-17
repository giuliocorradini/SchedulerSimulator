import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Vector;

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
    private JButton loadButton;
    private JButton dumpButton;

    private ActionListener startAction, stopAction;

    //Logic
    private Scheduler scheduler;
    private boolean autorunEnabled;
    private boolean running;
    private Timer clockTimer;

    private LinkedList<Process> processModelList;

    private int step;
    private int targetStep;
    private int speed;

    private Burst lastBurst;
    private Vector<String> stepList;

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

        ActionListener schedulerUpdate = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(step == targetStep) {
                    nextContextSwitch();
                } else {
                    stepForward();
                }
            }
        };

        stepButton.addActionListener(schedulerUpdate);

        clockTimer = new Timer(0, schedulerUpdate);

        processModelList = scheduler.getProcessQueue();

        stepList = new Vector<String>();

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
                processTable.updateUI();
            }
        });

        dumpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduler.dumpProcessState();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduler.loadProcessState();
                processTable.updateUI();
            }
        });

        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                speed = speedSlider.getValue();
                clockTimer.setDelay(speed);
                currentSpeedLabel.setText(String.valueOf(speed));
            }
        });

        ganttChart.setListData(stepList);

        ProcessTableModel ptmodel = new ProcessTableModel(processModelList);
        processTable.setModel(ptmodel);
    }

    public void start() {
        step = 0;
        targetStep = 0;

        scheduler.setTimeslice((Integer)timesliceSelector.getValue());
        timesliceSelector.setEnabled(false);

        scheduler.setPolicy(policySelector.getSelectedIndex());
        policySelector.setEnabled(false);

        speed = speedSlider.getValue();
        clockTimer.setDelay(speed);

        stepButton.setEnabled(true);

        startButton.removeActionListener(startAction);
        startButton.setText("Stop");
        startButton.addActionListener(stopAction);

        loadButton.setEnabled(false);

        stepCounterLabel.setText("0");

        stepList.clear();
        ganttChart.updateUI();

        if(autorunEnabled) {
            clockTimer.start();
        }

        running = true;
    }

    public void stop() {
        policySelector.setEnabled(true);
        timesliceSelector.setEnabled(true);

        stepButton.setEnabled(false);

        startButton.removeActionListener(stopAction);
        startButton.setText("Start");
        startButton.addActionListener(startAction);

        loadButton.setEnabled(true);

        //nextCSTimer.stop();
        clockTimer.stop();
        running = false;
    }

    public void nextContextSwitch() {   //Queries for new context switch
        lastBurst = scheduler.nextBurst();
        if(lastBurst != null) {
            targetStep += lastBurst.getTime();
            stepForward();
        } else {
            stop();
        }
        processTable.updateUI();
    }

    public void setAutorun() {
        autorunEnabled = autorunButton.isSelected();
        if(running) {
            if(autorunEnabled) {
                clockTimer.start();
            } else {
                clockTimer.stop();
            }
        }
        statusLabel.setText(autorunEnabled ? "enabled" : "disabled");
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

    public void stepForward() {
        stepList.add(lastBurst.getProcessName() + " " + step);

        step += 1;
        stepCounterLabel.setText(String.valueOf(step));

        ganttChart.updateUI();
    }

}
