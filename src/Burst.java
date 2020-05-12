public class Burst {

    private int process_id;
    private int time;
    private String process_name;

    public Burst(int process_id, int time, String process_name) {
        this.process_id = process_id;
        this.time = time;
    }

    public Burst(Process p, int time) {
        this.process_id = p.getId();
        this.process_name = p.getName();
        this.time = time;
    }

    public int getProcessId() {
        return this.process_id;
    }

    public int getTime() {
        return this.time;
    }

    public String getProcessName() {
        return this.process_name;
    }

    public String toString() {
        return "Burst of proc.id "+this.process_id+" last "+this.time+" t_units.";
    }

}
