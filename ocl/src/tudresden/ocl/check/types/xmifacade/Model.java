/*
Copyright (C) 2000  Ralf Wiebicke

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package tudresden.ocl.check.types.xmifacade;

import java.util.*;
import java.lang.*;
import tudresden.ocl.check.types.*;
import tudresden.ocl.check.OclTypeException;

/**
   Implements ModelFacade using model information of a xmi file.
   The actual Model object can be in one of two modes. In the first mode, called
   "not rough", the Model has flattened generalization relationships and
   dissolved associations. In the second mode, called "rough", the Model's
   generalizations relationships are not flattened and the associations are not
   dissolved. This mode enables the user of the Model to query the object oriented
   information from a more structural point of view.

   Additional notes:

   An attribute overrides an inherited attribute, if and and only if it has the same name.
   Ordinary attributes and attributes generated by association partner role names are
   handled equally.
   If an ambiguity is caused by this behaviour, the attribute name is marked to be
   ambiguious for this class, and cannot be used for OCL any more (see OCL spec 5.4.1).
   This ambiguity is inherited to all classes, which do not override this attribute name.
   However, overriding such an ambiguious attribute in a subclass makes this attribute
   available for the subclass and its subclasses again.

   Overriding attributes is considered to be unnormal, and issues a warning to standard out.

   An operation overrides an inherited operation, if and only if it has the same signature.
   The signature consist of the name and a vector of parameter types. Note, that parameter
   types must be exactly equal. Matching by inheritance is not enough.

   Overriding an operation with a different return type is considered to be unnormal,
   and issues a warning to standard out.

   If you dont like the warnings, edit ModelClass.flatten for your needs.
   @see #flatten()
*/
public final class Model implements ModelFacade
{
  /**
     Maps classifier names to ModelClass objects.
     @see ModelClass
  */
  private HashMap classifiers=new HashMap();

  /**
     A description of the source of this model.
     Usually the xmi file.
     Should be independent from system environment,
     so that regression tests have always the same description.
  */
  private String description;

  /**
   * Flag for the distinction of rough and ordinary models.
   * @author Sten Loecher
   */
  private boolean roughMode = false;

  /**
   * Set of associations in the model. If the model is in not rough mode,
   * this variable must be set to null.
   * @author Sten Loecher
   */
  private HashSet associations = null;

  public Model(String description)
  {
    this.description=description;
  }

  /**
     Flattens all classes in model.
  */
  public void flatten()
  {
    for(Iterator i=classifiers.values().iterator(); i.hasNext(); )
      ((ModelClass)(i.next())).flatten();
  }

  /**
     Useful for debugging.
  */
  public void printData()
  {
    String log;
    try
    {
      log=System.getProperty("tudresden.ocl.check.types.xmifacade.log");
    }
    catch(SecurityException e) {log=null;};

    if(log!=null)
    {
      printData(System.out);
    }
  }

  /**
     Useful for debugging.
  */
  public void printData(java.io.PrintStream o)
  {
    HashSet done=new HashSet();
    for(Iterator i=(new TreeSet(classifiers.keySet())).iterator(); i.hasNext(); )
    {
      String classname=(String)i.next();
      ModelClass classobject=(ModelClass)classifiers.get(classname);
      o.println("classifier "+classname+" --> "+classobject.getFullName());
      if(done.add(classobject))
        classobject.printData(o);
    }
  }

  public void putClassifier(ModelClass modelclass)
  {
    String fullname =modelclass.getFullName();
    String shortname=modelclass.getShortName();

    if(classifiers.put(fullname, modelclass)!=null)
      throw new ModelException("ambigous classifier name: "+fullname);

    if(!shortname.equals(fullname))
      if(classifiers.put(shortname, modelclass)!=null)
        throw new ModelException("ambigous classifier short name (THIS IS A BUG/TODO): "+shortname+" at fullname "+fullname);

    modelclass.setModel(this);
  }

  public static String strip(String x)
  {
    if(x.indexOf((int)' ')<0)
      return x;

    StringBuffer buf=new StringBuffer();
    for(int i=0; i<x.length(); i++)
    {
      char c=x.charAt(i);
      if(c!=' ')
        buf.append(c);
    }
    //System.out.println("stripping "+x+" to "+buf);
    return buf.toString();
  }

  public Any getClassifier(String name)
  {
    //System.out.println("xmifacade: getClassifier "+name);

    Any a=((Any)classifiers.get(strip(name)));
    if(a==null)
      throw new OclTypeException("Classifier \""+name+"\" in xmifacade["+description+"] not found.");
    return a;
  }

  /**
     Indicates, that a ModelAttribute is ambiguiuos, thus cannot be used in OCL.
     @see ModelAttribute
  */
  public static final Any AMBIGOUS=new Any()
  {
    public Type navigateQualified(String name, Type[] qualifiers) throws OclTypeException
    {
      throw new IllegalArgumentException(
        "called Model.AMBIGOUS.navigateQualified(\""+Basic.qualifierString(name,qualifiers)+"\"), this should never happen.");
    }

    public Type navigateParameterized(String name, Type[] params) throws OclTypeException
    {
      throw new IllegalArgumentException(
        "called Model.AMBIGOUS.navigateParameterized(\""+Basic.signatureString(name,params)+"\"), this should never happen.");
    }

    public boolean hasState(String name)
    {
      return false;
    }

    public boolean conformsTo(Type type)
    {
      throw new IllegalArgumentException(
        "called Model.AMBIGOUS.conformsTo(\""+type+"\"), this should never happen.");
    }

    public String toString()
    {
      return "AMBIGOUS";
    }
  };

  public String toString()
  {
    return getClass().toString()+"("+description+")";
  }

  // ---------------------------------------------------------------------
  /**
   * @param roughMode true if the model is flattend and dissolved, false otherwise
   * @author Sten Loecher
   */
  public void setRoughMode(boolean roughMode) {
  	this.roughMode = roughMode;

  	if (!roughMode) {
  		associations = null;
  	} else {
  		associations = new HashSet();
  	}
  }

  /**
   * @return true if the model is in rough mode, false otherwise
   * @author Sten Loecher
   */
  public boolean isRough() {
  	return roughMode;
  }

  /**
   * @param association an association
   * @exception IllegalStateException if the model is not in rough mode
   * @author Sten Loecher
   */
  public void putAssociation(ModelAssociation association)
  throws IllegalStateException {
  	if ((associations != null) && (roughMode == true)) {
  		associations.add(association);
  	} else {
  		throw new IllegalStateException("Model must be in rough mode to use method putAssociation(...) !");
  	}
  }

  /**
   * @return a map containing all classifiers of the model
   * @author Sten Loecher
   */
  public Map classifiers() {
  	return Collections.unmodifiableMap(classifiers);
  }

  /**
   * @return a set of all associations of the model
   * @exception IllegalStateException if the model is not in rough mode
   * @author Sten Loecher
   */
  public Set associations()
  throws IllegalStateException {
  	if ((associations != null) && (roughMode == true)) {
  		return Collections.unmodifiableSet(associations);
  	} else {
  		throw new IllegalStateException("Model must be in rough mode to use method associations() !");
  	}
  }

  /**
   * This methode will be called instead of flatten if the model is rough.
   * It determines all generalization relationships but does no flattening.
   * @exception IllegalStateException if the model is not in rough mode
   * @author Sten Loecher
   */
  public void determineAllSupertypes()
  throws IllegalStateException {
  	if (!roughMode) throw new IllegalStateException("Model must be in rough mode to use method determineAllSupertypes() !");

  	for(Iterator i=classifiers.values().iterator(); i.hasNext(); )
		((ModelClass)(i.next())).determineAllSupertypes();
  }

  /**
   * This methode determines all direct subtypes of a given class in the model.
   * @param mc the class
   * @return a set that contains all subtypes of the given class
   * @exception IllegalArgumentException if the class is not contained within this model
   * @author Sten Loecher
   */
  public Set getDirectClassSubtypes(ModelClass mc)
  throws IllegalArgumentException {
  	if (!classifiers.containsValue(mc)) throw new IllegalArgumentException("No valid ModelClass !");

  	Iterator i = classifiers.values().iterator();
  	HashSet retSet = new HashSet();
  	ModelClass c;

  	while (i.hasNext()) {
  		c = (ModelClass)i.next();
  		if (c.isDirectSupertype(mc)) {
  			retSet.add(c);
  		}
  	}

  	return retSet;
  }
}
