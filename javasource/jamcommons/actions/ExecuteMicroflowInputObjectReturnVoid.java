// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package jamcommons.actions;

import java.util.HashMap;
import java.util.Map;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;

public class ExecuteMicroflowInputObjectReturnVoid extends CustomJavaAction<java.lang.Boolean>
{
	private java.lang.String microflowName;
	private java.lang.String inputParameterName;
	private IMendixObject inputParameterValue;

	public ExecuteMicroflowInputObjectReturnVoid(IContext context, java.lang.String microflowName, java.lang.String inputParameterName, IMendixObject inputParameterValue)
	{
		super(context);
		this.microflowName = microflowName;
		this.inputParameterName = inputParameterName;
		this.inputParameterValue = inputParameterValue;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		// BEGIN USER CODE
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(this.inputParameterName, this.inputParameterValue);

		Core.microflowCall(this.microflowName).withParams(parameters).execute(this.getContext());
		return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "ExecuteMicroflowInputObjectReturnVoid";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
