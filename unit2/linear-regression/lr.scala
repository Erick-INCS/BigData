////////////////////////////////////////////
//// LINEAR REGRESSION EXERCISE ///////////
/// Coplete las tareas comentadas ///
/////////////////////////////////////////

// Import LinearRegression
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.{Dataset}

// Opcional: Utilice el siguiente codigo para configurar errores
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)


// Inicie una simple Sesion Spark
val spark = SparkSession.builder().getOrCreate()

// Utilice Spark para el archivo csv Clean-Ecommerce.
// Bonus: Downloading file if not exists
val file_name = "data.csv"
if (!new java.io.File(file_name).isFile) {
  val url = "https://raw.githubusercontent.com/jcromerohdz/BigData/master/Spark_Regression/Clean-Ecommerce.csv"
  val content = scala.io.Source.fromURL(url).mkString
  new java.io.PrintWriter(file_name) { write(content); close }
}

val data  = spark.
              read.
              option("header","true").
              option("inferSchema", "true").
              format("csv").
              load(file_name)

// Imprima el schema en el DataFrame.
data.printSchema

// Imprima un renglon de ejemplo del DataFrane.
data.head(1)


//////////////////////////////////////////////////////
//// Configure el DataFrame para Machine Learning ////
//////////////////////////////////////////////////////

// Transforme el data frame para que tome la forma de
// ("label","features")

// Importe VectorAssembler y Vectors
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors

// Renombre la columna Yearly Amount Spent como "label"
val df = data.select(data("Yearly Amount Spent").as("label"), $"Time on Website")

// Tambien de los datos tome solo la columa numerica 
// Deje todo esto como un nuevo DataFrame que se llame df

// Que el objeto assembler convierta los valores de entrada a un vector
val assembler = new VectorAssembler().setInputCols(Array("Time on Website"))


// Utilice el objeto VectorAssembler para convertir la columnas de entradas del df
// a una sola columna de salida de un arreglo llamado  "features"
// Configure las columnas de entrada de donde se supone que leemos los valores.
// Llamar a esto nuevo assambler.
val nuevo_assembler = assembler.setOutputCol("features")

// Utilice el assembler para transform nuestro DataFrame a dos columnas: label and features
val out = nuevo_assembler.transform(df).select($"label", $"features")

// Crear un objeto para modelo de regresion linea.
val lr = new LinearRegression()


// Ajuste el modelo para los datos y llame a este modelo lrModelo
val lrModel = lr.fit(out)

// Imprima the  coefficients y intercept para la regresion lineal
lrModel.coefficients
lrModel.intercept

// Resuma el modelo sobre el conjunto de entrenamiento imprima la salida de algunas metricas!
// Utilize metodo .summary de nuestro  modelo para crear un objeto
// llamado trainingSummary
val trainingSummary = lrModel.summary

// Muestre los valores de residuals, el RMSE, el MSE, y tambien el R^2 .
trainingSummary.resuduals.show()
trainingSummary.predictions.show()
trainingSummary.r2 //variaza que hay 
trainingSummary.rootMeanSquaredError


// Buen trabajo
