package deployment;

import deployment.portfolio_management.Portfolio_ManagementPFMan;
import pm_types.Priority_Level;
import pm_types.Component_Type;
import deployment.pm_control.pm_control.impl.PortfolioStateMachine;

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

import javax.lang.model.util.ElementScanner6;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class Portfolio_ManagementController {
	private static Portfolio_ManagementController singleton;
	private WaitForMsg waitPortfolios = new WaitForMsg();

	public Portfolio_ManagementController() {
		singleton = this;
	}

	public static Portfolio_ManagementController Singleton() {
		return singleton;
	}

	@PostMapping("/addpf")
	public ResponseEntity addpf(@RequestBody PortfolioMsg msg) {
		try {
			TableData<PortfolioMsg> td = getpf();
			if (td.findById(msg.getName()) != null) {
				System.out.printf("addpf() goalId: %s already exists\n", msg.getName());
				return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
			}

			//System.out.printf("addpf() name: %s, desc: %s\n", msg.getName(), msg.getDescription());

			Portfolio_Management.Singleton().PFMan().add_Portfolio(msg.getName(), msg.getDescription(), msg.getManager());

			if (!msg.getStrategicGoal().isEmpty())
				Portfolio_Management.Singleton().PFMan().link_Strategic_Goal(msg.getName(), msg.getStrategicGoal());

			if (!msg.getMission().isEmpty())	
				Portfolio_Management.Singleton().PFMan().state_Mission(msg.getName(), msg.getMission());

			if (!msg.getVision().isEmpty())	
				Portfolio_Management.Singleton().PFMan().state_Vision(msg.getName(), msg.getVision());

			/*for (String program : msg.getPrograms()) 
				Portfolio_Management.Singleton().PFMan().add_Component(msg.getName(), program, Component_Type.PROGRAM);	
			
			for (String project : msg.getProjects()) 
				Portfolio_Management.Singleton().PFMan().add_Component(msg.getName(), project, Component_Type.PROJECT);*/

			waitPortfolios.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in addpf()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	//@RequestMapping(value = "updatepf", method = RequestMethod.POST)
	@PostMapping("/updatepf")
	public ResponseEntity updatepf(@RequestBody PortfolioMsg msg, @RequestParam(value="action", required=true) String action) {
		try {
			//System.out.printf("updatepf() action: %s, name: %s, desc: %s, manager: %s\n", action, msg.getName(), msg.getDescription(), msg.getManager());
			TableData<PortfolioMsg> td = getpf();
			PortfolioMsg oldPf = td.findById(msg.getName()); 
			if (oldPf == null)
				throw new Exception("updatepf() - Portfolio not found: " + msg.getName());

			// TODO - only update the fields depending on current state
			if (!oldPf.getManager().equals( msg.getManager()))
				Portfolio_Management.Singleton().PFMan().change_Manager(msg.getName(), msg.getManager());

			if (!oldPf.getDescription().equals(msg.getDescription()))
				Portfolio_Management.Singleton().PFMan().update_Description(msg.getName(), msg.getDescription());

			if (!oldPf.getStrategicGoal().equals(msg.getStrategicGoal())){
				if (!oldPf.getStrategicGoal().isEmpty())
					Portfolio_Management.Singleton().PFMan().unlink_Strategic_Goal(msg.getName(), oldPf.getStrategicGoal());

				if (!msg.getStrategicGoal().isEmpty())
					Portfolio_Management.Singleton().PFMan().link_Strategic_Goal(msg.getName(), msg.getStrategicGoal());
			}

			for (String program : oldPf.getPrograms()) {
				if (msg.getPrograms().stream().filter(p -> p.equals(program)).findAny().orElse(null) == null)
					Portfolio_Management.Singleton().PFMan().remove_Component(msg.getName(), program, Component_Type.PROGRAM);
			}

			for (String program : msg.getPrograms()) {
				if (oldPf.getPrograms().stream().filter(p -> p.equals(program)).findAny().orElse(null) == null)
					Portfolio_Management.Singleton().PFMan().add_Component(msg.getName(), program, Component_Type.PROGRAM);
			}

			for (String project : oldPf.getProjects()) {
				if (msg.getProjects().stream().filter(p -> p.equals(project)).findAny().orElse(null) == null)
					Portfolio_Management.Singleton().PFMan().remove_Component(msg.getName(), project, Component_Type.PROJECT);
			}

			for (String project : msg.getProjects()) {
				if (oldPf.getProjects().stream().filter(p -> p.equals(project)).findAny().orElse(null) == null)
					Portfolio_Management.Singleton().PFMan().add_Component(msg.getName(), project, Component_Type.PROJECT);
			}

			if (!oldPf.getMission().equals(msg.getMission()))	
				Portfolio_Management.Singleton().PFMan().state_Mission(msg.getName(), msg.getMission());

			if (!oldPf.getVision().equals(msg.getVision()))	
				Portfolio_Management.Singleton().PFMan().state_Vision(msg.getName(), msg.getVision());

			if (!oldPf.getPriority().equals(msg.getPriority()))	{
				Priority_Level level = Priority_Level.valueOf(msg.getPriority().toUpperCase());
				Portfolio_Management.Singleton().PFMan().set_Priority(msg.getName(), level);
			}

			if (oldPf.getBudget() != msg.getBudget())
				Portfolio_Management.Singleton().PFMan().set_Budget(msg.getName(), msg.getBudget());
			
			// TODO - check current state before changing
			switch (action) {
				case "initcomplete":
					Portfolio_Management.Singleton().PFMan().start_Planning(msg.getName());	
					break;
				case "plancomplete":
					Portfolio_Management.Singleton().PFMan().start_Executing(msg.getName());
					break;
				case "execomplete":
					Portfolio_Management.Singleton().PFMan().close_Portfolio(msg.getName());
					break;
				case "replan":
					Portfolio_Management.Singleton().PFMan().replan(msg.getName());
					break;
				case "reactivate":
					Portfolio_Management.Singleton().PFMan().reactivate_Portfolio(msg.getName());
					break;
				case "save":
					break;
				default:
					throw new Exception("updatepf() - unknown action: " + action);
			}
			
			waitPortfolios.clear();
			return new ResponseEntity(HttpStatus.OK);
		}
		catch (Exception e) {
			System.out.printf("Exception, %s, in updatepf()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/initpf")
	public ResponseEntity initpf() {
		try {
			Portfolio_Management.Singleton().PFMan().add_Portfolio("Portfolio1", "Portfolio1", "Manager");
			Portfolio_Management.Singleton().PFMan().add_Portfolio("Portfolio2", "Portfolio2", "Manager2");
			Portfolio_Management.Singleton().PFMan().add_Portfolio("Portfolio3", "Portfolio2", "Manager3");
			Portfolio_Management.Singleton().PFMan().add_Portfolio("Portfolio4", "Portfolio4", "Manager3");
			Portfolio_Management.Singleton().PFMan().add_Portfolio("Portfolio5", "Portfolio5", "Manager3");
			waitPortfolios.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in initpf()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getpf")
	public TableData getpf() {
		try {
			TableData<PortfolioMsg> td = new TableData();

			if (!waitPortfolios.hasMsg()) {
				Portfolio_Management.Singleton().PFMan().get_Portfolios("");
				waitPortfolios.synchroniseAndWait();
			}

			td.setData(waitPortfolios.getMsg(), PortfolioMsg[].class);
			waitPortfolios.clear(); // TMP - clear messages
			return td;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getpf()\n", e);
			return new TableData();
		}
	}

	public void on_Portfolios(final String p_Portfolios, final String p_PF_Name) {
		waitPortfolios.onNotify(p_Portfolios);
	}
}
