# sudoku_solver
sudoku_solver for ai nanadegree implemented in java

I am now pretty happy with the solver. I implemented the following solutions techniques

1) limitSolutionsByEliminatingKnownValues - basic technique that eliminates unique solution values from row, col, and square peers
2) selectOnlyChoises is very similar to the ones suggested in the course, but it was implemented before consulting the course
3) all_twins similar to naked twin strategy, the idea was taken from the course, however it was extended from twins to triplets of 3 values, 4 boxes of 4 values, and so on
4) selectMultipleChoice - extra strategy (see description in SudokuNewSolver.java)
5) rowSquareInteraction 
6) colSquareInteraction
7) squareSquareRowInteraction
8) squareSquareColInteraction
9) swordFish (5-9 see the description at https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php)

When all the above strategies are exhausted and at least 1 box has only two solutions remaining the search strategy is applied recursively which looks for a unique solution. With the search strategy in place even very difficult puzzles can be solved as the one below.

Now the only thing remaining is to convert this code to python



One very hard puzzle from: http://www.telegraph.co.uk/news/science/science-news/9359579/Worlds-hardest-sudoku-can-you-crack-it.html
It is solved with the search tree depth of only 7


![ScreenShot](/screenshots/telegraph.png)

![ScreenShot](/screenshots/solver1.png)

![ScreenShot](/screenshots/solver2.png)
![ScreenShot](/screenshots/solver3.png)




