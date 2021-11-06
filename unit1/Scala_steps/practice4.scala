// 1. Versión recursiva descendente
def fib1(n:Integer): Integer = {
  if (n < 2) {
    return n
  }
  fib1(n - 1) + fib1(n - 2)
}

val n = 32
print(s"\nAlgoritmo 1: fib($n) = ${fib1(n)}\n\n")

// 2. Versión con fórmula explícita
val Phi = (1 + math.sqrt(5)) / 2

def fib2(n:Integer): Double = {
  if(n < 2){
    return n.toDouble
  }
  (math.pow(Phi, n.toDouble) - math.pow(1 - Phi, n.toDouble)) / math.sqrt(5).toDouble
}
print(s"\nAlgoritmo 2: fib($n) = ${fib2(n)}\n\n")

// 3. Versión iterativa
def fib3(n:Integer): Integer = {
  var a = 0
  var b = 1
  var c: Integer = null
  for (k <- Range(0, n)) {
    c = b + a
    a = b
    b = c
  }
  a
}
print(s"\nAlgoritmo 3: fib($n) = ${fib3(n)}\n\n")

// 4. Versión iterativa de 2 variables
def fib4(n:Integer): Integer = {
  var a = 0
  var b = 1

  for (k <- Range(0, n)) {
    b = b + a
    a = b - a
  }
  a
}
print(s"\nAlgoritmo 4: fib($n) = ${fib4(n)}\n\n")

// 5. Versión iterativa vector
def fib5(n:Integer): Integer = {
  if(n < 2){
    return n
  }

  val vec = collection.mutable.Seq.fill(n + 1)(0)
  vec(1) = 1
  for (k <- Range(2, n + 1)) {
    vec(k) = vec(k - 1) + vec(k - 2)
  }
  vec(n)
}
print(s"\nAlgoritmo 5: fib($n) = ${fib5(n)}\n\n")