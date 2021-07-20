package deployment;

// Spring requires a POJ class for each message - variables must start with a lower case (camel case convention)
public class StrategicGoalMsg {
	private String name;
	private String description;
	private String priority;

	public StrategicGoalMsg() {
	}

	public StrategicGoalMsg(String name, String description, String priority) {
		super();
		this.name = name;
		this.description = description;
		this.priority = priority;
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
}
