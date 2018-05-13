# Characteristic Numbers Computer

This project is meant to perform particular computations in the field of Algebraic Topology.  Specifically it computes the characteristic classes and characteristic numbers of type Chern, Stiefel-Whitney, and Pontryagin, for any product of complex and/or quaternionic projective spaces.  At a more basic level the software models arithmetic in truncated polynomial rings, and this general functionality is then applied to the specific problem.  A small demo is included in the main file, whose output is contained in the [demoOutput](https://github.com/wgolling/CharNumComputer/blob/master/NetBeansProjects/CharNumComputer/demoOutput) file.

The audience for this project is people working in or around the theory of Manifolds who have a desire for these computations: a very narrow demographic.  It was mostly written for a personal project and for developing coding skills.

The [math backgroud](https://github.com/wgolling/CharNumComputer/blob/master/NetBeansProjects/CharNumComputer/mathbackground.pdf) file contains a bit of exposition of how these terms fit together, but no effort is made there to explain what they mean.  I recomend that you download the pdf rather than try to read it in github's pdf reader (at least it works very poorly for me).  The interested reader is strongly encouraged to study Topology in general. 


## Author

* **William Gollinger** - [wgolling](https://github.com/wgolling)

## Acknowledgements

* Special thanks to **Wei Xi Fan** for endless advice and tips.

## Version History

* v0.9 - polynomials now have a generic parameter extending new Coefficient class, whole polynomial framework completely re-done; demos greatly improved

* v0.8 has the basic functionality, but a lot of improvements need to be made

## TODO

* Always Be Testing.

There are many tests already written but it's not exhaustive.  More thorough teststing would need to be done before this product could be considered "finished".

* GUI

The intended users of this software are algebraic topologists, who typically don't have a strong enough Java foundation to implement the code.  In order for users to be able to use the software, I need to learn how to make a GUI.

* Determining bordism classes.

The demo program currently runs two ad-hoc searches in order to determine the oriented bordism classes of HP(2) and HP(3).  There should be general functionality which takes a CharNumbers object and determines a bordism class.  This could (probably) involve defining a BordismRing singleton class to produce lists of generators of bordism groups and execute searches.

* RP(n).

Real projective space RP(n) is a bit dicey.  Its integral cohomology has two issues: 

(i) interpretted as a polynomial ring it has a variable which is 2-torsion, which is something the software doesn't handle at the moment; and more importantly 

(ii) its cohomology is not a free Z-module and so non-zero Tor terms appear in the Kunneth exact sequence, something the software currently has no need to take into account.  

Neither of these ultimately effect the computaton of characteristic numbers, since RP(n) is never complex and any product RP(n)xM has vanishing Pontryagin numbers.  It would still in principle be of value to acurately model the cohomology of these manifolds.

* Dold manifold.

If the software can model Dold manifolds as well, it has the ingredients for Wall's algorithm for computing oriented bordism groups. It would be able to give a list of generators of an arbitrary oriented bordism group, and so determine the oriented bordism class of any collection of characteristic numbers.
