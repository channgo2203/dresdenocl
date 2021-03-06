package testpackage.constraints;

/**
 * <p>Generated Aspect to enforce OCL constraint.</p>
 *
 * @author OCL22Java of Dresden OCL2 for Eclipse
 * @Generated
 */
public privileged aspect Class1_PostAspect_postOperation03 {

    protected java.util.Map<Object, Object> newInstances = new java.util.WeakHashMap<Object, Object>();

    /**
     * <p>Adds all new instances of the class {@link testpackage.Class1} to the {@link java.util.Map}
     * newInstances which were created after the beginning of a constraint instrumentation
     * method which calls the OCL operation oclIsNew().</p>
     */
    after(testpackage.Class1 aClass) : execution(testpackage.Class1.new(..)) && this(aClass) {
        this.newInstances.put(aClass, null);
    }

    /**
     * <p>Pointcut for all calls on {@link testpackage.Class1#postOperation03()}.</p>
     */
    protected pointcut postOperation03Caller(testpackage.Class1 aClass):
    	call(* testpackage.Class1.postOperation03())
    	&& target(aClass);

    /**
     * <p>Checks a postcondition for the operation {@link Class1#postOperation03()} defined by the constraint
     * <code>context Class1::postOperation03() : testpackage.Class1
     *       post testOclIsNew: result.oclIsNew()</code></p>
     */
    testpackage.Class1 around(testpackage.Class1 aClass): postOperation03Caller(aClass) {
        /* Disable this constraint for subclasses of Class1. */
        if (aClass.getClass().getCanonicalName().equals("testpackage.Class1")) {

        // Reset Map which collects new instances.
        this.newInstances.clear();

        testpackage.Class1 result;
        result = proceed(aClass);

        if (!this.newInstances.containsKey(result)) {
        	// TODO Auto-generated code executed when constraint is violated.
        	String msg = "Error: Constraint 'testOclIsNew' (post testOclIsNew: result.oclIsNew()) was violated for Object " + aClass.toString() + ".";
        	throw new RuntimeException(msg);
        }
        // no else.

        return result;
        }

        else {
            return proceed(aClass);
        }
    }
}