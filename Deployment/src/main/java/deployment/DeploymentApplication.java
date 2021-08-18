package deployment;


import deployment.Organisational_Management;
import deployment.PM_Control;
import deployment.Portfolio_Management;
import deployment.Program_Management;
import deployment.Project_Management;
import deployment.Risk_Management;
import deployment.Stakeholder_Management;

import io.ciera.runtime.summit.application.ApplicationExecutor;
import io.ciera.runtime.summit.application.IApplication;
import io.ciera.runtime.summit.application.ILogger;
import io.ciera.runtime.summit.application.tasks.GenericExecutionTask;
import io.ciera.runtime.summit.application.tasks.HaltExecutionTask;
import io.ciera.runtime.summit.components.IComponent;
import io.ciera.runtime.summit.exceptions.XtumlException;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeploymentApplication implements IApplication {

    private IComponent<?>[] components;
    private ApplicationExecutor[] executors;
	private static DeploymentApplication singleton;

    public DeploymentApplication() {
		singleton = this;
        components = new IComponent<?>[7];
        executors = new ApplicationExecutor[1];
		setup( null, null );
        initialize();
    }

    @Override
    public void run() {
        for (ApplicationExecutor executor : executors) {
            executor.run();
        }
    }

    @Override
    public void setup( String[] args, ILogger logger ) {
        for ( int i = 0; i < executors.length; i++ ) {
            if ( null != logger ) {
                executors[i] = new ApplicationExecutor( "DeploymentApplicationExecutor" + i, args, logger );
            }
            else {
                executors[i] = new ApplicationExecutor( "DeploymentApplicationExecutor" + i, args );
            }
        }
        components[0] = new Organisational_Management(this, executors[0], 0);
        components[1] = new PM_Control(this, executors[0], 1);
        components[4] = new Project_Management(this, executors[0], 4);
        components[5] = new Risk_Management(this, executors[0], 5);
        components[3] = new Program_Management(this, executors[0], 3);
        components[6] = new Stakeholder_Management(this, executors[0], 6);
        components[2] = new Portfolio_Management(this, executors[0], 2);
        ((Organisational_Management)components[0]).OrgMan().satisfy(((PM_Control)components[1]).OrgMan());
        ((PM_Control)components[1]).OrgMan().satisfy(((Organisational_Management)components[0]).OrgMan());
        ((Project_Management)components[4]).PrjMan().satisfy(((PM_Control)components[1]).PrjMan());
        ((PM_Control)components[1]).PrjMan().satisfy(((Project_Management)components[4]).PrjMan());
        ((Risk_Management)components[5]).RskMan().satisfy(((PM_Control)components[1]).RskMan());
        ((PM_Control)components[1]).RskMan().satisfy(((Risk_Management)components[5]).RskMan());
        ((Program_Management)components[3]).PrgMan().satisfy(((PM_Control)components[1]).PrgMan());
        ((PM_Control)components[1]).PrgMan().satisfy(((Program_Management)components[3]).PrgMan());
        ((Stakeholder_Management)components[6]).SHMan().satisfy(((PM_Control)components[1]).SHMan());
        ((PM_Control)components[1]).SHMan().satisfy(((Stakeholder_Management)components[6]).SHMan());
        ((Portfolio_Management)components[2]).PFMan().satisfy(((PM_Control)components[1]).PFMan());
        ((PM_Control)components[1]).PFMan().satisfy(((Portfolio_Management)components[2]).PFMan());
    }

    public Organisational_Management Organisational_Management() {
        return (Organisational_Management)components[0];
    }
    public PM_Control PM_Control() {
        return (PM_Control)components[1];
    }
    public Project_Management Project_Management() {
        return (Project_Management)components[4];
    }
    public Risk_Management Risk_Management() {
        return (Risk_Management)components[5];
    }
    public Program_Management Program_Management() {
        return (Program_Management)components[3];
    }
    public Stakeholder_Management Stakeholder_Management() {
        return (Stakeholder_Management)components[6];
    }
    public Portfolio_Management Portfolio_Management() {
        return (Portfolio_Management)components[2];
    }

    @Override
    public void initialize() {
        for ( IComponent<?> component : components ) {
            component.getRunContext().execute( new GenericExecutionTask() {
                @Override
                public void run() throws XtumlException {
                    component.initialize();
                }
            });
        }
    }

    @Override
    public void start() {
        if (1 == executors.length) {
            executors[0].run();
        }
        else {
            for ( ApplicationExecutor executor : executors ) {
                executor.start();
            }
        }
    }

    @Override
    public void printVersions() {
        io.ciera.runtime.Version.printVersion();
        for ( IComponent<?> c : components ) {
            System.out.printf("%s: %s (%s)", c.getClass().getName(), c.getVersion(), c.getVersionDate());
            System.out.println();
        }
    }

    @Override
    public void stop() {
        for ( ApplicationExecutor executor : executors ) {
            executor.execute(new HaltExecutionTask());
        }
    }

    public static void main( String[] args ) {
        SpringApplication.run( DeploymentApplication.class, args );
        singleton.start();
    }

}
