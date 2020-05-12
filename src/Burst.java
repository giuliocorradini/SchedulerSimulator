public class Burst {

    private int process_id;
    private int time;

    public Burst(int process_id, int time) {
        this.process_id = process_id;
        this.time = time;
    }

    public Burst(Process p, int time) {
        this.process_id = p.getId();
        this.time = time;
    }

    public int getProcessId() {
        return this.process_id;
    }

    public int getTime() {
        return this.time;
    }

    public String toString() {
        return "Burst of proc.id "+this.process_id+" last "+this.time+" t_units.";
    }

}
