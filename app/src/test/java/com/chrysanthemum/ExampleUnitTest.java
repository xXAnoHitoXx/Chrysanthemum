package com.chrysanthemum;

import android.util.Log;

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
        Metaphone mp = new Metaphone();

        String a = mp.encode("Kris");
        String b = mp.encode("Kristianna");

        a = mp.encode("Kris");
        b = mp.encode("Kristianna");

        a = mp.encode("Ing");
        b = mp.encode("Ingrid");

        a = mp.encode("Dick");
        b = mp.encode("Richard");

        a = mp.encode("Hsao");
        b = mp.encode("Xiao");

        a = mp.encode("Susan");
        b = mp.encode("Suzanne");
    }
}