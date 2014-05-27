package org.dresdenocl.examples.royalandloyal.ocl22javacode.constraints;

/**
 * <p>Generated Aspect to enforce OCL constraint.</p>
 *
 * @author OCL22Java of Dresden OCL2 for Eclipse
 * @Generated
 */
public privileged aspect CustomerCard_InvAspect5 {

    declare parents: org.dresdenocl.examples.royalandloyal.ocl22javacode.CustomerCard implements org.dresdenocl.tools.codegen.ocl2java.types.OclCheckable;

    public void org.dresdenocl.examples.royalandloyal.ocl22javacode.CustomerCard.checkInvariants() {
        /* Remains empty. Is only filled with behavior by advice(s). */
    }
    /**
     * <p>Pointcut for all calls on {@link org.dresdenocl.examples.royalandloyal.ocl22javacode.CustomerCard#checkInvariants()}.</p>
     */
    protected pointcut checkInvariantsCaller(org.dresdenocl.examples.royalandloyal.ocl22javacode.CustomerCard aClass):
    	call(void checkInvariants())
    	&& target(aClass);

    /**
     * <p><code>Checks an invariant on the class CustomerCard defined by the constraint
     * <code>context CustomerCard
     *       inv:  let correctDate : Boolean =   self.validFrom.isBefore(Date::now()) and   self.validThru.isAfter(Date::now()) in   if valid then     correctDate = true   else     correctDate = false   endif</code></p>
     */
    after(org.dresdenocl.examples.royalandloyal.ocl22javacode.CustomerCard aClass) : checkInvariantsCaller(aClass) {
        /* Disable this constraint for subclasses of CustomerCard. */
        if (aClass.getClass().getCanonicalName().equals("org.dresdenocl.examples.royalandloyal.ocl22javacode.CustomerCard")) {
        Boolean correctDate;
        correctDate = (aClass.validFrom.isBefore(org.dresdenocl.examples.royalandloyal.ocl22javacode.Date.now()) && aClass.validThru.isAfter(org.dresdenocl.examples.royalandloyal.ocl22javacode.Date.now()));

        Boolean ifExpResult1;

        if (aClass.valid) {
            ifExpResult1 = ((Object) correctDate).equals(new Boolean(true));
        } else {
            ifExpResult1 = ((Object) correctDate).equals(new Boolean(false));
        }

        if (!ifExpResult1) {
        	// TODO Auto-generated code executed when constraint is violated.
        	String msg = "Error: Constraint 'undefined' (inv:  let correctDate : Boolean =   self.validFrom.isBefore(Date::now()) and   self.validThru.isAfter(Date::now()) in   if valid then     correctDate = true   else     correctDate = false   endif) was violated for Object " + aClass.toString() + ".";
        	throw new RuntimeException(msg);
        }
        // no else.
        }
        // no else.
    }
}