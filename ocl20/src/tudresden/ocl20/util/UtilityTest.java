package tudresden.ocl20.util;
import java.io.File;
/**

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Utility u=new Utility();
		File modelFile=new File(".\\resources\\PoseidonProjects\\CarOwner.xmi");
		
		//Converting relative path to absolute path
		String urlOCL=OCLurl.getAbsolutePath();
		
		OclModel newModel=u.loadUMLModel(urlModel, urlOCL);
		
		//Test 1: Retrieving all constraints of a given class (Person in this example)
		Collection cons=u.getConstraintsClassifier(newModel,"Person");
		Vector vcons = new Vector(cons);
	    System.out.println("Person has " + vcons.size() + " constraints.");
		Iterator it = cons.iterator();
	    while ( it.hasNext() ) {
	    	Constraint c = (Constraint) it.next();
            System.out.println(c.getNameA());
	    }
	    
		//Test2: Retrieving all constraints defined in the model 
	    Collection cons2=u.getAllConstraintsModel(newModel);
	    Vector vcons2=new Vector(cons2);
	    System.out.println("The model has " + vcons2.size() + " constraints.");
		
	    //Test3: Traversing the tree of a given constraints 
	    Iterator it2 = cons2.iterator();
	    while ( it2.hasNext() ) {
	    	Constraint c2 = (Constraint) it2.next();
	    	System.out.println(c2.getNameA());
	        if(c2.getConstrainedElementA().getNameA().equals("Person"))
	        {
	       	   System.out.println("Restriction defined over the context " + c2.getConstrainedElementA().getNameA());
	       	   ExpressionInOcl exp2= (ExpressionInOcl) c2.getBodyA();
	       	   OclExpression o= (OclExpression) exp2.getBodyExpression();
	       	   //the object o points to the root of the tree representing the constraint 
	           //In this example I know how is the constraint so I do not check the type of each "node" when traversing the tree
	       	   OperationCallExp op = (OperationCallExp) o;
	           System.out.println( "Operation name: " + op.getReferredOperation().getNameA() );
	           //Per anar al fill esquerra (source, segons el metamodel) haur?em de fer
	           AttributeCallExp at= (AttributeCallExp) op.getSource();
	           System.out.println("Attribute name: " + at.getReferredAttribute().getNameA());
	           //Obviament en aquest cas ja sab?em com era la RI
	           //Per fer un recorregut gen?ric cal mirar a cada pas en quin tipus de node estem
	           //i actuar en conseq??ncia (segons el metamodel de l'OCL)
	        }  
	        /*if(c2.getNameA().equals("at_least_three_persons"))
	        {
	       	   System.out.println("Restricci? definida sobre " + c2.getConstrainedElementA().getNameA());
	       	   ExpressionInOcl exp2= (ExpressionInOcl) c2.getBodyA();
	       	   OclExpression o= (OclExpression) exp2.getBodyExpression();
	       	   //o apunta a l'arrel de l'arbre que representa la restricci?
	           OperationCallExp op = (OperationCallExp) o;
	           System.out.println( "Operation name: " + op.getReferredOperation().getNameA() );
	           
	           OperationCallExp op2 = (OperationCallExp) op.getSource();
	           System.out.println( "Operation name: " + op2.getReferredOperation().getNameA());
	         
	           OperationCallExp op3 = (OperationCallExp) op2.getSource();
	           System.out.println( "Operation name: " + op3.getReferredOperation().getNameA());

	           System.out.println(op3.getSrcType().getNameA());
	        }  */
	   	     
	    }
	    
	    //Test 4: Retrieving all classes from the model
	    //Cal vigilar perqu? alguns tipus de dades tamb? els torna com a classes
	    Collection classes=u.getAllClassesModel(newModel);
	    Vector vcons3=new Vector(classes);
	    System.out.println("The model has " + vcons3.size() + " classes");
	    Iterator it3 = classes.iterator();
	    while ( it3.hasNext() ) {
	       Classifier cl = (Classifier) it3.next();
           System.out.println("Name of Class " +i+": "+ cl.getNameA()); 
	    }
	    

	}

}