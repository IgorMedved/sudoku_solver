# sudoku_solver
sudoku_solver for ai nanadegree implemented in java

This code was written indpendently without consulting the course
// The first strategy for solving sudoku is like this

//if some number n that was either a part of the initial conditions
                      
// or was found during solution process occurs in a row r and col c 
                        
//  we know for sure that n cannot be present anywhere else
                        
//  where
                        
// 1) row = r, but col != c (in the same row)

// 2) col = c, but row!=r

// 3) row!=r || col!= c of a corresponding 3x3 square

// so we eliminate n from a solution set at these positions

// if during process of elimination one or more solution sets at r1 and c1

// were reduced to size 1 we can repeat the process again

// At the same time if solution sets are reduced to size 0, it means

// that our problem is overconstrained, and no solution is possible 

// without changing the initial conditions

// the second strategy is more time consuming do not run it

// unless there is no choice

// when we exhausted the possibility of advancement using 

// the first strategy we can try another strategy:

// if we look at a solution set in row r, and col c that consists of more than one entry

// but one of those entries x is not present in any of the solutions set in

// 1) row = r, but col!=c

// 2) col = c, but row!=r  

// 3) row!=r && col!= c of a corresponding 3x3 square

// then the solution for row r and col c must be x

To be implemented
1) naked_twin strategy as suggested in the course

2) search strategy as suggested in the course

3) diagonal sudoku


![ScreenShot](/screenshots/solver1.png)

![ScreenShot](/screenshots/solver2.png)
