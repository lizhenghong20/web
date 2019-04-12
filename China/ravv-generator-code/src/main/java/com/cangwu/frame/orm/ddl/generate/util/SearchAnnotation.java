package com.cangwu.frame.orm.ddl.generate.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.farwalker.waka.util.YMException;

/** 搜索类的注解 */
public class SearchAnnotation {
	/** 搜索类的注解 */
	@SuppressWarnings("rawtypes")
	public List<Class> searchClass(String[] packageName,
			Class<? extends Annotation> annotation) {
		List<Class> clazzs = new ArrayList<>();
		for (String pkname : packageName) {
			// getPackageController(pkname, DDLTable.class);
			List<Class> rs = getClassPath(pkname);
			clazzs.addAll(rs);
		}

		List<Class> tables = getAnnotationClasses(clazzs, annotation);
		return tables;
	}

	/** 取得包下面的所有类路径 */
	@SuppressWarnings("rawtypes")
	private List<Class> getClassPath(String packageName) {
		try {
			List<Class> files = new ArrayList<>();
			String packageDirName = packageName.replace('.', '/');

			// 获取当前目录下面的所有的子目录的url
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> dirs = loader.getResources(packageDirName);

			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				List<Class> rds = Collections.emptyList();

				// 如果当前类型是文件类型
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String path = URLDecoder.decode(url.getFile(), "UTF-8");
					if (path.charAt(0) == '/' || path.charAt(0) == '\\') {
						path = path.substring(1);
					}

					rds = getClassPathFile(packageName, path);
				} else if ("jar".equals(protocol)) {
					rds = getClassPathJar(url, packageDirName);
				}

				if (rds.size() > 0) {
					files.addAll(rds);
				}
			}

			return files;
		} catch (IOException e) {
			throw new YMException("", e);
		}
	}

	/**
	 * 在jar包里找包里的文件
	 * 
	 * @param jarURL
	 * @param packageDirName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Class> getClassPathJar(URL jarURL, String packageDirName) {
		final boolean recursive = true; // 是否循环迭代
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			List<Class> rs = new ArrayList<>();
			// 获取jar
			JarFile jar = ((JarURLConnection) jarURL.openConnection())
					.getJarFile();
			// 从此jar包 得到一个枚举类
			Enumeration<JarEntry> entries = jar.entries();
			// 同样的进行循环迭代
			while (entries.hasMoreElements()) {
				// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				// 如果是以/开头的
				if (name.charAt(0) == '/' || name.charAt(0) == '\\') {
					// 获取后面的字符串
					name = name.substring(1);
				}

				// 如果前半部分和定义的包名相同
				if (name.startsWith(packageDirName)) {
					String packageName;
					int idx = name.lastIndexOf('/');
					// 如果以"/"结尾 是一个包
					if (idx != -1) {
						// 获取包名 把"/"替换成"."
						packageName = name.substring(0, idx).replace('/', '.');
					} else {
						packageName = packageDirName.replace('/', '.');
					}

					// 如果可以迭代下去 并且是一个包
					// 如果是一个.class文件 而且不是目录
					if ((idx != -1 || recursive)
							&& (name.endsWith(".class") && !entry.isDirectory())) {
						// 去掉后面的".class" 获取真正的类名
						int pl = packageName.length() + 1, nl = name.length() - 6;
						String className = name.substring(pl, nl);

						// 添加到classes
						// rs.add(Class.forName(packageName + '.' + className));
						// classes.add(Class.forName(packageName + '.' +
						// className));
						// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
						Class clz = loader.loadClass(packageName + '.'
								+ className);
						rs.add(clz);
					}
				}
			}
			return rs;
		} catch (IOException | ClassNotFoundException e) {
			// log.error("在扫描用户定义视图时从jar包获取文件出错");
			throw new YMException("在扫描用户定义视图时从jar包获取文件出错", e);
		}
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 *
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	@SuppressWarnings("rawtypes")
	private List<Class> getClassPathFile(String packageName, String packagePath) {
		final boolean recursive = true; // 是否循环迭代
		File[] files;
		{
			// 获取此包的目录 建立一个File
			File dir = new File(packagePath);
			// 如果不存在或者 也不是目录就直接返回
			if (!dir.exists() || !dir.isDirectory()) {
				// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
				return Collections.emptyList();
			}

			// 如果存在 就获取包下的所有文件 包括目录
			files = dir.listFiles(new FileFilter() {
				// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
				@Override
				public boolean accept(File file) {
					return (recursive && file.isDirectory())
							|| (file.getName().endsWith(".class"));
				}
			});
		}
		try {
			List<Class> result = new ArrayList<>();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();

			// 循环所有文件
			for (File file : files) {
				String fileName = file.getName();
				if (file.isDirectory()) {// 如果是目录 则继续扫描
					List<Class> rs = getClassPathFile(packageName + "."
							+ fileName, file.getAbsolutePath());
					if (rs.size() > 0) {
						result.addAll(rs);
					}
				} else {
					// 如果是java类文件 去掉后面的.class 只留下类名
					int fl = fileName.length() - 6;
					String className = fileName.substring(0, fl);
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					Class clazz = loader.loadClass(packageName + '.'
							+ className);
					result.add(clazz);
				}
			}
			return result;
		} catch (ClassNotFoundException e) {
			// log.error("在扫描用户定义视图时从jar包获取文件出错");
			throw new YMException("以文件的形式来获取包下的所有Class出错", e);
		}
	}

	/**
	 * 取得包含注解类型的类
	 * 
	 * @param packagePath
	 *            包的文件路径
	 * @param annotation
	 *            注解类型
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Class> getAnnotationClasses(List<Class> clazzs,
			Class<? extends Annotation> annotation) {
		List<Class> rds = new ArrayList<>();
		for (Class c : clazzs) {
			// 判断该注解类型是不是所需要的类型
			if (null != c && null != c.getAnnotation(annotation)) {
				// 把这个文件加入classlist中
				rds.add(c);
			}
		}
		return rds;
	}
}