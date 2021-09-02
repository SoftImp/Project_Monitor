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
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.gson.Gson;

@RestController
public class Program_ManagementController {
	private static Program_ManagementController singleton;
	private ArrayList<WaitForSingleMsg> waitForSingle = new ArrayList<WaitForSingleMsg>();

	public Program_ManagementController() {
		singleton = this;
	}

	public static Program_ManagementController Singleton() {
		return singleton;
	}

	@PostMapping("/addprg")
	public ResponseEntity addprg(@RequestBody ProgramMsg msg) {
		try {
			if (getprg(msg.getName()) != null) {
				System.out.printf("addpf() goalId: %s already exists\n", msg.getName());
				return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
			}

			//System.out.printf("addpf() name: %s, desc: %s\n", msg.getName(), msg.getDescription());

			Program_Management.Singleton().PrgMan().add_Program(msg.getName(), msg.getDescription(), msg.getOwner());

			if (!msg.getStrategicGoal().isEmpty())
				Program_Management.Singleton().PrgMan().link_Strategic_Goal(msg.getName(), msg.getStrategicGoal());

			for (String project : msg.getProjects()) 
				Program_Management.Singleton().PrgMan().add_Project(msg.getName(), project);	

			if (!msg.getPortfolio().isEmpty())				
				Program_Management.Singleton().PrgMan().add_To_Portfolio(msg.getName(), msg.getPortfolio());

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in addprg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/updateprg")
	public ResponseEntity updatesg(@RequestBody ProgramMsg msg) {
		try {
			//System.out.printf("updateprg() name: %s, desc: %s, owner: %s\n", msg.getName(), msg.getDescription(), msg.getOwner());

			ProgramMsg oldPrg = getprg(msg.getName()); 
			if (oldPrg == null)
				throw new Exception("updateprg() - Program not found: " + msg.getName());

			if (!oldPrg.getDescription().equals(msg.getDescription()))
				Program_Management.Singleton().PrgMan().update_Description(msg.getName(), msg.getDescription());
			
			if (!oldPrg.getOwner().equals(msg.getOwner()))
				Program_Management.Singleton().PrgMan().update_Owner(msg.getName(), msg.getOwner());				
			
			if (!oldPrg.getStrategicGoal().equals(msg.getStrategicGoal())) {
				if (!oldPrg.getStrategicGoal().isEmpty())
					Program_Management.Singleton().PrgMan().unlink_Strategic_Goal(msg.getName(), oldPrg.getStrategicGoal());

				if (!msg.getStrategicGoal().isEmpty())	
					Program_Management.Singleton().PrgMan().link_Strategic_Goal(msg.getName(), msg.getStrategicGoal());
			}	

			for (String project : oldPrg.getProjects()) {
				if (msg.getProjects().stream().filter(p -> p.equals(project)).findAny().orElse(null) == null)
					Program_Management.Singleton().PrgMan().remove_Project(msg.getName(), project);
			}

			for (String project : msg.getProjects()) {
				if (oldPrg.getProjects().stream().filter(p -> p.equals(project)).findAny().orElse(null) == null)
					Program_Management.Singleton().PrgMan().add_Project(msg.getName(), project);
			}

			if (!oldPrg.getPortfolio().equals(msg.getPortfolio()) && !msg.getPortfolio().isEmpty()) 
				Program_Management.Singleton().PrgMan().add_To_Portfolio(msg.getName(), msg.getPortfolio());	
			
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in updateprg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/initprg")
	public ResponseEntity initprg() {
		try {
			Program_Management.Singleton().PrgMan().add_Program("Program1", "Program1", "Owner");
			Program_Management.Singleton().PrgMan().add_Program("Program2", "Program2", "Owner");
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in initprg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getallprg")
	public TableData getallprg() {
		try {
			TableData<ProgramMsg> td = new TableData();

			WaitForSingleMsg waitForMsg = new WaitForSingleMsg("", 0);
			waitForSingle.add(waitForMsg);

	
			Program_Management.Singleton().PrgMan().get_Programs("");
			waitForMsg.synchroniseAndWait();

			td.setData(waitForMsg.getMsg(), ProgramMsg[].class);
			waitForSingle.remove(waitForMsg);
			return td;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getallprg()\n", e);
			return new TableData();
		}
	}

	@GetMapping("/getprg")
	public ProgramMsg getprg(@RequestParam(value="name", required=true) String name) {
		try {
			WaitForSingleMsg waitForMsg = new WaitForSingleMsg(name, 0);
			waitForSingle.add(waitForMsg);

			Program_Management.Singleton().PrgMan().get_Programs(name);
			waitForMsg.synchroniseAndWait();

			List<ProgramMsg> prg = Arrays.asList(new Gson().fromJson(waitForMsg.getMsg(), ProgramMsg[].class));
			waitForSingle.remove(waitForMsg);
			if (prg.size() > 0)
				return prg.get(0);

			return null;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getprg()\n", e);
			return null;
		}
	}

	public void on_Programs(final String p_Programs, final String p_PRG_Name) {
		boolean found = false;
		for (WaitForSingleMsg waitFor : waitForSingle) {
			if (waitFor.isWaitfor(p_PRG_Name, 0)) {
				waitFor.onNotify(p_Programs);
				found = true;
				break;
			}
		}

		if (!found)
			System.out.printf("on_Programs() - unable to find sg: %s\n", p_PRG_Name);
	}
}
