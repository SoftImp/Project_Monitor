package deployment;

// Spring requires a POJO class for each message - variables must start with a lower case (camel case convention)
public class SummaryRepMsg extends AbstractMsg {
    private String program;
    private String project;
    private String trafficLight;
    private long date;

    public SummaryRepMsg() {
        program = "";
        project = "";
        trafficLight = "";
        date = 0;
    }

    public SummaryRepMsg(String program, String project, long date) {
        this.program = program;
        this.project = project;
        this.date = date;
        this.trafficLight = "";
    }

    public void setProgram(String program) {
        this.program = program;
    }
    
    public String getProgram() {
        return program == null ? "" : program;
    }
    
    public void setProject(String project) {
        this.project = project;
    }
    
    public String getProject() {
        return project == null ? "" : project;
    }

    public void setTrafficLight(String trafficLight) {
        this.trafficLight = trafficLight;
    }

    public String getTrafficLight() {
    return trafficLight == null ? "" : trafficLight;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }
}