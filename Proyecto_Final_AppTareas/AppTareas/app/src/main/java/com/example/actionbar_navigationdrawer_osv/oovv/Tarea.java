package com.example.actionbar_navigationdrawer_osv.oovv;

public class Tarea {

    private long id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String fechaCompleta;
    private boolean usarRecordatorio;

    public Tarea(String titulo, String descripcion, String fecha, String hora, String fechaCompleta, boolean usarRecordatorio) {
        this.titulo = titulo;
        if (descripcion.isEmpty()) {
            this.descripcion = "";
        }
        if (hora.isEmpty()) {
            this.hora = "";
        }
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.fechaCompleta = fechaCompleta;
        this.usarRecordatorio = usarRecordatorio;

    }

    // Constructor para cuando instanciamos desde la BD
    public Tarea(long id, String titulo, String descripcion, String fecha, String hora, String fechaCompleta, boolean usarRecordatorio) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.fechaCompleta = fechaCompleta;
        this.usarRecordatorio = usarRecordatorio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFechaCompleta() {
        return fechaCompleta;
    }

    public void setFechaCompleta(String fechaCompleta) {
        this.fechaCompleta = fechaCompleta;
    }

    public boolean isUsarRecordatorio() {
        return usarRecordatorio;
    }

    public void setUsarRecordatorio(boolean usarRecordatorio) {
        this.usarRecordatorio = usarRecordatorio;
    }

    @Override
    public String toString() {
        return "Tarea{" + "titulo=" + titulo + ", descripcion=" + descripcion + ", fecha=" + fecha + ", hora=" + hora + ", usarRecordatorio=" + usarRecordatorio + '}';
    }

}