/*
 [The "BSD licence"]
 Copyright (c) 2009 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.stringtemplate.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.stringtemplate.*;
import org.stringtemplate.Compiler;

import java.util.Arrays;

public class TestCompiler {   
    @Test public void testAttr() throws Exception {
        String template = "hi <name>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, " +
            "write, " +
            "load_attr 1, " +
            "write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[hi , name]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }
    
    @Test public void testProp() throws Exception {
        String template = "hi <a.b>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, write, load_attr 1, load_prop 2, write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[hi , a, b]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }
    
    @Test public void testMap() throws Exception {
        String template = "hi <name:bold>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, write, load_attr 1, load_str 2, map, write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[hi , name, bold]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }

    @Test public void testRepeatedMap() throws Exception {
        String template = "hi <name:bold:italics>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, " +
            "write, " +
            "load_attr 1, " +
            "load_str 2, " +
            "map, " +
            "load_str 3, " +
            "map, " +
            "write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[hi , name, bold, italics]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }

    @Test public void testRotMap() throws Exception {
        String template = "hi <name:bold,italics>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, write, load_attr 1, load_str 2, load_str 3, rot_map 2, write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[hi , name, bold, italics]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }

    @Test public void testIf() throws Exception {
        String template = "go: <if(name)>hi, foo<endif>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, write, load_attr 1, brf 14, load_str 2, write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[go: , name, hi, foo]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }

    @Test public void testIfElse() throws Exception {
        String template = "go: <if(name)>hi, foo<else>bye<endif>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, " +
            "write, " +
            "load_attr 1, " +
            "brf 17, " +
            "load_str 2, " +
            "write, " +
            "br 21, " +
            "load_str 3, " +
            "write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[go: , name, hi, foo, bye]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }

    @Test public void testElseIf() throws Exception {
        String template = "go: <if(name)>hi, foo<elseif(user)>a user<endif>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, " +
            "write, " +
            "load_attr 1, " +
            "brf 17, " +
            "load_str 2, " +
            "write, " +
            "br 27, " +
            "load_attr 3, " +
            "brf 27, " +
            "load_str 4, " +
            "write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[go: , name, hi, foo, user, a user]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }
    
    @Test public void testElseIfElse() throws Exception {
        String template = "go: <if(name)>hi, foo<elseif(user)>a user<else>bye<endif>";
        CompiledST code = new Compiler().compile(template);
        String asmExpected =
            "load_str 0, " +
            "write, " +
            "load_attr 1, " +
            "brf 17, " +
            "load_str 2, " +
            "write, " +
            "br 34, " +
            "load_attr 3, " +
            "brf 30, " +
            "load_str 4, " +
            "write, " +
            "br 34, " +
            "load_str 5, " +
            "write";
        String asmResult = code.instrs();
        assertEquals(asmExpected, asmResult);
        String stringsExpected = "[go: , name, hi, foo, user, a user, bye]";
        String stringsResult = Arrays.toString(code.strings);
        assertEquals(stringsExpected, stringsResult);
    }
}
