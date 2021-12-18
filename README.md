# Wordy 2.0
**Author: Lucy Tran, Apple Guo** 

[Project write-up link](https://docs.google.com/document/d/19wVOSd1yH1TPdBy2rZNaMeV4Ra4aYI04vq4gyDHJpu0/edit?usp=sharing)

Wordy is a toy programming language for students in Programming Languages class to do science to. Wordy 2.0 is an improvement upon Wordy, with the addition of functions and records.

## Functions
**Creating a function:** We can declare a new function using 
```
Set <functionName> to function of (<input values separated by ','>) in: <statements>. 
```

Note that you can also return a value from the function. The return statement be preceded by other statements.
```
Set <functionName> to function of (<input values separated by ','>) in: return <expression>. 
```
![Alt text](/res/Function1AST.png?raw=true "Creating a function AST")
![Alt text](/res/Function1Interpreter.png?raw=true "Creating a function Interpreter")

**Calling a function:** We can call a function using 
```
<functionName> of (<input values separated by ','>) executed.
```
![Alt text](/res/Function2AST.png?raw=true "Calling a function AST")
![Alt text](/res/Function2Interpreter.png?raw=true "Calling a function Interpreter")


## Records
**Creating a record:** We can create a new record using syntax as 
```
Set <recordName> to record where: (<record rows, separated by ','>)
```
where a record row is declared by:
```
<variable name> is <expression, or function call>
```
<img width="1000" alt="Screen Shot 2021-12-18 at 6 26 31 AM" src="https://user-images.githubusercontent.com/54861558/146640987-30e55bd3-bf85-4d67-9b7a-6c5d4d0ce9ba.png">

Record is not completed yet. We haven't implemented accessing the values from a record.

