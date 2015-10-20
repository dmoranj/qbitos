# Qbitos: Quantum Mechanics "On the rocks"

## Overview
This library is devoted to the experimentation with the mathematic operations and abstractions of the Quantum Computing and Quantum mechanics fields. It is not expected to be efficient or be part of any real-life system, but to provide some useful tools to study and understand some of the most typical Quantum Computing exercises. The exercises and examples of this library are directly based on the following book: "Quantum Computer Science. An Introduction", N. David Mermin, Cambridge Press.

This library uses its own Complex Matrix library, built from scratch, for learning purposes. Every comment on the library usage is appliable to this Math library as well.

The ide of the library is to provide:
* The most common mathematical functions used in Quantum Mechanics.
* A library with the most common operators and quantum gates.
* Some visualization features to show the results and intermediate numbers in a Dirac-like notation.

### A note about Complex Numbers
The Complex Numbers library used within Qbitos assumes that a Complex Number is a Clojure Vector of two real numbers, being the first the real part and the second one the imaginary part.

## Installation
There are two ways of using this library in order to follow the exercises: cloning the repository and using the Docker image.

In order to use the Docker image, just type the following command on a console:
```
docker run -ti dmoranj/qbitos
```
This will directly open a REPL with the preloaded library.

For those cases where docker is not available, the library can be executed from a computer with an installed [Leiningen](http://leiningen.org/), just cloning the repository, going to the root folder and typing:
```
lein repl
```

The result in both cases will be a command line interpreter in your console where you can start to type all the examples shown in the usage description.

## Usage
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
#### (generate-vectors bits)
This function defines all the bra and ket vectors for the selected number of bits (using the `defbits` function), so they can be referred without being explicitly declared.
```
qbitos.core=> (generate-vectors 3)
#'qbitos.core/<111|
qbitos.core=> <101|
[[[0 0] [0 0] [0 0] [0 0] [0 0] [1 0] [0 0] [0 0]]]
qbitos.core=> |100>
[[[0 0]] [[0 0]] [[0 0]] [[0 0]] [[1 0]] [[0 0]] [[0 0]] [[0 0]]]
qbitos.core=> |110>
[[[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[0 0]] [[1 0]] [[0 0]]]
qbitos.core=>
```

#### (measure state)
Performs a measure on the state passed as a parameter, following the Born Rule, i.e.: each of the values in the column vector is  taken to be the amplitude of the state represented by the binary expansion of its index. This function calculates the probabilities for each of the entangled states and select one of them randomly based on that probabilities. The result will be the string representation of the collapsed pure state.

## Exercises

### Exercise 1. The Deutsch's problem

The Deutch problems is one of the easiest exercises showing the power of the quantum computating algorithms in solving problems more efficiently than its classical counterparts. This is the problem's description:

> Imagine a black box quantum computing device that works as a 2-qbit machine that applies
> a certain **Uf** transformation to the input and output registers (the first and second
> qbits respectively). Due to the discrete nature of the bits there are only four possible
> Uf operators that take the input qbit into an output qbit, i.e:

| Name | Function        | Uf [00> | Uf [10> |
|:---- |:--------------- |:-------:|:-------:|
| f0   | Identity        | [00>    | [10>    |
| f1   | CNOT-01         | [00>    | [11>    |
| f2   | CNOT-01 · X0    | [01>    | [10>    |
| f3   | X0              | [01>    | [11>    |

> In the table, the operator is applied to a set of two QBits [io>, returning another set
> of two qbits, where both the input and output registers can be modified. The problem is
> the following: how can we determine if a function inside the black box is constant,
> f(0)= f(1), or not constant, f(0) != f(1), without opening it, using the minimum number
> of operations.

First of all, let's try to get an idea about the problem using classical computation and the QBitos library. Start by loading the common scenario variables by typing the following from the REPL
```
qbitos.core=> (loadDeutch)
#'qbitos.scenarios/H0
qbitos.core=> (generate-vectors 2)
#'qbitos.core/<11|
```
This will load some global variables that will be useful during this exercises. We can start by applying the different functions to each of the four possible input values, to see their possible outcomes:
```
qbitos.core=> (mmul f0 |00>)
[[[1 0]] [[0 0]] [[0 0]] [[0 0]]]
qbitos.core=> (mmul f0 |10>)
[[[0 0]] [[0 0]] [[1 0]] [[0 0]]]
qbitos.core=>

qbitos.core=> (mmul f1 |00>)
[[[1 0]] [[0 0]] [[0 0]] [[0 0]]]
qbitos.core=> (mmul f1 |10>)
[[[0 0]] [[0 0]] [[0 0]] [[1 0]]]
qbitos.core=>

qbitos.core=> (mmul f2 |00>)
[[[0 0]] [[1 0]] [[0 0]] [[0 0]]]
qbitos.core=> (mmul f2 |10>)
[[[0 0]] [[0 0]] [[1 0]] [[0 0]]]
qbitos.core=>

qbitos.core=> (mmul f3 |00>)
[[[0 0]] [[1 0]] [[0 0]] [[0 0]]]
qbitos.core=> (mmul f3 |10>)
[[[0 0]] [[0 0]] [[0 0]] [[1 0]]]
qbitos.core=>
```
Using classical operations, the only way we have to tell whether both images are equal or distinct is to have the operator applied to each of the input vectors, and compare the results. But quantum computation gives you another approach. For the rest of the exercise we will use a variable `Uf` containing a random function from the set shown above.

Let's prepaire our state using the common quantum computing standard trick: prepairing the state in a superposition. In order to do this, we use the Hadamar operator in our initial register, before applying the operator:
```
qbitos.core=> (mmul Uf H0 |10>)
[[[0.0 0.0]] [[0.7071067811865475 0.0]] [[-0.7071067811865475 0.0]] [[0.0 0.0]]]
qbitos.core=>
```
Measuring this value will get us one of the possible outcomes for the operators, but this will still give os no information whatsoever about it being constant. In order to get more info, we will apply some more unitary operators to our vector (you can find the theoretical demonstration in the book, Section 2.2).
```
qbitos.core=> (mmul H0 Uf H0H1 X0X1 |00>)
[[[-0.7071067811865474 0.0]] [[0.7071067811865474 0.0]] [[0.0 0.0]] [[0.0 0.0]]]
qbitos.core=>
```
What have we gained with this approach? Let's apply our formula to each one of the possible functions, to know more about our new approach:
```
qbitos.core=> (mmul H0 f0 H0H1 X0X1 |00>)
[[[0.0 0.0]] [[0.0 0.0]] [[0.7071067811865474 0.0]] [[-0.7071067811865474 0.0]]]
qbitos.core=> (mmul H0 f1 H0H1 X0X1 |00>)
[[[0.7071067811865474 0.0]] [[-0.7071067811865474 0.0]] [[0.0 0.0]] [[0.0 0.0]]]
qbitos.core=> (mmul H0 f2 H0H1 X0X1 |00>)
[[[-0.7071067811865474 0.0]] [[0.7071067811865474 0.0]] [[0.0 0.0]] [[0.0 0.0]]]
qbitos.core=> (mmul H0 f3 H0H1 X0X1 |00>)
[[[0.0 0.0]] [[0.0 0.0]] [[-0.7071067811865474 0.0]] [[0.7071067811865474 0.0]]]
qbitos.core=>
```
What we can see in the outputs for each of the functions is that, even when the output bit is uncertain, the most significant bit (i.e.: the input bit) clearly sepparates the different outputs: if and only if the input bit is |1> (in the modified state) is the state constant. Thus, in our example, even if we don't know what the real function is (and we don't even know its value for any input), we know it must be f1 or f2, as it's input bit is clearly |0>.

This example shows some of the most interesting characteristics of the quantum computation: how we can extract relational information from our state, even when we can't extract any deterministic data from it.

### Exercise 2. The Bernstein-Vazirani problem

This is the problem's description:

> Imagine a quantum computing device with an input register of n qbits and an output register of one. Let a be an
> unsigned integer less than 2^n. We define f(x) as the following:
>
>     f(x) = a0·x0 + a1·x1 + .... an·xn
>
> where all the operations are modulo-2 bit operations. Suppose our quantum device computes f(x). How many times should
> we call our quantum device in order to guess a?

Classicaly, the solution typically involves n calls. A strategy could be making one call with each power of two below 2^n.
As any power of two has only one bit with value `1`, each call will determine whether that same bit is active in the value
a or not. So, making the full n calls we will have the exact bit representation of a. Surprisingly, we will see that, using
quantum computing tricks, the total number of calls can be reduced to 1, for any n.

Let's try this classical approach, in order to understand the problem better. First of all, we define f(x), using some auxiliar functions:
```
qbitos.core=> (loadBernsteinVazirani 5 4)
#'qbitos.scenarios/fx
qbitos.core=> (generate-vectors 4)
#'qbitos.core/<1111|
qbitos.core=>
```
Here, we are using the `loadBernsteinVazirani` function to define `fx` with the given parameters, i.e.: a 4 qbit unitary operator that computes f(x) in the last qbit, using the first three as the input register (being `a = 5` in this case). We also define all the possible bras and kets in the 4 qbits space, as we will use them in our calculations.

We can now start to guess the bits in the `a` number (and it better ends up being 5):
```
qbitos.core=> (mmul fx |1000>)
[[[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[1.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]]]
qbitos.core=>

qbitos.core=> (mmul fx |0100>)
[[[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[1.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]]]
qbitos.core=>

qbitos.core=> (mmul fx |0010>)
[[[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[1.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]]]
qbitos.core=>
```
This may be a little difficult to read, but conceptually, is the same classical algorithm we described above. In any of the iterations we are applying (via a matrix multiplication) the unitary operator f(x) to a register composed of four qbits: three qbits for the input and one single qbit for the output. In order to guess the number, we try with the three powers of two below 2^3, that is: 2^2 = 4, 2^1  = 2 and 2^0 = 1. The results are given as the tensor product of the three bits; the corresponding bit representations of the answers are the following:

| x      | f(x)   |
|:------ |:------ |
| [1000> | [1001> |
| [0100> | [0100> |
| [0010> | [0011> |

As we can see the output register (the last qbit) is 1 for the first and the last entries (the ones having the most and least significant qbits with value 1), so a must have just those two qbits on, i.e. it must be 101, or 5 (as we expected).

Using quabtym computing operators, we can perform the following trick:
```
qbitos.core=> (mmul H0H1H2H3 fx H0H1H2H3 |0001>)
[[[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[1.3877787807814457E-17 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[-1.3877787807814457E-17 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.9999999999999996 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[0.0 0.0]] [[1.3877787807814457E-17 0.0]]]
qbitos.core=>
```
First of all, concerning the result: we can see that there is a single bit in a state "near 1" (the 11th bit) and several bits in a state "near 0" (due to the discrete nature of the simulation). This is the tensor form of the |1011> qbit vector. As we can see, the input state has changed to the binary value '101', i.e, the decimal number 5, that is the value of a. We have discovered the value of a in a single operation, instead of the **n** operations needed in its classical counterpart.
