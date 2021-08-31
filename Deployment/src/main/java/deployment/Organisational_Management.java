package deployment;


import deployment.organisational_management.Organisational_ManagementOrgMan;

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


public class Organisational_Management extends Component<Organisational_Management> {

    private Map<String, Class<?>> classDirectory;
    private static Organisational_Management Singleton;

    public Organisational_Management(IApplication app, IRunContext runContext, int populationId) {
        super(app, runContext, populationId);



        classDirectory = new TreeMap<>();
        Singleton = this;
    }
	
    public static Organisational_Management Singleton() {
        return Singleton;
    }

    // domain functions
    public void on_Strategic_Goals( final String p_Strategic_Goals,  final String p_SG_Name ) throws XtumlException {
		try {
			//System.out.printf("on_Strategic_Goals() JSON: %s\n", p_Strategic_Goals);	
            Organisational_ManagementController.Singleton().onStrategicGoals( p_Strategic_Goals,  p_SG_Name );
      	} catch ( Exception e ) {}
    }


    // relates and unrelates


    // instance selections


    // relationship selections


    // ports
    private Organisational_ManagementOrgMan Organisational_ManagementOrgMan;
    public Organisational_ManagementOrgMan OrgMan() {
        if ( null == Organisational_ManagementOrgMan ) Organisational_ManagementOrgMan = new Organisational_ManagementOrgMan( this, null );
        return Organisational_ManagementOrgMan;
    }


    // utilities


    // component initialization function
    @Override
    public void initialize() throws XtumlException {

    }

    @Override
    public String getVersion() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("Organisational_ManagementProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version", "Unknown");
    }
    @Override
    public String getVersionDate() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("Organisational_ManagementProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version_date", "Unknown");
    }

    @Override
    public boolean addInstance( IModelInstance<?,?> instance ) throws XtumlException {
        if ( null == instance ) throw new BadArgumentException( "Null instance passed." );
        if ( instance.isEmpty() ) throw new EmptyInstanceException( "Cannot add empty instance to population." );

        return false;
    }

    @Override
    public boolean removeInstance( IModelInstance<?,?> instance ) throws XtumlException {
        if ( null == instance ) throw new BadArgumentException( "Null instance passed." );
        if ( instance.isEmpty() ) throw new EmptyInstanceException( "Cannot remove empty instance from population." );

        return false;
    }

    @Override
    public Organisational_Management context() {
        return this;
    }

    @Override
    public Class<?> getClassByKeyLetters(String keyLetters) {
        return classDirectory.get(keyLetters);
    }

}
