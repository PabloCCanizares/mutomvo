# MuTomVo-C: MUTatiOn testing fraMework designed for supercomputer enVirOnments

MuTomVo-C is a lightweight, standalone mutation testing framework designed for measuring the suitability of test suites in the C programming language.

For this, it supports a suite of X mutation operators, and a mechanism that allows the user to design its own mutation operators based on program API. With respect to the test generator, MuTomVo-C provides a random test generator engine that aids to create test cases easily.

As a novelty, this framework focuses on providing all the mechanisms necessaries to be deployed and executed in supercomputer environments.

#### Mutation Operators

Traditional mutation operators:
* AORb: Arithmetic operator replacement
* AORs:
* AOIu:
* AODs:
* ROR:
* LOI:
* COR:
* COI:
* COD:
* ASOR:

API-based mutation operators:
* Method Call Deletion: This operator removes calls to methods included in the APIs provided by the user.
* Method Call Replacement: This kind of operators is focused on the replacement of a method call with
a different one that presents the same number and type of parameters.
* Method Call Move Up: 
* Method Call Move Down:
* Method Shuffling Parameters

#### GUI
The framework integrates both, a GUI and a comman-line interface for allows user to interact in graphical and non-graphical environments.

#### Reports
MuTomVo-C provides an API for automatically generating graphs and reports.


#### Other features
In addition, MuTomVo-C supports the following features:
* Trivial compiler analysis.
* Subsuming analysis.

