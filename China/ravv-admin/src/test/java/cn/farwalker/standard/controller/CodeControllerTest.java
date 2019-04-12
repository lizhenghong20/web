//package cn.farwalker.standard.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.beetl.core.GroupTemplate;
//import org.beetl.core.Template;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MvcResult;
//
//import com.baomidou.mybatisplus.enums.IdType;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
//import com.baomidou.mybatisplus.generator.config.GlobalConfig;
//import com.baomidou.mybatisplus.generator.config.PackageConfig;
//import com.baomidou.mybatisplus.generator.config.StrategyConfig;
//import com.baomidou.mybatisplus.generator.config.rules.DbType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import com.sun.javafx.PlatformUtil;
//
//import cn.farwalker.standard.modular.code.service.TableService;
//import cn.farwalker.waka.generator.action.model.GenQo;
//
//public class CodeControllerTest extends BaseJUnitController{
//
//	private GroupTemplate groupTemplate;
//
//	@Autowired
//	private TableService tableService;
//
//	/**
//     * 是否强制带上注解
//     */
//    boolean enableTableFieldAnnotation = false;
//    /**
//     * 生成的注解带上IdType类型
//     */
//    IdType tableIdType = null;
//    /**
//     * 是否去掉生成实体的属性名前缀
//     */
//    String[] fieldPrefix = null;
//    /**
//     * 生成的Service 接口类名是否以I开头
//     * 默认是以I开头
//     * user表 -> IUserService, UserServiceImpl
//     */
//    boolean serviceClassNameStartWithI = true;
//
//	@Test
//	public void genAll() {
//        List<Map<String, Object>> allTables = tableService.getAllTables();
//        String[] exportTables = {"sys_dept","sys_dict","sys_expense","sys_login_log",
//        							"sys_menu","sys_notice","sys_operation_log","sys_param",
//        							"sys_relation","sys_role","sys_user"};
//      // ,"pam_login_log","pam_member","pam_operation_log"};
//
//
//        List<String> tableList = new ArrayList<>();
//        for(Map<String, Object> table : allTables) {
//        	String tableName = (String) table.get("tableName");
//        	Boolean flag = false;
//        	for(String etable : exportTables) {
//        		if(etable.equals(tableName)) {
//        			flag = true;
//        			break;
//        		}
//        	}
//        	if(!flag) {
//        		tableList.add(tableName);
//        	}
//        }
//
//        String[] tables = new String[tableList.size()];
//        tableList.toArray(tables);
//        codeGen(tables);
//	}
//
//	private void codeGen(String[] tables) {
//		String packageName = "cn.farwalker.ledian";
//        enableTableFieldAnnotation = false;
//        tableIdType = null;
//      /*  generateByTables(packageName + ".noannoidtype", "sys_param");
//        enableTableFieldAnnotation = true;
//        tableIdType = null;
//        generateByTables(packageName + ".noidtype", "sys_param");
//        enableTableFieldAnnotation = false;
//        tableIdType = IdType.INPUT;
//        generateByTables(packageName + ".noanno", "sys_param");
//        enableTableFieldAnnotation = true;
//        tableIdType = IdType.INPUT;
//        generateByTables(packageName + ".both", "sys_param");
//
//        fieldPrefix = new String[]{"test"};
//        enableTableFieldAnnotation = false;
//        tableIdType = null;
//        generateByTables(packageName + ".noannoidtypewithprefix", "sys_param");
//        enableTableFieldAnnotation = true;
//        tableIdType = null;
//        generateByTables(packageName + ".noidtypewithprefix", "sys_param");
//        enableTableFieldAnnotation = false;
//        tableIdType = IdType.INPUT;
//        generateByTables(packageName + ".noannowithprefix", "sys_param");
//        enableTableFieldAnnotation = true;
//        tableIdType = IdType.INPUT;
//        generateByTables(packageName + ".withannoidtypeprefix", "sys_param");*/
//
//        serviceClassNameStartWithI = false;
//        generateByTables(packageName, tables);
//	}
//
//	private void generateByTables(String packageName, String... tableNames) {
//        GlobalConfig config = new GlobalConfig();
//        String dbUrl = "jdbc:mysql://120.77.217.189:3306/ledian";
//        DataSourceConfig dataSourceConfig = new DataSourceConfig();
//        dataSourceConfig.setDbType(DbType.MYSQL)
//            .setUrl(dbUrl)
//            .setUsername("ledian")
//            .setPassword("Ledian1688")
//            .setDriverName("com.mysql.jdbc.Driver");
//
//
//        StrategyConfig strategyConfig = new StrategyConfig();
//        strategyConfig
//            .setCapitalMode(true)
//            .setEntityLombokModel(false)
//            .setDbColumnUnderline(true)
//            .setNaming(NamingStrategy.underline_to_camel)
//            .entityTableFieldAnnotationEnable(enableTableFieldAnnotation)
//            .fieldPrefix(fieldPrefix)//test_id -> id, test_type -> type
//            .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
//        config.setActiveRecord(false)
//            .setIdType(tableIdType)
//            .setAuthor("K神带你飞")
//            .setOutputDir("d:\\codeGen")
//            .setFileOverride(true);
//        if (!serviceClassNameStartWithI) {
//            config.setServiceName("%sService");
//        }
//        new AutoGenerator().setGlobalConfig(config)
//            .setDataSource(dataSourceConfig)
//            .setStrategy(strategyConfig)
//            .setPackageInfo(
//                new PackageConfig()
//                    .setParent(packageName)
//                    .setController("controller")
//                    .setEntity("entity")
//            ).execute();
//    }
//
////	@Test
//	public void generate() throws Exception {
//		/*String path = ToolUtil.format(super.getContextConfig().getAdminPath() + getPageConfig().getPageEditPathTemplate(),
//                super.getContextConfig().getBizEnName(), super.getContextConfig().getBizEnName());
//        generateFile(super.getContextConfig().getTemplatePrefixPath() + "/page_edit.html.btl", path);
//        System.out.println("生成编辑页面成功!");*/
//		GenQo genQo = getConfig();
////		genQo.set
//		MvcResult result = mockMvc.perform(post("/code/generate")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//				.header("Content-Type", "application/json")
//				.param("tableName", "base_area")
//				.param("projectPath", "/D:/code/farsvn/waka/wakayun/standard/standard-service")
//				.param("adminPath", "/D:/code/farsvn/waka/wakayun/standard/standard-admin")
//				.param("author", "Jason Chen")
//				.param("projectPackage", "cn.farwalker.ledian")
//				.param("corePackage", "cn.farwalker.waka.core")
//				.param("ignoreTabelPrefix", "")
//				.param("bizName", "地区")
//				.param("moduleName", "system")
//				.param("parentMenuName", "系统管理")
//				.param("controllerSwitch", "false")
//				.param("entitySwitch", "true")
//				.param("serviceSwitch", "false")
//				.param("daoSwitch", "false")
//				.param("indexPageSwitch", "false")
//				.param("addPageSwitch", "false")
//				.param("editPageSwitch", "false")
//				.param("jsSwitch", "false")
//				.param("infoJsSwitch", "false")
//				.param("sqlSwitch", "false")
//				.accept(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk()) //模拟发出post请求
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
//				.andReturn();//返回执行结果
//		System.out.println(result.getResponse().getContentAsString());
//	}
//
//	private GenQo getConfig() {
//		GenQo genQo = new GenQo();
//		genQo.setTableName("");
//		genQo.setProjectPath("/D:/code/farsvn/waka/wakayun/standard/standard-service");
//		genQo.setAdminPath("/D:/code/farsvn/waka/wakayun/standard/standard-admin");
//		genQo.setAuthor("Jason Chen");
//		genQo.setProjectPackage("cn.farwalker.standard");
//		genQo.setCorePackage("cn.farwalker.waka.core");
//		genQo.setIgnoreTabelPrefix("sys_");
//		genQo.setBizName("地区");
//		genQo.setModuleName("system");
//		genQo.setParentMenuName("系统管理");
//		genQo.setControllerSwitch(false);
//		genQo.setEntitySwitch(true);
//		genQo.setServiceSwitch(false);
//		genQo.setDaoSwitch(false);
//		genQo.setIndexPageSwitch(false);
//		genQo.setAddPageSwitch(false);
//		genQo.setEditPageSwitch(false);
//		genQo.setJsSwitch(false);
//		genQo.setInfoJsSwitch(false);
//		genQo.setSqlSwitch(false);
//
//		return genQo;
//	}
//
//	protected void generateFile(String template, String filePath) {
//        Template pageTemplate = groupTemplate.getTemplate(template);
//      //  configTemplate(pageTemplate);
//        if (PlatformUtil.isWindows()) {
//            filePath = filePath.replaceAll("/+|\\\\+", "\\\\");
//        } else {
//            filePath = filePath.replaceAll("/+|\\\\+", "/");
//        }
//        File file = new File(filePath);
//        File parentFile = file.getParentFile();
//        if (!parentFile.exists()) {
//            parentFile.mkdirs();
//        }
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(file);
//            pageTemplate.renderTo(fileOutputStream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fileOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
