package deployment.pm_control.pm_message.impl;

import deployment.StrategicGoalMsg;
import deployment.PM_Control;
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
//import com.google.gson.Gson;


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
