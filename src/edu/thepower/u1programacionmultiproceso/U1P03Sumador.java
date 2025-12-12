package edu.thepower.u1programacionmultiproceso;

public class U1P03Sumador {
//programa que nos sume 2 numeros  entre los numeros que recibe el argumento

    private void sumar(int num1, int num2){
        int resultado = 0 ;
        if (num1 > num2){
            int aux = num1;
            num1 = num2;
            num2 = aux;
        }
        for (int i = num1; i <= num2; i++) {
            resultado += i;
        }

        System.out.println(" La suma de los numeros entre " +num1 + " y " + num2+ " es " + resultado);
    }
    public static void main(String[] args) { // String de Arrays sirve para pasar argumentos al Main
        // instanciar la clase
        U1P03Sumador  test  = new U1P03Sumador();
        test.sumar (Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        // ver como pasamos argumentos al programa
    }
}

