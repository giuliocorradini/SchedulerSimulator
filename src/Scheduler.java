import java.util.Queue;
import java.util.LinkedList;
import java.util.Timer;

public class Scheduler {    //high level scheduler

    private SchedulerPolicy lls; //low level scheduler

    //Context
    int timeslice;
    Queue<Process> readyQueue;
    Queue<Process> exitQueue;
    Queue<Process> processDump;

    private String[] availablePolicies = {"First come first served", "Round robin"};

    public Scheduler() {
        readyQueue = new LinkedList<Process>();
        exitQueue = new LinkedList<Process>();
    }

    public String[] getAvailablePolicies() {
        return availablePolicies;
    }

    public void setPolicy(int policy) {
        switch(policy) {
            default:
            case 0:
                lls = new FirstComeFirstServed(this);
                break;
            case 1:
                lls = new RoundRobin(this);
                break;
        }
    }

    public void addProcess(Process p) {
        readyQueue.add(p);
    }

    /**
     * Function call simulates the next context-switching.
     */
    public Burst nextBurst() {
        if(readyQueue.isEmpty()) {
            return null;
        }

        Burst ts = lls.dispatch();
        return ts;
    }

    public void setTimeslice(int ts) {
        timeslice = ts;
    }

    public void flushProcessQueue() {
        readyQueue.clear();
    }

    public LinkedList<Process> getProcessQueue() {
        return (LinkedList<Process>) readyQueue;
    }

    public void dumpProcessState() {
        processDump = new LinkedList<Process>(readyQueue);
    }

    public void loadProcessState() {
        flushProcessQueue();
        if(processDump != null) {
            readyQueue.addAll(processDump);
        }
    }

}
