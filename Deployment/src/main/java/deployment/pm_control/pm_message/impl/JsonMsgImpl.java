package deployment.pm_control.pm_message.impl;


import deployment.PM_Control;
import deployment.pm_control.pm_control.Portfolio;
import deployment.pm_control.pm_control.PortfolioSet;
import deployment.pm_control.pm_control.Program;
import deployment.pm_control.pm_control.ProgramSet;
import deployment.pm_control.pm_control.Project;
import deployment.pm_control.pm_control.ProjectSet;
import deployment.pm_control.pm_control.Strategic_Goal;
import deployment.pm_control.pm_control.Strategic_GoalSet;
import deployment.pm_control.pm_message.JsonMsg;

import io.ciera.runtime.instanceloading.InstanceCreatedDelta;
import io.ciera.runtime.summit.application.ActionHome;
import io.ciera.runtime.summit.application.IRunContext;
import io.ciera.runtime.summit.classes.IInstanceIdentifier;
import io.ciera.runtime.summit.classes.InstanceIdentifier;
import io.ciera.runtime.summit.classes.ModelInstance;
import io.ciera.runtime.summit.exceptions.EmptyInstanceException;
import io.ciera.runtime.summit.exceptions.InstancePopulationException;
import io.ciera.runtime.summit.exceptions.XtumlException;
import io.ciera.runtime.summit.types.IWhere;
import io.ciera.runtime.summit.types.IXtumlType;
import io.ciera.runtime.summit.types.UniqueId;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import deployment.StrategicGoalMsg;

public class JsonMsgImpl extends ModelInstance<JsonMsg,PM_Control> implements JsonMsg {

    public static final String KEY_LETTERS = "JsonMsg";
    public static final JsonMsg EMPTY_JSONMSG = new EmptyJsonMsg();

    private PM_Control context;

    // constructors
    private JsonMsgImpl( PM_Control context ) {
        this.context = context;
    }

    private JsonMsgImpl( PM_Control context, UniqueId instanceId ) {
        super(instanceId);
        this.context = context;
    }

    public static JsonMsg create( PM_Control context ) throws XtumlException {
        JsonMsg newJsonMsg = new JsonMsgImpl( context );
        if ( context.addInstance( newJsonMsg ) ) {
            newJsonMsg.getRunContext().addChange(new InstanceCreatedDelta(newJsonMsg, KEY_LETTERS));
            return newJsonMsg;
        }
        else throw new InstancePopulationException( "Instance already exists within this population." );
    }

    public static JsonMsg create( PM_Control context, UniqueId instanceId ) throws XtumlException {
        JsonMsg newJsonMsg = new JsonMsgImpl( context, instanceId );
        if ( context.addInstance( newJsonMsg ) ) {
            return newJsonMsg;
        }
        else throw new InstancePopulationException( "Instance already exists within this population." );
    }



    // attributes


    // instance identifiers

    // operations


    // static operations
    public static class CLASS extends ActionHome<PM_Control> {

        public CLASS( PM_Control context ) {
            super( context );
        }

        public String get_Portfolios() throws XtumlException {
            PortfolioSet portfolios = context().Portfolio_instances();
            Portfolio p;
            for ( Iterator<Portfolio> _p_iter = portfolios.elements().iterator(); _p_iter.hasNext(); ) {
                p = _p_iter.next();
                context().LOG().LogInfo( ( ( "pf: " + p.getPF_Name() ) + " " ) + p.getDescription() );
                Strategic_Goal goal = p.R1_aligns_with_Strategic_Goal();
                if ( !goal.isEmpty() ) {
                    context().LOG().LogInfo( "g: " + goal.getSG_Name() );
                }
                ProgramSet programs = p.R4_may_include_Program();
                Program prg;
                for ( Iterator<Program> _prg_iter = programs.elements().iterator(); _prg_iter.hasNext(); ) {
                    prg = _prg_iter.next();
                    context().LOG().LogInfo( "prg: " + prg.getPRG_Name() );
                }
                ProjectSet projects = p.R6_may_include_Project();
                Project prj;
                for ( Iterator<Project> _prj_iter = projects.elements().iterator(); _prj_iter.hasNext(); ) {
                    prj = _prj_iter.next();
                    context().LOG().LogInfo( "prj: " + prj.getPRJ_Name() );
                }
            }
            return "";
        }

        public String get_Programs() throws XtumlException {
            ProgramSet programs = context().Program_instances();
            Program p;
            for ( Iterator<Program> _p_iter = programs.elements().iterator(); _p_iter.hasNext(); ) {
                p = _p_iter.next();
                context().LOG().LogInfo( ( ( "prg: " + p.getPRG_Name() ) + " " ) + p.getDescription() );
                Portfolio pf = p.R4_may_be_part_of_Portfolio();
                if ( !pf.isEmpty() ) {
                    context().LOG().LogInfo( "pf: " + pf.getPF_Name() );
                }
                ProjectSet projects = p.R5_may_include_Project();
                Project prj;
                for ( Iterator<Project> _prj_iter = projects.elements().iterator(); _prj_iter.hasNext(); ) {
                    prj = _prj_iter.next();
                    context().LOG().LogInfo( "prj: " + prj.getPRJ_Name() );
                }
            }
            return "";
        }

        public String get_Projects() throws XtumlException {
            ProjectSet projects = context().Project_instances();
            Project p;
            for ( Iterator<Project> _p_iter = projects.elements().iterator(); _p_iter.hasNext(); ) {
                p = _p_iter.next();
                context().LOG().LogInfo( ( ( "prg: " + p.getPRJ_Name() ) + " " ) + p.getDescription() );
                Strategic_Goal goal = p.R3_may_be_driven_by_Strategic_Goal();
                if ( !goal.isEmpty() ) {
                    context().LOG().LogInfo( "g: " + goal.getSG_Name() );
                }
                Program prg = p.R5_may_be_part_of_Program();
                if ( !prg.isEmpty() ) {
                    context().LOG().LogInfo( "prg: " + prg.getPRG_Name() );
                }
            }
            return "";
        }

        public String get_Strategic_Goals() throws XtumlException {
            Strategic_GoalSet goals = context().Strategic_Goal_instances();
            Strategic_Goal g;
			ArrayList<StrategicGoalMsg> msgArr = new ArrayList<StrategicGoalMsg>();
            for ( Iterator<Strategic_Goal> _g_iter = goals.elements().iterator(); _g_iter.hasNext(); ) {
                g = _g_iter.next();
                //context().LOG().LogInfo( ( ( "sg: " + g.getSG_Name() ) + " " ) + g.getDescription() );
				msgArr.add(new StrategicGoalMsg(g.getSG_Name(), g.getDescription(), g.getPriority().toString()));	
            }
			
			JSONArray jsArray = new JSONArray(msgArr);
			context().LOG().LogInfo("JSON: " + jsArray.toString());
            return jsArray.toString();
        }



    }


    // events


    // selections


    @Override
    public IRunContext getRunContext() {
        return context().getRunContext();
    }

    @Override
    public PM_Control context() {
        return context;
    }

    @Override
    public String getKeyLetters() {
        return KEY_LETTERS;
    }

    @Override
    public JsonMsg self() {
        return this;
    }

    @Override
    public JsonMsg oneWhere(IWhere<IXtumlType> condition) throws XtumlException {
        if (null == condition) throw new XtumlException("Null condition passed to selection.");
        if (condition.evaluate(this)) return this;
        else return EMPTY_JSONMSG;
    }

}

class EmptyJsonMsg extends ModelInstance<JsonMsg,PM_Control> implements JsonMsg {

    // attributes


    // operations


    // selections


    @Override
    public String getKeyLetters() {
        return JsonMsgImpl.KEY_LETTERS;
    }

    @Override
    public JsonMsg self() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public JsonMsg oneWhere(IWhere<IXtumlType> condition) throws XtumlException {
        if (null == condition) throw new XtumlException("Null condition passed to selection.");
        return JsonMsgImpl.EMPTY_JSONMSG;
    }

}
