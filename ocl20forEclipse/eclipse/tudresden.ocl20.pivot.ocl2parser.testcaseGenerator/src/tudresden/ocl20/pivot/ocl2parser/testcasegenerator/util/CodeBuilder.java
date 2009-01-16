/*
    Copyright (C) 2008  Nils (s0006383@inf.tu-dresden.de)

    This file is part of the testcase generator for OCL parser of the Dresden OCL2 for Eclipse.

    The testcase generator is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The testcase generator is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with the testcase generator.  If not, see <http://www.gnu.org/licenses/>.
.
*/
package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.ecore.CodeGenEcorePlugin;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osgi.framework.Bundle;

import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.Activator;
import de.hunsicker.jalopy.Jalopy;

/**
 * The code builder generates the test plugin with its elements (MANIFEST.MF, etc.) and
 * also the test case files. 
 * @author Nils
 *
 */
public class CodeBuilder implements ICodeBuilder {
	private String projectname;
	private IProject project;
	
	/**
	 * This is the package name that is used for generating
	 * the code for testcase and test suites.
	 */
	private String packagename;
	
	/**
	 * While creating the code location we save us the source folder to
	 * add new files.
	 */
	private IFolder sourceContainer;
	
	/**
	 * This is the default folder with the name of the project.
	 */
	private IFolder defaultFolder;
	
	public CodeBuilder(String projectname) throws Exception {
		this.projectname = projectname;
		
		/*
		 * The packagename is set to the projectname. This can be altered
		 * by the test suites and the testcases, but we need a at least one
		 * packagename.
		 */
		this.packagename = projectname;
		try {
			Velocity.init();
		}catch(Exception ex) {
			throw new BuilderException("An error occurred while initializing the velocity engine.", ex);
		}
		
		createCodeLocation();
		createDefaultFolder();
		createComparator();
		createActivatorFile();
		createManifestMF();
		createBuildProperties();
		
		
	}

	/**
	 * The following code has been adopted from
	 * tudresden.ocl20.pivot.codegen.adapter$GenModelPivotAdapterGeneratorAdapter.ensureProjectExists
	 */
	public void createCodeLocation() {
		List<IClasspathEntry> classpathEntries = new UniqueEList<IClasspathEntry>();

		// Get a project with the projectname. Note: this project must not exist.
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectname);

		try {
			// A project description.
			IProjectDescription projectDescription = null;
			
			// We create a new java project.
			IJavaProject javaProject = JavaCore.create(project);

			// If the project already exists we delete it.
			if (project.exists()) {
				project.delete(true, null);
			}
			
			
			
			// Create a new project description with the projectname.
			projectDescription = ResourcesPlugin.getWorkspace().newProjectDescription(projectname);
			
			project.create(projectDescription, new NullProgressMonitor());
			
			// set the nature (Java + PDE) of the project if not already done
			String[] natureIds = projectDescription.getNatureIds();
			if (natureIds == null) {
				natureIds = new String[] { JavaCore.NATURE_ID };
			} else {
				// boolean value to indicate whether the project is java project
				boolean hasJavaNature = false;
				
				// boolean value to indicate whether the project is pde project
				boolean hasPDENature = false;
				
				/* 
				 * Iterate over all nature id and determine whether the project is already a java and pde project.
				 * If so, set the corresponding flag.
				 */
				for (int i = 0; i < natureIds.length; ++i) {
					if (JavaCore.NATURE_ID.equals(natureIds[i])) {
						hasJavaNature = true;
					}
					if ("org.eclipse.pde.PluginNature".equals(natureIds[i])) {
						hasPDENature = true;
					}
				}
				
				// If the project isn't a java project then we set the corresponding nature id.
				if (!hasJavaNature) {
					String[] oldNatureIds = natureIds;
					natureIds = new String[oldNatureIds.length + 1];
					System.arraycopy(oldNatureIds, 0, natureIds, 0, oldNatureIds.length);
					natureIds[oldNatureIds.length] = JavaCore.NATURE_ID;
				}
				
				// If the project isn't a pde project then we set the corresponding nature id.
				if (!hasPDENature) {
					String[] oldNatureIds = natureIds;
					natureIds = new String[oldNatureIds.length + 1];
					System.arraycopy(oldNatureIds, 0, natureIds, 0, oldNatureIds.length);
					natureIds[oldNatureIds.length] = "org.eclipse.pde.PluginNature";
				}
			}
			
			// Here we set the nature id's
			projectDescription.setNatureIds(natureIds);

			// set the builders (Manifest + Schema) of the project 
			ICommand[] builders = projectDescription.getBuildSpec();
			if (builders == null) {
				builders = new ICommand[0];
			}
			boolean hasManifestBuilder = false;
			boolean hasSchemaBuilder = false;
			for (int i = 0; i < builders.length; ++i) {
				if ("org.eclipse.pde.ManifestBuilder".equals(builders[i]
						.getBuilderName())) {
					hasManifestBuilder = true;
				}
				if ("org.eclipse.pde.SchemaBuilder"
						.equals(builders[i].getBuilderName())) {
					hasSchemaBuilder = true;
				}
			}
			if (!hasManifestBuilder) {
				ICommand[] oldBuilders = builders;
				builders = new ICommand[oldBuilders.length + 1];
				System.arraycopy(oldBuilders, 0, builders, 0, oldBuilders.length);
				builders[oldBuilders.length] = projectDescription.newCommand();
				builders[oldBuilders.length]
						.setBuilderName("org.eclipse.pde.ManifestBuilder");
			}
			if (!hasSchemaBuilder) {
				ICommand[] oldBuilders = builders;
				builders = new ICommand[oldBuilders.length + 1];
				System.arraycopy(oldBuilders, 0, builders, 0, oldBuilders.length);
				builders[oldBuilders.length] = projectDescription.newCommand();
				builders[oldBuilders.length]
						.setBuilderName("org.eclipse.pde.SchemaBuilder");
			}
			projectDescription.setBuildSpec(builders);

			project.open(new NullProgressMonitor());
			project.setDescription(projectDescription, new NullProgressMonitor());

			/*
			 * Here we add a src- and bin-directory to the new project.
			 */
			sourceContainer = project.getFolder("src");
			sourceContainer.create(false, true, new NullProgressMonitor());

			IClasspathEntry sourceClasspathEntry = JavaCore.newSourceEntry(new Path("/" + projectname + "/src"));
			classpathEntries.add(0, sourceClasspathEntry);
			
			

			// Here we add a java runtime environment.
			String jreContainer = JavaRuntime.JRE_CONTAINER;
			String complianceLevel = CodeGenUtil.EclipseUtil.getJavaComplianceLevel(project);
			if ("1.5".equals(complianceLevel)) {
				jreContainer += "/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5";
			} else if ("1.6".equals(complianceLevel)) {
				jreContainer += "/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.6";
			}
			classpathEntries.add(JavaCore.newContainerEntry(new Path(jreContainer)));

			classpathEntries.add(JavaCore.newContainerEntry(new Path("org.eclipse.pde.core.requiredPlugins")));
			
			javaProject.setOutputLocation(new Path("/" + projectname + "/bin"),	new NullProgressMonitor());
			javaProject.setRawClasspath(classpathEntries
				.toArray(new IClasspathEntry[classpathEntries.size()]), new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			CodeGenEcorePlugin.INSTANCE.log(e);
		}

	}
	
	/**
	 * Creates the file buildProperties.
	 * @throws Exception thrown if the build.properties file isn't found or an error occurs while creating the file in the new project
	 */
	public void createBuildProperties() throws Exception {
		// We take the build.properties file.
		File buildPropertiesFile = new File("./template/build.properties");
		
		// If the file doesn't exist it is an error.
		if (!(buildPropertiesFile.exists())) throw new FileNotFoundException("The template file build.properties was not found in the directory template.");
		
		// Open a stream to the content of the build.properties file.
		InputStream inputStream = new FileInputStream(buildPropertiesFile);
		
		// Get the build.properties file of the project (it must be exist).
		IFile projectBuildProperties = project.getFile("build.properties");
		
		// Create the build.properties file in the project.
		projectBuildProperties.create(inputStream, true, null);
	}

	public void generateTestcase(HashMap map) throws Exception {
		// Get the package name of the testcase.
		String localPackagename = (String) map.get("packagename");
		
		/*
		 * If the package name exists we take this and make it to the
		 * local package name from which the directory structure will be built.
		 */ 
		if ((localPackagename != null) && (!(localPackagename.equals("")))) {
			localPackagename = new String(packagename);
			
			// We set the package name in the java file.
			map.put("packagename", localPackagename);
			
		} else {// The testcase has no own packagename. We take the project name as default.
			localPackagename = new String(projectname);
			map.put("packagename", localPackagename);
		}
		
		/* 
		 * The packagename consists of points between the package parts
		 * but we need a path name. So we replace all points through the slash.
		 */
		String packagePathName = localPackagename.replace('.', '/');
		
		// Get the name of the testcase. That will be the name of the file and the class that we generate.
		String testcasename = (String) map.get("testname");
		
		
		/*
		 * First we must create the directory that map to the package structure.
		 * So we create a file instance that refers to the directory structure.
		 */
		File packageDirectory = new File(sourceContainer.getLocation().toFile().getCanonicalPath() + "/" + packagePathName);
		
		// Here we create the directory structure in the file system.
		packageDirectory.mkdirs();
		
		/*
		 * Now we can create the java file in the directory.
		 */
		File newJavaFile = new File(packageDirectory, testcasename + ".java");
		if (newJavaFile.exists()) {
			newJavaFile.delete();
		}
		
		newJavaFile.createNewFile();
		
		// We put a new key into the map that denotes the default package name.
		map.put("defaultpackage", projectname + ".internal");
		
		// We build a velocity context with our hashmap.
		VelocityContext ctx = new VelocityContext(map);
		
		// Get the template file.
		Template templ = Velocity.getTemplate("./template/NamedTestcase.java");
		
		/* 
		 * Create a writer that references the new java file.
		 * 
		 */
		Writer writer = new BufferedWriter(new FileWriter(newJavaFile));
		
		// Here the template will be processed.
		templ.merge(ctx, writer);
		
		// Flush the writer and close it.
		writer.flush();
		writer.close();
		
		//beautifyCode(newJavaFile);
		
		// Here we copy the test model file into the new eclipse plugin.
		String modelFile = (String) map.get("modelfile");
		createTestdata(modelFile);
		
		
	}

	public void generateTestsuite(HashMap map) throws Exception {
		String localProjectName = projectname.replace('.', '/');
		
		File testsuitePath = null;
		String packageName = (String) map.get("packagename");
		if (packageName.equals("")) {
			testsuitePath = new File(sourceContainer.getLocation().toString() + "/" + localProjectName);
			map.put("packagename", projectname);
		} else {
			String packagePath = packageName.replace('.', '/');
			testsuitePath = new File(sourceContainer.getLocation().toString() + "/" + packagePath);
		}
		
		if (!(testsuitePath.exists())) testsuitePath.mkdirs();
		
		String fileName = (String) map.get("testsuitename");
		File testsuiteFile = new File(testsuitePath.getAbsolutePath() + "/" + fileName + ".java");
		testsuiteFile.createNewFile();
		
		FileWriter fileWriter = new FileWriter(testsuiteFile);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		VelocityContext ctx = new VelocityContext(map);
		
		Template tmpl = Velocity.getTemplate("./template/TemplateSuite.java");
		tmpl.merge(ctx, bufferedWriter);
		
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	/**
	 * Initialize the builder with a valid projectname.
	 * If <i>projectname</i> is null an {@link IllegalArgumentException}
	 * will be thrown.
	 * 
	 * @param projectname the name of the project
	 */
	public void initialize(String projectname) {
		if (projectname == null) throw new IllegalArgumentException("The projectname must not be null");
		this.projectname = projectname; 
		

	}
	
	/**
	 * Create the activator file of the new plugin. An exception
	 * can occur if a problem occur with the file system or if
	 * the velocity engine has a problem For more details see the error
	 * message of the exception.
	 * @throws Exception is thrown while accessing the file system or through the velocity engine
	 */
	private void createActivatorFile() throws Exception {
			// We need the project name as the path name of the activator file.
			String localProjectname = projectname.replace('.', '/');
			
			// Create the path to the activator file.
			File activatorFilePath = new File(sourceContainer.getLocation().toFile().getCanonicalPath() + "/" + localProjectname);
			activatorFilePath.mkdirs();
			
			// Create the activator file.
			File activatorFile = new File(activatorFilePath.getAbsolutePath() + "/Activator.java");
			activatorFile.createNewFile();
			
			// Create two writers for writing the activator file.
			FileWriter activatorFileWriter = new FileWriter(activatorFile);
			BufferedWriter bufWriter = new BufferedWriter(activatorFileWriter, 2048);
			
			// We build a velocity context with the projectpackage name.
			VelocityContext ctx = new VelocityContext();
			ctx.put("projectpackage", projectname);
			
			// Get the template file and merge it with the values of the context.
			Template templ = Velocity.getTemplate("./template/Activator.java");
			templ.merge(ctx, bufWriter);
			
			// Flush and close the writer.
			bufWriter.flush();
			bufWriter.close();
	}
	
	/**
	 * Creates the manifest file of the new project. An exception can be thrown
	 * if an access to the file system isn't possible  or the velocity engine
	 * report an error. For more information see the
	 * exception message.
	 * @throws Exception can be thrown if a file system error occurs or the velocity report an error
	 */
	private void createManifestMF() throws Exception {
		// First we create the manifest directory in the project folder.
		IFolder metaInfFolder = project.getFolder("META-INF");
		metaInfFolder.create(true, true, null);
		
		// We create the manifest.mf file.
		IFile projectManifestFile = metaInfFolder.getFile("MANIFEST.MF");
		projectManifestFile.create(null, true, null);
		
		// Next make the manifest file available for writing content.
		File manifestFile = projectManifestFile.getLocation().toFile(); 
				
		// Two writers for writing the filled template out.
		FileWriter manifestWriter = new FileWriter(manifestFile);
		BufferedWriter bufWriter = new BufferedWriter(manifestWriter);
		
		// Create a velocity context and set the variable in the template.
		VelocityContext ctx = new VelocityContext();
		ctx.put("projectname", projectname);
		ctx.put("activatorpackage", projectname);
		ctx.put("bundlename", projectname);
		
		// Get the template and merge them with the variables of the context.
		Template templ = Velocity.getTemplate("./template/MANIFEST.MF");
		templ.merge(ctx, bufWriter);
		
		// Flush and close the writer.
		bufWriter.flush();
		bufWriter.close();
	}
	
	/**
	 * Creates the classes for the compare package.
	 * @throws Exception is thrown if an error occurs while creating the files and folders
	 */
	private void createComparator() throws Exception {
		IFolder internalFolder = defaultFolder.getFolder("internal");
		internalFolder.create(true, true, null);
		
		IFolder destCompareDirectory = internalFolder.getFolder("compare");
		destCompareDirectory.create(true, true, null);
		
		IFolder srcStringTreeFolder = destCompareDirectory.getFolder("stringTree");
		srcStringTreeFolder.create(true, true, null);
		
		VelocityContext ctx = new VelocityContext();
		ctx.put("packagename", projectname + ".internal");
		
		File testFile = new File(".");
		System.out.println(testFile.getAbsolutePath());
		
		Activator activator = Activator.getDefault();
				
		Bundle bundle = Activator.getDefault().getBundle();
				
		URL fileLocatorURL = FileLocator.find(Activator.getDefault().getBundle(), new Path("template/compare"), null);
		URL fileLocatorFileURL = FileLocator.toFileURL(fileLocatorURL);
		
		File srcCompareDirectory = new File(fileLocatorFileURL.toURI());
		
		Velocity.setProperty("File.resource.loader.path", srcCompareDirectory.getAbsoluteFile());
		
		//File compareDirectory = new File("./template/compare");
		//File compareDirectory = new File(url.getFile());
		//url.openStream();
		
		File[] contentCompareDirectory = srcCompareDirectory.listFiles();
		
		for(File sourceFile : contentCompareDirectory) {
			if (sourceFile.isDirectory()) continue;
			
			IFile destinationFile = destCompareDirectory.getFile(sourceFile.getName());
			destinationFile.create(null, true, null);
			
			Writer destWriter = new FileWriter(destinationFile.getLocation().toFile());
			Writer bufWriter = new BufferedWriter(destWriter);
			
			Template templ = Velocity.getTemplate("./" + sourceFile.getName());
			templ.merge(ctx, bufWriter);
			
			bufWriter.flush();
			bufWriter.close();
		}
		
		URL stringTreeDirectoryURL = FileLocator.toFileURL(FileLocator.find(Activator.getDefault().getBundle(), new Path("template/compare/stringTree"), null));
		File srcStringTreeDirectory = new File(stringTreeDirectoryURL.toURI());
		
		//File stringTreeDirectory = new File("template/compare/stringTree");
		File[] contentStringTreeDirectory = srcStringTreeDirectory.listFiles();
		
		for(File sourceFile : contentStringTreeDirectory) {
			if (sourceFile.isDirectory()) continue;
			
			IFile destinationFile = srcStringTreeFolder.getFile(sourceFile.getName());
			destinationFile.create(null, true, null);
			
			Writer destWriter = new FileWriter(destinationFile.getLocation().toFile());
			Writer bufWriter = new BufferedWriter(destWriter);
			
			Template templ = Velocity.getTemplate("./stringTree/"  + sourceFile.getName());
			templ.merge(ctx, bufWriter);
			
			bufWriter.flush();
			bufWriter.close();
		}
		
	}
	
	/**
	 * Creates the default package (folder) for the project. It is named
	 * by the projectname.
	 * @throws Exception is thrown if an error occurs while creating the new folder
	 */
	private void createDefaultFolder() throws Exception {
		IPath defaultFolderPath = new Path(projectname.replace(".", "/"));
		
		IFolder tempFolder = sourceContainer;
		for(int i = 0; i < defaultFolderPath.segmentCount(); i++) {
			String segment = defaultFolderPath.segment(i);
			
			tempFolder = tempFolder.getFolder(segment);
			tempFolder.create(true, true, null);
			
		}
		defaultFolder = tempFolder;
	}
	
	private void beautifyCode(File toBeautify) throws Exception {
		Jalopy beautifier = new Jalopy();
		
		beautifier.setInput(toBeautify);
		beautifier.setOutput(toBeautify);
		
		beautifier.format();
		
		System.out.println(beautifier.getState());
	}
	
	private void createTestdata(String testFilePath) throws Exception {
		File srcTestFile = new File(testFilePath);
		// The path is relative.
		if (testFilePath.charAt(0) == '.') {
			
			File parentFile = srcTestFile.getParentFile();
			List<String> srcPath = new ArrayList<String>();
			while(parentFile != null) {
				if (!parentFile.getName().equals(".")) {
					srcPath.add(parentFile.getName());
				}
				parentFile = parentFile.getParentFile();
			}
			
			IFolder destElement = null;
			if (srcPath.size() == 0) {
				destElement = project.getFolder(".");
			} else {
				destElement = project.getFolder(srcPath.get(srcPath.size()-1));
				if (!destElement.exists()) destElement.create(true, true, null);
				for(int i = srcPath.size()-1; i >= 1; i--) {
					destElement = destElement.getFolder(srcPath.get(i-1));
					if (!destElement.exists()) destElement.create(true, true, null);
					
				}
			}
			
			
			
			InputStream srcStream = new FileInputStream(srcTestFile);
			
			IFile testFile = destElement.getFile(srcTestFile.getName());
			if (!testFile.exists()) testFile.create(srcStream, true, null);
		} else { // The path doesn't begin with a point, so it can be an absolute path.
			// The path can consists only of a single file name, in that case the path name has no "/".
			if (testFilePath.indexOf("/") == -1) {
				// So we take the root path of the project.
				IFolder destFolder = project.getFolder(".");
				
				InputStream srcStream = new FileInputStream(srcTestFile);
				
				IFile testFile = destFolder.getFile(srcTestFile.getName());
				if (!testFile.exists()) testFile.create(srcStream, true, null);
			}
		}
			
	}

}