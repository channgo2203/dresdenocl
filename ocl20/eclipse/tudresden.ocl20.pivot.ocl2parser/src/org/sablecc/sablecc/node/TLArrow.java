/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.sablecc.sablecc.node;

import org.sablecc.sablecc.analysis.*;

public final class TLArrow extends Token
{
    public TLArrow()
    {
        super.setText("<-");
    }

    public TLArrow(int line, int pos)
    {
        super.setText("<-");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TLArrow(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLArrow(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TLArrow text.");
    }
}