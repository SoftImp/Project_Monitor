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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Arrays;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.gson.Gson;

@RestController
public class Organisational_ManagementController {
	private static Organisational_ManagementController singleton;
	private ArrayList<WaitForSingleMsg> waitForSingle = new ArrayList<WaitForSingleMsg>();

	public Organisational_ManagementController() {
		singleton = this;
	}

	public static Organisational_ManagementController Singleton() {
		return singleton;
	}

	@PostMapping("/addsg")
	public ResponseEntity addsg(@RequestBody StrategicGoalMsg sg) {
		try {
			if (getsg(sg.getName()) != null)
				throw new Exception("addsg() - Strategic Goal already exists: " + sg.getName());

			//System.out.printf("addsg() goalId: %s, desc: %s, priority: %s\n", sg.getName(), sg.getDescription(), sg.getPriority());

			Priority_Level level = Priority_Level.valueOf(sg.getPriority().toUpperCase());

			Organisational_Management.Singleton().OrgMan().add_Strategic_Goal(sg.getName(), sg.getDescription(),
					level);
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

			StrategicGoalMsg oldSG = getsg(sg.getName()); 
			if (oldSG == null)
				throw new Exception("updatesg() - Strategic Goal not found: " + sg.getName());

			if (!oldSG.getDescription().equals(sg.getDescription()))
				Organisational_Management.Singleton().OrgMan().update_Description(sg.getName(), sg.getDescription());

			if (!oldSG.getPriority().equals(sg.getPriority())) {
				Priority_Level level = Priority_Level.valueOf(sg.getPriority().toUpperCase());
				Organisational_Management.Singleton().OrgMan().update_Priority(sg.getName(), level);
			}

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
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in init()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getallsg")
	public TableData getallsg() {
		try {
			TableData<StrategicGoalMsg> td = new TableData();

			WaitForSingleMsg waitForMsg = new WaitForSingleMsg("", 0);
			waitForSingle.add(waitForMsg);

			Organisational_Management.Singleton().OrgMan().get_Strategic_Goals("");
			waitForMsg.synchroniseAndWait();

			td.setData(waitForMsg.getMsg(), StrategicGoalMsg[].class);

			waitForSingle.remove(waitForMsg);
			return td;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getallsg()\n", e);
			return new TableData();
		}
	}

	@GetMapping("/getsg")
	public StrategicGoalMsg getsg(@RequestParam(value="name", required=true) String name) {
		try {
			WaitForSingleMsg waitForMsg = new WaitForSingleMsg(name, 0);
			waitForSingle.add(waitForMsg);

			Organisational_Management.Singleton().OrgMan().get_Strategic_Goals(name);
			waitForMsg.synchroniseAndWait();

			List<StrategicGoalMsg> sg = Arrays.asList(new Gson().fromJson(waitForMsg.getMsg(), StrategicGoalMsg[].class));
			waitForSingle.remove(waitForMsg);

			if (sg.size() > 0)
				return sg.get(0);

			return null;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getsg()\n", e);
			return null;
		}
	}

	public void onStrategicGoals(final String p_Strategic_Goals,  final String p_SG_Name) {
		boolean found = false;
		for (WaitForSingleMsg waitFor : waitForSingle) {
			if (waitFor.isWaitfor(p_SG_Name, 0)) {
				waitFor.onNotify(p_Strategic_Goals);
				found = true;
				break;
			}
		}

		if (!found)
			System.out.printf("onStrategicGoals() - unable to find sg: %s\n", p_SG_Name);
	}
}
