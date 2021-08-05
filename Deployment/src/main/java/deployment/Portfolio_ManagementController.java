package deployment;

import deployment.portfolio_management.Portfolio_ManagementPFMan;
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
			if (!waitPortfolios.hasMsg()) {
				Portfolio_Management.Singleton().PFMan().get_Portfolios();
				waitPortfolios.synchroniseAndWait();
			}

			if (waitPortfolios.getMsg().contains("\"name\":\"" + msg.getName() + "\"")) {
				System.out.printf("addpf() name: %s already exists\n", msg.getName());
				return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
			}

			System.out.printf("addpf() name: %s, desc: %s\n", msg.getName(), msg.getDescription());

			Portfolio_Management.Singleton().PFMan().add_Portfolio(msg.getName(), msg.getDescription(), msg.getManager());

			if (msg.getStrategicGoal().length() > 0)
				Portfolio_Management.Singleton().PFMan().link_Strategic_Goal(msg.getName(), msg.getStrategicGoal());

			waitPortfolios.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in addsg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/updatepf")
	public ResponseEntity updatesg(@RequestBody PortfolioMsg msg) {
		try {
			System.out.printf("updatepf() name: %s, desc: %s, manager: %s\n", msg.getName(), msg.getDescription(), msg.getManager());

			//Priority_Level level = Priority_Level.valueOf(sg.getPriority().toUpperCase());

			//Organisational_Management.Singleton().OrgMan().update_Description(sg.getGoalId(), sg.getDescription());
			//Organisational_Management.Singleton().OrgMan().update_Priority(sg.getGoalId(), level);

			waitPortfolios.clear();
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			System.out.printf("Exception, %s, in updatesg()\n", e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/initpf")
	public ResponseEntity initpf() {
		try {
			/*Organisational_Management.Singleton().OrgMan().add_Strategic_Goal("Goal1", "Goal1", Priority_Level.LOW);*/
			Portfolio_Management.Singleton().PFMan().add_Portfolio("Portfolio1", "Portfolio1", "Manager");
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
				Portfolio_Management.Singleton().PFMan().get_Portfolios();
				waitPortfolios.synchroniseAndWait();
			}

			td.setData(waitPortfolios.getMsg(), PortfolioMsg[].class);
			return td;
		} catch (Exception e) {
			System.out.printf("Exception, %s, in getsg()\n", e);
			return new TableData();
		}
	}

	public void on_Portfolios(String portfolios) {
		waitPortfolios.onNotify(portfolios);
	}
}
