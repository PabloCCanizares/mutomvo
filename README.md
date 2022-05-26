# MuTomVo-C: MUTatiOn testing fraMework designed for supercomputer enVirOnments

MuTomVo-C is a lightweight, standalone mutation testing framework designed for measuring the suitability of test suites in the C programming language.

For this, it supports a suite of X mutation operators, and a mechanism that allows the user to design its own mutation operators based on program API. With respect to the test generator, MuTomVo-C provides a random test generator engine that aids to create test cases easily.

As a novelty, this framework focuses on providing all the mechanisms necessaries to be deployed and executed in supercomputer environments.

#### Mutation Operators

Traditional mutation operators:
* **AORb**: Replaces basic binary arithmetic operators with other binary arithmetic operators.
* **AORs**: Replaces short-cut arithmetic operators with other unary arithmetic operators.
* **AOIu**: Inserts basic unary arithmetic operators.
* **AODs**: Deletes short-cut arithmetic operators.
* **ROR**: Replaces relational operators with other relational operators, and replace the entire predicate with true
and false.
* **LOI**: Inserts unary logical operator.
* **COR**: Replaces binary conditional operators with other binary conditional operators
* **COI**: Inserts unary conditional operators.
* **COD**: Deletes unary conditional operators.
* **ASOR**:  Replaces short-cut assignment operators with other short-cut operators of the same kind.

API-based mutation operators:
* **Method Call Deletion**: This operator removes calls to methods included in the APIs provided by the user.
* **Method Call Replacement**: This kind of operators is focused on the replacement of a method call with
a different one that presents the same number and type of parameters.
* **Method Call Move Up**:  Shift method calls up.
* **Method Call Move Down**: Shift method calls down.
* **Method Shuffling Parameters**: This operator  involves swapping actual parameters in methods calls. These changes can be applied in those functions in which the types of the parameters are equal.

#### GUI
The framework integrates both, a GUI and a comman-line interface for allows user to interact in graphical and non-graphical environments.

#### Reports
MuTomVo-C provides an API for automatically generating graphs and reports.


#### Other features
In addition, MuTomVo-C supports the following features:
* Trivial compiler analysis.
* Subsuming analysis.
* Hyperthreading.
* Avoiding mutations in specific lines.
