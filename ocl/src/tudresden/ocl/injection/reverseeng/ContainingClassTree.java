/*
 * ContainingClassTree.java
 *
 * Created on 11. September 2000, 17:00
 */
 
package tudresden.ocl.injection.reverseeng;

import tudresden.ocl.injection.*;

import javax.swing.*;
import javax.swing.tree.*;

/** 
 *
 * @author  sz9
 * @version 
 */
public class ContainingClassTree extends javax.swing.JPanel {

  private DefaultTreeModel m_dtmModel;
  private DefaultMutableTreeNode m_dmtnLastNode = null;
  
  static class ContainingLevel {
    private boolean m_fIsPackage;
    private String m_sName;
    
    public ContainingLevel (boolean fIsPackage,
                               String sName) {
      super();
      
      m_fIsPackage = fIsPackage;
      m_sName = sName;
    }
    
    public String toString() {
      return m_sName;
    }
    
    public boolean isPackage() {
      return m_fIsPackage;
    }
  }
  
  static class ContainingLevelRenderer extends DefaultTreeCellRenderer {

    static Icon s_iClass = new javax.swing.ImageIcon (ContainingLevelRenderer.class.getResource ("resources/class.gif"));

    public ContainingLevelRenderer () {
      super();
    }
    
    public java.awt.Component getTreeCellRendererComponent (javax.swing.JTree tree,
                                                                 Object value,
                                                                 boolean sel,
                                                                 boolean expanded,
                                                                 boolean leaf,
                                                                 int row,
                                                                 boolean hasFocus) {
      super.getTreeCellRendererComponent (tree, value, sel, expanded, leaf, row, hasFocus);
      
      ContainingLevel cl = (ContainingLevel) ((DefaultMutableTreeNode) value).getUserObject();
      
      if (! cl.isPackage()) {
        setIcon (s_iClass);
      }
      
      return this;
    }
  }
  
  /** Creates new form ContainingClassTree */
  public ContainingClassTree (AbstractDescriptor ad) {
    fillInClassTree (ad.getContainingClassJavaClass());

    initComponents ();
  }

  private void fillInPackageTree (JavaClass jc) {
    String sPackage = jc.getPackageName();
    
    if ((sPackage != null) &&
         (sPackage != "")) {
      int nDotPos;
      while ((nDotPos = sPackage.indexOf ('.')) > -1) {
        String sTopPackage = sPackage.substring (0, nDotPos);
        
        if (m_dmtnLastNode == null) {
          m_dmtnLastNode = new DefaultMutableTreeNode ();
          m_dtmModel = new DefaultTreeModel (m_dmtnLastNode);
        }
        else {
          DefaultMutableTreeNode dmtnNext = new DefaultMutableTreeNode();
          m_dmtnLastNode.add (dmtnNext);
          m_dmtnLastNode = dmtnNext;
        }

        m_dmtnLastNode.setUserObject (new ContainingLevel (true, sTopPackage));
        
        sPackage = sPackage.substring (nDotPos + 1);
      }
      
      if (m_dmtnLastNode == null) {
        m_dmtnLastNode = new DefaultMutableTreeNode ();
        m_dtmModel = new DefaultTreeModel (m_dmtnLastNode);
      }
      else {
        DefaultMutableTreeNode dmtnNext = new DefaultMutableTreeNode();
        m_dmtnLastNode.add (dmtnNext);
        m_dmtnLastNode = dmtnNext;
      }

      m_dmtnLastNode.setUserObject (new ContainingLevel (true, sPackage));
    }
  }
  
  private void fillInClassTree (JavaClass jcParent) {
    if (jcParent == null) {
      return;
    }
    
    if (jcParent.getParent() == null) {
      fillInPackageTree (jcParent);
    }
    else {
      fillInClassTree (jcParent.getParent());
    }
    
    if (m_dmtnLastNode == null) {
      m_dmtnLastNode = new DefaultMutableTreeNode ();
      m_dtmModel = new DefaultTreeModel (m_dmtnLastNode);
    }
    else {
      DefaultMutableTreeNode dmtnNext = new DefaultMutableTreeNode();
      m_dmtnLastNode.add (dmtnNext);
      m_dmtnLastNode = dmtnNext;
    }

    m_dmtnLastNode.setUserObject (new ContainingLevel (false, jcParent.getName()));
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents () {//GEN-BEGIN:initComponents
    m_jspScroller = new javax.swing.JScrollPane ();
    m_jtContainingClassTree = new javax.swing.JTree ();
    setLayout (new java.awt.BorderLayout ());


      m_jtContainingClassTree.setModel (m_dtmModel);
      m_jtContainingClassTree.setCellRenderer (new ContainingLevelRenderer());
  
      m_jspScroller.setViewportView (m_jtContainingClassTree);
  

    add (m_jspScroller, java.awt.BorderLayout.CENTER);

  }//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane m_jspScroller;
  private javax.swing.JTree m_jtContainingClassTree;
  // End of variables declaration//GEN-END:variables

}