public class RoundRobin implements SchedulerPolicy {

    private Scheduler context;
    private int quantum;

    public RoundRobin(Scheduler context) {
        this.context = context;
        if(quantum > 0) {
            this.quantum = context.timeslice;
        } else {
            throw new RuntimeException("Quantum value must be a positive non-zero integer.");
        }
    }

    public void setTimeslice(Integer timeslice) {
        if(timeslice == null) {
            //compute best timeslice based on <80% bursts
        } else {
            this.quantum = timeslice;
        }
    }

    public Burst dispatch() {
        Process runningProcess = context.readyQueue.remove();

        int burstTime = runningProcess.getBurst();

        if (context.readyQueue.isEmpty()) {
            burstTime = runningProcess.getBurst();
            context.exitQueue.add(runningProcess);
        } else if (burstTime > quantum) {
            runningProcess.setBurst(burstTime - quantum);
            burstTime = quantum;
            context.readyQueue.add(runningProcess);
        } else {
            context.exitQueue.add(runningProcess);
        }

        Burst thisburst = new Burst(runningProcess, burstTime);

        return thisburst;
    }

}
