package com.chrysanthemum;

import android.util.Log;

import com.chrysanthemum.appdata.Util.DynamicLCSTable;

import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        String a = "christine";
        String b = "kristianna";
        String c = "";

        DynamicLCSTable t = new DynamicLCSTable(a, b);
        c = t.getLCS();
        c = t.getShortestCommonSuperString();

        a = "ing";
        b = "ingrid";

        t = new DynamicLCSTable(a, b);
        c = t.getLCS();
        c = t.getShortestCommonSuperString();

        a = "dick";
        b = "richard";

        t = new DynamicLCSTable(a, b);
        c = t.getLCS();
        c = t.getShortestCommonSuperString();

        a = "hsao";
        b = "xiao";

        t = new DynamicLCSTable(a, b);
        c = t.getLCS();
        c = t.getShortestCommonSuperString();

        a = "susan";
        b = "suzanne";

        t = new DynamicLCSTable(a, b);
        c = t.getLCS();
        c = t.getShortestCommonSuperString();

    }
}