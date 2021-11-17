package jamcommons;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.mendix.core.Core;
import com.mendix.core.objectmanagement.member.MendixAutoNumber;
import com.mendix.core.objectmanagement.member.MendixBinary;
import com.mendix.core.objectmanagement.member.MendixBoolean;
import com.mendix.core.objectmanagement.member.MendixObjectReference;
import com.mendix.core.objectmanagement.member.MendixObjectReferenceSet;
import com.mendix.logging.LogLevel;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.IMendixObjectMember;
import com.mendix.systemwideinterfaces.core.IMendixObjectMember.MemberState;

import jamcommons.impl.SerialExecutor;

public class JamCommons {

	public static void runMicroflowAsyncInQueue(String queue, String microflowName) {
		SerialExecutor.getInstance(queue).execute(new Runnable() {
			@Override
			public void run() {
				try {
					IContext context = Core.createSystemContext();
					Core.executeAsync(context, microflowName, true, new HashMap<>()).get();
				} catch (Exception e) {
					throw new RuntimeException("[" + queue + "] Failed to run async: " + microflowName + ": " + e.getMessage(), e);
				}
			}
		});
	}

	public static String getObjectName(IMendixObject mendixObject) {
		String type = mendixObject.getType();
		return JamCommons.getShortname(type);
	}

	public static String getModule(String type) {
		String[] splitted = type.split("\\.");
		if (splitted.length > 0) {
			return splitted[0];
		}
		return type;
	}

	public static String getShortname(String type) {
		String[] splitted = type.split("\\.");
		if (splitted.length > 1) {
			return splitted[1];
		}
		return type;
	}

	public static String getDatabaseSaveName(String tableName) {
		// Max length of MySql database is 64 characters
		if (tableName.length() > 64) {
			return tableName.substring(0, 63);
		}
		return tableName;
	}

	public static boolean isObjectChanged(IContext context, IMendixObject mendixObject) {
		for (IMendixObjectMember<?> member : mendixObject.getMembers(context).values()) {
			if (member.getState() == MemberState.CHANGED) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSerializable(IContext context, IMendixObject mendixObject) {
		if (mendixObject == null) {
			return false;
		}

		if (!isObjectChanged(context, mendixObject)) {
			return false;
		}
		return true;
	}

	public static void clearMembers(IContext context, IMendixObject mendixObject) {
		// Clear all values of main object except ObjectId
		Map<String, ? extends IMendixObjectMember<?>> members = mendixObject.getMembers(context);

		for (String key : members.keySet()) {
			if (key.equals("ObjectId")) {
				continue;
			}

			IMendixObjectMember<?> member = members.get(key);
			if (member.isVirtual()) {
				continue;
			} else if (member instanceof MendixAutoNumber) {
				continue;
			} else if (member instanceof MendixBoolean) {
				continue;
			} else if (member instanceof MendixBinary) {
				try {
					Core.storeFileDocumentContent(context, mendixObject, mendixObject.getValue(context, "Name"), new ByteArrayInputStream("".getBytes("UTF-8")));
					// mendixObject.setValue(context, "Contents", new
					// EmptyMendixBinary());
					mendixObject.setValue(context, "HasContents", false);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
				continue;
			} else if (member instanceof MendixObjectReference) {
				continue;
			} else if (member instanceof MendixObjectReferenceSet) {
				continue;
			} else {
				mendixObject.setValue(context, key, null);
			}
		}
	}

	public static LogLevel getMendixLogLevel(jamcommons.proxies.LogLevel logLevel) {
		switch (logLevel) {
		case Critical:
			return LogLevel.CRITICAL;
		case Debug:
			return LogLevel.DEBUG;
		case Error:
			return LogLevel.ERROR;
		case Info:
			return LogLevel.INFO;
		case None:
			return LogLevel.NONE;
		case Trace:
			return LogLevel.TRACE;
		case Warning:
			return LogLevel.WARNING;
		default:
			return LogLevel.NONE;
		}
	}

}
