package com.ergonzalez.gestiondecpm;

public class Configuraciones {
	
	private int id;
    private String nombre;
    private String descripcion;
    private int conexion;
    private int tipo;
 
    // Constructor de un objeto Contactos
    public Configuraciones(int id, String nombre, String descripcion, int conexion, int tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.conexion = conexion;
        this.tipo = tipo;
    }
 
    // Recuperar/establecer ID
    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }
 
    // Recuperar/establecer NOMBRE
    public String getNOMBRE() {
        return nombre;
    }
    public void setNOMBRE(String nombre) {
        this.nombre = nombre;
    }
 
    // Recuperar/establecer DESCRIPCION
    public String getDESCRIPCION() {
        return descripcion;
    }
    public void setDESCRIPCION(String descripcion) {
        this.descripcion = descripcion;
    }
 
    // Recuperar/establecer CONEXION
    public int getC0NEXION() {
        return conexion;
    }
    public void setCONEXION(int conexion) {
        this.conexion = conexion;
    }
    
    // Recuperar/establecer TIPO
    public int getTIPO() {
        return tipo;
    }
    public void setTIPO(int tipo) {
        this.conexion = tipo;
    }

}
