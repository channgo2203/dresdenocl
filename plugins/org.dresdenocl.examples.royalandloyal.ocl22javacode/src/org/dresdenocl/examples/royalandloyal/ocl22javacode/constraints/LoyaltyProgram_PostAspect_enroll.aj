package org.dresdenocl.examples.royalandloyal.ocl22javacode.constraints;

/**
 * <p>Generated Aspect to enforce OCL constraint.</p>
 *
 * @author OCL22Java of Dresden OCL2 for Eclipse
 * @Generated
 */
public privileged aspect LoyaltyProgram_PostAspect_enroll {

    /**
     * <p>Pointcut for all calls on {@link org.dresdenocl.examples.royalandloyal.ocl22javacode.LoyaltyProgram#enroll(org.dresdenocl.examples.royalandloyal.ocl22javacode.Customer c)}.</p>
     */
    protected pointcut enrollCaller(org.dresdenocl.examples.royalandloyal.ocl22javacode.LoyaltyProgram aClass, org.dresdenocl.examples.royalandloyal.ocl22javacode.Customer c):
    	call(* org.dresdenocl.examples.royalandloyal.ocl22javacode.LoyaltyProgram.enroll(org.dresdenocl.examples.royalandloyal.ocl22javacode.Customer))
    	&& target(aClass) && args(c);

    /**
     * <p>Checks a postcondition for the operation {@link LoyaltyProgram#enroll(, org.dresdenocl.examples.royalandloyal.ocl22javacode.Customer c)} defined by the constraint
     * <code>context LoyaltyProgram::enroll(c: org.dresdenocl.examples.royalandloyal.ocl22javacode.Customer) : Boolean
     *       post: participants = participants@pre->including(c)</code></p>
     */
    Boolean around(org.dresdenocl.examples.royalandloyal.ocl22javacode.LoyaltyProgram aClass, org.dresdenocl.examples.royalandloyal.ocl22javacode.Customer c): enrollCaller(aClass, c) {
        /* Disable this constraint for subclasses of LoyaltyProgram. */
        if (aClass.getClass().getCanonicalName().equals("org.dresdenocl.examples.royalandloyal.ocl22javacode.LoyaltyProgram")) {

        java.util.HashSet<org.dresdenocl.examples.royalandloyal.ocl22javacode.Customer> atPreValue1;

        if ((Object) aClass.participants == null) {
            atPreValue1 = null;
        } else {
            atPreValue1 = new java.util.HashSet<org.dresdenocl.examples.royalandloyal.ocl22javacode.Customer>(aClass.participants);
        }

        Boolean result;
        result = proceed(aClass, c);

        if (!org.dresdenocl.tools.codegen.ocl2java.types.util.OclSets.equals(aClass.participants, org.dresdenocl.tools.codegen.ocl2java.types.util.OclSets.including(atPreValue1, c))) {
        	// TODO Auto-generated code executed when constraint is violated.
        	String msg = "Error: Constraint 'undefined' (post: participants = participants@pre->including(c)) was violated for Object " + aClass.toString() + ".";
        	throw new RuntimeException(msg);
        }
        // no else.

        return result;
        }

        else {
            return proceed(aClass, c);
        }
    }
}