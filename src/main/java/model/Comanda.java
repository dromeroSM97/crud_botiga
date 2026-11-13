/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Comanda {
    private int id;
    private int clientId;
    private Timestamp data;
    private double total;
    private List<LiniaComanda> linies;

    public Comanda() {
        this.linies = new ArrayList<>();
    }

    public Comanda(int id, int clientId, Timestamp data, double total) {
        this.id = id;
        this.clientId = clientId;
        this.data = data;
        this.total = total;
        this.linies = new ArrayList<>();
    }

    public Comanda(int clientId) {
        this.clientId = clientId;
        this.linies = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<LiniaComanda> getLinies() {
        return linies;
    }

    public void setLinies(List<LiniaComanda> linies) {
        this.linies = linies;
    }

    public void afegirLinia(LiniaComanda linia) {
        this.linies.add(linia);
    }
}