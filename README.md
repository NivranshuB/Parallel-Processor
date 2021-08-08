# Softeng 306 Project 1 - Team 13
This project is about using artificial intelligence (AI) and parallel processing power to solve a
difficult scheduling problem.

The program accepts a .dot file format graph with integer node and edge weights, and outputs a dot graph with an 
assigned start time and processor.

## Team members
| Name               | GitHub Username |   UPI   |
|--------------------|:---------------:|---------|
| Nivranshu Bose     | nbos443         | nbos443 |
| Joe Ee Cheong      | JoeCheong23     | jche545 |
| Corban Draper      | cdra334         | cdra334 |
| Calvin Kart        | ckar364         | ckar364 |
| SV Singh           | sv-singh        | ssin610 |

## Running the program
Have the scheduler.jar and the valid .dot file format graph in the same directory. 

In the command line, input the following:


```
java -jar scheduler.jar INPUT.dot P [OPTION]
```

INPUT.dot &emsp;&emsp;a task graph with integer weights in .dot file format<br>
P &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;the number of processors to schedule the INPUT.dot graph on

Optional:<br>
-p N &emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;where N is the number of cores to execute in parallel (default is 1)<br>
-v &emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;enable visualisation of the search (default is disabled)<br>
-o OUTPUT &emsp;output file name is OUTPUT.dot (default is INPUT-output.dot)<br>