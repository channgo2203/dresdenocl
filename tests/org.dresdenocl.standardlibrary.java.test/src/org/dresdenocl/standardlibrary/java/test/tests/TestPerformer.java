/*
Copyright (C) 2009 by Claas Wilke (info@claaswilke.de)

This file is part of the OCL Interpreter Test Suite of Dresden OCL2 for
Eclipse.

Dresden OCL2 for Eclipse is free software: you can redistribute it and/or modify 
it under the terms of the GNU Lesser General Public License as published by the 
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

Dresden OCL2 for Eclipse is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License 
for more details.

You should have received a copy of the GNU Lesser General Public License along 
with Dresden OCL2 for Eclipse. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dresdenocl.standardlibrary.java.test.tests;

import java.io.File;
import java.io.IOException;

import org.dresdenocl.testsuite._abstract.AbstractDresdenOclTest;

import org.dresdenocl.essentialocl.EssentialOclPlugin;
import org.dresdenocl.essentialocl.standardlibrary.factory.IStandardLibraryFactory;
import org.dresdenocl.essentialocl.types.OclLibrary;
import org.dresdenocl.examples.royalandloyal.Customer;
import org.dresdenocl.examples.royalandloyal.CustomerCard;
import org.dresdenocl.examples.royalandloyal.Date;
import org.dresdenocl.examples.royalandloyal.LoyaltyAccount;
import org.dresdenocl.examples.royalandloyal.LoyaltyProgram;
import org.dresdenocl.examples.royalandloyal.ProgramPartner;
import org.dresdenocl.metamodels.uml2.UML2MetamodelPlugin;
import org.dresdenocl.model.IModel;
import org.dresdenocl.model.IModelRegistry;
import org.dresdenocl.model.ModelAccessException;
import org.dresdenocl.model.ModelConstants;
import org.dresdenocl.model.metamodel.IMetamodel;
import org.dresdenocl.modelbus.ModelBusPlugin;
import org.dresdenocl.modelinstance.IModelInstance;
import org.dresdenocl.modelinstance.IModelInstanceProvider;
import org.dresdenocl.modelinstance.IModelInstanceRegistry;
import org.dresdenocl.modelinstancetype.exception.OperationNotFoundException;
import org.dresdenocl.modelinstancetype.exception.TypeNotFoundInModelException;
import org.dresdenocl.modelinstancetype.java.internal.provider.JavaModelInstanceProvider;
import org.dresdenocl.modelinstancetype.types.IModelInstanceElement;
import org.dresdenocl.pivotmodel.Operation;
import org.dresdenocl.standardlibrary.java.JavaStandardlibraryPlugin;

/**
 * <p>
 * This class loads a given model file, a given model instance and given OCL
 * files. Then it can be used to perform some interpreter tests.
 * </p>
 * 
 * @author Claas Wilke
 * @author Michael Thiele
 */
public class TestPerformer {

	/**
	 * The qualified Name of the package that contains the {@link Class}es used
	 * for testing.
	 */

	public static String QUALIFIED_NAME_MODEL_PACKAGE = ModelConstants.ROOT_PACKAGE_NAME
			+ "::org::dresdenocl::examples::royalandloyal";

	/** The qualified Name of {@link Customer}. */
	public static String QUALIFIED_NAME_CUSTOMER = QUALIFIED_NAME_MODEL_PACKAGE
			+ "::Customer";

	/** The qualified Name of {@link CustomerCard}. */
	public static String QUALIFIED_NAME_CUSTOMER_CARD = QUALIFIED_NAME_MODEL_PACKAGE
			+ "::CustomerCard";

	/** The qualified Name of {@link Date}. */
	public static String QUALIFIED_NAME_DATE = QUALIFIED_NAME_MODEL_PACKAGE
			+ "::Date";

	/** The qualified Name of {@link LoyaltyAccount}. */
	public static String QUALIFIED_NAME_LOYALTY_ACCOUNT = QUALIFIED_NAME_MODEL_PACKAGE
			+ "::LoyaltyAccount";

	/** The qualified Name of {@link LoyaltyProgram}. */
	public static String QUALIFIED_NAME_LOYALTY_PROGRAM = QUALIFIED_NAME_MODEL_PACKAGE
			+ "::LoyaltyProgram";

	/** The qualified Name of {@link LoyaltyProgram}. */
	public static String QUALIFIED_NAME_MEMBERSHIP = QUALIFIED_NAME_MODEL_PACKAGE
			+ "::Membership";

	/** The qualified Name of {@link ProgramPartner}. */
	public static String QUALIFIED_NAME_PROGRAM_PARTNER = QUALIFIED_NAME_MODEL_PACKAGE
			+ "::ProgramPartner";

	/** The package of the UML2 meta model. */
	private final static String META_MODEL = UML2MetamodelPlugin.ID;

	/** The name of the bundle of the model file. */
	private final static String MODEL_BUNDLE = "org.dresdenocl.examples.royalandloyal";

	/** The path of the UML model file. */
	private final static String MODEL_FILE = "model/royalandloyal.uml";

	/** The standard library factory to create OclAny and sub-types. */
	protected IStandardLibraryFactory myStandardLibraryFactory = JavaStandardlibraryPlugin
			.getStandardLibraryFactory();

	/** Contains the UML2 meta model. */
	protected IMetamodel myMetaModel = null;

	/** Contains the loaded UML2 model. */
	protected IModel myModel = null;

	/** Contains the loaded UML2 model instance. */
	protected IModelInstance myModelInstance = null;

	private static TestPerformer instance = null;

	/**
	 * <p>
	 * Creates a new TestPerformer.
	 * </p>
	 */
	private TestPerformer() {

		super();
		this.init();

	}

	public static TestPerformer getInstance() {

		if (instance == null)
			instance = new TestPerformer();
		return instance;
	}

	/**
	 * Returns the {@link IStandardLibraryFactory} used for creating Standard
	 * Library types.
	 * 
	 * @return the {@link IStandardLibraryFactory} used for creating Standard
	 *         Library types
	 */
	public IStandardLibraryFactory getSLFactory() {

		return myStandardLibraryFactory;
	}

	/**
	 * Returns the {@link OclLibrary}.
	 * 
	 * @return the {@link OclLibrary}
	 */
	public OclLibrary getOclLibrary() {

		return EssentialOclPlugin.getOclLibraryProvider().getOclLibrary();
	}

	/**
	 * <p>
	 * Adapts a given {@link Object} as {@link IModelInstanceElement} and adds
	 * it to the {@link IModelInstance} under test.
	 * </p>
	 * 
	 * @param object
	 *            The {@link Object} that shall be adapted and added.
	 * @return The adapted {@link IModelInstanceElement}.
	 */
	public IModelInstanceElement addModelObject(Object object) {

		IModelInstanceElement result;

		try {
			result = myModelInstance.addModelInstanceElement(object);
		}

		// FIXME: Shouldn't there be a better way of handling this exception?
		catch (TypeNotFoundInModelException e) {
			result = null;
		}

		return result;
	}

	/**
	 * <p>
	 * Initializes the {@link TestPerformer} after all required parameters are
	 * set.
	 * </p>
	 * 
	 * @throws RuntimeException
	 *             Is thrown if any error occurred while loading the model or
	 *             the meta model.
	 */
	private void init() throws RuntimeException {

		/* Try to load model and meta model. */
		try {

			this.myMetaModel = ModelBusPlugin.getMetamodelRegistry()
					.getMetamodel(META_MODEL);

			if (myMetaModel == null) {
				throw new RuntimeException(
						"Unable to load meta model during test.");
			}
			// no else.

			/* Load the model. */
			this.loadModel();

			/* Load the model instance. */
			this.loadModelInstance();

		}

		catch (Exception e) {
			throw new RuntimeException(
					"Unable to initialize the test. Reason: " + e.getMessage());
		}
	}

	// TODO JavaDoc; regard parameter types as well?
	public Operation findOperation(IModelInstanceElement imiElement, String name)
			throws OperationNotFoundException {

		for (Operation ownedOperation : imiElement.getType()
				.getOwnedOperation()) {

			if (ownedOperation.getName().equals(name)) {
				return ownedOperation;
			}
		}

		throw new OperationNotFoundException("Cannot find operation " + name
				+ " on " + imiElement);
	}

	/**
	 * <p>
	 * Resets the environment ({@link IModel} and {@link IModelInstance} used by
	 * this {@link TestPerformer} and reinitializes it. Used to remove new
	 * defined or initialized {@link IModelInstanceElement}s from the
	 * {@link IModel}.
	 * </p>
	 * 
	 * @throws ModelAccessException
	 */
	public void reset() throws ModelAccessException {

		/* Remove all loaded models. */
		ModelBusPlugin.getModelRegistry().dispose();

		this.myModel = null;
		this.myModelInstance = null;

		this.init();

	}

	/**
	 * <p>
	 * Loads the {@link IModel} used for testing.
	 * </p>
	 * 
	 * @throws Ocl22JavaException
	 *             Is thrown if the {@link IModel} cannot be initialized or the
	 *             model file is not found.
	 */
	protected void loadModel() throws RuntimeException {

		/* Check if the model has not already been loaded yet. */
		if (!(this.myModel != null && this.myModel.getDisplayName().equals(
				MODEL_FILE))) {

			/* Try to load the model. */
			try {
				File modelFile = AbstractDresdenOclTest.getFile(MODEL_FILE,
						MODEL_BUNDLE);

				IModelRegistry modelRegistry;

				this.myModel = this.myMetaModel.getModelProvider().getModel(
						modelFile);

				modelRegistry = ModelBusPlugin.getModelRegistry();

				modelRegistry.addModel(myModel);
				modelRegistry.setActiveModel(myModel);
			}

			catch (ModelAccessException e) {
				throw new RuntimeException("The model could not be loaded. "
						+ e.getMessage());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		// no else.
	}

	/**
	 * <p>
	 * Loads a new empty {@link IModelInstance} of the actual selected
	 * {@link IModel}.
	 * </p>
	 * 
	 * @throws RuntimeException
	 *             Thrown, if an error during {@link IModelInstance}
	 *             initialization occurs.
	 */
	protected void loadModelInstance() throws RuntimeException {

		/* Check if a model has been set yet. */
		if (this.myModel != null) {

			this.myModelInstance = null;

			/* Load the model instance. */
			IModelInstanceProvider modelInstanceProvider = new JavaModelInstanceProvider();
			this.myModelInstance = modelInstanceProvider
					.createEmptyModelInstance(this.myModel);

			/*
			 * Add the successfully loaded model instance to the model instance
			 * registry.
			 */
			IModelInstanceRegistry modelInstanceRegistry;
			modelInstanceRegistry = ModelBusPlugin.getModelInstanceRegistry();

			modelInstanceRegistry.addModelInstance(this.myModelInstance);
			modelInstanceRegistry.setActiveModelInstance(myModel,
					this.myModelInstance);
		}

		else {
			throw new RuntimeException(
					"No model found to load a model instance.");
		}
	}

}