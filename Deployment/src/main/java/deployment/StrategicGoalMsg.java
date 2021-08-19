package deployment;

import java.util.ArrayList;
import java.util.List;

// Spring requires a POJO class for each message - variables must start with a lower case (camel case convention)
public class StrategicGoalMsg extends AbstractMsg {
	private String name;
	private String description;
	private String priority;
	private List<String> portfolios;
	private List<String> programs;
  	private List<String> projects;

	@Override
	public String getId() {
		return name;
	}

	public StrategicGoalMsg() {
		portfolios = new ArrayList<String>();
		programs = new ArrayList<String>();
    	projects = new ArrayList<String>();
		name = "";
		description = "";
		priority = "";
	}

	public StrategicGoalMsg(String name, String description, String priority) {
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.portfolios = new ArrayList<String>();
		this.programs = new ArrayList<String>();
    	this.projects = new ArrayList<String>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPriority() {
		return priority;
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

	public void addPortfolio(String portfolio) {
		portfolios.add(portfolio);
	}

	public List<String> getPortfolios() {
	return portfolios;
	}
}
