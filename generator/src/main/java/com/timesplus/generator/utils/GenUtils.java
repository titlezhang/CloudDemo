package com.timesplus.generator.utils;

import com.timesplus.common.exception.BizException;
import com.timesplus.common.utils.DateUtils;
import com.timesplus.generator.bean.TableBean;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 代码生成器   工具类
 *  
 */
public class GenUtils {

	public static List<String> getTemplates() {
		List<String> templates = new ArrayList<String>();
		templates.add( "template/Bean.java.vm" );
		templates.add( "template/domain.java.vm" );
		templates.add( "template/Dao.java.vm" );
		templates.add( "template/Dao.xml.vm" );
		templates.add( "template/Service.java.vm" );
		templates.add( "template/ServiceImpl.java.vm" );
		templates.add( "template/Controller.java.vm" );
		return templates;
	}

	/**
	 * 生成代码
	 */
	public static void generatorCode( Map<String, String> table, List<Map<String, String>> columns, ZipOutputStream zip )
      throws Exception {
		// 配置信息
		Configuration config=null;
		try {
			config= new PropertiesConfiguration( "generator.properties" );
		} catch( ConfigurationException e ) {
			throw new BizException( "代码生成获取配置generator.properties文件失败");
		}

		// 表信息
		TableBean tableBean = new TableBean();
		tableBean.setTableName( table.get( "ENGINE" ) );
		String tablecomment = table.get("TABLECOMMENT");
		if(tablecomment!=null&&tablecomment.length()>0){
			tableBean.setComments( tablecomment.split( ";" )[0] );
		}
		// 表名转换成Java类名
		String className = tableToJava( tableBean.getTableName(), config.getString( "tablePrefix" ) );
		tableBean.setClassName( className );
		tableBean.setClasssname( StringUtils.uncapitalize( className ) );

		// 列信息
		List<com.timesplus.generator.bean.ColumnBean> columsList = new ArrayList<>();
		for( Map<String, String> column : columns ) {
			com.timesplus.generator.bean.ColumnBean columnBean = new com.timesplus.generator.bean.ColumnBean();
			columnBean.setColumnName( column.get( "COLUMNNAME" ) );
			columnBean.setDataType( column.get( "DATATYPE" ) );
			columnBean.setComments( column.get( "COLUMNCOMMENT" ) );
//			columnBean.setExtra( column.get( "extra" ) );

			// 列名转换成Java属性名
			String attrName = columnToJava( columnBean.getColumnName() );
			columnBean.setAttrName( attrName );
			columnBean.setAttrrname( StringUtils.uncapitalize( attrName ) );

			// 列的数据类型，转换成Java类型
			String attrType = config.getString( columnBean.getDataType().toLowerCase(), "unknowType" );
			columnBean.setAttrType( attrType );

			// 是否主键
			if( "PRI".equalsIgnoreCase( column.get( "COLUMNKEY" ) ) && tableBean.getPk() == null ) {
				tableBean.setPk( columnBean );
			}

			columsList.add( columnBean );
		}
		tableBean.setColumns( columsList );

		// 没主键，则第一个字段为主键
		if( tableBean.getPk() == null ) {
			tableBean.setPk( tableBean.getColumns().get( 0 ) );
		}

		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put( "file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
		Velocity.init(prop);

		// 封装模板数据
		Map<String, Object> map = new HashMap<>();
		map.put( "tableName", tableBean.getTableName() );
		map.put( "comments", tableBean.getComments() );
		map.put( "pk", tableBean.getPk() );
		map.put( "className", tableBean.getClassName() );
		map.put( "classname", tableBean.getClasssname() );
		map.put( "pathName", tableBean.getClasssname().toLowerCase() );
		map.put( "columns", tableBean.getColumns() );
		map.put( "package", config.getString( "package" ) );
		map.put( "author", config.getString( "author" ) );
		map.put( "email", config.getString( "email" ) );
		map.put( "datetime", DateUtils.format( new Date(), DateUtils.DATE_TIME_PATTERN ) );
		VelocityContext context = new VelocityContext( map );

		// 获取模板列表
		List<String> templates = getTemplates();
		for( String template : templates ) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate( template, "UTF-8" );
			tpl.merge( context, sw );

			try {
				// 添加到zip
				zip.putNextEntry( new ZipEntry( getFileName( template, tableBean.getClassName(), config.getString( "controllerpackage" ) ) ) );
				IOUtils.write( sw.toString(), zip, "UTF-8" );
				IOUtils.closeQuietly( sw );
				zip.closeEntry();
			} catch( IOException e ) {
				throw new BizException( "渲染模板失败，表名：" + tableBean.getTableName(), e );
			}
		}
	}

	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava( String columnName ) {
		return WordUtils.capitalizeFully( columnName, new char[] { '_' } ).replace( "_", "" );
	}

	/**
	 * 表名转换成Java类名
	 */
	public static String tableToJava( String tableName, String tablePrefix ) {
		if( StringUtils.isNotBlank( tablePrefix ) ) {
			tableName = tableName.replace( tablePrefix, "" );
		}
		return columnToJava( tableName );
	}

	/**
	 * 获取配置信息
	 */
//	public static Configuration getConfig() {
//		try {
//			return new PropertiesConfiguration( "generator.properties" );
//		} catch( ConfigurationException e ) {
//			throw new RRException( "获取配置文件失败，", e );
//		}
//	}

	/**
	 * 获取文件名
	 */
	public static String getFileName( String template, String className, String packageName ) {
		String packagePath = "main" + File.separator + "java" + File.separator;
		if( StringUtils.isNotBlank( packageName ) ) {
			packagePath += packageName.replace( ".", File.separator ) + File.separator;
		}

		if( template.contains( "Bean.java.vm" ) ) {
			return packagePath + "bean" + File.separator + className + "Bean.java";
		}
		
		if( template.contains( "domain.java.vm" ) ) {
			return packagePath + "bean" + File.separator + className + ".java";
		}

		if( template.contains( "Dao.java.vm" ) ) {
			return packagePath + "dao" + File.separator + className + "Dao.java";
		}

		if( template.contains( "Dao.xml.vm" ) ) {
			return packagePath + "dao" + File.separator + className + "Dao.xml";
		}

		if( template.contains( "Service.java.vm" ) ) {
			return packagePath + "service" + File.separator + className + "Service.java";
		}

		if( template.contains( "ServiceImpl.java.vm" ) ) {
			return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}

		if( template.contains( "Controller.java.vm" ) ) {
			return packagePath + "controller" + File.separator + className + "Controller.java";
		}


		return null;
	}

}
