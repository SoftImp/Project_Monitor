package deployment;

import java.util.ArrayList;
import java.util.List;

// Spring requires a POJ class for each message - variables must start with a lower case (camel case convention)
public class ProjectMsg {
  private String name;
  private String description;
  private String strategicGoal;
  private String program;
  private String portfolio;

  public ProjectMsg() {
  }

  public ProjectMsg(String name, String description) {
    super();
    this.name = name;
    this.description = description;
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

  public void setPortfolio(String portfolio) {
    this.portfolio = portfolio;
  }

  public String getPortfolio() {
    return portfolio;
  }

  public void setStrategicGoal(String strategicGoal) {
    this.strategicGoal = strategicGoal;
  }

  public String getStrategicGoal() {
    return strategicGoal;
  }

  public void setProgram(String program) {
    this.program = program;
  }

  public String getProgram() {
    return program;
  }
}