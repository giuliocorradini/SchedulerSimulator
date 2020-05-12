/**
 * Represents a policy adopted by the low-level scheduler.
 * Also known as dispatcher.
 */
public interface SchedulerPolicy {
    public Burst dispatch();
    void setTimeslice(Integer ts);
}
