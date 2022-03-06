!!!!!!!!!!!!!!!!!!! PLEASE READ THE ENTIRE DOCUMENT. !!!!!!!!!!!!!!!!!!!

As of 03/05/2022 CSVLibrary+app works as intended.

-------------------------------------------- Hi ! -------------------------------------------------------

Please check assignment_documents for other relevant documents.

---------------------------------------------------------------------------------------------------------




State of the Applications Address ;)


Library:
____________

CSVLibrary is functioning nicely. Some bugs were discovered while I was implementing CSVApp, but I believe I've since resolved them.
I am not confident in it's ability to handle "all" errors as detailed in the initial requirements. Looking back at that statement, it was silly. 
Though I can prevent the program from "crashing", I cannot always fix the malformed documents. 

I am happy with how the function system came out, both the hard-coded functions and the reflection 
interface to allow for developers to add their own functions into the calculation handler. 
I am also pleased with how the parser came out. Dynamic cell references and dynamic cell ranges work as I expected them to!
I will say, I feel like I got carried away here.



Take a look at external-functino-example-code for an example of a reflected function!

The delimiter handling worked nicely when tested. I didn't expect it to work well when changing the delimiter, but it seems to work fine! 
It can go back and check for previously escaped delimiters and remove the escaping since it no longer needs the escaping. 


App:
___________

CSVApp came out successfully. There were some changes from the initial prospective requirements. 
Caching files was not implemented, however the state of the spreadsheet does not live in  the GUI, but the CLI. 

So if the GUI crashes you can still save your work via the CLI. I did not get to implement as many GUI popup
warnings as I wanted to, however, I feel the warnings in place are sufficient. I removed the requirement for live 
updating calculations. Since it IS a CSV application, I can't do as many dynamic things as I would like. 

BUT! Calculations results are still given to the user on the status bar at the bottom of the screen, as well
as the Row and column of the selected cell. On regular cells the status bar will show the current full value within 
the cell, however on equations the status bar will show the equation result. 



---------------------------------------------------------------------------------------------------------

New GUI works in conjunction with CLI!

The CLI and GUI share the same spreadsheet state. Allowing actions to be performed in both at the same time.

Example:
> load 1000 Sales Records.csv
> set 0 0 Testing 1 2 3
> gui


To evaluate functions in the GUI, select the function cell and the evaluated value will appear on the status bar at the bottom of the GUI.

---------------------------------------------------------------------------------------------------------

New way of running!

Verbose Error Logging: java -jar csvapp.jar verbose
Limited Logging: java -jar csvapp.jar

---------------------------------------------------------------------------------------------------------

Added functions via reflection! (See CLI entry below for more information)
- To load external functions, there must be a "functions" directory in the same root as the .jar file or the source code.
- External functions must depend on CSVLibrary and extend "ReflectableFunction"
- Within the external function code, there must be a function.yml file in the root src/ directory. The file must looks like this:

name: [FUNCTION NAME FOR REFERENCE IN CALCULATIONS ex: PYTHAG]
main: [PATH OF FUNCTION CLASS FILE ex: net.Andrewcpu.pythag.PythagoreanTheorem]

- External functions are loaded by net.andrewcpu.calculation.reflectable.Bootloader
- External functions can then be compiled to a jar and placed into the "functions" directory mentioned above.
- Upon loading a spreadsheet the functions directory will be iterated through and external functions will be loaded into the CalculationParser.
- External functions are now accessible from calculations!


An example external function jar has been compiled and placed in /functions/, it's source code has been placed in /external-function-example-code/


---------------------------------------------------------------------------------------------------------

Added CLI, here are steps to use it: (or you could run the "help" command and see everything for yourself!)
> load 1000 Sales Records.csv
You will see a notice that functions are being loaded from root directory. (the functions/ folder)
You will see all functions within the spreadsheet be calculated.

> print
> reload
Not required for any functionality, however it will show you what the function does.

> get 0 0
> get 0 0 11 11
> set 0 0 Test Words
> fill 0 0 5 0 Test Words
> calculate all
> calculate =DISTANCE(0, 0, 5, 5)
The above function comes from an externally compiled jar using reflection. It is the distance between two points, (x1, y1, x2, y2)

> gui
The above command will open a Swing GUI with your CLI state.
If you've already loaded a spreadsheet, and run the GUI command you will skip the file & delimiter picker.
Otherwise, if you run the gui command before doing anything else you can go through a gui file picker and avoid the CLI almost entirely.
Also note, you can use both the GUI and CLI at the same time. You can flip flop back and forth between the GUI & the CLI and the state will be shared.

> save NewFile.csv
> exit

Added verbose error logging mode:
Must launch program with argument "verbose".
Note: Without "verbose" errors will be truncated to their localized message.

---------------------------------------------------------------------------------------------------------

Inline Functions!

Functions will always start with an equal symbol!

Hardcoded functions:
- Absolute Value: ABS(NUM)
- Addition: ADD(X1, X2, X3, ..., Xn)
- Division Function: DIV(X1, X2, X3, ..., Xn)
    X1 is divided by X2 which is divided by X3, etc.
- Exponent: POW(NUM, EXPONENT)
- Multiplication: MULT(X1, X2, X3, ..., Xn)
- Subtraction: SUB(X1, X2)
    X1 - X2
- Sum: SUM(X1, X2, X3, ..., Xn)
    Equivalent to ADD

How to reference a cell?:
Excel uses letters & numbers for their references in xlsl files. However, I decided to stick with the format of ROW:COLUMN to reference a cell.
For example, the top-left most cell is called 0:0, one below that is 1:0.

How to reference multiple cells?:
You can reference multiple cells with a dash! If you've loaded up 1000 Sales Records.csv you will notice at the very bottom in the last row and last column I've included an example function.
=DIV(ADD(1:11-50:11), ADD(51:11-101:11))

^ This function will sum the first 49 rows (without title bar) and divide it by the next 50 rows.

Functions can be written within the CSV file, or they can be calculated via the CLI, 
example: "calculate =DIV(ADD(1:11-50:11), ADD(51:11-101:11))"

Range Definition: ROW1:COL1-ROW2:COL2


---------------------------------------------------------------------------------------------------------

Known issues:
- Recursive formulas can freeze processing, example: =ADD(1:1,2:2) in position Row: 1, Column 1, and =ADD(1:1,2:2) in position Row: 2, Column: 2.
    - They will just go back and forth and never execute.