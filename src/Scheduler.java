import java.util.Queue;
import java.util.LinkedList;
import java.util.Timer;

public class Scheduler {    //high level scheduler

    private SchedulerPolicy lls; //low level scheduler
    Queue<Process> readyQueue;
    //public Queue<Process> blockedQueue;
    Queue<Process> exitQueue;

    private String[] availablePolicies = {"First come first served", "Round robin"};

    public Scheduler() {    //need creational pattern, maybe AbstractFactory?
        readyQueue = new LinkedList<Process>();
        exitQueue = new LinkedList<Process>();
        lls = new RoundRobin(this, 2);
    }

    public String[] getAvailablePolicies() {
        return availablePolicies;
    }

    public void setPolicy(int policy) {
        //AbstractFactory?
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
        lls.setTimeslice(ts);
    }

    public void flushProcessQueue() {
        readyQueue.clear();
    }

    public LinkedList<Process> getProcessQueue() {
        return (LinkedList<Process>) readyQueue;
    }

    public static void main(String args[]) {
        Scheduler hls = new Scheduler();

        Burst runningProcess;

        do {
            runningProcess = hls.nextBurst();
            System.out.println(runningProcess);
        } while (runningProcess != null);
    }
}
