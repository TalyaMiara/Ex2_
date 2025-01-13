Ex2 Introduction
This assignment focuses on the foundation of object-oriented design and programming.

This project implements a basic spreadsheet application. The spreadsheet simulates a 2D grid of cells, with each cell capable of holding one of the following:

Text (e.g., "hello")
Number (e.g., 42, 3.14)
Formula (e.g., =1+2, =(A1+2)*3, =B2/2)
The program validates formulas, resolves cell references, and computes results using object-oriented principles and recursion.

Features
Cell Types
Text: Any value that does not represent a valid number or formula (e.g., "hello" or "123a").
Number: A valid numeric value (int or double) like 123 or -3.14.
Formula: Starts with = and can include:
Basic arithmetic operations (+, -, *, /).
Parentheses for precedence (e.g., =(1+2)*3).
References to other cells (e.g., =A1+B2).
Formula Validation
Valid formulas include:
=1
=(2+3)*4
=A1+B2/2
Invalid formulas include:
a, AB, =(), =5**, or cyclic references (A1:A1).
Error Handling
The program identifies and flags errors such as:

Invalid formulas (ERR_WRONG_FORM).
Cyclic dependencies (ERR_CYCLE).
Formula evaluation errors (ERR_FORM).
Core Functionality
Cell Operations:
Set a cell's content (text, number, or formula).
Get a cell's content or computed value.
Spreadsheet Operations:
Evaluate all cells (eval()).
Compute the computational depth of each cell (depth()).


<img width="960" alt="gui" src="https://github.com/user-attachments/assets/89aa7080-99d8-431a-9ec3-b801d26bb8b8" />
