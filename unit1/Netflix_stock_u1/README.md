## Netflix stocks data exploration

For this data exploration exersice the following actions were carried out:

#### Loading a simple spark session
```scala
val spark = SparkSession.builder().getOrCreate()
```

#### Loading the data
```scala
val dfPath = "../Datasets/Netflix_2011_2016.csv"
val df = spark.read.
  option("header", "true").
  option("inferSchema", "true")csv(dfPath)
```

#### What are the column names?
```scala
print(s"Columns: ${df.columns.mkString(", ")}\n\n")
```
`Columns: Date, Open, High, Low, Close, Volume, Adj Close`

#### How is the schema?
```scala
df.printSchema()
```
```
root
 |-- Date: string (nullable = true)
 |-- Open: double (nullable = true)
 |-- High: double (nullable = true)
 |-- Low: double (nullable = true)
 |-- Close: double (nullable = true)
 |-- Volume: integer (nullable = true)
 |-- Adj Close: double (nullable = true)
```

#### Print the first 5 columns
```scala
val columns = df.columns.take(5)
df.select(columns.head, columns.tail:_*).show(3)
```
```
+----------+----------+------------------+----------+----------+
|      Date|      Open|              High|       Low|     Close|
+----------+----------+------------------+----------+----------+
|2011-10-24|119.100002|120.28000300000001|115.100004|118.839996|
|2011-10-25| 74.899999|         79.390001| 74.249997| 77.370002|
|2011-10-26|     78.73|         81.420001| 75.399997| 79.400002|
+----------+----------+------------------+----------+----------+
only showing top 3 rows
```

#### Using "describe" to learn more about the DataFrame
```scala
df.describe().show()
```
```
+-------+----------+------------------+------------------+------------------+------------------+--------------------+------------------+
|summary|      Date|              Open|              High|               Low|             Close|              Volume|         Adj Close|
+-------+----------+------------------+------------------+------------------+------------------+--------------------+------------------+
|  count|      1259|              1259|              1259|              1259|              1259|                1259|              1259|
|   mean|      null|230.39351086656092|233.97320872915006|226.80127876251044|  230.522453845909|2.5634836060365368E7|55.610540036536875|
| stddev|      null|164.37456353264244| 165.9705082667129| 162.6506358235739|164.40918905512854| 2.306312683388607E7|35.186669331525486|
|    min|2011-10-24|         53.990001|         55.480001|             52.81|              53.8|             3531300|          7.685714|
|    max|2016-10-24|        708.900017|        716.159996|        697.569984|        707.610001|           315541800|        130.929993|
+-------+----------+------------------+------------------+------------------+------------------+--------------------+------------------+
```

#### Creating the "High-Volume Ratio" column
```scala
val df2 = df.withColumn("HV Ratio", col("High")/col("Volume"))
df2.select("High", "Volume", "HV Ratio").show(5)
```
```
+------------------+---------+--------------------+
|              High|   Volume|            HV Ratio|
+------------------+---------+--------------------+
|120.28000300000001|120460200|9.985040951285156E-7|
|         79.390001|315541800|2.515989989281927E-7|
|         81.420001|148733900|5.474206014903126E-7|
| 82.71999699999999| 71190000|1.161960907430818...|
|         84.660002| 57769600|1.465476686700271...|
+------------------+---------+--------------------+
only showing top 5 rows
```

#### Day with the max value of "Open"
```scala
val maxOpenDay = df.orderBy(desc("Open")).select("Date").first.get(0)
print(s"Max value of 'Open' finded in: $maxOpenDay\n\n")
```
`Max value of 'Open' finded in: 2015-07-14`


#### Meaning of "Close" column
The closing price is the last price of that stock registered during that day.

#### Maximum and Minumum value of the "Volume" column
```scala
df.select(max("Volume"), min("Volume")).show()
```
```
+-----------+-----------+
|max(Volume)|min(Volume)|
+-----------+-----------+
|  315541800|    3531300|
+-----------+-----------+
```

#### With the "$" syntax:
1. How many days are there with "Close" lower than $600?
```scala
val days = df.where($"Close" < 600).count()
print(s"There are $days days with a 'Close' price lower than $$600\n\n")
```
`There are 1218 days with a 'Close' price lower than $600`

2. What percentage of the time was "High" greater than $ 500?
```scala
val days = df.where($"High" > 500).count().toFloat
print(s"${days / df.count() * 100}% of the time 'High' was higher than $$500\n\n")
```
`4.9245434% of the time 'High' was higher than $500`

3. Pearson correlation between "High" and "Volume"
```scala
print("Pearson correlation between 'High' and 'Volume'\n")
df.select(corr("High", "Volume")).show()
```
```
Pearson correlation between 'High' and 'Volume'
+--------------------+
|  corr(High, Volume)|
+--------------------+
|-0.20960233287942157|
+--------------------+
```

4. Max value of 'High' per year
```scala
print("Max value of 'High' by year\n")
df.groupBy(year($"Date")).max("High").show()
```
```
Max value of 'High' by year
+----------+------------------+                                                 
|year(Date)|         max(High)|
+----------+------------------+
|      2015|        716.159996|
|      2013|        389.159988|
|      2014|        489.290024|
|      2012|        133.429996|
|      2016|129.28999299999998|
|      2011|120.28000300000001|
+----------+------------------+
```

5. Mean of 'Close' by calendar month
```scala
val df3 = df.groupBy(year($"Date"), month($"Date")).
  mean("Close").
  toDF("Year", "Month", "Mean")

df3.orderBy($"Year", $"Month").show()
```
```
+----+-----+------------------+                                                 
|Year|Month|              Mean|
+----+-----+------------------+
|2011|   10| 87.11500133333334|
|2011|   11| 79.76380923809522|
|2011|   12| 70.30428566666667|
|2012|    1| 97.75149895000001|
|2012|    2|119.92049895000002|
|2012|    3|113.00181809090908|
|2012|    4|100.88399985000001|
|2012|    5| 72.98772681818181|
|2012|    6| 65.75380899999999|
|2012|    7|  75.2542851904762|
|2012|    8|60.736521347826084|
|2012|    9| 56.57736921052631|
|2012|   10| 65.78095142857143|
|2012|   11| 80.04190514285713|
|2012|   12| 89.40500014999998|
|2013|    1|117.01380985714289|
|2013|    2|182.45684194736842|
|2013|    3|       184.6934989|
|2013|    4|182.83045363636364|
|2013|    5|223.71999777272728|
+----+-----+------------------+
only showing top 20 rows
```
