# JamaScript
| Members|
|-|
|Alan Hernandez|
|Jonathan Gomez|
|Angelica De Leon|
|Megan Ngo|

# Syntax
```
var is a variable
classname is the name of a class
methodname is the name of a method
string is a string
int is an integer

type ::= `Boolean` | `String` | `Int` | `class`

op ::= `+` | `-` | `*` | `/` | `>` | `<` | `>=` | `<=` | `==`

exp ::= exp op exp |
        var | int | string |
        true | false |
        exp.methodname(exp*)|
        new classname(exp*)

vardec ::= type var

stmt ::= vardec `=` exp |
        while (exp) stmt |
        if (exp)stmt else stmt; |
        { stmt* } |
        println(exp)|
        return (exp) 

methoddef ::=  type methodname(vardec*) stmt
 
classdef ::= class classname extends classname{
                vardec*
                constructor(vardec*){
                    super(exp*)|                              
                    stmt* 
                }
                methoddef*
            }

program ::= classdef* stmt
```