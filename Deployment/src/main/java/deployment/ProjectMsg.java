package deployment;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

// Spring requires a POJO class for each message - variables must start with a lower case (camel case convention)
public class ProjectMsg extends AbstractMsg {
  private String name;
  private String description;
  private String strategicGoal;
  private String program;
  private String portfolio;
  private List<EarnedValueMsg> earnedValues;

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
    earnedValues = new ArrayList<EarnedValueMsg>();
  }

  public ProjectMsg(String name, String description) {
    this.name = name;
    this.description = description;
    this.strategicGoal = "";
    this.program = "";
    this.portfolio = "";
    this.earnedValues = new ArrayList<EarnedValueMsg>();
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

  public void addEarnedValue(int repId, double bac, double ev, double pv, double ac) {
    EarnedValueMsg earnedVal = new EarnedValueMsg(repId, bac, ev, pv, ac);
    earnedValues.add(earnedVal);
  }

  public List<EarnedValueMsg> getEarnedValues() {
      List<EarnedValueMsg> sortedList = earnedValues.stream()
            .sorted(Comparator.comparingInt(EarnedValueMsg::getRepId))
            .collect(Collectors.toList());

    return sortedList;
  }
}