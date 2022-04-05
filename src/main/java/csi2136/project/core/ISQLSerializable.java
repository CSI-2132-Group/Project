package csi2136.project.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ISQLSerializable<T> {

	T write(Database db) throws SQLException;

	T read(ResultSet result, Database db) throws SQLException;

}
