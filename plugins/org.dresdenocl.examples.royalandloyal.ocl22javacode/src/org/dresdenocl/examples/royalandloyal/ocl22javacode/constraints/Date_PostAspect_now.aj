package org.dresdenocl.examples.royalandloyal.ocl22javacode.constraints;

/**
 * <p>Generated Aspect to enforce OCL constraint.</p>
 *
 * @author OCL22Java of Dresden OCL2 for Eclipse
 * @Generated
 */
public privileged aspect Date_PostAspect_now {

    /**
     * <p>Pointcut for all calls on {@link org.dresdenocl.examples.royalandloyal.ocl22javacode.Date#now()}.</p>
     */
    protected pointcut nowCaller():
    	execution(* org.dresdenocl.examples.royalandloyal.ocl22javacode.Date.now());

    /**
     * <p>Checks a postcondition for the operation {@link Date#now()} defined by the constraint
     * <code>context Date::now() : org.dresdenocl.examples.royalandloyal.ocl22javacode.Date
     *       post: not result.oclIsUndefined()</code></p>
     */
    org.dresdenocl.examples.royalandloyal.ocl22javacode.Date around(): nowCaller() {

        org.dresdenocl.examples.royalandloyal.ocl22javacode.Date result;
        result = proceed();

        if (!!(result == null)) {
        	// TODO Auto-generated code executed when constraint is violated.
        	String msg = "Error: Constraint 'undefined' (post: not result.oclIsUndefined()) was violated for Object static field or operation.";
        	throw new RuntimeException(msg);
        }
        // no else.

        return result;
    }
}