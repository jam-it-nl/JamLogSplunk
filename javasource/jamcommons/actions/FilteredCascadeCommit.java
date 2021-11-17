package jamcommons.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;
import com.mendix.logging.LogLevel;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.meta.IMetaAssociation;
import com.mendix.systemwideinterfaces.core.meta.IMetaObject;

public class FilteredCascadeCommit {
	
	private ILogNode logger;
	private String filter;
	private IContext context;
	
	public FilteredCascadeCommit(String filter, IContext context) {
		this.logger = Core.getLogger(FilteredCascadeCommit.class.getName());
		this.filter = filter;
		this.context = context;
	}
	
	public void cascadeCommit(IMendixObject object) {
		List<IMendixObject> committedObjects = new ArrayList<IMendixObject>();
		
		if (isFilterMatch(object)){
			cascadeCommit(object, committedObjects);
		}
	}

	private void cascadeCommit(IMendixObject object, List<IMendixObject> committedObjects) {
		if (!committedObjects.contains(object)){
			committedObjects.add(object);
		
			commitObject(object);
			
			// Associations are not comitted by default. They are being autocomitted by Mendix, but will be removed on logout.
			
			Collection<? extends IMetaAssociation> metaAssociationsParent = object.getMetaObject().getMetaAssociationsParent();
			for (IMetaAssociation metaAssociation : metaAssociationsParent)
			{
				
				commitAssociations(object, committedObjects, metaAssociation);
			}
			
			Collection<? extends IMetaAssociation> metaAssociationsChild = object.getMetaObject().getMetaAssociationsChild();
			for (IMetaAssociation metaAssociation : metaAssociationsChild)
			{
				
				commitAssociations(object, committedObjects, metaAssociation);
			}
		}
	}

	private void commitAssociations(IMendixObject object, List<IMendixObject> committedObjects, IMetaAssociation metaAssociation) {
		try{
			// Do not traverse System entities
			if (!metaAssociation.getName().contains("System.")){	
				List<IMendixObject> parentObjects = Core.retrieveByPath(this.getContext(), object, metaAssociation.getName());
				
				for (IMendixObject parent : parentObjects){	
					if (!parent.getType().contains("System.")){				
						if (isFilterMatch(parent)){
							cascadeCommit(parent, committedObjects);
						}
					}
				}
			}
		}catch (Exception e){
			if (this.logger.isDebugEnabled()){
				this.logger.log(LogLevel.DEBUG, "Could not commit " + metaAssociation.getName() + " because of error: " + e.getMessage());
			}
		}
	}

	private boolean isFilterMatch(IMendixObject object) {
		if (this.filter != null && object != null && object.getType() != null){
		
			if (object.getType().matches(this.filter)){
				return true;
			}
			
			for (IMetaObject superObject : object.getMetaObject().getSuperObjects()){
				if (superObject.getName().matches(this.filter)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void commitObject(IMendixObject object) {
		Core.commitWithoutEvents(this.getContext(), object);
		
		if (this.logger.isDebugEnabled()){
			this.logger.log(LogLevel.DEBUG, "Comitted " + object.getType() + " id: " + object.getId().toLong());
		}
	}

	public IContext getContext() {
		return context;
	}

}
