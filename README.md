## Sorted file merger
DISCLAIMER: A friend of mine completed the task as a test for Junior Java Developer position.
I decided it can be a good starting point for me to learn basics of Java.

### Problem statement
Write a console application that merges multiple sorted files into one. The application shall be able to work with
files that do not fit into RAM. Input files may contain out-of-order values, that shall be discarded.
Application shall accept following arguments:
1. Sort type (required): `-s` or `-i` for string and integer respectively.
2. Sort order (optional): `-a` or `-d` for ascending and descending order respectively. Default is `-a`
3. List of arguments, first being output file name and the rest is input file name.

Example: `<application> -s -d output.txt input1.txt input2.txt input3.txt`

### Build and Run
```
# Test
$ mvn test

# Create sample input
$ cat << EOF > file1
97
1
98
100
EOF

$ cat << EOF > file2
31
1
32
200
EOF

# Build and Run
$ mvn compiler:compile
$ mvn exec:java exec:java -Dexec.mainClass=Main -Dexec.args="-i output file1 file2"

# Check output
$ cat output
```
