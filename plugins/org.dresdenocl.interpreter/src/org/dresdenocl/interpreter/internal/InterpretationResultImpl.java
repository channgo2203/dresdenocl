/*
Copyright (C) 2009 by Claas Wilke (info@claaswilke.de)

This file is part of the Treaty OCL Vocabulary which adapts Dresden OCL2 for
Eclipse to Treaty.

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
package org.dresdenocl.interpreter.internal;

import org.dresdenocl.essentialocl.standardlibrary.OclAny;
import org.dresdenocl.interpreter.IInterpretationResult;
import org.dresdenocl.modelinstancetype.types.IModelInstanceElement;
import org.dresdenocl.pivotmodel.Constraint;

/**
 * <p>
 * Represents interpretation results of Ocl {@link Constraint} associated to an
 * {@link IModelInstanceElement}.
 * </p>
 * 
 * @author Claas Wilke
 * 
 */
public class InterpretationResultImpl implements IInterpretationResult {

	/**
	 * The {@link IModelInstanceElement} of this {@link InterpretationResultImpl}.
	 */
	private IModelInstanceElement myModelObject;

	/** The result of this {@link InterpretationResultImpl} as {@link OclAny}. */
	private OclAny myResult;

	/** The {@link Constraint} of this {@link InterpretationResultImpl}. */
	private Constraint myConstraint;

	/**
	 * <p>
	 * Creates a new {@link InterpretationResultImpl}.
	 * </p>
	 * 
	 * @param modelObject
	 *          The {@link IModelInstanceElement} of this
	 *          {@link InterpretationResultImpl}.
	 * @param constraint
	 *          The {@link Constraint} of this {@link InterpretationResultImpl}.
	 * @param result
	 *          The result of this {@link InterpretationResultImpl} as
	 *          {@link OclAny}.
	 */
	public InterpretationResultImpl(IModelInstanceElement modelObject,
			Constraint constraint, OclAny result) {

		this.myConstraint = constraint;
		this.myModelObject = modelObject;
		this.myResult = result;
	}

	/**
	 * <p>
	 * Returns the {@link IModelInstanceElement} of this
	 * {@link InterpretationResultImpl}.
	 * </p>
	 * 
	 * @return The {@link IModelInstanceElement} of this
	 *         {@link InterpretationResultImpl}.
	 */
	public IModelInstanceElement getModelObject() {

		return myModelObject;
	}

	/**
	 * <p>
	 * Returns the result of this {@link InterpretationResultImpl} as
	 * {@link OclAny} .
	 * </p>
	 * 
	 * @return The result of this {@link InterpretationResultImpl} as
	 *         {@link OclAny} .
	 */
	public OclAny getResult() {

		return myResult;
	}

	/**
	 * <p>
	 * Returns the {@link Constraint} of this {@link InterpretationResultImpl}.
	 * </p>
	 * 
	 * @return The {@link Constraint} of this {@link InterpretationResultImpl}.
	 */
	public Constraint getConstraint() {

		return myConstraint;
	}

	public String toString() {

		return myResult.toString();
	}
}