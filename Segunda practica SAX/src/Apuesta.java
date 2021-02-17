public class Apuesta {

    //Atributos
    private String usuario;
    private int combate;
    private float cantidad;
    private float cuota;
    private String ganador;
    private int puntuacion;
    private TipoViiictoria tipoVictoria;

    //Costructores
    public Apuesta(){//por defecto
        this.usuario = "usuario";
        this.combate = 0;
        this.cantidad = 0;
        this.cuota = 0;
        this.ganador="Indefinido";
        this.puntuacion=-1;
        this.tipoVictoria=TipoViiictoria.JAVI;
    }

    //por parametros simple
    public Apuesta(String usuario, int combate, float cantidad, float cuota) {
        this();
        this.usuario = usuario;
        this.combate = combate;
        this.cantidad = cantidad;
        this.cuota = cuota;
    }

    //por parametros
    public Apuesta (String usuario, int combate, float cantidad, float cuota, String ganador) {
        this();
        this.usuario = usuario;
        this.combate = combate;
        this.cantidad = cantidad;
        this.cuota = cuota;
        this.ganador=ganador;
    }
    public Apuesta (String usuario, int combate, float cantidad, float cuota, int puntuacion) {
        this();
        this.usuario = usuario;
        this.combate = combate;
        this.cantidad = cantidad;
        this.cuota = cuota;
        this.puntuacion=puntuacion;
    }
    public Apuesta (String usuario, int combate, float cantidad, float cuota,  TipoViiictoria tipo) {
        this();
        this.usuario = usuario;
        this.combate = combate;
        this.cantidad = cantidad;
        this.cuota = cuota;
        this.tipoVictoria=tipo;
    }

    //Getters y Setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getCombate() {
        return combate;
    }

    public void setCombate(int combate) {
        this.combate = combate;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public float getCuota() {
        return cuota;
    }

    public void setCuota(float cuota) {
        this.cuota = cuota;
    }

    public String getGanador() { return ganador; }

    public void setGanador(String ganador) { this.ganador = ganador; }

    public int getPuntuacion() { return puntuacion; }

    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public TipoViiictoria getTipoVictoria() { return tipoVictoria; }

    public void setTipoVictoria(TipoViiictoria tipoVictoria) {this.tipoVictoria = tipoVictoria;}

}
