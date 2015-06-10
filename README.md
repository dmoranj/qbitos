# Qbitos: Quantum Mechanics "On the rocks"

## Overview
This library is devoted to the experimentation with the mathematic operations and abstractions of the Quantum Computing and Quantum mechanics fields. It is not expected to be efficient or be part of any real-life system, but to provide some useful tools to study and understand some of the most typical Quantum Computing exercises.

This library uses its own Complex Matrix library, built from scratch, for learning purposes. Every comment on the library usage is appliable to this Math library as well.

The ide of the library is to provide:
* The most common mathematical functions used in Quantum Mechanics.
* A library with the most common operators and quantum gates.
* Some visualization features to show the results and intermediate numbers in a Dirac-like notation.

### A note about Complex Numbers
The Complex Numbers library used within Qbitos assumes that a Complex Number is a Clojure Vector of two real numbers, being the first the real part and the second one the imaginary part.

## Installation
This library is not distributed in any form apart from its Github Repository. In order to use it and complete examples and exercises, just clone the repository as usual.

## Usage
This project is aimed to be used with [Leiningen](http://leiningen.org/); follow the instructions in the link and install it in your system.

All the examples that will be shown in this document will be using lein's REPL. In order to execute the REPL, go to the root folder of the project and type:
```
lein repl
```
The result will be a command line interpreter in your console where you can start to type all the examples shown in the usage description.

### The Complex Matrices API
At the very roots of the Quantum Computation fields there is a whole bunch of mathematical operations, mainly related with vector spaces and complex matrices. This API aims to deal with all this operations in an easey way. BIG IMPORTANT DISCLAIMER: this API is not meant to be efficient (and it doesn't happen to be); its sole purpose is to be used as a didactical resource on the learning of quantum computing and, as such, it has severe limitations on the size of the operations it can execute. As a hint, you better not work with more than six or eight QBits at a time (as you will see, you will be operating with matrices of 2^n range, being n the number of QBits, so you can see this will grow really, really fast).

This API represents matrices as vectors of vectors of vectors:
* each complex number is a vector of two elements, the first one the real part, and the second the imaginary part.
* each matrix is composed of a vector of rows
* each row is composed of a vector of elements

As an example, you can see the range 3 identity matrix:
```
qbitos.core=> (ident 3)
[[[1 0] [0 0] [0 0]] [[0 0] [1 0] [0 0]] [[0 0] [0 0] [1 0]]]
qbitos.core=> 
```
As you can see, there are three rows, with real parts of the main diagonal with value one and the rest of the elements with zero value.

The following sections show the API one operator at a time.

#### (ident r)
Creates an identity matrix of range `r`.
```
qbitos.core=> (ident 3)
[[[1 0] [0 0] [0 0]] [[0 0] [1 0] [0 0]] [[0 0] [0 0] [1 0]]]
qbitos.core=> 
```

#### (null r)
Creates a null matrix of range `r`.
```
qbitos.core=> (null 3)
[[[0 0] [0 0] [0 0]] [[0 0] [0 0] [0 0]] [[0 0] [0 0] [0 0]]]
qbitos.core=> 
```

#### (inv r)
Creates an inverse matrix of range `r`, i.e.: a matrix that, multiplied by another matrix give the reversed matrix.
```
qbitos.core=> (def pruebas (msum (ident 3) (ident 3) (ident 3)))
#'qbitos.core/pruebas
qbitos.core=> pruebas
[[[3 0] [0 0] [0 0]] [[0 0] [3 0] [0 0]] [[0 0] [0 0] [3 0]]]
qbitos.core=> (mmul pruebas (inv 3))
[[[0 0] [0 0] [3 0]] [[0 0] [3 0] [0 0]] [[3 0] [0 0] [0 0]]]
qbitos.core=>
```

#### (trans m)
Generates the transpose of the matrix `m`.

```
qbitos.core=> (def CUA3 [[[5 2][6 0]][[7 -9][8 0]]])
#'qbitos.core/CUA3
qbitos.core=> (trans CUA3)
[[[5 2] [7 -9]] [[6 0] [8 0]]]
qbitos.core=> 
```

#### (msum m1..mn)
Sum matrices of the same dimensions.
```
qbitos.core=> (msum (ident 3) (ident 3) (ident 3))
[[[3 0] [0 0] [0 0]] [[0 0] [3 0] [0 0]] [[0 0] [0 0] [3 0]]]
qbitos.core=> 
```

#### (mmul m1..mn)
Multiplies the list of matrices passed as a parameter. Take into account that the multiplication may change the dimensions of non-rectangular matrices; if this change makes a multiplication illegal, an exception will raise.
```
qbitos.core=> (def CUA1 [[[1 0][2 0]][[3 0][4 0]]])
#'qbitos.core/CUA1
qbitos.core=> (def CUA2 [[[5 0][6 0]][[7 0][8 0]]])
#'qbitos.core/CUA2
qbitos.core=> (mmul CUA1 CUA2)
[[[19 0] [22 0]] [[43 0] [50 0]]]
qbitos.core=> 
```

#### (cmul c m)
Multiplies a matrix `m` by a constant complex number `c`.
```
qbitos.core=> (cmul [6 2] (ident 3))
[[[6 2] [0 0] [0 0]] [[0 0] [6 2] [0 0]] [[0 0] [0 0] [6 2]]]
qbitos.core=> 
```

#### (conjugate c)
Returns the conjugate of the complex number `c`.

```
qbitos.core=> (conjugate [4 -2])
[4 2]
qbitos.core=> 
```

#### (mul c1 c2)
Multiplies two complex numbers.

```
qbitos.core=> (mul [2 0] [-3 1])
[-6 2]
qbitos.core=> 
```

#### (sum c1 c2)
Add to complex numbers together.

```
qbitos.core=> (sum [2 0] [-3 1])
[-1 1]
qbitos.core=> 
```

### The Quantum Computing API

