package edu.thepower.u3comunicacionred;

class Validacion {
    public static int validarPuerto(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Debe ingresar un único argumento");
        }
        int puerto = Integer.parseInt(args[0]);
        if (puerto < 1024 || puerto > 65535) {
            throw new IllegalArgumentException("Debe ingresar un puerto entre 1024 y 65535");
        }
        return puerto;
    }
}
