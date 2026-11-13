/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Descompte {
    private int id;
    private int producteId;
    private String tipus;
    private double valor;

    public Descompte() {}

    public Descompte(int id, int producteId, String tipus, double valor) {
        this.id = id;
        this.producteId = producteId;
        this.tipus = tipus;
        this.valor = valor;
    }

    public Descompte(int producteId, String tipus, double valor) {
        this.producteId = producteId;
        this.tipus = tipus;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProducteId() {
        return producteId;
    }

    public void setProducteId(int producteId) {
        this.producteId = producteId;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}