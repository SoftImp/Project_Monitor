package deployment;


import deployment.project_management.Project_ManagementPrjMan;

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


public class Project_Management extends Component<Project_Management> {

    private Map<String, Class<?>> classDirectory;
    private static Project_Management Singleton;

    public Project_Management(IApplication app, IRunContext runContext, int populationId) {
        super(app, runContext, populationId);



        classDirectory = new TreeMap<>();
        Singleton = this;
    }

    public static Project_Management Singleton() {
        return Singleton;
    }

    // domain functions
    public void on_Perf_Rep( final String p_PRJ_Name,  final int p_Rep_ID,  final String p_Perf_Rep ) throws XtumlException {
        try {
			//System.out.printf("on_Perf_Rep() JSON: %s\n", p_Perf_Rep);	
            Project_ManagementController.Singleton().on_Perf_Rep( p_PRJ_Name,  p_Rep_ID, p_Perf_Rep );
      	} catch ( Exception e ) {}
    }

    public void on_Projects( final String p_Projects, final String p_PRJ_Name ) throws XtumlException {
        try {
			//System.out.printf("on_Projects() JSON: %s\n", p_Projects);	
            Project_ManagementController.Singleton().on_Projects( p_Projects,  p_PRJ_Name);
      	} catch ( Exception e ) {}
    }



    // relates and unrelates


    // instance selections


    // relationship selections


    // ports
    private Project_ManagementPrjMan Project_ManagementPrjMan;
    public Project_ManagementPrjMan PrjMan() {
        if ( null == Project_ManagementPrjMan ) Project_ManagementPrjMan = new Project_ManagementPrjMan( this, null );
        return Project_ManagementPrjMan;
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
            prop.load(getClass().getResourceAsStream("Project_ManagementProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version", "Unknown");
    }
    @Override
    public String getVersionDate() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("Project_ManagementProperties.properties"));
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
    public Project_Management context() {
        return this;
    }

    @Override
    public Class<?> getClassByKeyLetters(String keyLetters) {
        return classDirectory.get(keyLetters);
    }

}
