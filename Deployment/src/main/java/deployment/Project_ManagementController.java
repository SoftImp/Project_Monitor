package deployment;

import deployment.project_management.Project_ManagementPrjMan;
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
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class Project_ManagementController {
	private static Project_ManagementController singleton;
	private WaitForMsg waitProjects = new WaitForMsg();

	public Project_ManagementController() {
		singleton = this;
	}

	public static Project_ManagementController Singleton() {
		return singleton;
	}

	@PostMapping("/addprj")
	public ResponseEntity addprj(@RequestBody ProjectMsg msg) {
		try {
			TableData<ProjectMsg> td = getprj();
			if (td.findById(msg.getName()) != null) {
				System.out.printf("addprj() goalId: %s already exists\n", msg.getName());
				return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
			}

			//System.out.printf("addprj() name: %s, desc: %s\n", msg.getName(), msg.getDescription());

			Project_Management.Singleton().PrjMan().add_Project(msg.getName(), msg.getDescription());

			if (!msg.getStrategicGoal().isEmpty())
				Project_Management.Singleton().PrjMan().link_Strategic_Goal(msg.getName(), msg.getStrategicGoal());

			if (!msg.getProgram().isEmpty())
				Project_Management.Singleton().PrjMan().add_To_Program(msg.getName(), msg.getProgram());	

			waitProjects.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in addprj()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/updateprj")
	public ResponseEntity updateprj(@RequestBody ProjectMsg msg) {
		try {
			//System.out.printf("updateprj() name: %s, desc: %s, goal: %s, program: %s\n", 
			//	msg.getName(), msg.getDescription(), msg.getStrategicGoal(), msg.getProgram());

			TableData<ProjectMsg> td = getprj();
			ProjectMsg oldPrj = td.findById(msg.getName()); 
			if (oldPrj == null)
				throw new Exception("updateprj() - Project not found: " + msg.getName());
			
			if (!oldPrj.getDescription().equals(msg.getDescription()))
				Project_Management.Singleton().PrjMan().update_Description(msg.getName(), msg.getDescription());	

			if (!oldPrj.getStrategicGoal().equals(msg.getStrategicGoal())) {
				if (!oldPrj.getStrategicGoal().isEmpty())
					Project_Management.Singleton().PrjMan().unlink_Strategic_Goal(msg.getName(), oldPrj.getStrategicGoal());

				if (!msg.getStrategicGoal().isEmpty())	
					Project_Management.Singleton().PrjMan().link_Strategic_Goal(msg.getName(), msg.getStrategicGoal());
			}

			if (!oldPrj.getProgram().equals(msg.getProgram())) {
				if (!oldPrj.getProgram().isEmpty())
					Project_Management.Singleton().PrjMan().remove_From_Program(msg.getName(), oldPrj.getProgram());

				if (!msg.getProgram().isEmpty())
					Project_Management.Singleton().PrjMan().add_To_Program(msg.getName(), msg.getProgram());
			}

			waitProjects.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in updateprj()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/addevdata")
	public ResponseEntity addevdata(@RequestBody EarnedValueMsg ev, @RequestParam(value="project", required=true) String project) {
		try {
			Project_Management.Singleton().PrjMan().input_EV_Data(project, ev.getBac(), ev.getEv(), ev.getPv(), ev.getAc());
			return new ResponseEntity(HttpStatus.OK);
		}
		catch (Exception e) {
			System.out.printf("Exception, %s, in addevdata()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/initprj")
	public ResponseEntity initprj() {
		try {
			Project_Management.Singleton().PrjMan().add_Project("Project1", "Project1");
			Project_Management.Singleton().PrjMan().add_Project("Project2", "Project2");
			Project_Management.Singleton().PrjMan().add_Project("Project3", "Project3");
			Project_Management.Singleton().PrjMan().add_Project("Project4", "Project4");
			Project_Management.Singleton().PrjMan().add_Project("Project5", "Project5");
			waitProjects.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in initprj()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getprj")
	public TableData getprj() {
		try {
			TableData<ProjectMsg> td = new TableData();

			if (!waitProjects.hasMsg()) {
				Project_Management.Singleton().PrjMan().get_Projects();
				waitProjects.synchroniseAndWait();
			}

			td.setData(waitProjects.getMsg(), ProjectMsg[].class);
			waitProjects.clear(); // TMP - clear messages
			return td;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getprj()\n", e);
			return new TableData();
		}
	}

	public void on_Projects(String projects) {
		waitProjects.onNotify(projects);
	}
}
