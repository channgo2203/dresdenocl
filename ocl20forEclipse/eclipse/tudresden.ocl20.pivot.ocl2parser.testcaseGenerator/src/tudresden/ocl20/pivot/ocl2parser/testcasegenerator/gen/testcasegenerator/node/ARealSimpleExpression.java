/* This file was generated by SableCC (http://www.sablecc.org/). */

package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.node;

import java.util.*;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.analysis.*;

public final class ARealSimpleExpression extends PSimpleExpression
{
    private TRealValue _realValue_;

    public ARealSimpleExpression()
    {
    }

    public ARealSimpleExpression(
        TRealValue _realValue_)
    {
        setRealValue(_realValue_);

    }
    public Object clone()
    {
        return new ARealSimpleExpression(
            (TRealValue) cloneNode(_realValue_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseARealSimpleExpression(this);
    }

    public Object apply(SwitchWithReturn sw, Object param) throws AttrEvalException {
        return ((AnalysisWithReturn) sw).caseARealSimpleExpression(this, param);
    }

    public TRealValue getRealValue()
    {
        return _realValue_;
    }

    public void setRealValue(TRealValue node)
    {
        if(_realValue_ != null)
        {
            _realValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _realValue_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_realValue_);
    }

    void removeChild(Node child)
    {
        if(_realValue_ == child)
        {
            _realValue_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_realValue_ == oldChild)
        {
            setRealValue((TRealValue) newChild);
            return;
        }

    }
}