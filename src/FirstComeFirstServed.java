public class FirstComeFirstServed implements SchedulerPolicy {
    private Scheduler context;

    public FirstComeFirstServed(Scheduler context) {
        this.context = context;
    }

    public Burst dispatch() {
        Process runningProcess = context.readyQueue.remove();

        Burst thisburst = new Burst(runningProcess, runningProcess.getBurst());
        context.exitQueue.add(runningProcess);

        return thisburst;
    }

    public void setTimeslice(Integer ts){}  //This method has no meaning
}
