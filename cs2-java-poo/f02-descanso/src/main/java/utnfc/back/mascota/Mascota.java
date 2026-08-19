package utnfc.back.mascota;

public class Mascota{
    private String nombre;
    private boolean dormida;

    public Mascota(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public boolean isDormida(){
        return this.dormida;
    }

    public void dormir(){
        this.dormida = true;
    }

    public void despertar(){
        this.dormida = false;   
    }

   public boolean  respondeA(String nombre){
    if(this.isDormida()){
        //No va a responder a nada
        return false;
    }else{
        return this.nombre.equals(nombre);
    }
   }

    @Override
    public String toString(){
        return "Soy "+this.nombre;
    }
}