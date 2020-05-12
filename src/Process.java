public class Process {
    //private SchedulingState state;
    private int id;
    private String name;
    private int burst;

    private static int id_counter = 0;

    public Process(String pname, int burst) {
        //this.state = SchedulingState.NEW;
        this.name = pname;
        this.id = ++id_counter;
        if(burst > 0) {
            this.burst = burst;
        } else {
            throw new RuntimeException("Process burst must be a positive integer.");
        }
    }

    public Process(Process p, int burst) {
        this.name = p.name;
        this.id = p.id;
        this.burst = burst;
    }

    /*public SchedulingState getState() {
        return state;
    }

    public void setState(SchedulingState state) {
        this.state = state;
    }*/

    public int getId() {
        return id;
    }

    public int getBurst() {
        return burst;
    }

    public String getName() {
        return name;
    }
}
