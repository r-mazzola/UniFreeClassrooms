package com.project.rm.unifreeclassrooms;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by rm on 05/07/2017.
 */

public class CorsoTest {
    @Test
    public void createCorsoTest() throws Exception {
        Corso c = new Corso();
        assertEquals(null, c.getNumero_Corso());
        assertEquals(null, c.getNome_Corso());
    }

    @Test
    public void createCorsoWithParametersTest() throws Exception{
        Corso c = new Corso("21055","Analisi I");
        assertTrue(c.getNumero_Corso().equals("21055"));
        assertTrue(c.getNome_Corso().equals("Analisi I"));
    }

    @Test
    public void setCorsoTest() throws Exception{
        Corso c = new Corso();
        c.setNumero_Corso("21010");
        c.setNome_Corso("Chimica");
        assertEquals("21010",c.getNumero_Corso());
        assertTrue(c.getNome_Corso().equals("Chimica"));
    }
}
