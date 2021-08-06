package deployment;

import deployment.program_management.Program_ManagementPrgMan;
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
public class Program_ManagementController {
	private static Program_ManagementController singleton;
	private WaitForMsg waitPrograms = new WaitForMsg();

	public Program_ManagementController() {
		singleton = this;
	}

	public static Program_ManagementController Singleton() {
		return singleton;
	}

	@PostMapping("/addprg")
	public ResponseEntity addprg(@RequestBody ProgramMsg msg) {
		try {
			if (!waitPrograms.hasMsg()) {
				Program_Management.Singleton().PrgMan().get_Programs();
				waitPrograms.synchroniseAndWait();
			}

			if (waitPrograms.getMsg().contains("\"name\":\"" + msg.getName() + "\"")) {
				System.out.printf("addpf() name: %s already exists\n", msg.getName());
				return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
			}

			System.out.printf("addpf() name: %s, desc: %s\n", msg.getName(), msg.getDescription());

			Program_Management.Singleton().PrgMan().add_Program(msg.getName(), msg.getDescription(), msg.getOwner());

			if (msg.getStrategicGoal().length() > 0)
				Program_Management.Singleton().PrgMan().link_Strategic_Goal(msg.getName(), msg.getStrategicGoal());

			waitPrograms.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in addsg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/updateprg")
	public ResponseEntity updatesg(@RequestBody ProgramMsg msg) {
		try {
			System.out.printf("updatepf() name: %s, desc: %s, owner: %s\n", msg.getName(), msg.getDescription(), msg.getOwner());

			//Priority_Level level = Priority_Level.valueOf(sg.getPriority().toUpperCase());

			//Organisational_Management.Singleton().OrgMan().update_Description(sg.getGoalId(), sg.getDescription());
			//Organisational_Management.Singleton().OrgMan().update_Priority(sg.getGoalId(), level);

			waitPrograms.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in updateprg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/initprg")
	public ResponseEntity initprg() {
		try {
			/*Organisational_Management.Singleton().OrgMan().add_Strategic_Goal("Goal1", "Goal1", Priority_Level.LOW);*/
			Program_Management.Singleton().PrgMan().add_Program("Program1", "Program1", "Owner");
			Program_Management.Singleton().PrgMan().add_Program("Program2", "Program2", "Owner");
			waitPrograms.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in initprg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getprg")
	public TableData getpf() {
		try {
			TableData<ProgramMsg> td = new TableData();

			if (!waitPrograms.hasMsg()) {
				Program_Management.Singleton().PrgMan().get_Programs();
				waitPrograms.synchroniseAndWait();
			}

			td.setData(waitPrograms.getMsg(), ProgramMsg[].class);
			return td;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getprg()\n", e);
			return new TableData();
		}
	}

	public void on_Programs(String programs) {
		waitPrograms.onNotify(programs);
	}
}
