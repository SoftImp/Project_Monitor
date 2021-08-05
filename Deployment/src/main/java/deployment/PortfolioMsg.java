package deployment;

import java.util.ArrayList;
import java.util.List;

// Spring requires a POJ class for each message - variables must start with a lower case (camel case convention)
public class PortfolioMsg {
  private String name;
  private String description;
  private String manager;
  private String strategicGoal;
  private ArrayList<String> programs;
  private ArrayList<String> projects;

  public PortfolioMsg() {
    programs = new ArrayList<String>();
    projects = new ArrayList<String>();
  }

  public PortfolioMsg(String name, String description, String manager) {
    super();
    this.name = name;
    this.description = description;
    this.manager = manager;
    this.programs = new ArrayList<String>();
    this.projects = new ArrayList<String>();
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
    return strategicGoal;
  }

  public void addProgram(String program) {
    programs.add(program);
  }

  public List getPrograms() {
    return programs;
  }

  public void addProject(String project) {
    programs.add(project);
  }

  public List getProjects() {
    return projects;
  }
}