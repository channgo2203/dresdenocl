/*
Copyright (C) 2010 by Claas Wilke (claaswilke@gmx.net)

This file is part of the OCL 2 Java Code Generator of Dresden OCL2 for Eclipse.

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

package org.dresdenocl.tools.codegen.ocl2java.test.aspectj.constraintkinds;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import testpackage.Class1;

/**
 * <p>
 * Tests the generated code for a <code>Constraint</code> of the
 * <code>ConstraintKind.INITIAL</code>.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestInit {

	/**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.INITIAL</code>.
	 * </p>
	 */
	@Test
	public void testInit01() {

		Class1 class1 = new Class1();
		
		assertEquals(new Integer(42), class1.initProperty01);
		
		class1.initProperty01 = 43;
		
		/* Should not be modified again by the aspect. */
		assertEquals(new Integer(43), class1.initProperty01);
	}

	
	/**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.INITIAL</code>.
	 * </p>
	 */
	@Test
	public void testStaticInit01() {

		assertEquals(new Integer(42), Class1.staticInitProperty01);
		
		Class1.staticInitProperty01 = 43;

		/* Should not be modified again by the aspect. */
		assertEquals(new Integer(43), Class1.staticInitProperty01);
	}
}
