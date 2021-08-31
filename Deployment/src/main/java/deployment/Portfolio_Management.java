package deployment;


import deployment.portfolio_management.Portfolio_ManagementPFMan;

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


public class Portfolio_Management extends Component<Portfolio_Management> {

    private Map<String, Class<?>> classDirectory;
    private static Portfolio_Management Singleton;

    public Portfolio_Management(IApplication app, IRunContext runContext, int populationId) {
        super(app, runContext, populationId);



        classDirectory = new TreeMap<>();
        Singleton = this;
    }

    public static Portfolio_Management Singleton() {
        return Singleton;
    }

    // domain functions
    public void on_Portfolios( final String p_Portfolios, final String p_PF_Name ) throws XtumlException {
        try {
			//System.out.printf("on_Portfolios() JSON: %s\n", p_Portfolios);	
            Portfolio_ManagementController.Singleton().on_Portfolios( p_Portfolios, p_PF_Name );
      	} catch ( Exception e ) {}
    }



    // relates and unrelates


    // instance selections


    // relationship selections


    // ports
    private Portfolio_ManagementPFMan Portfolio_ManagementPFMan;
    public Portfolio_ManagementPFMan PFMan() {
        if ( null == Portfolio_ManagementPFMan ) Portfolio_ManagementPFMan = new Portfolio_ManagementPFMan( this, null );
        return Portfolio_ManagementPFMan;
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
            prop.load(getClass().getResourceAsStream("Portfolio_ManagementProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version", "Unknown");
    }
    @Override
    public String getVersionDate() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("Portfolio_ManagementProperties.properties"));
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
    public Portfolio_Management context() {
        return this;
    }

    @Override
    public Class<?> getClassByKeyLetters(String keyLetters) {
        return classDirectory.get(keyLetters);
    }

}
