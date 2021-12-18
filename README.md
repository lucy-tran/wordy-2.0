# Wordy 2.0
**Author: Lucy Tran, Apple Guo** 

[Project write-up link](https://docs.google.com/document/d/19wVOSd1yH1TPdBy2rZNaMeV4Ra4aYI04vq4gyDHJpu0/edit?usp=sharing)

Wordy is a toy programming language for students in Programming Languages class to do science to. Wordy 2.0 is an improvement upon Wordy, with the addition of functions and records.

## Functions
**Creating a function:** We can declare a new function using 
```
Set (functionName) to function of (Input values) in: (function). 
```
![Alt text](/res/Function1AST.png?raw=true "Creating a function AST")
![Alt text](/res/Function1Interpreter.png?raw=true "Creating a function Interpreter")

**Calling a function:** We can call a function using 
```
(functionName) of (Input Values) executed.
```
![Alt text](/res/Function2AST.png?raw=true "Calling a function AST")
![Alt text](/res/Function2Interpreter.png?raw=true "Calling a function Interpreter")


## Records
**Creating a record:** We can create a new record using syntax as 
```
(RecordName) has: (Attributes) End of record. 
```
![Alt text](/res/RecordDemo.png?raw=true "Record AST")

Record is not completed yet. We haven't implemented accessing the values from a record.
