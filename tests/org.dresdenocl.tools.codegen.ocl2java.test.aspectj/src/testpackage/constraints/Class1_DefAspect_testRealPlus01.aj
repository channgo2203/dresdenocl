package testpackage.constraints;

/**
 * <p>Generated Aspect to enforce OCL constraint.</p>
 *
 * @author OCL22Java of Dresden OCL2 for Eclipse
 * @Generated
 */
public privileged aspect Class1_DefAspect_testRealPlus01 {

    /**
     * <p>Defines the method testRealPlus01(Float source, Float arg01) defined by the constraint
     * <code>context Class1
     *       def: testRealPlus01(source: Real, arg01: Real): Real = source + arg01</code></p>
     */
    public Float testpackage.Class1.testRealPlus01(Float source, Float arg01) {
        return (source + arg01);
    }
}