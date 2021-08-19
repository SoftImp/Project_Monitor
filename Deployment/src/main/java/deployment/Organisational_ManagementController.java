package deployment;

import deployment.organisational_management.Organisational_ManagementOrgMan;
import pm_types.Priority_Level;

import io.ciera.runtime.summit.application.IApplication;
import io.ciera.runtime.summit.application.IRunContext;
import io.ciera.runtime.summit.classes.IModelInstance;
import io.ciera.runtime.summit.components.Component;
import io.ciera.runtime.summit.exceptions.BadArgumentException;
import io.ciera.runtime.summit.exceptions.EmptyInstanceException;
import io.ciera.runtime.summit.exceptions.XtumlException;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class Organisational_ManagementController {
	private static Organisational_ManagementController singleton;
	private WaitForMsg waitStrategicGoals = new WaitForMsg();

	public Organisational_ManagementController() {
		singleton = this;
	}

	public static Organisational_ManagementController Singleton() {
		return singleton;
	}

	@PostMapping("/addsg")
	public ResponseEntity addsg(@RequestBody StrategicGoalMsg sg) {
		try {
			TableData<StrategicGoalMsg> td = getsg();
			if (td.findById(sg.getName()) != null)
				throw new Exception("addsg() - Strategic Goal already exists: " + sg.getName());

			//System.out.printf("addsg() goalId: %s, desc: %s, priority: %s\n", sg.getName(), sg.getDescription(), sg.getPriority());

			Priority_Level level = Priority_Level.valueOf(sg.getPriority().toUpperCase());

			Organisational_Management.Singleton().OrgMan().add_Strategic_Goal(sg.getName(), sg.getDescription(),
					level);
			waitStrategicGoals.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in addsg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/updatesg")
	public ResponseEntity updatesg(@RequestBody StrategicGoalMsg sg) {
		try {
			//System.out.printf("updatesg() goalId: %s, desc: %s, priority: %s\n", sg.getName(), sg.getDescription(), sg.getPriority());

			TableData<StrategicGoalMsg> td = getsg();
			StrategicGoalMsg oldSG = td.findById(sg.getName()); 
			if (oldSG == null)
				throw new Exception("updatesg() - Strategic Goal not found: " + sg.getName());

			if (!oldSG.getDescription().equals(sg.getDescription()))
				Organisational_Management.Singleton().OrgMan().update_Description(sg.getName(), sg.getDescription());

			if (!oldSG.getPriority().equals(sg.getPriority())) {
				Priority_Level level = Priority_Level.valueOf(sg.getPriority().toUpperCase());
				Organisational_Management.Singleton().OrgMan().update_Priority(sg.getName(), level);
			}

			waitStrategicGoals.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in updatesg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/init")
	public ResponseEntity init() {
		try {
			Organisational_Management.Singleton().OrgMan().add_Strategic_Goal("Goal1", "Goal1", Priority_Level.LOW);
			Organisational_Management.Singleton().OrgMan().add_Strategic_Goal("Goal2", "Goal2", Priority_Level.HIGH);
			Organisational_Management.Singleton().OrgMan().add_Strategic_Goal("Goal3", "Goal3", Priority_Level.LOW);
			Organisational_Management.Singleton().OrgMan().add_Strategic_Goal("Goal4", "Goal4", Priority_Level.MEDIUM);
			Organisational_Management.Singleton().OrgMan().add_Strategic_Goal("Goal5", "Goal5", Priority_Level.MEDIUM);
			waitStrategicGoals.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in init()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getsg")
	public TableData getsg() {
		try {
			TableData<StrategicGoalMsg> td = new TableData();

			if (!waitStrategicGoals.hasMsg()) {
				Organisational_Management.Singleton().OrgMan().get_Strategic_Goals();
				waitStrategicGoals.synchroniseAndWait();
			}

			td.setData(waitStrategicGoals.getMsg(), StrategicGoalMsg[].class);
			waitStrategicGoals.clear(); // TMP - clear messages
			return td;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getsg()\n", e);
			return new TableData();
		}
	}

	public void onStrategicGoals(String strategicGoals) {
		waitStrategicGoals.onNotify(strategicGoals);
	}
}
