// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package jamcommons.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;

/**
 * Splits this string around matches of the given regular expression.
 * Trailing empty strings are not included in the resulting array.
 * 
 * The string "boo:and:foo", for example, yields the following results with these expressions:
 * 
 * Regex	Result
 * :	{ "boo", "and", "foo" }
 * o	{ "b", "", ":and:f" }
 */
public class StringJoin extends CustomJavaAction<java.lang.String>
{
	private java.lang.String delimiter;
	private java.util.List<IMendixObject> objects;
	private java.lang.String attribute;
	private java.lang.Boolean urlEncode;

	public StringJoin(IContext context, java.lang.String delimiter, java.util.List<IMendixObject> objects, java.lang.String attribute, java.lang.Boolean urlEncode)
	{
		super(context);
		this.delimiter = delimiter;
		this.objects = objects;
		this.attribute = attribute;
		this.urlEncode = urlEncode;
	}

	@java.lang.Override
	public java.lang.String executeAction() throws Exception
	{
		// BEGIN USER CODE
		List<String> elements = this.objects.stream().map((object) -> {
			try {
				Object value = object.getValue(super.getContext(), this.attribute);
				if (value == null) {
					return null;
				}

				if (this.urlEncode) {
					return URLEncoder.encode(value.toString(), "UTF-8");
				}

				return value.toString();
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}).collect(Collectors.toList());
		return String.join(this.delimiter, elements);
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "StringJoin";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
