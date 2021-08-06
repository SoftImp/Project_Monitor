package deployment;

import java.util.ArrayList;
import java.util.List;

// Spring requires a POJ class for each message - variables must start with a lower case (camel case convention)
public class ProgramMsg {
  private String name;
  private String description;
  private String owner;
  private String strategicGoal;
  private String portfolio;
//  private double budget;
//  private String manager;
  private ArrayList<String> projects;

  public ProgramMsg() {
    projects = new ArrayList<String>();
  }

  public ProgramMsg(String name, String description, String owner) {
    super();
    this.name = name;
    this.description = description;
    this.owner = owner;
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

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getOwner() {
    return owner;
  }

  public void setStrategicGoal(String strategicGoal) {
    this.strategicGoal = strategicGoal;
  }

  public String getStrategicGoal() {
    return strategicGoal;
  }

  public void setPortfolio(String portfolio) {
    this.portfolio = portfolio;
  }

  public String getPortfolio() {
    return portfolio;
  }

  /*public void setBudget(double budget) {
    this.budget = budget;
  }

  public double getBudget() {
    return budget;
  }

  public void setManager(String manager) {
    this.manager = manager;
  }

  public String getManager() {
    return manager;
  }*/

  public void addProject(String project) {
    projects.add(project);
  }

  public List getProjects() {
    return projects;
  }
}