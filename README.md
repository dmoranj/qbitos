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
[
[[1 0] [0 0] [0 0]]
[[0 0] [1 0] [0 0]]
[[0 0] [0 0] [1 0]]
]
qbitos.core=> 
```
As you can see, there are three rows, with real parts of the main diagonal with value one and the rest of the elements with zero value.

The following sections show the API one operator at a time.

#### (ident r)
Creates an identity matrix of range `r`.
```
qbitos.core=> (ident 3)
[
[[1 0] [0 0] [0 0]]
[[0 0] [1 0] [0 0]]
[[0 0] [0 0] [1 0]]
]
qbitos.core=> 
```

#### (null r)
Creates a null matrix of range `r`.
```
qbitos.core=> (null 3)
[
[[0 0] [0 0] [0 0]]
[[0 0] [0 0] [0 0]]
[[0 0] [0 0] [0 0]]
]
qbitos.core=> 
```

#### (inv r)
Creates an inverse matrix of range `r`, i.e.: a matrix that, multiplied by another matrix give the reversed matrix.
```
qbitos.core=> (def pruebas (msum (ident 3) (ident 3) (ident 3)))
#'qbitos.core/pruebas
qbitos.core=> pruebas
[
[[3 0] [0 0] [0 0]]
[[0 0] [3 0] [0 0]]
[[0 0] [0 0] [3 0]]
]
qbitos.core=> (mmul pruebas (inv 3))
[
[[0 0] [0 0] [3 0]]
[[0 0] [3 0] [0 0]]
[[3 0] [0 0] [0 0]]
]
qbitos.core=>
```

#### (trans m)
Generates the transpose of the matrix `m`.

```
qbitos.core=> (def CUA3 [[[5 2][6 0]][[7 -9][8 0]]])
#'qbitos.core/CUA3
qbitos.core=> (trans CUA3)
[
[[5 2] [7 -9]] 
[[6 0] [8 0]]
]
qbitos.core=> 
```

#### (msum m1..mn)
Sum matrices of the same dimensions.
```
qbitos.core=> (msum (ident 3) (ident 3) (ident 3))
[
[[3 0] [0 0] [0 0]]
[[0 0] [3 0] [0 0]]
[[0 0] [0 0] [3 0]]
]
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
[
[[6 2] [0 0] [0 0]]
[[0 0] [6 2] [0 0]]
[[0 0] [0 0] [6 2]]
]
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

#### (tensorp m1..mn)
Calculates the tensor product of all the matrices passed as a parameter.

```
qbitos.core=> (tensorp (ident 2) (inv 2))
[
[[0 0] [1 0] [0 0] [0 0]] 
[[1 0] [0 0] [0 0] [0 0]] 
[[0 0] [0 0] [0 0] [1 0]] 
[[0 0] [0 0] [1 0] [0 0]]
]
qbitos.core=> 
```

### The Quantum Computing API
This API contains functions and constants that may be useful in dealing with examples and exercises related to the Quantum Computing subject (Constants are shown as identifiers and functions between parentheses and with their parameters). Some constants are shown grouped, for brevity.

This part of the API aims to offer an interface as similar as Dirac's notation as possible. To that extent, it offers bra and ket definition and operations with bras and kets, as well as functions to globally define matrix operators that will be used later in designing quantum gates. 

#### |0>
The representation of the bit 0 in a vectorial classical base.

```
qbitos.core=> |0>
[[[1 0]] [[0 0]]]
qbitos.core=> 
```

#### |1>
The representation of the bit 0 in a vectorial classical base.

```
qbitos.core=> |1>
[[[0 0]] [[1 0]]]
qbitos.core=>
```

#### (defbits bits)
Defines in the global scope a variable containing the matrix representation of the bit combination specified in the parameter `bits`; i.e.: the tensor product of all the bits in the sequence enclosed by the ket symbols. Both bra and ket vectors can be defined using this function (in the later case the representation will be a column vector and a row vector in the former).

```
qbitos.core=> (defbits |0100>)
#'qbitos.core/|0100>
qbitos.core=> |0100>
[[[0 0]] [[0 0]] [[0 0]] [[0 0]] [[1 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]]]
qbitos.core=> 
qbitos.core=> (defbits <0100|)
#'qbitos.core/<0100|
qbitos.core=> <0100|
[[[0 0] [0 0] [0 0] [0 0] [1 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0]]]
qbitos.core=> 
```
#### X Y and Z
Matrix representation of the range 2 Pauli matrices.

```
qbitos.core=> X
[
[[0 0] [1 0]]
[[1 0] [0 0]]
]
qbitos.core=> Y
[
[[0 0] [0 -1]]
[[0 1] [0 0]]
]
qbitos.core=> Z
[
[[1 0] [0 0]]
[[0 0] [-1 0]]
]
qbitos.core=> 
```

#### H
The Hadamar operator (numeric value).

```
qbitos.core=> H
[
[[0.7071067811865475 0.0] [0.7071067811865475 0.0]]
[[0.7071067811865475 0.0] [-0.7071067811865475 0.0]]
]
qbitos.core=> 
```

#### (defoperator op n)
Defines a new operator consisting in a tensor product of the selected one bit operators and one bit identity matrices to fill up to n bits. The `op` param is the name the operator variable will use, and its description. It must have the format `(<ID><BIT>)+` where <ID> is an operator already defined globally and <BIT> the number of the bit over which it applies. E.g.: the operator `(defoperator X1Z0 4)` would define a 4 bit operator that applies the Z operator to the 0 bit, the X operator to the 1 bit and the identity operator to the rest, that is, the tensor product: X * Z * 1 * 1; where X and Z are Pauli matrices and 1 the range 2 identity matrix. The result would be a 16x16 matrix.

```
qbitos.core=> (defoperator X1Z2 3)
#'qbitos.core/X1Z2
qbitos.core=> X1Z2
[
[[0 0] [0 0] [1 0] [0 0] [0 0] [0 0] [0 0] [0 0]]
[[0 0] [0 0] [0 0] [-1 0] [0 0] [0 0] [0 0] [0 0]]
[[1 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0]]
[[0 0] [-1 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0]]
[[0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [1 0] [0 0]]
[[0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [-1 0]]
[[0 0] [0 0] [0 0] [0 0] [1 0] [0 0] [0 0] [0 0]]
[[0 0] [0 0] [0 0] [0 0] [0 0] [-1 0] [0 0] [0 0]]
]
qbitos.core=> 
```

#### (defcij i j n)
Defines a conditional NOT operator where i is the control bit and j the controlled bit, appliable to n-bit vectors (i.e.: the operator is a matrix of 2^n rows). The operator is defined in the global scope as `C-ij` being i and j the numbers passed as parameters.

```
qbitos.core=> (defcij 1 2 3)
#'qbitos.core/C-12
qbitos.core=> C-12
[
[[1.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0]]
[[0.0 0.0] [0.0 0.0] [0.0 0.0] [1.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0]]
[[0.0 0.0] [0.0 0.0] [1.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0]]
[[0.0 0.0] [1.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0]]
[[0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [1.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0]]
[[0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [1.0 0.0]]
[[0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [1.0 0.0] [0.0 0.0]]
[[0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [0.0 0.0] [1.0 0.0] [0.0 0.0] [0.0 0.0]]
]
qbitos.core=> 
```

