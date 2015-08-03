/**
 * 
 */
package org.dresdenocl.modelbus.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.dresdenocl.model.metamodel.IMetamodel;
import org.dresdenocl.modelbus.ModelBusPlugin;
import org.dresdenocl.modelinstance.IModelInstanceType;

/**
 * @author Lars Schuetze
 * 
 */
public final class ModelLoaderUtility {

	/**
	 * Prevent this class from being instantiated.
	 */
	private ModelLoaderUtility() {

		throw new AssertionError("This class must not be instantiated.");
	}

	/**
	 * <p>
	 * Helper method which substitutes the "/src/" with "/target/classes/" and set the file
	 * ending to ".class"
	 * </p>
	 * 
	 * @param A
	 *            {@link String} which represents the path to the .java file
	 * @return A {@link String} which represents the possible path to the
	 *         corresponding .class file of a .java file
	 */
	public static String getCorrespondingClassFileName(String pathToJavaFile) {

		if (pathToJavaFile == null)
			return null;

		// Check which type of slashes are used and replace src with bin
		String pathToClassFile = null;
		if (pathToJavaFile.contains("\\src\\")) {
			pathToClassFile = pathToJavaFile.replace("\\src\\", "\\target\\classes\\");
		} else if (pathToJavaFile.contains("/src/")) {
			pathToClassFile = pathToJavaFile.replace("/src/", "/target/classes/");
		}

		// now check the file ending
		if (pathToClassFile.toLowerCase().endsWith(".java")) {
			pathToClassFile = pathToClassFile.substring(0,
					pathToClassFile.length() - 4)
					+ "class";
		} else {
			// the file does not end with .java
			return null;
		}

		return pathToClassFile;
	}

	/**
	 * <p>
	 * Checks if the file extension is known so we can derive the metamodel to
	 * load.
	 * </p>
	 * 
	 * @param fileExtension
	 * @return the metamodel to load or null
	 */
	public static IMetamodel getMetamodelByExtension(String fileExtension) {

		IMetamodel[] metaModels;

		boolean isApplicable = false;
		metaModels = ModelBusPlugin.getMetamodelRegistry().getMetamodels();

		if (metaModels.length > 0) {

			IMetamodel mmType = null;

			if (fileExtension != null) {
				/* Check which meta model can be applied */
				if (fileExtension.equalsIgnoreCase("class")
						|| fileExtension.equalsIgnoreCase("javamodel")
						|| fileExtension.equalsIgnoreCase("java")) {
					mmType = ModelBusPlugin.getMetamodelRegistry()
							.getMetamodel("org.dresdenocl.metamodels.java");

					isApplicable = true;
				} else if (fileExtension.equalsIgnoreCase("uml")) {
					mmType = ModelBusPlugin.getMetamodelRegistry()
							.getMetamodel("org.dresdenocl.metamodels.uml2");

					isApplicable = true;
				} else if (fileExtension.equalsIgnoreCase("ecore")) {
					mmType = ModelBusPlugin.getMetamodelRegistry()
							.getMetamodel("org.dresdenocl.metamodels.ecore");

					isApplicable = true;
				} else if (fileExtension.equalsIgnoreCase("xsd")) {
					mmType = ModelBusPlugin.getMetamodelRegistry()
							.getMetamodel("org.dresdenocl.metamodels.xsd");

					isApplicable = true;
				}
			}

			/* Search for the meta model and select it */
			if (isApplicable && (mmType != null)) {
				List<IMetamodel> mmList = Arrays.asList(metaModels);
				return mmList.get(mmList.indexOf(mmType));
			}
			// no else.
		}
		// no else.
		return null;
	}

	public static IModelInstanceType getModelinstanceTypeByExtension(
			String fileExtension) {

		IModelInstanceType[] miTypes;
		boolean isApplicable = false;

		miTypes = ModelBusPlugin.getModelInstanceTypeRegistry()
				.getModelInstanceTypes();

		if (miTypes.length > 0) {

			IModelInstanceType miType = null;

			if (fileExtension != null) {
				/* Check whether the model instance file is a .class file */
				if (fileExtension.equalsIgnoreCase("class")
						|| fileExtension.equalsIgnoreCase("java")) {
					miType = ModelBusPlugin.getModelInstanceTypeRegistry()
							.getModelInstanceType(
									"org.dresdenocl.modelinstancetype.java");

					isApplicable = true;

				} else if (fileExtension.equalsIgnoreCase("pml")) {
					miType = ModelBusPlugin.getModelInstanceTypeRegistry()
							.getModelInstanceType(
									"org.dresdenocl.modelinstancetype.ecore");

					isApplicable = true;

				} else if (fileExtension.equalsIgnoreCase("xml")) {
					miType = ModelBusPlugin.getModelInstanceTypeRegistry()
							.getModelInstanceType(
									"org.dresdenocl.modelinstancetype.xml");

					isApplicable = true;
				}
			}
			// no else

			if (isApplicable && (miType != null)) {
				/* Search for the model instance type and select it */
				List<IModelInstanceType> miList = Arrays.asList(miTypes);
				return miList.get(miList.indexOf(miType));
			}
		}
		return null;
	}
}
