# Characteristic Numbers Computer

This project is meant to perform particular computations in the field of Algebraic Topology.  Specifically it computes the characteristic classes and characteristic numbers of type Chern, Stiefel-Whitney, and Pontryagin, for any product of complex projective spaces.  At a more basic level the software models the arithmetic of truncated polynomials rings, and this general functionality is then applied to the specific problem.  A small demo is included in the [main](https://github.com/wgolling/CharNumComputer/blob/master/NetBeansProjects/CharNumComputer/src/charnumcomputer/CharNumComputer.java) file.

The audience for this project is people working in or around the theory of Manifolds who have a desire for these computations: a very narrow demographic.  It was mostly written for a personal project and for developing coding skills.

The [math backgroud](https://github.com/wgolling/CharNumComputer/blob/master/NetBeansProjects/CharNumComputer/mathbackground.pdf) file contains a bit of exposition of how these terms fit together, but no effort is made there to explain what they mean.  It is recomended that you download the pdf rather than try to read it in github's pdf reader (at least it works very poorly for me).  The interested reader is strongly encouraged to study Topology in general. 


## Author

* **William Gollinger** - [wgolling](https://github.com/wgolling)

## TODO

* The //TODO's in the code.

* Debugging.

There are many tests already written but it's not exhaustive.  More thorough teststing would need to be done before this product could be considered "finished".

* Make Polynomial more generic.

Polynomial currently uses BigInteger, but it would be good if it could use Integers when we know the coefficients are small, or different data types when we want rational or real coefficients.

Maybe make a Coefficient interface that represents a basic numeric type and provides methods like zero(), one(), plus(a, b), and times(a, b).  Then it can be implemented by IntCoefficient, BigIntCoefficient, RationalCoefficient, RealCoefficient, etc.

* Add more Manifold objects.

Quaternionic projective space HP(n) should be simple to incorporate, I just have to do it.  Real projective space RP(n), on the other hand, is a bit dicey because its integral cohomology is not a type of ring that is modeled by this project (it can be expressed as an integral polynomial ring, but the degree 1 variable is 2-torsion, which is not something that the project currently handles).  This does not effect the computation of their characteristic numbers: the Stiefel-Whitney classes are in mod-2 cohomoloy, there are no Chern classes because RP(n) is never complex, and there are no Pontryagin numbers since RP(n) is only orientable when n is odd (in fact the extra Tor terms from the Kunneth sequence don't effect the Pontryagin classes at all).  Nevertheless I feel like I ought to properly model the cohomology of RP(n)xRP(m) at some point.

If I can push my software far enough to be able to model Dold manifolds then I could try to implement Wall's algorithm for computing oriented bordism groups, and compute the appropriate characteristic numbers of an arbitrary bordism class.
