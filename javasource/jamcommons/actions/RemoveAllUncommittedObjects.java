// This file was generated by Mendix Modeler.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package jamcommons.actions;

import java.util.Collection;

import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.IUser;
import com.mendix.systemwideinterfaces.core.meta.IMetaAssociation;
import com.mendix.webui.CustomJavaAction;

public class RemoveAllUncommittedObjects extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject anyObject;

	public RemoveAllUncommittedObjects(IContext context, IMendixObject anyObject)
	{
		super(context);
		this.anyObject = anyObject;
	}

	@Override
	public java.lang.Boolean executeAction() throws Exception
	{
		// BEGIN USER CODE
		
		IUser currentUser = this.getContext().getSession().getUser(this.getContext());
		if (currentUser != null){
			IMendixObject currentUserMendixObject = currentUser.getMendixObject();
			
			Collection<? extends IMetaAssociation> metaAssociationsParent = currentUserMendixObject.getMetaObject().getMetaAssociationsParent();
			Collection<? extends IMetaAssociation> metaAssociationsChild = currentUserMendixObject.getMetaObject().getMetaAssociationsChild();
			
			Core.retrieveByPath(this.getContext(), currentUserMendixObject, "SessionScopedImage_Session");
			
			
			Core.rollback(super.getContext(), anyObject);
		}
		
		
		
		//Core.createOQLTextGetRequest()
		
		//Iterable<IMendixObject> allMendixObjects = Core.getAllMendixObjects();
		
		//Core.
		
		//List<IMendixIdentifier> contextObjects = this.getContext().getContextObjects();
		//this.getContext().get
		//Iterator<IMetaObject> it = Core.getMetaObjects();
		//Core.retrieveByPath(arg0, arg1, arg2)
		return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@Override
	public java.lang.String toString()
	{
		return "RemoveAllUncommittedObjects";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}