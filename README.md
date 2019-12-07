# How to Use Our Files

## TSPSolver.java ##
For TSPSolver.java, we implemented the JGraphT framework. To use this framework proceed as follows:
1. Open up your favorite java editor (we used IntelliJ) and usen maven or another such tool to download JGraphT. For more info, visit: https://jgrapht.org/
2. Run the main function with the following specifications:
  a) If you which to run Christophides, uncomment the run_chris()call and specify the correct file inside the two scanners and one buffered reader
  b) Same as a) but function run_double() for the Two Approximation Metric TSP
  c) Same as a) but function run_karp() for the Held Karp TSP solver
3. Outputs will be put in the file you specified that can now be used in the main file. 

## main.py ##
main.py contains a variety of different functions. To run each of these functions, copy the function and input the 
necessary inputs (usually file paths which are stored in constants on the top of the file. Then, you can run the code for desired effect. 

There main sets of algorithm functions fall under:

Christofides: includes a output generator for TSPSolver, parser to read from TSPSolver's outputs
Christofides_R2: includes a output generator for TSPSolver, parser to read from TSPSolver's outputs
Karp: includes a parser to read from TSPSolver's outputs
Double Approximation: includes a parser to read from Double's outputs and a double_r2 functionality and a combiner with bouncer
Bouncer: includes a output generator for TSPSolver, parser to read from TSPSolver's outputs
Bounce_Bounce: includes a output generator for TSPSolver, parser to read from TSPSolver's outputs
R2: calls rule of two function and reads data

Combine: get the best output files for each algorithm



