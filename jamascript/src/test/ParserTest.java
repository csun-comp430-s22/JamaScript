package com.jamascript;

import com.jamascript.parser.*;

import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testEqualsOpExp() {
        // 1 + 1 == 1 + 1
        final OpExp first = new OpExp(new IntegerExp(1),
                new PlusOp(),
                new IntegerExp(1));

        final OpExp second = new OpExp(new IntegerExp(1),
                new PlusOp(),
                new IntegerExp(1));

        assertEquals(first, second);
    }
}
