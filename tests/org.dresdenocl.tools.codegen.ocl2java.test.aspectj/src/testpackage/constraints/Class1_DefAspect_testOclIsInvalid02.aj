package testpackage.constraints;

/**
 * <p>Generated Aspect to enforce OCL constraint.</p>
 *
 * @author OCL22Java of Dresden OCL2 for Eclipse
 * @Generated
 */
public privileged aspect Class1_DefAspect_testOclIsInvalid02 {

    /**
     * <p>Defines the method testOclIsInvalid02() defined by the constraint
     * <code>context Class1
     *       def: testOclIsInvalid02(): Boolean = (Sequence { } ->first()).oclIsInvalid()</code></p>
     */
    public Boolean testpackage.Class1.testOclIsInvalid02() {
        Boolean result1;

        /* Check if the expression results in invalid. */
        try {
        	java.util.ArrayList collection1;
        	collection1 = new java.util.ArrayList();

            /* DUMMY variable is necessary to form literals into a statement. */
            Object DUMMY = org.dresdenocl.tools.codegen.ocl2java.types.util.OclSequences.first(collection1); 
            result1 = false;
        }

        catch (Exception e) {
            result1 = true;
        }

        return result1;
    }
}