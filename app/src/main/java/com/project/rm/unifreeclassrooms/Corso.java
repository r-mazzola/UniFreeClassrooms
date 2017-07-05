package com.project.rm.unifreeclassrooms;

/**
 * Created by Marco on 18/05/2017.
 */

public class Corso {

    String numero_Corso;
    String nome_Corso;

    //now create constructor and getter setter method using shortcut like command+n for mac & Alt+Insert for window.
    public Corso(){}

/* TEST COSTRUTTORE */
    public Corso(String numero_Corso, String nome_Corso) {
        this.numero_Corso = numero_Corso;
        this.nome_Corso = nome_Corso;
    }

    public String getNumero_Corso() {
        return numero_Corso;
    }

    public void setNumero_Corso(String numero_Corso) {
        this.numero_Corso = numero_Corso;
    }

    public String getNome_Corso() {
        return nome_Corso;
    }

    public void setNome_Corso(String nome_Corso) {
        this.nome_Corso = nome_Corso;
    }
}
