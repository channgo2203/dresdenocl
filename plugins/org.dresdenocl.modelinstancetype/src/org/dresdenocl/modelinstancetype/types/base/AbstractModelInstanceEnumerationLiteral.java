/*
Copyright (C) 2009 by Claas Wilke (claaswilke@gmx.net)

This file is part of the Java Model Instance Plug-in of Dresden OCL2 for Eclipse.

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
package org.dresdenocl.modelinstancetype.types.base;

import org.dresdenocl.modelinstancetype.types.IModelInstanceEnumerationLiteral;
import org.dresdenocl.pivotmodel.Type;

/**
 * <p>
 * An abstract adapter {@link Class} for
 * {@link AbstractModelInstanceEnumerationLiteral}s of
 * <code>IModelInstance</code>s.
 * </p>
 * 
 * @author Claas Wilke
 */
public abstract class AbstractModelInstanceEnumerationLiteral extends
		AbstractModelInstanceElement implements
		IModelInstanceEnumerationLiteral {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.modelinstancetype.types.IModelInstanceElement
	 * #getName()
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
			resultBuffer.append("MIEnumerationLiteral");
			resultBuffer.append("[");

			if (this.isUndefined()) {
				resultBuffer.append("undefined");
			}

			else {
				resultBuffer.append(this.getLiteral().getEnumeration()
						.getName()
						+ "::" + this.getLiteral().getName());
			}

			resultBuffer.append("]");
		}
		// end else.

		return resultBuffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.dresdenocl.modelbus.modelinstance.types.base.
	 * AbstractModelInstanceElement#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {

		boolean result;

		if (object == null) {
			result = false;
		}

		else if (object instanceof AbstractModelInstanceEnumerationLiteral) {

			AbstractModelInstanceEnumerationLiteral other;
			other = (AbstractModelInstanceEnumerationLiteral) object;

			/*
			 * This should not happen. But anyway, null == null results in
			 * false.
			 */
			if (this.isUndefined() || other.isUndefined()) {
				result = false;
			}

			else {
				result = this.getLiteral().getQualifiedName().equals(
						other.getLiteral().getQualifiedName());
			}
		}

		else {
			result = false;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.dresdenocl.modelbus.modelinstance.types.base.
	 * AbstractModelInstanceElement#hashCode()
	 */
	@Override
	public int hashCode() {

		int result;

		if (this.isUndefined()) {
			result = 0;
		}

		else {
			result = 31 * this.getLiteral().getQualifiedName().hashCode();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dresdenocl.modelinstancetype.types.IModelInstanceElement#isKindOf
	 * (org.dresdenocl.pivotmodel.Type)
	 */
	public boolean isKindOf(Type type) {

		return this.getType().conformsTo(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dresdenocl.modelinstancetype.types.IModelInstanceElement
	 * #isUndefined()
	 */
	public boolean isUndefined() {

		return this.getLiteral() == null;
	}
}