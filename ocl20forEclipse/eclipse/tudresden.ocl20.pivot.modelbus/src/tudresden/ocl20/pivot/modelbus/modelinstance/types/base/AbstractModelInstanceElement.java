/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2007 Ronny Brandt (Ronny_Brandt@web.de).                    *
 * All rights reserved.                                                      *
 *                                                                           *
 * This work was done as a project at the Chair for Software Technology,     *
 * Dresden University Of Technology, Germany (http://st.inf.tu-dresden.de).  *
 * It is understood that any modification not identified as such is not      *
 * covered by the preceding statement.                                       *
 *                                                                           *
 * This work is free software; you can redistribute it and/or modify it      *
 * under the terms of the GNU Library General Public License as published    *
 * by the Free Software Foundation; either version 2 of the License, or      *
 * (at your option) any later version.                                       *
 *                                                                           *
 * This work is distributed in the hope that it will be useful, but WITHOUT  *
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or     *
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Library General Public     *
 * License for more details.                                                 *
 *                                                                           *
 * You should have received a copy of the GNU Library General Public License *
 * along with this library; if not, you can view it online at                *
 * http://www.fsf.org/licensing/licenses/gpl.html.                           *
 *                                                                           *
 * To submit a bug report, send a comment, or get the latest news on this    *
 * project, please visit the website: http://dresden-ocl.sourceforge.net.    *
 * For more information on OCL and related projects visit the OCL Portal:    *
 * http://st.inf.tu-dresden.de/ocl                                           *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package tudresden.ocl20.pivot.modelbus.modelinstance.types.base;

import java.util.Set;

import tudresden.ocl20.pivot.modelbus.model.IModel;
import tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceCollection;
import tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceElement;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * An abstract implementation of {@link IModelInstanceElement}.
 * </p>
 * 
 * @author Ronny Brandt: Built the first version.
 * @author Claas Wilke: Did re-factoring and added Java-doc.
 */
public abstract class AbstractModelInstanceElement implements
		IModelInstanceElement {

	/**
	 * The {@link Type}s of the {@link IModel} of which this
	 * {@link IModelInstanceElement} is an instance.
	 */
	protected Set<Type> myTypes;

	/**
	 * The name of this {@link IModelInstanceElement}. Could be <code>null</code>.
	 */
	protected String myName;

	/** The id of this {@link IModelInstanceElement}. Could be <code>null</code>. */
	protected String myId;

	/**
	 * <p>
	 * Checks if the given {@link Object} is equal to this
	 * {@link AbstractModelInstanceElement}. This method must be implemented in
	 * respect to equivalence of two {@link IModelInstanceElement}s contained in
	 * the same {@link IModelInstanceCollection}.
	 * </p>
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(Object object);

	/**
	 * <p>
	 * This method must be overriden according to the required implementation of
	 * {@link AbstractModelInstanceElement#equals(Object)}.
	 * </p>
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 * @see tudresden.ocl20.pivot.modelbus.modelinstance.types.base.AbstractModelInstanceElement#equals(Object)
	 */
	@Override
	public abstract int hashCode();

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelInstanceElement#getName()
	 */
	public String getName() {

		StringBuffer resultBuffer;

		resultBuffer = new StringBuffer();

		/* Probably return the element's name. */
		if (this.myName != null) {
			resultBuffer.append(this.myName);
		}

		/* Else probably return the element's id. */
		else if (this.myId != null) {
			resultBuffer.append(this.myId);
		}

		/* Else construct a name of all implemented types. */
		else {
			resultBuffer.append("[");

			for (Type aType : this.getTypes()) {

				if (resultBuffer.length() == 1) {
					resultBuffer.append(",");
				}
				// no else.

				resultBuffer.append(aType.getName());
			}

			resultBuffer.append("]");
		}
		// end else.

		return resultBuffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelInstanceElement#getTypes()
	 */
	public Set<Type> getTypes() {

		return this.myTypes;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.IModelInstanceElement#isInstanceOf(tudresden
	 * .ocl20 .pivot.pivotmodel.Type)
	 */
	public boolean isKindOf(Type type) {

		boolean result;

		result = false;

		for (Type aType : this.myTypes) {

			if (aType.conformsTo(type)) {
				result = true;
				break;
			}
			// no else.
		}
		// end for.

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceElement
	 * #isTypeOf(tudresden.ocl20.pivot.pivotmodel.Type)
	 */
	public boolean isTypeOf(Type type) {

		boolean result;

		/*
		 * Check if this IModelInstanceElement has exactly one type. Otherwise it
		 * cannot be of the given Type. Elements that have more than one type are a
		 * sub type of these Types.
		 */
		if (this.myTypes.size() != 1) {
			result = false;
		}
		
		else {
			result = type.equals(this.myTypes.iterator().next());
		}

		return result;
	}
}