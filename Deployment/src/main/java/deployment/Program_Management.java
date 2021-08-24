package deployment;


import deployment.program_management.Program_ManagementPrgMan;

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


public class Program_Management extends Component<Program_Management> {

    private Map<String, Class<?>> classDirectory;
    private static Program_Management Singleton;

    public Program_Management(IApplication app, IRunContext runContext, int populationId) {
        super(app, runContext, populationId);



        classDirectory = new TreeMap<>();
        Singleton = this;
    }

    public static Program_Management Singleton() {
        return Singleton;
    }

    // domain functions
    public void on_Programs( final String p_Programs ) throws XtumlException {
        try {
			//System.out.printf("on_Programs() JSON: %s\n", p_Programs);	
            Program_ManagementController.Singleton().on_Programs( p_Programs );
      	} catch ( Exception e ) {}
    }



    // relates and unrelates


    // instance selections


    // relationship selections


    // ports
    private Program_ManagementPrgMan Program_ManagementPrgMan;
    public Program_ManagementPrgMan PrgMan() {
        if ( null == Program_ManagementPrgMan ) Program_ManagementPrgMan = new Program_ManagementPrgMan( this, null );
        return Program_ManagementPrgMan;
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
            prop.load(getClass().getResourceAsStream("Program_ManagementProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version", "Unknown");
    }
    @Override
    public String getVersionDate() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("Program_ManagementProperties.properties"));
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
    public Program_Management context() {
        return this;
    }

    @Override
    public Class<?> getClassByKeyLetters(String keyLetters) {
        return classDirectory.get(keyLetters);
    }

}
