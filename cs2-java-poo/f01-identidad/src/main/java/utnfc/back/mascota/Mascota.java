package utnfc.back.mascota;

public class Mascota{
    private String nombre;

    public Mascota(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    @Override
    public String toString(){
        return "Soy "+this.nombre;
    }
}