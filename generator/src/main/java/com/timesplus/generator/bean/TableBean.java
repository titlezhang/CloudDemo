package com.timesplus.generator.bean;

import java.util.List;

import lombok.Data;

/**
 * 表数据对应实体类
 *
 *  
 */
@Data
public class TableBean {

	// 表的名称
	private String tableName;

	// 表的备注
	private String comments;

	// 表的主键
	private ColumnBean pk;

	// 表的列名(不包含主键)
	private List<ColumnBean> columns;

	// 类名(第一个字母大写)，如：sys_user => SysUser
	private String className;

	// 类名(第一个字母小写)，如：sys_user => sysUser
	private String classsname;

}
