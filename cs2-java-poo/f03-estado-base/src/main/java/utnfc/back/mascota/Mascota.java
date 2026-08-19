package utnfc.back.mascota;

public class Mascota {

    private String nombre;
    private int energia;
    private int humor;
    private boolean dormida;

    public Mascota(String nombre) {
        this(nombre, 50, 3);
    }

    public Mascota(String nombre, int energia, int humor) {
        this.nombre = nombre;
        this.setEnergia(energia);
        this.setHumor(humor);
        this.dormida = false;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEnergia() {
        return this.energia;
    }

    private void setEnergia(int energia) {
        if (energia < 0) {
            this.energia = 0;
        } else if (energia > 100) {
            this.energia = 100;
        } else {
            this.energia = energia;
        }
    }

    public String getHumor() {
        return switch (this.humor) {
            case 1 ->
                "Muy enojado";
            case 2 ->
                "Enojado";
            case 3 ->
                "Neutral";
            case 4 ->
                "Contento";
            case 5 ->
                "Chocho";
            default ->
                "Desconocido";
        };
    }

    private void setHumor(int humor) {
        if (humor < 1) {
            this.humor = 1;
        } else if (humor > 5) {
            this.humor = 5;
        } else {
            this.humor = humor;
        }
    }

    public boolean isDormida() {
        return this.dormida;
    }

    public void dormir() {
        if (this.dormida) {
            return;
        }

        this.dormida = true;
        this.setEnergia(this.energia + 25);
        this.setHumor(this.humor + 2);
    }

    public void despertar() {
        if (!this.dormida) {
            return;
        }

        this.dormida = false;
        this.setHumor(this.humor - 1);
    }

    public boolean respondeA(String nombre) {
        if (this.dormida) {
            return false;
        }

        return this.nombre.equals(nombre);
    }

    public boolean comer() {
        if (this.dormida) {
            return false;
        }

        int incrementoEnergia = (int) (this.energia * 0.10);
        this.setEnergia(this.energia + incrementoEnergia);
        this.setHumor(this.humor + 1);
        return true;
    }

    public boolean beber() {
        if (this.dormida) {
            return false;
        }

        int incrementoEnergia = (int) (this.energia * 0.05);
        this.setEnergia(this.energia + incrementoEnergia);
        this.setHumor(this.humor + 1);
        return true;
    }

    public boolean correr() {
        if (this.dormida) {
            return false;
        }

        int consumoEnergia = (int) (this.energia * 0.35);
        this.setEnergia(this.energia - consumoEnergia);
        this.setHumor(this.humor - 2);
        return true;
    }

    public boolean saltar() {
        if (this.dormida) {
            return false;
        }

        int consumoEnergia = (int) (this.energia * 0.15);
        this.setEnergia(this.energia - consumoEnergia);
        this.setHumor(this.humor - 2);
        return true;
    }

    @Override
    public String toString() {
        return """
                {
                  "nombre": "%s",
                  "energia": %d,
                  "humor": "%s",
                  "dormida": %b
                }""".formatted(
                this.nombre,
                this.energia,
                this.getHumor(),
                this.dormida
        );
    }
}
