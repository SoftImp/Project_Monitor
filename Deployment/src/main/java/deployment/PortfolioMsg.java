package deployment;

import java.util.ArrayList;
import java.util.List;

// Spring requires a POJO class for each message - variables must start with a lower case (camel case convention)
public class PortfolioMsg extends AbstractMsg {
  private String name;
  private String description;
  private String manager;
  private String strategicGoal;
  private String priority;
  private String vision;
  private String mission;
  private double budget;
  private List<String> programs;
  private List<String> projects;
  private int currentState;

  @Override
	public String getId() {
		return name;
	}

  public PortfolioMsg() {
    programs = new ArrayList<String>();
    projects = new ArrayList<String>();
    currentState = 0;
    budget = 0d;
    name = "";
    description = "";
    strategicGoal = "";
    priority = "";
    vision = "";
    mission = "";
    manager = "";
  }

  public PortfolioMsg(String name, String description, String manager) {
    this.name = name;
    this.description = description;
    this.manager = manager;
    this.programs = new ArrayList<String>();
    this.projects = new ArrayList<String>();
    this.currentState = 0;
    budget = 0d;
    strategicGoal = "";
    priority = "";
    vision = "";
    mission = "";
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setManager(String manager) {
    this.manager = manager;
  }

  public String getManager() {
    return manager;
  }

  public void setStrategicGoal(String strategicGoal) {
    this.strategicGoal = strategicGoal;
  }

  public String getStrategicGoal() {
    return strategicGoal == null ? "" : strategicGoal;
  }

  public void addProgram(String program) {
    programs.add(program);
  }

  public List<String> getPrograms() {
    return programs;
  }

  public void addProject(String project) {
    projects.add(project);
  }

  public List<String> getProjects() {
    return projects;
  }

  public void setcurrentState(int currentState) {
    this.currentState = currentState;
  }

  public int getCurrentState() {
    return currentState;
  }

  public void setPriority(String priority) {
		this.priority = priority;
	}

  public String getPriority() {
    return priority == null ? "" : priority;
	}

  public void setVision(String vision) {
		this.vision = vision;
	}

  public String getVision() {
    return vision == null ? "" : vision;
	}

  public void setMission(String mission) {
		this.mission = mission;
	}

  public String getMission() {
    return mission == null ? "" : mission;
	}

  public void setBudget(double budget) {
		this.budget = budget;
	}

  public double getBudget() {
		return budget;
	}
}