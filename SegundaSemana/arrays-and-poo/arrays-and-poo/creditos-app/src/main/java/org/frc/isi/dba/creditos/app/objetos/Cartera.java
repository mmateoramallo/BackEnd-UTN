package org.frc.isi.dba.creditos.app.objetos;

public class Cartera {

    //Arreglo de tipo cuentas
    private Cuenta[] listaCuentas = new Cuenta[10];

    public Cartera(Cuenta[] cuentas) {
        //Verificar que las cuentas sea mayor a 0
        if (cuentas.length > 0) {
            this.listaCuentas = cuentas;
        }
    }

    //Agregar cuenta a  la cartera
    public void agregarCuenta(Cuenta cuenta) {
        boolean agregado = false;
        if (cuenta != null) {
            for (int i = 0; i < listaCuentas.length; i++) {
                if (listaCuentas[i] == null) {
                    listaCuentas[i] = cuenta;
                    agregado = true;
                    break;
                }
            }
        }
        if (!agregado) {
            System.out.println("La cartera está llena");
        }
    }

    //Buscar una cuenta en particular
    public Cuenta buscarCuenta(Cuenta cuentaABuscar) {
        //Recorrer la lista de cuenta
        for (int i = 0; i < listaCuentas.length; i++) {
            if (listaCuentas[i].equals(cuentaABuscar)) {
                return listaCuentas[i];
            } else {
                System.out.println("Cuenta no encontrada");
            }
        }
        return null;
    }

    public int searchByCode(String codigo) {
        for (int i = 0; i < listaCuentas.length; i++) {
            if (listaCuentas[i].getCodigo().equals(codigo)) {
                return i;
            } else {
                System.out.println("Cuenta no encontrada");
            }
        }
        return -1;
    }

    //Devolver cantidad de cuentas
    public int totalCuentas() {
        int total = 0;
        for (int i = 0; i < listaCuentas.length; i++) {
            if (listaCuentas[i] != null) {
                total++;
            }
        }
        return total;
    }

    //Obtener un elemento i del arreglo
    public Object obtenerElemento(int i) {
        for (int j = 0; j < listaCuentas.length; j++) {
            if (listaCuentas[i] != null) {
                return listaCuentas[i];
            }
        }
        System.out.println("Elemento no encontrado");
        return null;
    }
}
