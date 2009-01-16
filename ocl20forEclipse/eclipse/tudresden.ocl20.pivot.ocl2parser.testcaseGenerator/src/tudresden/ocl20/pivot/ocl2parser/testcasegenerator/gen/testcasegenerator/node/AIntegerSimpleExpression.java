/* This file was generated by SableCC (http://www.sablecc.org/). */

package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.node;

import java.util.*;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.analysis.*;

public final class AIntegerSimpleExpression extends PSimpleExpression
{
    private TIntegerValue _integerValue_;

    public AIntegerSimpleExpression()
    {
    }

    public AIntegerSimpleExpression(
        TIntegerValue _integerValue_)
    {
        setIntegerValue(_integerValue_);

    }
    public Object clone()
    {
        return new AIntegerSimpleExpression(
            (TIntegerValue) cloneNode(_integerValue_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIntegerSimpleExpression(this);
    }

    public Object apply(SwitchWithReturn sw, Object param) throws AttrEvalException {
        return ((AnalysisWithReturn) sw).caseAIntegerSimpleExpression(this, param);
    }

    public TIntegerValue getIntegerValue()
    {
        return _integerValue_;
    }

    public void setIntegerValue(TIntegerValue node)
    {
        if(_integerValue_ != null)
        {
            _integerValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _integerValue_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_integerValue_);
    }

    void removeChild(Node child)
    {
        if(_integerValue_ == child)
        {
            _integerValue_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_integerValue_ == oldChild)
        {
            setIntegerValue((TIntegerValue) newChild);
            return;
        }

    }
}