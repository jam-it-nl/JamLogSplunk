package jamcommons;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class AbstractWhereQueryInformation {

	private String tableName;
	protected SortedMap<String, Object> whereColumnValues = new TreeMap<String, Object>();

	public AbstractWhereQueryInformation(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void addWhereColumnValue(String key, Object value) {
		whereColumnValues.put(key, value);
	}

	public Collection<Object> getWhereColumnValues() {
		return whereColumnValues.values();
	}

	public String getWhereClause() {
		StringBuilder where = new StringBuilder("WHERE ");
		int parameterIndex = 1;
		for (String key : whereColumnValues.keySet()) {
			where.append(key);
			where.append(" = ?");

			if (parameterIndex != whereColumnValues.size()) {
				where.append(" AND ");
			}

			parameterIndex++;
		}
		return where.toString();
	}

}
