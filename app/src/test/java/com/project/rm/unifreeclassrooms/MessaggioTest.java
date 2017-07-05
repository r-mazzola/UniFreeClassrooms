package com.project.rm.unifreeclassrooms;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by rm on 05/07/2017.
 */

public class MessaggioTest {
    @Test
    public void createAndSetMessaggioTest() throws Exception{
        Messaggio m = new Messaggio();
        assertEquals(null, m.getId());
        assertEquals(null, m.getCorso());
        assertEquals(null, m.getTestoMessaggio());
        assertEquals(null, m.getTimeStamp());
        m.setCorso("Analisi I");
        m.setTestoMessaggio("Ritardo lezione causa traffico");
        m.setTimeStamp("28/07/2017 08:26");
        assertEquals("Analisi I", m.getCorso());
        assertTrue(m.getTestoMessaggio().equals("Ritardo lezione causa traffico"));
        assertTrue(m.getTimeStamp().equals("28/07/2017 08:26"));
    }


    @Test
    public void createMessaggioWithParametersTest() throws Exception{
        Messaggio m = new Messaggio("1","Fisica I","Sospensione lezione","28/07/2017 15:15");
        assertEquals("1", m.getId());
        assertEquals("Fisica I", m.getCorso());
        assertEquals("Sospensione lezione", m.getTestoMessaggio());
        assertEquals("28/07/2017 15:15", m.getTimeStamp());
    }

    @Test
    public void setMessaggioTest() throws Exception{
        Messaggio m = new Messaggio("Fisica II", "Esame rinviato", "29/07/2017 9:35");
        m.setId("1");
        assertEquals("1", m.getId());
        assertTrue(m.getCorso().equals("Fisica II"));
        assertTrue(m.getTestoMessaggio().equals("Esame rinviato"));
        assertTrue(m.getTimeStamp().equals("29/07/2017 9:35"));
    }
}
