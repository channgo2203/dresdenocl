/*
 * Copyright (C) 2008-2010 by Claas Wilke (claas.wilke@tu-dresden.de)
 *
 * This file is part of the OCL2Java Code Generator of Dresden OCL.
 *
 * Dresden OCL is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Dresden OCL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License 
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along 
 * with Dresden OCL. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dresdenocl.tools.codegen.ocl2java.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dresdenocl.pivotmodel.ConstrainableElement;
import org.dresdenocl.pivotmodel.Constraint;
import org.dresdenocl.pivotmodel.Feature;
import org.dresdenocl.tools.codegen.code.ITransformedCode;
import org.dresdenocl.tools.codegen.code.impl.TransformedCodeImpl;
import org.dresdenocl.tools.codegen.ocl2java.IOcl2JavaSettings;

/**
 * <p>
 * This class implements the interface {@link IOcl22CodeSettings}.
 * </p>
 * 
 * @author Claas Wilke
 */
public class Ocl2JavaSettings implements IOcl2JavaSettings {

	/**
	 * Pattern used for the body of a violated constraint within a violation
	 * macro.
	 */
	public static final String CONSTRAINT_BODY_PATTERN = "<CONSTRAINT_BODY>";

	/**
	 * Pattern used for the name of a violated constraint within a violation
	 * macro.
	 */
	public static final String CONSTRAINT_NAME_PATTERN = "<CONSTRAINT_NAME>";

	/**
	 * Pattern used for the String representation of the object violating a
	 * constraint within a violation macro.
	 */
	public static final String OBJECT_IN_ILLEGAL_STATE_PATTERN = "<OBJECT_IN_ILLEGAL_STATE>";

	/** The basis package. */
	protected String basisPackage = "";

	/**
	 * Specifies whether or not getter methods shall be generated for new
	 * defined attributes.
	 */
	protected boolean createGettersForDefinedAttributes;

	/** The sub folder into which the aspect files shall be generated. */
	protected String constraintFolder;

	/**
	 * Contains the default invariant check mode for all invariants without a
	 * special setting.
	 */
	protected int defaultInvariantCheckMode;

	/**
	 * Specifies whether or not inheritance is disabled by default for all
	 * {@link Constraint}s.
	 */
	protected boolean defaultIsInheritanceDisabled;

	/**
	 * Contains the default violation macro for all {@link Constraint}s without
	 * a special setting.
	 */
	protected ITransformedCode defaultViolationMacro;

	/** Contains all {@link Constraint}s which a special inheritance settings. */
	protected Set<Constraint> disabledInheritance;

	/**
	 * Declares whether or not getters should be used for
	 * PropertyCallExpressions (required when generating code for Ecore models).
	 */
	protected boolean gettersForPropertyCallsEnabled = false;

	/**
	 * Contains the invariant check mode of all invariants with a special
	 * setting.
	 */
	protected Map<Constraint, Integer> invariantCheckMode;

	/**
	 * Specifies whether or not the transformed Code shall be saved in files.
	 */
	protected int saveCode;

	/** The location where the transformed Code shall be saved. */
	protected String sourceDirectory;

	/**
	 * Contains the violation macro for all {@link Constraint}s with a special
	 * setting.
	 */
	protected Map<Constraint, ITransformedCode> violationMacros;

	/**
	 * <p>
	 * Creates a new {@link Ocl2JavaSettings}.
	 * </p>
	 */
	public Ocl2JavaSettings() {

		this.sourceDirectory = "";

		this.constraintFolder = "constraints";

		this.saveCode = 1;

		this.createGettersForDefinedAttributes = false;

		this.defaultIsInheritanceDisabled = true;

		this.disabledInheritance = new HashSet<Constraint>();

		this.defaultInvariantCheckMode = IOcl2JavaSettings.INVARIANT_CHECK_AFTER_CONSTRUCT_AND_ATTRIBUTE_CHANGE;

		this.invariantCheckMode = new HashMap<Constraint, Integer>();

		this.defaultViolationMacro = new TransformedCodeImpl();
		this.defaultViolationMacro
				.addCode("// TODO Auto-generated code executed when constraint is violated.");
		this.defaultViolationMacro.addCode("String msg = \"Error: Constraint '"
				+ CONSTRAINT_NAME_PATTERN + "' (" + CONSTRAINT_BODY_PATTERN
				+ ") was violated for Object "
				+ OBJECT_IN_ILLEGAL_STATE_PATTERN + ".\";");
		this.defaultViolationMacro.addCode("throw new RuntimeException(msg);");

		this.violationMacros = new HashMap<Constraint, ITransformedCode>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.tools.codegen.ocl2java.IOcl2JavaSettings#
	 * getBasisPackage()
	 */
	public String getBasisPackage() {
		return this.basisPackage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#getConstraintDirectory()
	 */
	public String getConstraintDirectory() {

		return this.constraintFolder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#getInvariantCheckMode
	 * (org.dresdenocl.pivotmodel.Constraint)
	 */
	public int getInvariantCheckMode(Constraint aConstraint) {

		int result;

		if (this.invariantCheckMode.containsKey(aConstraint)) {
			result = this.invariantCheckMode.get(aConstraint);
		}

		else {
			result = this.defaultInvariantCheckMode;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#getSourceDirectory()
	 */
	public String getSourceDirectory() {

		return this.sourceDirectory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.tools.codegen.ocl2java.IOcl2JavaSettings#
	 * getViolationMacro(org.dresdenocl.pivotmodel.Constraint)
	 */
	public String getViolationMacro(Constraint constraint) {

		ITransformedCode result;

		if (constraint != null && this.violationMacros.containsKey(constraint)) {
			result = this.violationMacros.get(constraint);
		}

		else {
			result = this.defaultViolationMacro;
		}

		/* Replace specific templates from the violation macro. */
		String stringResult = result.getCode();

		if (constraint != null) {
			if (stringResult.contains(CONSTRAINT_NAME_PATTERN)) {
				String constraintName = constraint.getName();
				if (constraintName == null || constraintName.length() == 0)
					constraintName = "undefined";
				// no else.

				stringResult = stringResult.replaceAll(CONSTRAINT_NAME_PATTERN,
						constraintName);
			}
			// no else.

			if (stringResult.contains(CONSTRAINT_BODY_PATTERN)) {
				stringResult = stringResult.replaceAll(CONSTRAINT_BODY_PATTERN,
						constraint.getSpecification().getBody().trim()
								.replaceAll("\r\n", " ").replaceAll("\r", " ")
								.replaceAll("\n", " "));
			}
			// no else.

			if (stringResult.contains(OBJECT_IN_ILLEGAL_STATE_PATTERN)) {
				ConstrainableElement constrainedElement = constraint
						.getConstrainedElement().iterator().next();

				/* Static fields must be handled special here. */
				if (constrainedElement instanceof Feature
						&& ((Feature) constrainedElement).isStatic()) {
					stringResult = stringResult.replaceAll(
							OBJECT_IN_ILLEGAL_STATE_PATTERN,
							"static field or operation");
				}

				else {
					/*
					 * TODO Claas: use the variable from the specific template
					 * here instead.
					 */
					stringResult = stringResult.replaceAll(
							OBJECT_IN_ILLEGAL_STATE_PATTERN,
							"\" + aClass.toString() + \"");
				}
			}
			// no else.
		}
		// no else.

		return stringResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.dresdenocl.ocl2java.IOcl2CodeSettings#
	 * isGettersForDefinedAttributesEnabled()
	 */
	public boolean isGettersForDefinedAttributesEnabled() {

		return this.createGettersForDefinedAttributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.tools.codegen.ocl2java.IOcl2JavaSettings#
	 * isGettersForPropertyCallsEnabled()
	 */
	public boolean isGettersForPropertyCallsEnabled() {
		return this.gettersForPropertyCallsEnabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#isInheritanceDisabled
	 * (org.dresdenocl.pivotmodel.Constraint)
	 */
	public boolean isInheritanceDisabled(Constraint aConstraint) {

		boolean result;

		if (aConstraint != null
				&& this.disabledInheritance.contains(aConstraint)) {
			result = true;
		}

		else {
			result = this.defaultIsInheritanceDisabled;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.ocl2java.IOcl2CodeSettings#isSaveCode()
	 */
	public int getSaveCode() {

		return this.saveCode;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.tools.codegen.ocl2java.IOcl2JavaSettings#isSaveCode()
	 */
	public boolean isSaveCode() {

		return getSaveCode() > 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.tools.codegen.ocl2java.IOcl2JavaSettings#
	 * setBasisPackahe(java.lang.String)
	 */
	public void setBasisPackage(String path) {
		this.basisPackage = path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#setConstraintDirectory
	 * (java.lang.String)
	 */
	public void setConstraintDirectory(String folderName) {

		this.constraintFolder = folderName;
	}

	public void setDefaultInheritanceDisabled(boolean disable) {

		this.defaultIsInheritanceDisabled = disable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#setDefaultInvariantCheckMode
	 * (int)
	 */
	public void setDefaultInvariantCheckMode(int mode) {

		this.defaultInvariantCheckMode = mode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#setDefaultViolationMacro
	 * (org.dresdenocl.ocl2java.code.ITransformedCode)
	 */
	public void setDefaultViolationMacro(ITransformedCode macro) {

		this.defaultViolationMacro = macro;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.dresdenocl.ocl2java.IOcl2CodeSettings#
	 * setGettersForDefinedAttributesEnabled(boolean)
	 */
	public void setGettersForDefinedAttributesEnabled(boolean enable) {

		this.createGettersForDefinedAttributes = enable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.tools.codegen.ocl2java.IOcl2JavaSettings#
	 * setGettersForPropertyCallsEnabled(boolean)
	 */
	public void setGettersForPropertyCallsEnabled(boolean enable) {
		this.gettersForPropertyCallsEnabled = enable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#disableInheritance(tudresden
	 * .ocl20.pivot.pivotmodel.Constraint)
	 */
	public void setInheritanceDisabled(Constraint aConstraint, boolean disable) {

		if (disable) {
			this.disabledInheritance.add(aConstraint);
		}

		else if (this.disabledInheritance.contains(aConstraint)) {
			this.disabledInheritance.remove(aConstraint);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#setInvariantCheckMode
	 * (org.dresdenocl.pivotmodel.Constraint, int)
	 */
	public void setInvariantCheckMode(Constraint aConstraint, int mode) {

		this.invariantCheckMode.put(aConstraint, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#setSaveCode(int)
	 */
	public void setSaveCode(int saveCode) {

		this.saveCode = saveCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#setSourceDirectory(java
	 * .lang.String)
	 */
	public void setSourceDirectory(String path) {

		this.sourceDirectory = path;

		if (!this.sourceDirectory.endsWith("/")) {
			this.sourceDirectory += "/";
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.ocl2java.IOcl2CodeSettings#setViolationMacro(tudresden
	 * .ocl20.pivot.ocl2java.code.ITransformedCode,
	 * org.dresdenocl.pivotmodel.Constraint)
	 */
	public void setViolationMacro(ITransformedCode macro, Constraint constraint) {

		this.violationMacros.put(constraint, macro);
	}
}