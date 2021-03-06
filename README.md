# Correlated Iterators Processor

The goal of this module is to offer a convenient and efficient way to iterate over correlated data contained within multiple sorted iterators.

Each sequential iteration of the resulting sorted stream allows the processing of the correlated data as a single unit of work. 

## Context

It is frequent for banking institutions to make available flat CSV files containing account information such as positions, transactions and/or other account information. Those files are usually sorted by account number and can become too large to mount entirely in memory.

Using **Correlated Iterators Processor**, it is possible to open a streamed iterator on those different files and process all the data related to an account as a chunk.

## Example

Consider the two following data sets:

  - Data Set 1

| Key  | Value |
| --- | --- |
| B | value11 |
| C	| value12 |
| C	| value13 |
| D	| value14 |
| D	| value15 |
| E	| value16 |

  - Data Set 2

| Key  | Value |
| --- | --- |
| A | value21 |
| A	| value22 |
| C	| value23 |
| C	| value24 |
| C	| value25 |
| D	| value26 |
| G	| value27 |

Opening an iterator on those two streams and running them through CIP using **Key** as the **CorrelationKey** would allow the following processing:

| Key  | Data Set 1 Values | Data Set 2 Values |
| --- | --- |  --- |
| A | | value21, value22 | 
| B | value11 | | 
| C | value12, value13 | value23, value24, value25 | 
| D | value14, value15 | value26 | 
| E | value16 |  | 
| G |  | value27 | 

The corresponding java code would be:

```java
CorrelatedIterables.correlate(
    dataSet1.iterator(), EntryA.class,
    dataSet2.iterator(), EntryB.class,
    new CorrelationDoubleStreamConsumer<String, EntryA, EntryB>() {
        @Override
        public void consume(String key, List<EntryA> aElements, List<EntryB> bElements) {
            //process the chunk
        }
    }
);
```



## Usage

**Maven dependency:**
```
<dependency>
  <groupId>com.teketik</groupId>
  <artifactId>cip</artifactId>
  <version>1.0</version>
</dependency>
```

**Main classes:**

[CorrelatedIterables](src/main/java/com/teketik/cip/CorrelatedIterables.java) contains a collection of convenient iterators to process multiple correlated iterators. If this does not contain what you need, have a look at [CorrelatedIterable](https://github.com/antoinemeyer/correlated-iterators-processor/blob/master/src/main/java/com/teketik/cip/CorrelatedIterable.java).

The java classes iterated should contain a field annotated with [@CorrelationKey](src/main/java/com/teketik/cip/CorrelationKey.java) that will be used to find the correlations within all the iterators.



