package com.timesplus.generator.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * 代码生成器DAO
 *  
 */

public interface SysGeneratorDao {

	List<Map<String, Object>> queryList( Map<String, Object> map );

	int queryTotal( Map<String, Object> map );

	Map<String, String> queryTable( String tableName );

	List<Map<String, String>> queryColumns( String tableName );
}
