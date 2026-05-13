package ThreadPractice;

public class Task {
    private final String title;
    private String status;

    public Task(String title) {
        this.title = title;
        this.status = "PENDING";
    }
    public void markDone() {this.status="DONE";}
    public void repr() {
        System.out.println("-----------------------");
        System.out.println("TASK: " + this.title);
        System.out.println("STATUS: " + this.status);
        System.out.println("-----------------------\n");
    }
}