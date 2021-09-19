// 1. Desarrollar un algoritmo en scala que calcule el radio de un circulo

import math.Pi
import util.control.Breaks._

def cradio(circunferencia:Double): Double = {
  circunferencia + 10
}

val testVar = 9.4
print(s"\nRadio de un circulo con circunferencia $testVar: ${cradio(testVar)}\n")


//2. Desarrollar un algoritmo en scala que me diga si un número es primo
def esPrimo(num:Int):Boolean = {
  Range(2, num).map(n => num % n != 0).reduce((a,b) => a && b)
}

print(s"\n9 es primo -> ${esPrimo(9)}")
print(s"\n11 es primo -> ${esPrimo(11)}\n")

//3. Dada la variable  var bird = "tweet", utiliza interpolación de strings para
//    imprimir "Estoy ecribiendo un tweet"
var bird = "tweet"
print(s"\nEstoy escribiendo un $bird.\n")

//4. Dada la variable var mensaje = "Hola Luke yo soy tu padre!" utiliza slice para extraer la
//    secuencia "Luke"
var mensaje = "Hola Luke yo soy tu padre!"
print("\n")
print(
  mensaje.slice(
    mensaje.indexOf(" ") + 1,
    mensaje.indexOf(" ", mensaje.indexOf(" ") + 1),
  ))
print("\n")

//5. Cúal es la diferencia entre value (val) y una variable (var) en scala?
//> Las variables declaradas con "val" son constantes y su valor no pude cambiar
//> sin redeclarar la variable, mientras que el valor de las variabes declaradas
//> con "var" puede ser modificado en cualquier momento

//6. Dada la tupla (2,4,5,1,2,3,3.1416,23) regresa el número 3.1416
val tup = (2, 4, 5, 1, 2, 3, 3.1416, 23)
print(s"\n${tup._7}\n")
