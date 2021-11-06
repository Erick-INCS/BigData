import org.apache.spark.sql.SparkSession

// 1. Loading a simple spark session
val spark = SparkSession.builder().getOrCreate()
val dfPath = "../Datasets/Netflix_2011_2016.csv"

// 2. Loading the data
val df = spark.read.
  option("header", "true").
  option("inferSchema", "true")csv(dfPath)

// 3. What are the column names?
print(s"Columns: ${df.columns.mkString(", ")}\n\n")

// 4. How is the schema?
df.printSchema()

// 5. Print the first 5 columns
val columns = df.columns.take(5)
df.select(columns.head, columns.tail:_*).show(3)

// 6. Using "describe" to learn more about the DataFrame
df.describe().show()

// 7. Creating the "High-Volume Ratio" column
val df2 = df.withColumn("HV Ratio", col("High")/col("Volume"))
df2.select("High", "Volume", "HV Ratio").show(5)

// 8. Day with the max value of "Open"
val maxOpenDay = df.orderBy(desc("Open")).select("Date").first.get(0)
print(s"Max value of 'Open' finded in: $maxOpenDay\n\n")

// 9. Meaning of "Close" column
// The closing price is the last price of that stock registered during that day.

// 10. Maximum and Minumum value of the "Volume" column
df.select(max("Volume"), min("Volume")).show()

// 11. With the "$" syntax:
// 11.1 How many days are there with "Close" lower than $600?
val days = df.where($"Close" < 600).count()
print(s"There are $days days with a 'Close' price lower than $$600\n\n")

// 11.2 What percentage of the time was "High" greater than $ 500?
val days = df.where($"High" > 500).count().toFloat
print(s"${days / df.count() * 100}% of the time 'High' was higher than $$500\n\n")

// 11.3 Pearson correlation between "High" and "Volume"
print("Pearson correlation between 'High' and 'Volume'\n")
df.select(corr("High", "Volume")).show()

// 11.4 Max value of 'High' per year
print("Max value of 'High' by year\n")
df.groupBy(year($"Date")).max("High").show()

// 11.5 Mean of 'Close' by calendar month
val df3 = df.groupBy(year($"Date"), month($"Date")).
  mean("Close").
  toDF("Year", "Month", "Mean")

df3.orderBy($"Year", $"Month").show()
