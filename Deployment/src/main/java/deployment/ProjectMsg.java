package deployment;

import java.util.ArrayList;
import java.util.List;

// Spring requires a POJO class for each message - variables must start with a lower case (camel case convention)
public class ProjectMsg extends AbstractMsg {
  private String name;
  private String description;
  private String strategicGoal;
  private String program;
  private String portfolio;

  @Override
	public String getId() {
		return name;
	}
  
  public ProjectMsg() {
    name = "";
    description = "";
    strategicGoal = "";
    program = "";
    portfolio = "";
  }

  public ProjectMsg(String name, String description) {
    this.name = name;
    this.description = description;
    this.strategicGoal = "";
    this.program = "";
    this.portfolio = "";
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
    return portfolio == null ? "" : portfolio;
  }

  public void setStrategicGoal(String strategicGoal) {
    this.strategicGoal = strategicGoal;
  }

  public String getStrategicGoal() {
    return strategicGoal == null ? "" : strategicGoal;
  }

  public void setProgram(String program) {
    this.program = program;
  }

  public String getProgram() {
    return program == null ? "" : program;
  }
}