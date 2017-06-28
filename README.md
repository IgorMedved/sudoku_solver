# sudoku_solver
sudoku_solver for ai nanadegree implemented in java

This current iteration of the design is the combination of my independent effort for writing a sudoku solver with the ideas taken from the AI nanodegree introductory project

At present there are 5 different strategies for solving sudoku
The first 2 

1) limitSolutionsByEliminatingKnownValues
2) selectOnlyChoises are very similar to the ones suggested in the course, but were implemented before consulting the course
3) all_twins similar to naked twin strategy, the idea was taken from the course, however it was extended from twins to triplets of 3 values 4 boxes of 4 values and so on
4) selectMultipleChoice - extra strategy (see description in SudokuNewSolver)
5) onlyRowOrColInSquare - strategy for combining square row with row constraints and square col with col constraints 

To be implemented 
1) search strategy as suggested in the course
2) translate code to python


![ScreenShot](/screenshots/solver1.png)

![ScreenShot](/screenshots/solver2.png)
![ScreenShot](/screenshots/solver3.png)
