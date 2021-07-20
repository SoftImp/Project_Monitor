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
	@PostMapping("/addsg")
	public ResponseEntity addsg(@RequestBody StrategicGoalMsg sg) {
		try {
			System.out.printf("addsg() name: %s, desc: %s, priority: %s\n", sg.getName(), sg.getDescription(), sg.getPriority());

			Priority_Level level = Priority_Level.valueOf(sg.getPriority().toUpperCase());
			/*Priority_Level level = Priority_Level.LOW;

			if (sg.getPriority().equalsIgnoreCase("Medium"))
				level = Priority_Level.MEDIUM;
			else if (sg.getPriority().equalsIgnoreCase("High"))
				level = Priority_Level.HIGH;*/

			Organisational_Management.Singleton().OrgMan().add_Strategic_Goal(sg.getName(), sg.getDescription(), level);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in addsg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}
}
