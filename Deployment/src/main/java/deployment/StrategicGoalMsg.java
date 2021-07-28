package deployment;

// Spring requires a POJ class for each message - variables must start with a lower case (camel case convention)
public class StrategicGoalMsg {
	private String goalId;
	private String description;
	private String priority;

	public StrategicGoalMsg() {
	}

	public StrategicGoalMsg(String goalId, String description, String priority) {
		super();
		this.goalId = goalId;
		this.description = description;
		this.priority = priority;
	}

	public void setGoalId(String goalId) {
		this.goalId = goalId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getGoalId() {
		return goalId;
	}

	public String getDescription() {
		return description;
	}

	public String getPriority() {
		return priority;
	}
}
